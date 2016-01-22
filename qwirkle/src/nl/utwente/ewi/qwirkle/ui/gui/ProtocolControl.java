package nl.utwente.ewi.qwirkle.ui.gui;

import java.util.List;

import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.model.*;

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
	
<<<<<<< HEAD
	public boolean handleMove(List<Move> moveSet) {
		if(gui.getPlayer().equals(game.getTurnPlayer())) {
			client.sendMessage(protocol.getInstance().clientPutMove(moveSet));
			return true;
		} else {
			return false;
		}
	}

=======
 
>>>>>>> refs/remotes/origin/master
}
