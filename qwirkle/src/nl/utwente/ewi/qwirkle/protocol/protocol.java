package nl.utwente.ewi.qwirkle.protocol;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.model.player.Player;

public class protocol implements IProtocol {
	private final static boolean DEBUG = true;
	
	public protocol() {
	}

	public static protocol getInstance() {
		return new protocol();
	}

	/*
	 * Client
	 */
	public void printDebug(String result) {
		if (DEBUG) {
			System.out.println("Protocol > "+result);
		}
	}
	
	public String clientGetConnectString(String name, IProtocol.Feature[] features) {
		String result = "";

		result += this.CLIENT_IDENTIFY;
		result += " " + name;

		int count = 0;

		for (Feature f : features) {
			if (count >= 1) {
				result += ",";
			}

			result += f.toString();

			count += 1;
		}

		printDebug(result);

		return result;
	}

	public String clientQuit() {
		String result = "";

		result += this.CLIENT_QUIT;

		printDebug(result);

		return result;
	}

	public String clientQueue(int[] noOfPlayers) {
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

		printDebug(result);

		return result;
	}

	public String clientPutMove(List<Move> moves) {
		String result = "";

		result += this.CLIENT_MOVE_PUT;
		result += " ";

		int count = 0;

		for (Move m : moves) {
			result += m.getTile().toString();
			result += "@";

			Point p = m.getPoint();
			result += p.getX() + "," + p.getY();

			count += 1;

			if (moves.size() > 1 && count < moves.size()) {
				result += " ";
			}
		}

		printDebug(result);

		return result;
	}

	public String clientTradeMove(List<Tile> tiles) {
		String result = "";

		result += this.CLIENT_MOVE_TRADE;
		result += " ";

		int count = 0;

		for (Tile t : tiles) {
			result += t.toString();

			count += 1;

			if (tiles.size() > 1 && count < tiles.size()) {
				result += " ";
			}
		}

		printDebug(result);

		return result;
	}

	/*
	 * Server
	 */

	public String serverConnectOk(IProtocol.Feature[] features) {
		String result = "";

		result += this.SERVER_IDENITFY;
		result += " ";

		int count = 0;

		for (Feature f : features) {
			if (count >= 1) {
				result += ",";
			}

			result += f.toString();

			count += 1;
		}

		printDebug(result);

		return result;
	}

	public String serverStartGame(List<Player> players) {
		String result = "";

		result += this.SERVER_GAMESTART;
		result += " ";

		int count = 0;

		for (Player p : players) {
			result += p.getName();

			count += 1;

			if (players.size() > 1 && count < players.size()) {
				result += " ";
			}
		}

		printDebug(result);

		return result;
	}

	/**
	 * @param wonByWin
	 *            - 1 if won by win, 0 if won by error
	 */
	public String serverEndGame(List<Player> players, int[] scores, int wonByWin) {
		String result = "";

		result += this.SERVER_GAMEEND;
		result += " ";
		result += (wonByWin == 1 ? "WIN" : "ERROR");
		result += " ";

		int count = 0;

		for (Player p : players) {
			result += p.getName();
			result += ",";
			result += scores[count];

			count += 1;

			if (players.size() > 1 && count < players.size()) {
				result += " ";
			}
		}

		printDebug(result);

		return result;
	}

	public String serverTurn(Player p) {
		String result = "";

		result += this.SERVER_TURN;
		result += " ";
		result += p.getName();

		printDebug(result);

		return result;
	}

	public String serverDrawTile(List<Tile> tiles) {
		String result = "";

		result += this.SERVER_DRAWTILE;
		result += " ";

		int count = 0;

		for (Tile t : tiles) {
			result += t.toString();

			count += 1;

			if (tiles.size() > 1 && count < tiles.size()) {
				result += " ";
			}
		}

		printDebug(result);

		return result;
	}

	public String serverMovePut(List<Move> moves) {
		String result = "";

		result += this.SERVER_MOVE_PUT;
		result += " ";

		int count = 0;

		for (Move m : moves) {
			result += m.getTile().toString();
			result += "@";

			Point p = m.getPoint();
			result += p.getX() + "," + p.getY();

			count += 1;

			if (moves.size() > 1 && count < moves.size()) {
				result += " ";
			}
		}

		printDebug(result);

		return result;
	}

	public String serverMoveTrade(int noOfTilesTraded) {
		String result = "";

		result += this.SERVER_MOVE_PUT;
		result += " ";
		result += noOfTilesTraded;

		printDebug(result);

		return result;
	}

	public String serverError(Error errorCode) {
		return serverError(errorCode, null);
	}

	public String serverError(Error err, String message) {
		String result = "";

		result += this.SERVER_ERROR;
		result += " ";
		result += err.toString();

		if (message != null) {
			result += " ";
			result += message;
		}

		printDebug(result);

		return result;
	}

	/*
	 * Chat
	 */

	public String clientChat(String channel, String message) {
		String result = "";

		result += this.CLIENT_CHAT;
		result += " ";
		result += channel;
		result += " ";
		result += message;

		printDebug(result);

		return result;
	}

	public String serverChat(String channel, String sender, String message) {
		String result = "";

		result += this.CLIENT_CHAT;
		result += " ";
		result += channel;
		result += " ";
		result += sender;
		result += " ";
		result += message;

		printDebug(result);

		return result;
	}
}
