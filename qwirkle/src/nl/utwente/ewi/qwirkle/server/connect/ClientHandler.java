package nl.utwente.ewi.qwirkle.server.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.model.player.SocketPlayer;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.Protocol;
import nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature;
import nl.utwente.ewi.qwirkle.server.Game;

public class ClientHandler extends Thread {

	private Server server;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName = "";
	private List<Feature> features;
	private Game game = null;
	private Player player;
	Map<IProtocol.Feature, Boolean> featuresEnabled = new HashMap<>();
	
	/**
	 * Constructor of the ClientHandler which initialises the reader and writer
	 * @param server
	 * @param socket
	 * @throws IOException
	 */
	public ClientHandler(Server server, Socket socket) throws IOException {
		this.server = server;
		this.sock = socket;
		this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}

	/**
	 * Reads input from client and passes it to the server
	 */
	public void run() {
		boolean isActive = true;
		while (isActive) {
			try {
				String input = in.readLine();
				System.out.println("From " + getClientName() + " to server: " + input);
				server.getCommand(input, this);
			} catch (IOException e) {
				shutDown();
				isActive = false;
			}
		}
	}

	/**
	 * 
	 * @return returns the <code> Player </code> connected to this <code>ClientHandler</code>
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean isEnabled(Feature f) {
		return featuresEnabled.get(f);
	}
	
	/**
	 * Sets the <code> Player </code> connected to this <code> ClientHandler </code>
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * 
	 * @return returns the <code> Game </code> this <code> ClientHandler </code> is in, if he is in any
	 */
	public Game getGame() {
		return this.game;
	}

	/**
	 * Sets the clientName value and creates a new <code> SocketPlayer </code> with that name
	 * @param name
	 */
	public void setClientName(String name) {
		this.clientName = name;
		setPlayer(new SocketPlayer(name));
	}
	
	/**
	 * 
	 * @return returns the name of the <code> Player </code>
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Sets the features 
	 * @param features
	 */
	public void setFeatures(List<Feature> features) {
		for(Feature featu : IProtocol.Feature.values()) {
			featuresEnabled.put(featu, false);
		}
		for(Feature feat : features) {
			featuresEnabled.put(feat, true);
		}
		this.features = features;
	}
	
	/**
	 * Returns an array of features
	 * @return
	 */
	public List<IProtocol.Feature> getFeatures() {
		return features;
	}
	/**
	 * Sets the game 
	 * @param g
	 */
	public void setGame(Game g) {
		this.game = g;
	}

	/**
	 * Sends a message to the client
	 * Shuts down if the connection is lost
	 * @param msg
	 */
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			System.out.println("Socket connection lost");
			shutDown();
		}
	}
	
	/**
	 * Removes the <code> ClientHandler </code> from all <code> Server </code> connections and ends the <code> Game </code> if he was in any
	 */
	public void shutDown() {
		server.removeHandler(this);
		server.removeFromAll(this);
		if(getGame() != null) {
			server.removeGameHandler(this);
			int[] scores = new int[this.getGame().getPlayers().size()];
			int i = 0;
			List<Player> players = new ArrayList<>();
			for(ClientHandler ch : this.getGame().getPlayers()) {
				scores [i] = ch.getPlayer().getScore();
				players.add(ch.getPlayer());
				i++;
			}
			List<ClientHandler> list = this.getGame().getPlayers();
			list.remove(this);
			server.broadcast(list, Protocol.getInstance().serverEndGame(players, scores, 0));
			
			//TODO game instance should close here right?
			return;
		}
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
