package nl.utwente.ewi.qwirkle.ui.gui;

import java.awt.EventQueue;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.gui.panels.ConnectPanel;



public class GUIView implements UserInterface {
	ConnectPanel frame;
	
	public GUIView() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ConnectPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	@Override
	public Player login() {
		boolean isActive = true;
		while(isActive) {
			System.out.println(frame.getName());
		}
		String name = frame.getName();
		System.out.println(name);
		if(name.equals("COMPUTERMAN")){
			return new ComputerPlayer("pcman" + (int)(Math.random() * 4));
		}
		
		return new HumanPlayer(name);
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

	@Override
	public String askForChatMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
