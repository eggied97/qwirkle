package nl.utwente.ewi.qwirkle.ui.gui;

import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;

public class ProtocolControl {
	
	private GUIView gui;
	private Game game;
	private Client client;
	

	public ProtocolControl(UserInterface ui, Game g) {
		this.game = g;
		this.client = game.getClient();
		this.gui = (GUIView)ui;
		gui.getFrame().setControl(this);
	}
	
	public void handleChat(String message) {
		String channel = "global";
		if(message.charAt(0) == '@') {
			channel = message.split(" ")[0];
		}
		client.sendMessage(protocol.getInstance().clientChat(channel, message));
	}
	
 
}
