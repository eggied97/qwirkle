package nl.utwente.ewi.qwirkle.ui.gui;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.text.Style;

import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.model.player.strategy.SuperStrategy;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.imageGetter;
import nl.utwente.ewi.qwirkle.ui.gui.panels.ConnectPanel;
import nl.utwente.ewi.qwirkle.ui.gui.panels.MainFrame;




public class GUIView implements UserInterface {
	ConnectPanel frame;
	MainFrame mFrame;
	private imageGetter img;
	
	public GUIView() {
		img = new imageGetter();
		
		frame = new ConnectPanel();
		frame.setVisible(true);
		
		mFrame = new MainFrame();
		mFrame.setVisible(false);
		
	}
	
	public MainFrame getFrame() {
		return mFrame;
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
		
		frame.setNameSet(false); //reset name for when it is invalid (otherwise you get into a infinte loop)
		
		System.out.println(name);
		if(name.equals("COMPUTERMAN")){
			return new ComputerPlayer("pcman" + (int)(Math.random() * 4));
		}else if(name.equals("COMPUTERMANSLIM")){
			return new ComputerPlayer("pcmanslim" + (int)(Math.random() * 4), new SuperStrategy());
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

	public void showScore(List<Player> players) {
		String result = "";
		Map<Integer, String> scores = new TreeMap<>();
		for(Player p : players) {
			scores.put(p.getScore(), new String(p.getName() + " - " +  p.getScore()));
		}
		for(Map.Entry<Integer, String> e : scores.entrySet()) {
			result += e.getValue();
			result += "\n";
		}
		
		
		
	}

	@Override
	public void showHand(List<Tile> tiles) {
		List<BufferedImage> tileImg = new ArrayList<>();
		for(Tile t : tiles) {
			tileImg.add(img.getImageByTile(t));
		}
		List<JToggleButton> buttons =  mFrame.getHandTiles();
		int i = 0;
		for(JToggleButton b : buttons) {
			b.setIcon(new ImageIcon(tileImg.get(i)));
			i++;
		}
		
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
	
	public void setChat(String message, Style s) {
		mFrame.setTextArea(message, s);
	}



	public void updateBoard() {
		
		
	}
	
	public static void main(String[] args) {
		GUIView gui = new GUIView();
		List<Tile> tiles = new ArrayList<>();
		tiles.add(new Tile(2));
		tiles.add(new Tile(16));
		tiles.add(new Tile(28));
		tiles.add(new Tile(24));
		tiles.add(new Tile(3));
		tiles.add(new Tile(6));
		gui.showHand(tiles);
	}



	@Override
	public void showScore(Map<Player, Integer> scoreMap) {
		// TODO Auto-generated method stub
		
	}


}
