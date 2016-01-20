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
import nl.utwente.ewi.qwirkle.ui.gui.panels.MainFrame;



public class GUIView implements UserInterface {
	ConnectPanel frame;
	MainFrame mFrame;
	
	public GUIView() {
		frame = new ConnectPanel();
		frame.setVisible(true);
		
		mFrame = new MainFrame();
		mFrame.setVisible(false);
		
	}
	
	

	@Override
	public Player login() {
		while(!frame.isNameSet()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		while(!frame.isQueueSet()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List<Boolean> queues = frame.getQueues();
		int j = 0;
		for(int i = 0; i < queues.size(); i++) {
			if(queues.get(i)) {
				j++;
			}
		}
		
		int[] enteredQueues = new int[j];
		j = 0;
		
		for(int i = 0; i < queues.size(); i++) {
			if(queues.get(i)) {
				enteredQueues[j] = i+2;
				j++;
			}
		}
		return enteredQueues;
	}

	public void changeFrame() {
		if(frame.isVisible()) {
			frame.setVisible(false);
			mFrame.setVisible(true);
		} else {
			frame.setVisible(true);
			mFrame.setVisible(false);
		}
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
		mFrame.setMessageLabel(message);
		
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
	
	public void setScore(int[] score) {
		
	}
	
	public void setChat(String message) {
		mFrame.setTextArea(message);
	}

}
