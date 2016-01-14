package nl.utwente.ewi.qwirkle.ui.tui;

import java.util.Map;
import java.util.Map.Entry;

import nl.utwente.ewi.qwirkle.util;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.ui.UserInterface;

public class TUIView implements UserInterface {

	private static final String QUESTION_ASK_NAME = "What is your name? ";
	private static final String QUESTION_QUEUE = "With how many players would you like to play (2-4)? ( format: 2,3 )";

	public TUIView() {

	}

	@Override
	public Player login() {
		// First ask name, then create a player instance, then return
		String name = util.readString(QUESTION_ASK_NAME);

		return new HumanPlayer(name);
	}

	@Override
	public int[] queueWithHowManyPlayers() {
		String queue = util.readString(QUESTION_QUEUE);

		String[] queues = queue.split(",");

		if (queues.length == 0 || queues.length > 3) {
			showError("Wrong input: " + QUESTION_QUEUE);
			return queueWithHowManyPlayers();
		} else {
			int[] result = new int[queues.length];

			for (int i = 0; i < result.length; i++) {
				result[i] = Integer.parseInt(queues[i].trim());
			}
			return result;
		}
	}

	@Override
	public void changeTurn(Player p) {
		printMessage("It is " + p.getName() + "'s turn");
	}

	@Override
	public void playerTraded(Player p, int noOfTilesTraded) {
		printMessage(p.getName() + " has traded " + noOfTilesTraded + " tiles");
	}

	@Override
	public void showError(String message) {
		System.err.println(message);
	}

	@Override
	public void printMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void showScore(Map<Player, Integer> scoreMap) {
		printMessage("Game was ended, score:");

		for (Entry e : scoreMap.entrySet()) {
			printMessage(((Player) e.getKey()).getName() + " has reached the score of: " + e.getValue());
		}
	}
}
