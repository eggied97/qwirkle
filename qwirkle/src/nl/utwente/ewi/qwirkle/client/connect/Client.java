package nl.utwente.ewi.qwirkle.client.connect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client extends Thread {
	private String clientName;
	private Socket sock;
	private static BufferedReader systemIn;
	private BufferedReader in;
	private BufferedWriter out;
	private resultCallback rc = null;

	public Client(InetAddress host, int port) {
		this.clientName = host.getHostAddress();

		try {
			this.sock = new Socket(host, port);
			this.systemIn = new BufferedReader(new InputStreamReader(System.in));
			this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		} catch (IOException e) {
			System.out.println("Could not create a socket on " + host + " and port " + port);
		}
	}
	
	public void setCallback(resultCallback rc){
		this.rc = rc;
	}
	
	public void run() {
		try {
			while(true) {
				if(rc != null){
					rc.resultFromServer(in.readLine());
				}
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
