package nl.utwente.ewi.qwirkle.ui.gui;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	ConnectPanel connectPanelFrame;
	MainFrame mFrame;
	private imageGetter img;

	private Player play;

	GraphicalCallback callback = null;

	
	public GUIView() {
		img = new imageGetter();
		
		connectPanelFrame = new ConnectPanel();
		connectPanelFrame.setVisible(true);
		
		mFrame = new MainFrame();
		mFrame.setVisible(false);
		
	}
	
	public Player getPlayer() {
		return play;
	}
	
	public void setCallback(GraphicalCallback gc){
		this.callback = gc;
	}
	
	public MainFrame getFrame() {
		return mFrame;
	}

	@Override
	public Player login() {
		while(!connectPanelFrame.isNameSet()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String name = connectPanelFrame.getName();
		
		connectPanelFrame.setNameSet(false); //reset name for when it is invalid (otherwise you get into a infinte loop)
		
		System.out.println(name);
		if(name.equals("COMPUTERMAN")){
			play = new ComputerPlayer("pcman" + (int)(Math.random() * 4));
			return play;
		}else if(name.equals("COMPUTERMANSLIM")){
			play =  new ComputerPlayer("pcmanslim" + (int)(Math.random() * 4), new SuperStrategy());
			return play;
		}
		play = new HumanPlayer(name);
		return play;
	}

	@Override
	public int[] queueWithHowManyPlayers() {
		while(!connectPanelFrame.isQueueSet()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List<Boolean> queues = connectPanelFrame.getQueues();
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
		if(connectPanelFrame.isVisible()) {
			connectPanelFrame.setVisible(false);
			mFrame.setVisible(true);
		} else {
			connectPanelFrame.setVisible(true);
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
		mFrame.setTiles(tiles);
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
		String result = "";
		TreeMap<Player, Integer> score = new TreeMap<>();
		score.putAll(scoreMap);
		for(Entry<Player, Integer> e : score.entrySet()) {
			result += (e.getKey().getName() + " - " + e.getValue());
		}
		
	}


}
