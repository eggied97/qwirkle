package nl.utwente.ewi.qwirkle.client.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends Thread {

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Requires 2 arguments: <host> <port>");
			System.exit(0);
		}

		InetAddress host = null;
		int port = 0;

		try {
			host = InetAddress.getByName(args[0]);
		} catch (IOException e) {
			System.out.println("No host known by that name");
			System.exit(0);
		}

		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			System.out.println("No valid portnumber");
			System.exit(0);
		}

		try {
			Client client = new Client(args[0], host, port);
			client.sendMessage(args[0]);
			client.start();

			do {
				String input = null;
				input = systemIn.readLine();
				// TODO transform input via protocol
				client.sendMessage(input);
			} while (true);

		} catch (IOException e) {
			System.out.println("Could not construct a client object!");
			System.exit(0);
		}
	}

	private String clientName;
	private Socket sock;
	private static BufferedReader systemIn;
	private BufferedReader in;
	private BufferedWriter out;

	public Client(String name, InetAddress host, int port) {
		this.clientName = name;

		try {
			this.sock = new Socket(host, port);
			this.systemIn = new BufferedReader(new InputStreamReader(System.in));
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Could not create a socket on " + host + " and port " + port);
		}
	}
	
	public void run() {
		try {
			while(true) {
				System.out.println(in.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
