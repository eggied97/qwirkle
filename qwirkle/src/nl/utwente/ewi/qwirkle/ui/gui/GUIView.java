package nl.utwente.ewi.qwirkle.ui.gui;

import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.ui.UserInterface;

public class GUIView implements UserInterface {
	public GUIView() {
		
	}

	@Override
	public Player login() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] queueWithHowManyPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeTurn(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerTraded(Player p, int noOfTilesTraded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showScore(Map<Player, Integer> scoreMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showHand(List<Tile> tiles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String askForPlayOrExchange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String askForMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String askForTrade() {
		// TODO Auto-generated method stub
		return null;
	}

}
