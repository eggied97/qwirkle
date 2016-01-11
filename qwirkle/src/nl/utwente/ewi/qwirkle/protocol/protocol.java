package nl.utwente.ewi.qwirkle.protocol;

import java.awt.Point;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.*;

public class protocol implements IProtocol {
	private final static boolean DEBUG = true;

	/*
	 * Client
	 */
	public String clientGetConnectString(String name, String[] features) {
		String result = "";

		result += this.CLIENT_IDENTIFY;
		result += " " + name;

		int count = 0;

		for (String f : features) {
			if (count >= 1) {
				result += ",";
			}

			result += f;

			count += 1;
		}

		if (DEBUG){
			System.out.println(result);
		}

		return result;
	}

	public String clientQuit() {
		String result = "";

		result += this.CLIENT_QUIT;

		if (DEBUG){
			System.out.println(result);
		}
		
		return result;
	}
	
	public String clientQueue(int[] noOfPlayers){
		String result = "";

		result += this.CLIENT_QUEUE;
		result += " ";
		
		int count = 0;

		for (int no : noOfPlayers) {
			if (count >= 1) {
				result += ",";
			}

			result += no;

			count += 1;
		}

		if (DEBUG){
			System.out.println(result);
		}
		
		return result;
	}
	
	public String clientPutMove(List<Move> moves){
		String result = "";

		result += this.CLIENT_MOVE_PUT;
		result += " ";
		
		for(Move m : moves){
			result += m.getTile().toString();
			result += "@";
			
			Point p = m.getPoint();
			result += p.getX()+","+p.getY();
			
			result += " ";
		}

		if (DEBUG){
			System.out.println(result);
		}
		
		return result;
	}

}
