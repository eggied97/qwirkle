package nl.utwente.ewi.qwirkle;

import nl.utwente.ewi.qwirkle.client.QwirkleClient;
import nl.utwente.ewi.qwirkle.server.connect.Server;

public class HoofdKlasse {

	//TODO client en server online werkt, offline msischien multithreaden?
	
	private static final String QUESTION_ONNLINE_OR_OFFLINE = "Do you want to play locally or online? (l/o)";
	private static final String QUESTION_AMOUNT_OF_PLAYERS = "With how many players would you like to connect? (Integers only)";
	private static final String QUESTION_FOR_CLIENT_OR_SERVER = "Do you want to run a server or a client? (s/c)";

	public static void main(String[] args) {

		while(true){
			String onlineOrOffline = util.readString(QUESTION_ONNLINE_OR_OFFLINE);
	
			switch (onlineOrOffline) {
				case "l":
					int amount = util.readInt(QUESTION_AMOUNT_OF_PLAYERS);
		
					setupLocalGame(amount);
					
					break;
		
				case "o":
					String serverOrClient = util.readString(QUESTION_FOR_CLIENT_OR_SERVER);
					
					switch (serverOrClient) {
					case "s":
						new Server();
						break;
	
					case "c":
						new QwirkleClient(null);
						break;
						
					default:
						System.err.println("unknown command");
						break;
					}
					
					break;
		
				default:
					System.err.println("unknown command");
					break;
			}
		}

	}

	private static void setupLocalGame(int amountOfPlayers) {
		int port = 1234;

		Server s = null;

		while (s == null) {
			port += 1;
			s = new Server(port, true);
		}
		
		System.out.println("Server created");
		
		//now the server is created
		
		for(int i = 0; i < amountOfPlayers; i++){
			new QwirkleClient(port);
		}
		
	}

}
