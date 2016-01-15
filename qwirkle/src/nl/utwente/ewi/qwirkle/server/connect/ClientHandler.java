package nl.utwente.ewi.qwirkle.server.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.model.player.SocketPlayer;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature;
import nl.utwente.ewi.qwirkle.server.Game;

public class ClientHandler extends Thread {

	private Server server;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName;
	private List<Feature> features;
	private Game game = null;
	private Player player;
	
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
		while (true) {
			try {
				String input = in.readLine();
				System.out.println("From client to server: " + input);
				server.getCommand(input, this);
			} catch (IOException e) {
				shutDown();
			}
		}
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Game getGame() {
		return this.game;
	}

	public void setClientName(String name) {
		this.clientName = name;
		setPlayer(new SocketPlayer(name));
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
	
	public void setGame(Game g) {
		this.game = g;
	}

	/**
	 * Sends a message to the client
	 * 
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
			server.broadcast(list, protocol.getInstance().serverEndGame(players, scores, 0));
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
