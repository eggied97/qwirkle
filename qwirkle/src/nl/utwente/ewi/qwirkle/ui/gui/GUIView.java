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
import javax.swing.JOptionPane;
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
import nl.utwente.ewi.qwirkle.ui.gui.panels.PortFrame;

public class GUIView implements UserInterface {
	private ConnectPanel connectPanelFrame;
	private MainFrame mFrame;
	private imageGetter img;
	private PortFrame servFrame;
	private String result;
	private Player play;

	private UserInterfaceCallback callback;
	
	/**
	 * Initialize the image cutter
	 */
	public GUIView() { 
		img = new imageGetter();
	}
	
	/**
	 * Create all frames with the corresponding callback
	 * @param callback
	 */
	public void setup(UserInterfaceCallback callback) {
		servFrame = new PortFrame(this.callback);
		servFrame.setVisible(true);
		
		connectPanelFrame = new ConnectPanel(this.callback);
		connectPanelFrame.setVisible(false);
		
		mFrame = new MainFrame(this.callback);
		mFrame.setVisible(false);
	}
	
	/**
	 * Returns the player
	 * @return
	 */
	public Player getPlayer() {
		return play;
	}
	
	/**
	 * Sets the player
	 * @param p
	 */
	public void setPlayer(Player p) {
		this.play = p;
	}
	
	/**
	 * Returns the <code> MainFrame </code>
	 * @return
	 */
	public MainFrame getFrame() {
		return mFrame;
	}
	
	/**
	 * Set the callback for the <code> MainFrame </code>
	 * @param callback
	 */
	public void setUICallback(UserInterfaceCallback callback) {
		this.getFrame().setCallback(callback);
	}

	/**
	 * Switch from <code> ConnectPanel </code> to <code> MainFrame </code>
	 */
	public void changeFrame() {
		if(connectPanelFrame.isVisible()) {
			connectPanelFrame.dispose();
			mFrame.setVisible(true);
		} else {
			connectPanelFrame.setVisible(true);
			mFrame.setClose();
		}
	}

	/**
	 * Displays the incoming chat message
	 * @param message
	 * @param s
	 */
	public void setChat(String message, Style s) {
		mFrame.setTextArea(message, s);
	}

	/**
	 * Update the board with the new tiles
	 * @param board
	 */
	public void updateBoard(Map<Point, Tile> board) {
		mFrame.update(board);
	}
	
	/**
	 * Undo the last put moves
	 */
	public void handleProblemWithMove(){
		mFrame.undoMoves();
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
			if(tileImg.size() > i){
				b.setIcon(new ImageIcon(tileImg.get(i)));
				i++;
				b.setEnabled(true);
			}else{
				i++;
				b.setIcon(null);
				b.setEnabled(false);
			}
		}
		
	}



	@Override
	public void showScore(Map<Player, Integer> scoreMap, boolean fromGameEnd) {
		for(Player p : scoreMap.keySet()) {
			result = p.getName() + " : " + scoreMap.get(p) + "\n" + result;
		}
		result = ("---------------" + "\n") + result;
		result = ("SCORE" + "\n") + result;
		
		mFrame.setScoreboard(result);
		result = "";
	}
	
	/**
	 * Show the bag size and add it to the score text
	 * @param bagSize
	 * @param scoreMap
	 * @param fromGameEnd
	 */
	public void showBag(int bagSize, Map<Player, Integer> scoreMap, boolean fromGameEnd) {
		result = "\n" + "Bag: " + bagSize;
		showScore(scoreMap, fromGameEnd);
	}
	
	@Override
	public void setCallback(UserInterfaceCallback callback) {
		this.callback = callback;
	}

	@Override
	public void askForLogin() {
		servFrame.dispose();
		connectPanelFrame.setVisible(true);
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
	public void printMessage(String message) {
		mFrame.setMessageLabel(message);		
	}

	@Override
	public void showError(String message) {
		if(mFrame.isDisplayable()) {
			JOptionPane.showMessageDialog(mFrame, message, "Error!", JOptionPane.ERROR_MESSAGE);
		} else if(connectPanelFrame.isDisplayable()) {
			JOptionPane.showMessageDialog(connectPanelFrame, message, "Error!", JOptionPane.ERROR_MESSAGE);
		} else if(servFrame.isDisplayable()) {
			JOptionPane.showMessageDialog(servFrame, message, "Error!", JOptionPane.ERROR_MESSAGE);
		} else {
			System.err.println(message);
		}
		
		mFrame.emptyMoveSet();
	}

	public void printHint(Move m) {
		mFrame.handleHint(m);
	}
	
	@Override
	public void askForServerInformation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askForAITime() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableChat() {
		mFrame.disableChat();
	}
}
