package nl.utwente.ewi.qwirkle.server.connect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import nl.utwente.ewi.qwirkle.util;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.Protocol;
import nl.utwente.ewi.qwirkle.server.Game;
import nl.utwente.ewi.qwirkle.server.HandleCommand;
import nl.utwente.ewi.qwirkle.ui.gui.ServerGUI;

public class Server {

	private static int port = 0;
	private ServerSocket serverSock;
	private List<ClientHandler> all;
	private List<ClientHandler> start;
	private List<ClientHandler> identified;
	private Map<Integer, List<ClientHandler>> queues;
	private Map<Integer, List<ClientHandler>> games;
	private HandleCommand handle = HandleCommand.getInstance(this);
	private int gameCounter = 0;
	private IProtocol.Feature[] features;

	
	public static void main(String[] args) {
		Server server = null;
		ServerGUI gui = new ServerGUI(server);
		gui.setVisible(true);

		while (server == null) {

			try {
				while(port == 0) {
					port = gui.getPort();
					System.out.print("");
				}
				server = new Server(port, false);
				gui.showRunning();
				server.run();
			} catch (IOException e) {
				System.err.println("this port is already in use, use another one.");
				server = null;
				port = 0;
			} catch (NumberFormatException e) {
				System.err.println("port is not a number");
				server = null;
				port = 0;
			}
		}
	}

	
	/**
	 * Returns the <code> ClientHandlers </code> from a game to a queue
	 * selection list
	 * 
	 * @param list
	 */
	//@ requires list != null;
	public void addIdentified(List<ClientHandler> list) {
		identified.addAll(list);
	}

	/**
	 * 
	 * @return returns the features of the <code> Server </code>
	 */
	/*@ pure */public IProtocol.Feature[] getFeatures() {
		return features;
	}

	/**
	 * 
	 * @return returns the queues of the <code> Server </code>
	 */
	
	/*@ pure */public Map<Integer, List<ClientHandler>> getQueues() {
		return this.queues;
	}
	
	public Server(){
		//this.main(null);
	}

