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

import nl.utwente.ewi.qwirkle.callback.UserInterfaceCallback;
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
	private ConnectPanel connectPanelFrame;
	private MainFrame mFrame;
	private imageGetter img;

	private Player play;

	private UserInterfaceCallback callback;
	
	public GUIView(UserInterfaceCallback callback) { //var needed in callback, because our frame needs it :/
		img = new imageGetter();
		
		this.callback = callback;
		
		connectPanelFrame = new ConnectPanel(this.callback);
		connectPanelFrame.setVisible(true);
		
		mFrame = new MainFrame(this.callback);
		mFrame.setVisible(false);
	}
	
	public Player getPlayer() {
		return play;
	}
	
	
	public MainFrame getFrame() {
		return mFrame;
	}
	
	@Override
	public void setCallback(UserInterfaceCallback callback) {
		this.callback = callback;
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
	
	public void setChat(String message, Style s) {
		mFrame.setTextArea(message, s);
	}



	public void updateBoard() {
		
		
	}
	
	//TODO remove below?
	/*public static void main(String[] args) {
		GUIView gui = new GUIView();
		List<Tile> tiles = new ArrayList<>();
		tiles.add(new Tile(2));
		tiles.add(new Tile(16));
		tiles.add(new Tile(28));
		tiles.add(new Tile(24));
		tiles.add(new Tile(3));
		tiles.add(new Tile(6));
		gui.showHand(tiles);
	}*/



	@Override
	public void showScore(Map<Player, Integer> scoreMap) {
		String result = "";
		TreeMap<Player, Integer> score = new TreeMap<>();
		score.putAll(scoreMap);
		for(Entry<Player, Integer> e : score.entrySet()) {
			result += (e.getKey().getName() + " - " + e.getValue());
		}
		
	}

	@Override
	public void askForLogin() {
		connectPanelFrame.resetName();
	}

	@Override
	public void askQueueWithHowManyPlayers() {
		connectPanelFrame.resetQueue();
	}

	@Override
	public void askForPlayOrExchange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askForMove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askForTrade() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askForChatMessage() {
		// TODO Auto-generated method stub		
	}
}
