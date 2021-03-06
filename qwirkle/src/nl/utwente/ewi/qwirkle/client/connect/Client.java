package nl.utwente.ewi.qwirkle.client.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import nl.utwente.ewi.qwirkle.callback.ResultCallback;

public class Client extends Thread {
	private String clientName;
	private Socket sock;
	private static BufferedReader systemIn;
	private BufferedReader in;
	private BufferedWriter out;
	private ResultCallback rc = null;

	/**
	 * creates a <code> Client </code> instance, to talk to server.
	 * 
	 * @param host
	 *            the <code> InetAddress </code> of the server
	 * @param port
	 *            the port of the server
	 * @throws IOException
	 *             if there is no server on the given host & port
	 */
	public Client(InetAddress host, int port) throws IOException {
		this.clientName = host.getHostAddress();

		this.sock = new Socket(host, port);
		this.systemIn = new BufferedReader(new InputStreamReader(System.in));
		this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	/**
	 * sets the callback.
	 * 
	 * @param aRC
	 *            the class that implements
	 *            {@link nl.utwente.ewi.qwirkle.callback.ResultCallback}
	 */
	public void setCallback(ResultCallback aRC) {
		this.rc = aRC;
	}

	/**
	 * needs to be called in order to receive messages.
	 */
	public void run() {
		try {
			while (true) {
				if (rc != null) {
					rc.resultFromServer(in.readLine());
				}
			}
		} catch (IOException e) {
			// server closed
			System.out.println("Server disconnected");
			System.exit(0);
		}
	}

	/**
	 * writes a given message to the connected server.
	 * 
	 * @param msg
	 *            the message to be send
	 */
	public void sendMessage(String msg) {
		try {
			out.write(msg);
			out.newLine();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
