package nl.utwente.ewi.qwirkle.ui.tui;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.utwente.ewi.qwirkle.util;
import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.ui.UserInterface;

public class TUIView implements UserInterface {

	private static final String QUESTION_ASK_NAME = "What is your name? ";
	private static final String QUESTION_QUEUE = "With how many players would you like to play (2-4)? ( format: 2,3 )";
	private static final String QUESTION_PLAY_OR_EXHANGE = "Do you want to play a Tile, or exhange a Tile or chat? (p/e/c)";
	private static final String QUESTION_ASK_FOR_MOVE = "Which Tiles do you want to lay down? \n format: [no_tile@x,y] :";
	private static final String QUESTION_ASK_FOR_TRADE = "Which Tiles do you want to trade? \n format: [no_tile] :";
	private static final String QUESTION_ASK_FOR_CHAT = "The format for a chat message is as follow: \n global/@username message";

	
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

		//TODO check if this check is needed or not
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

	@Override
	public void showHand(List<Tile> tiles) {
		
		printMessage("Your hand :");
		
		String result = "";
		
		for (int i = 0; i < tiles.size(); i++) {
				result += tiles.get(i).getHumanReadableString() + " (" + i+")";
				result += "     ";
		}
	}

	@Override
	public String askForPlayOrExchange() {
		return util.readString(QUESTION_PLAY_OR_EXHANGE);
	}

	@Override
	public String askForMove() {
		return util.readString(QUESTION_ASK_FOR_MOVE);
	}

	@Override
	public String askForTrade() {
		return util.readString(QUESTION_ASK_FOR_TRADE);
	}

	@Override
	public String askForChatMessage() {
		return util.readString(QUESTION_ASK_FOR_CHAT);
	}
}
