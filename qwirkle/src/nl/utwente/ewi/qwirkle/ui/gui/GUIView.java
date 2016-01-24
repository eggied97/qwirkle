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
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
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
		
		setCallback(callback);
		
		connectPanelFrame = new ConnectPanel(this.callback);
		connectPanelFrame.setVisible(true);
		
		mFrame = new MainFrame(this.callback);
		mFrame.setVisible(false);
	}
	
	public Player getPlayer() {
		return play;
	}
	
	public void setPlayer(Player p) {
		this.play = p;
	}
	
	public MainFrame getFrame() {
		return mFrame;
	}
	
	public void setUICallback(UserInterfaceCallback callback) {
		this.getFrame().setCallback(callback);
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
			b.setEnabled(true);
		}
		
	}
		
	public void setChat(String message, Style s) {
		mFrame.setTextArea(message, s);
	}

	public void updateBoard(List<Move> moves) {
		mFrame.addButton(moves);
	}
	
	public void handleProblemWithMove(){
		mFrame.undoMoves();
	}

	@Override
	public void showScore(Map<Player, Integer> scoreMap, boolean fromGameEnd) {
		StringBuilder result = new StringBuilder();
		result.append("SCORE" + "\n");
		TreeMap<Integer, Player> score = new TreeMap<>();
		for(Entry<Player, Integer> e : scoreMap.entrySet()) {
			score.put(e.getValue(), e.getKey());
		}
		for(Entry<Integer, Player> e : score.entrySet()) {
			result.append(e.getValue().getName() + " - " + e.getValue() + "\n");
		}
		mFrame.setScoreboard(result.toString());
		
	}
	
	@Override
	public void setCallback(UserInterfaceCallback callback) {
		this.callback = callback;
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
		mFrame.setMessageLabel(message);
		mFrame.emptyMoveSet();
	}

	@Override
	public void askForServerInformation() {
		// TODO Auto-generated method stub
		
	}
}
