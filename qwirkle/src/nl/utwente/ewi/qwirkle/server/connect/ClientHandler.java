package nl.utwente.ewi.qwirkle.server.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

	private Server server;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName;

	public ClientHandler(Server server, Socket socket) {
		this.server = server;

		try {
			this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true) {
			try {
				String input = in.readLine();
				// TODO sent notification to server about the input that has been send by client
			} catch(IOException e) {
				
			}
		}
	}
	
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
	
	private void shutDown() {
		server.removeHandler(this);
	}

}
