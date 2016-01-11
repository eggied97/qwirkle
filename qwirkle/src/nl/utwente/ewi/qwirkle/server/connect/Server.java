package nl.utwente.ewi.qwirkle.server.connect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {
	
	
	
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Give only the port as input");
			System.exit(0);
		}
		
		Server server = new Server(Integer.parseInt(args[0]));
		server.run();
	}
	
	private int port;
	private ServerSocket serverSock;
	private List<ClientHandler> start;
	private List<ClientHandler> identified;
	private Map<Integer, List<ClientHandler>> queues;
	private Map<Integer, List<ClientHandler>>  games;
	
	
	
	public Server(int port) {
		this.port = port;
		queues.put(2, new ArrayList<ClientHandler>());
		queues.put(3, new ArrayList<ClientHandler>());
		queues.put(4, new ArrayList<ClientHandler>());
	}
	
	
	public void run() {
		try {
			serverSock = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				Socket client = serverSock.accept();
				System.out.println("Client connected!");
				ClientHandler handler = new ClientHandler(this, client);
				handler.start();
				start.add(handler);
				sendIdentifier();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendIdentifier() {
		for (ClientHandler ch : start) {
			ch.sendMessage("Identify yourself: CONNECT <name> [<feature>]");
		}
	}
	
	public void removeHandler(ClientHandler ch) {
		if(start.contains(ch)) {
			start.remove(ch);
			return;
		} else if(identified.contains(ch)) {
			identified.remove(ch);
			return;
		}
		// TODO add lists when created
		for(List<ClientHandler> l : games.values()) {
			if(l.contains(ch)) {
				l.remove(ch);
			}
		}
		for(List<ClientHandler> l : queues.values()) {
			if(l.contains(ch)) {
				l.remove(ch);
			}
		}
		
	}
	

}