	/**
	 * Create a new instance of <code> Server </code>
	 * 
	 * @param port
	 */
	//@ requires port != null;
	//@ requires fromMainMenu != null;
	public Server(int port, boolean fromMainMenu) {
		
		try{
			this.port = port;
			init();
			
			if(fromMainMenu){
				run();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the <code> Server </code>
	 */
	public void init() {
		queues = new TreeMap<>();
		queues.put(2, new ArrayList<ClientHandler>());
		queues.put(3, new ArrayList<ClientHandler>());
		queues.put(4, new ArrayList<ClientHandler>());
		games = new HashMap<>();
		all = new ArrayList<>();
		start = new ArrayList<>();
		identified = new ArrayList<>();
		features = new IProtocol.Feature[1];
		features[0] = IProtocol.Feature.CHAT;
	}

	/**
	 * Starts looking for <code> Clients </code> that want to connect to the
	 * <code> Server </code>
	 */
	public void run() throws IOException {

		serverSock = new ServerSocket(port);

		while (true) {
			try {
				Socket client = serverSock.accept();
				System.out.println("Client connected!");
				ClientHandler handler = new ClientHandler(this, client);
				handler.start();
				all.add(handler);
				start.add(handler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Prints the message to the standard output
	 * 
	 * @param message
	 */
	//@ requires message != null;
	public void print(String message) {
		System.out.println(message);
	}

	/**
	 * 
	 * @return returns the <code> List </code> with all
	 *         <code> ClientHandlers </code>
	 */
	public List<ClientHandler> getAll() {
		return this.all;
	}

	/**
	 * Sends a message to all <code> ClientHandlers </code> of the
	 * <code> List </code>
	 * 
	 * @param list
	 * @param msg
	 */
	//@ requires list != null;
	//@ requires msg != null;
	public void broadcast(List<ClientHandler> list, String msg) {
		for (ClientHandler ch : list) {
			ch.sendMessage(msg);
		}
	}

	/**
	 * Removes the <code> CliendHandler </code> from the all <code> List </code>
	 * 
	 * @param ch
	 */
	//@ requires ch != null;
	public void removeFromAll(ClientHandler ch) {
		all.remove(ch);
	}

	/**
	 * Removes the <code> ClientHandler </code> from his current state
	 * 
	 * @param ch
	 */
	//@ requires ch != null;
	public void removeHandler(ClientHandler ch) {
		if (start.contains(ch)) {
			start.remove(ch);
		} else if (identified.contains(ch)) {
			identified.remove(ch);
		}
		for (List<ClientHandler> l : queues.values()) {
			if (l.contains(ch)) {
				l.remove(ch);
			}
		}

	}

	/**
	 * If the <code> ClientHandler </code> is in a game, remove him from it
	 * 
	 * @param ch
	 */
	//@ requires ch != null;
	public void removeGameHandler(ClientHandler ch) {
		for (List<ClientHandler> l : games.values()) {
			if (l.contains(ch)) {
				l.clear();
			}
		}
	}

	/**
	 * Starts a game with the given <code> List </code> of
	 * <code> ClientHandlers </code>
	 * 
	 * @param list
	 */
	//@ requires list != null;
	//TODO remove players from the other QUEUS?????? - Egbert
	public void startGame(List<ClientHandler> list) {
		List<Player> players = new ArrayList<>();
		List<ClientHandler> list1 = new ArrayList<>(list);
		for (ClientHandler ch : list) {
			ch.getPlayer().newGame(); // So we start off clean

			players.add(ch.getPlayer());
		}
		
		Game game = new Game(list);
		
		//TODO merge those two for loops together?
		for (ClientHandler ch : list) {
			ch.setGame(game);
			ch.getGame().setRunning(true);
		}
		broadcast(list, Protocol.getInstance().serverStartGame(players));

		game.run();
		
		Player p = playerWithHighestScorePossible(players);

		game.setTurn(players.indexOf(p));

		broadcast(list, Protocol.getInstance().serverTurn(game.getPlayerTurn()));
		
		for(List<ClientHandler> l : queues.values()) {
			for(ClientHandler c : list1) {
				l.remove(c);
			}
		}
	}

	/**
	 * Determines which <code> Player </code> should start
	 * @param players
	 * @return
	 */
	//@ requires players != null;
	private Player playerWithHighestScorePossible(List<Player> players) {
		Player result = null;

		int lengthStreak = 0;

		for (Player p : players) {
			int tussenLengthStreak = p.getLengthStreak();

			if (tussenLengthStreak >= lengthStreak) {
				lengthStreak = tussenLengthStreak;
				result = p;
			}
		}

		return result;
	}

	/**
	 * Checks whether one of the queues has the set amount of players required
	 * to start a <code> Game </code>
	 */
	
	public void checkQueues() {
		for (Entry<Integer, List<ClientHandler>> entry : ((TreeMap<Integer, List<ClientHandler>>) queues)
				.descendingMap().entrySet()) {
			List<ClientHandler> queue = (List<ClientHandler>) entry.getValue();

			if ((int) entry.getKey() == queue.size()) {

				gameCounter++;

				games.put(gameCounter, queue);

				startGame(queues.get((int) entry.getKey()));

				queues.get((int) entry.getKey()).clear();

				return;
			}
		}
	}

	/**
	 * Analyze the input from the <code> Client </code> and act accordingly
	 * 
	 * @param input
	 * @param ch
	 */
	//@ requires input != null;
	//@ requires ch != null;
	public void getCommand(String input, ClientHandler ch) {
		String[] inputArr = input.split(" ");
		if (inputArr[0] == null) {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_COMMAND));
			return;
		}

		switch (inputArr[0]) {

			case IProtocol.CLIENT_IDENTIFY:
				if (inputArr.length > 1) {
					if (ch.getGame() != null) {
						ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_COMMAND));
						break;
					}
	
					handle.handleIdentifyName(inputArr, ch);
					
					if (handle.getWentWell()) {
						handle.setWentWell(true);
						handle.handleIdentifyFeatures(inputArr, ch);
					}
	
					if (handle.getWentWell()) {
						removeHandler(ch);
						identified.add(ch);
					}
	
					handle.setWentWell(true);
				} else {
					ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_PARAMETER));
				}
				break;
	
			case IProtocol.CLIENT_QUIT:
				ch.shutDown();
				break;
	
			case IProtocol.CLIENT_MOVE_PUT:
				if (inputArr.length > 1) {
					handle.handleMovePut(inputArr, ch);
				} else {
					ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_PARAMETER));
				}
				break;
	
			case IProtocol.CLIENT_MOVE_TRADE:
				if (inputArr.length > 1) {
					handle.handleMoveTrade(inputArr, ch);
				} else {
					ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_PARAMETER));
				}
				break;
	
			case IProtocol.CLIENT_QUEUE:
				if (inputArr.length > 1) {
					if(ch.getClientName().equals("")) {
						break;
					}
					
					removeHandler(ch);
					handle.handleQueue(inputArr, ch);
					
					if (handle.getWentWell()) {
						checkQueues();
					} else {
						removeHandler(ch);
						identified.add(ch);
					}
					handle.setWentWell(true);
				} else {
					ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_PARAMETER));
				}
	
				break;
			case IProtocol.CLIENT_CHAT:
				if (inputArr.length > 1) {
					if (ch.isEnabled(IProtocol.Feature.CHAT)) {
						handle.handleChat(inputArr, ch);
					}
				} else {
					ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_PARAMETER));
				}
	
				break;
			/*
			 * case IProtocol.CLIENT_CHALLENGE: case
			 * IProtocol.CLIENT_CHALLENGE_ACCEPT: case
			 * IProtocol.CLIENT_CHALLENGE_DECLINE: case
			 * IProtocol.CLIENT_LEADERBOARD: case IProtocol.CLIENT_LOBBY:
			 */
			default:
				ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_COMMAND));
				break;
		}
	}

}
