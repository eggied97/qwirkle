package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.ui.UserInterface;
import nl.utwente.ewi.qwirkle.ui.tui.TUIView;

public class Main {

	public static void main(String[] args) {
		UserInterface UI = new TUIView();
		
		setupServer();
		
		Player p = UI.login();
		//TODO make server check
		

	}
	
	private static void setupServer(){
		
	}

}
