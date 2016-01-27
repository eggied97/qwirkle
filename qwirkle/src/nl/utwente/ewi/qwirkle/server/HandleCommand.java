package nl.utwente.ewi.qwirkle.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature;
import nl.utwente.ewi.qwirkle.protocol.Protocol;
import nl.utwente.ewi.qwirkle.server.connect.ClientHandler;
import nl.utwente.ewi.qwirkle.server.connect.Server;
import nl.utwente.ewi.qwirkle.server.score.ScoreCalc;

public class HandleCommand {

	private Server server;
	private Protocol protocol = new Protocol();
	private ValidMove valid = new ValidMove();
	private boolean wentWell = true;
	private final static String REGEX = "^[a-zA-Z0-9-_]{2,16}$";

	/**
	 * 
	 * @param server
	 */
	// @requires server != null;
	public HandleCommand(Server server) {
		this.server = server;
	}

	/**
	 * Returns true if all went well.
	 * 
	 * @return
	 */
	/* @ pure */ public boolean getWentWell() {
		return wentWell;
	}

	/**
	 * Set the value of WentWell.
	 * 
	 * @param t
	 */
	// @ ensures getWentWell() == t;
	/* @ pure */public void setWentWell(boolean t) {
		wentWell = t;
	}

	/**
	 * Returns an instance of <code> HandleCommand </code>.
	 * 
	 * @param server
	 * @return
	 */
	// @ requires server != null;
	public static HandleCommand getInstance(Server server) {
		return new HandleCommand(server);
	}

	/**
	 * Takes the name from the String array and match it with the given REGEX.
	 * Furthermore checks whether this name is already in use.
	 * 
	 * @param strAy
	 * @param ch
	 */
	// @ requires strAy != null;
	// @ requires strAy.length() > 2;
	// @ requires ch != null;

	public void handleIdentifyName(String[] strAy, ClientHandler ch) {
		String name = strAy[1];
		if (name.matches(REGEX) && !name.equals(null)) {
			for (ClientHandler handler : server.getAll()) {
				// Check if you're not trying to get the name of the object
				// whose
				// name you want to set
				if (handler.equals(ch)) {
				} else if (handler.getClientName().equals(name)) {
					ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.NAME_USED));
					wentWell = false;
					return;
				}
			}

			ch.setClientName(name);
		} else {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.NAME_INVALID));
			wentWell = false;
			return;
		}

	}

	/**
	 * Checks the String array for features that are implemented by the Client
	 * If there are any, add them to a list of features
	 * 
	 * @param strAy
	 * @param ch
	 */
	// @ requires ch != null;
	// @ requires strAy != null;
	public void handleIdentifyFeatures(String[] strAy, ClientHandler ch) {
		List<IProtocol.Feature> features = new ArrayList<>();

		//IDENTITY NAME asd,asd
		
		
		
		if (strAy.length > 2) {
			
			String stringFeatures = strAy[2];
			String[] stringsFeature = stringFeatures.split(",");
			
			for (int i = 0; i < stringsFeature.length; i++) {
				try {
					switch (IProtocol.Feature.valueOf(stringsFeature[i])) {
					case CHAT:
						features.add(IProtocol.Feature.CHAT);
						break;
					case CHALLENGE:
						features.add(IProtocol.Feature.CHALLENGE);
						break;
					case LEADERBOARD:
						features.add(IProtocol.Feature.LEADERBOARD);
						break;
					case LOBBY:
						features.add(IProtocol.Feature.LOBBY);
						break;
					default:
						setWentWell(false);
						return;
					}
				} catch (IllegalArgumentException ex) {
					setWentWell(false);
					ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_PARAMETER));
				}

			}
			ch.setFeatures(features);
			IProtocol.Feature[] featAr = new IProtocol.Feature[features.size()];
			int i = 0;
			for (IProtocol.Feature feat : features) {
				featAr[i] = feat;
				i++;
			}
		}
		ch.sendMessage(protocol.serverConnectOk(server.getFeatures()));
	}

	/**
	 * Check the String array for queue numbers to add the
	 * <code> ClientHandler </code> to.
	 * 
	 * @param strAy
	 * @param ch
	 */
	// @ requires ch != null;
	// @ requires strAy != null;
	public void handleQueue(String[] strAy, ClientHandler ch) {
		String queue = strAy[1];
		String[] queueSpl = queue.split(",");
		Map<Integer, List<ClientHandler>> map = server.getQueues();
		for (int i = 0; i < queueSpl.length; i++) {
			int q = 0;
			try {
				q = Integer.parseInt(queueSpl[i]);
			} catch (NumberFormatException e) {
				ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.QUEUE_INVALID));
				setWentWell(false);
				return;
			}
			switch (q) {
			case 2:
				map.get(2).add(ch);
				break;
			case 3:
				map.get(3).add(ch);
				break;
			case 4:
				map.get(4).add(ch);
				break;
			default:
				ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.QUEUE_INVALID));
				setWentWell(false);
				return;
			}
		}

		int[] result = new int[queueSpl.length];
		int i = 0;

		for (String r : queueSpl) {
			result[i] = Integer.parseInt(r);
			i++;
		}

		ch.sendMessage(Protocol.getInstance().serverQueueOk(result));

	}

	/**
	 * Checks whether the <code> ClientHandler </code> has the turn Then check
	 * whether or not the trade is valid, and throw a fitting error if not If it
	 * is valid, handle the trade and replace the tiles.
	 * 
	 * @param strAy
	 * @param ch
	 */
	// @ requires ch != null;
	// @ requires strAy != null;
	public void handleMoveTrade(String[] strAy, ClientHandler ch) {

		if (!ch.getGame().hasTurn(ch)) {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.ILLEGAL_STATE));
			return;
		}

		List<Tile> tiles = new ArrayList<>();
		for (int i = 1; i < strAy.length; i++) {
			try {
				tiles.add(new Tile(Integer.parseInt(strAy[i])));
			} catch (NumberFormatException e) {
				ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_PARAMETER));
				return;
			}
		}

		List<Integer> tilesInt = new ArrayList<>();
		
		for (Tile t : tiles) {
			tilesInt.add(t.getIntOfTile());
		}

		if (ch.getGame().getBag().isEmpty() || ch.getGame().getBag().getAmountOfTiles() - tiles.size() < 0) {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.DECK_EMPTY));
			return;
		} else if (ch.getGame().getBoard().isEmpty()) {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.TRADE_FIRST_TURN));
			return;
		} else if (!checkTiles(tiles, ch)) {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.MOVE_TILES_UNOWNED));
			return;
		}

		List<Tile> newTiles = new ArrayList<>();
		
		ch.getPlayer().removeTilesFromHand(tiles);

		newTiles = ch.getGame().getBag().getRandomTile(tiles.size());
		ch.getPlayer().bagToHand(newTiles);
		ch.sendMessage(Protocol.getInstance().serverDrawTile(newTiles));
		
		ch.getGame().getBag().addTiles(tilesInt);
		
		server.broadcast(ch.getGame().getPlayers(), protocol.serverMoveTrade(tilesInt.size()));

		handleTurn(ch);
	}

	/**
	 * Check whether the <code> ClientHandler </code> owns all given tiles.
	 * 
	 * @param tiles
	 * @param ch
	 * @return
	 */
	// @ requires ch != null;
	// @ requires tiles != null;
	// @ ensures ((\forall int i; (i >= 0 && i < tiles.size()) ==>
	// (ch.getPlayer().getHand().contains(tiles.get(i)))));
	public boolean checkTiles(List<Tile> tiles, ClientHandler ch) {
		List<Tile> playerTiles = ch.getPlayer().getHand();
		server.print(playerTiles.size()+"");
		for (Tile t : tiles) {
			if (!playerTiles.contains(t)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Transform the String array into tiles and points Validate the moves Put
	 * the tiles on the board.
	 * 
	 * @param strAy
	 * @param ch
	 */
	// @ requires ch != null;
	// @ requires strAy != null;
	public void handleMovePut(String[] strAy, ClientHandler ch) {

		if (!ch.getGame().hasTurn(ch)) {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.ILLEGAL_STATE));
			return;
		}

		List<Move> moves = new ArrayList<>();
		List<Tile> tiles = new ArrayList<>();
		for (int i = 1; i < strAy.length; i++) {
			try {
				String[] a = strAy[i].split("@");
				int tileNo = Integer.parseInt(a[0]);
				String[] b = a[1].split(",");
				int x = Integer.parseInt(b[0]);
				int y = Integer.parseInt(b[1]);
				Tile t = new Tile(tileNo);
				Move m = new Move(new Point(x, y), t);
				tiles.add(t);
				moves.add(m);
			} catch (NumberFormatException e) {
				ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_PARAMETER));
				return;
			}
		}
		if (ch.getGame().getBoard().isEmpty()) {
			boolean startPoint = false;
			for (Move m : moves) {
				if (m.getPoint().equals(new Point(0, 0))) {
					startPoint = true;
				}
			}
			if (!startPoint) {
				ch.sendMessage(protocol.serverError(IProtocol.Error.MOVE_INVALID));
				return;
			}
		}

		if (!checkTiles(tiles, ch)) {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.MOVE_TILES_UNOWNED));
			return;
		}
		if (!valid.validMoveSet(moves, ch.getGame().getBoard())) {
			ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.MOVE_INVALID));
			return;
		}
		ch.getPlayer().removeTilesFromHand(tiles);
		
		ch.getGame().getBoard().putTile(moves);

		ch.getPlayer().addScore(ScoreCalc.getInstance().calculate(ch.getGame().getBoard(), moves));

		server.broadcast(ch.getGame().getPlayers(), Protocol.getInstance().serverMovePut(moves));

		// TODO what to do when list.size() > bag.size() || bag.isEMpty()?
		if (ch.getGame().getBag().getAmountOfTiles() - tiles.size() >= 0) {
			List<Tile> newTiles = ch.getGame().getBag().getRandomTile(tiles.size());
			ch.getPlayer().bagToHand(newTiles);
			ch.sendMessage(Protocol.getInstance().serverDrawTile(newTiles));
		} else {
			if(ch.getGame().getBag().getAmountOfTiles() > 0) {
				List<Tile> newTiles = ch.getGame().getBag()
						.getRandomTile(ch.getGame().getBag().getAmountOfTiles());
				ch.getPlayer().bagToHand(newTiles);
				ch.sendMessage(Protocol.getInstance().serverDrawTile(newTiles));
			}
		}

		handleTurn(ch);
	}

	/**
	 * Checks whether the game ends, if it does not, pass the turn to the next
	 * player.
	 * 
	 * @param ch
	 */
	// @ requires ch != null;
	public void handleTurn(ClientHandler ch) {
		
		ch.getGame().nextTurn();
		
		if (ch.getGame().gameEnd()) {
			handleEndGame(ch);
			return;
		}

		server.broadcast(ch.getGame().getPlayers(), protocol.serverTurn(ch.getGame().getPlayerTurn()));

		List<Player> playersPlay = new ArrayList<>();
		for (ClientHandler player : ch.getGame().getPlayers()) {
			playersPlay.add(player.getPlayer());
		}
		for (int i = 0; i < handleScores(ch).length; i++) {
			server.print(ch.getGame().getPlayers().get(i).toString() + handleScores(ch)[i]);
		}
	}

	/**
	 * End the game and round up the scores.
	 * 
	 * @param ch
	 */
	// @ requires ch != null;
	public void handleEndGame(ClientHandler ch) {
			ch.getGame().setRunning(false);

			List<ClientHandler> players = ch.getGame().getPlayers();
			List<Player> playersPlay = new ArrayList<>();
			int[] scores = new int[players.size()];
			int i = 0;

			for (ClientHandler player : players) {
				player.setGame(null);
				playersPlay.add(player.getPlayer());
				scores[i] = player.getPlayer().getScore();
				i++;
			}

			server.broadcast(players, protocol.serverEndGame(playersPlay, scores, 1));
			server.addIdentified(players);
	}

	/**
	 * Present a score array with the scores in order of the players in the
	 * game. array
	 * 
	 * @param ch
	 * @return
	 */
	// @ requires ch != null;
	public int[] handleScores(ClientHandler ch) {
		List<ClientHandler> players = ch.getGame().getPlayers();
		List<Player> playersPlay = new ArrayList<>();
		int[] scores = new int[players.size()];
		int i = 0;
		for (ClientHandler player : players) {
			playersPlay.add(player.getPlayer());
			scores[i] = player.getPlayer().getScore();
			i++;
		}
		return scores;
	}

	/**
	 * Checks to whom the message is concerned and write it to him/her if it is
	 * a valid channel.
	 * 
	 * @param strAy
	 * @param ch
	 */
	// @ requires ch != null;
	// @ requires strAy != null;
	public void handleChat(String[] strAy, ClientHandler ch) {
		String channel = strAy[1];
		System.out.println(channel);
		String[] message = Arrays.copyOfRange(strAy, 2, strAy.length);
		StringBuilder builder = new StringBuilder();
		for (String s : message) {
			builder.append(s + " ");
		}
		String messa = builder.toString();
		if (channel.equals("global")) {
			List<ClientHandler> receivers = new ArrayList<>();
			for (ClientHandler c : ch.getGame().getPlayers()) {
				for (Feature f : c.getFeatures()) {
					if (f.equals(Feature.CHAT)) {
						receivers.add(c);
					}
				}
			}
			server.broadcast(receivers, Protocol.getInstance().serverChat(channel, ch.getClientName(), messa));
			return;
		} else {
			for (ClientHandler clienthand : ch.getGame().getPlayers()) {
				System.out.println("@" + channel);
				if (("@" + clienthand.getClientName()).equals(channel)) {
					for (Feature f : clienthand.getFeatures()) {
						if (f.equals(Feature.CHAT)) {
							clienthand
									.sendMessage(Protocol.getInstance().serverChat(channel, ch.getClientName(), messa));
						}
					}
					return;
				}
			}
		}
		ch.sendMessage(Protocol.getInstance().serverError(IProtocol.Error.INVALID_CHANNEL));
	}

	/**
	 * Checks whether there are any valid moves left for the next player,
	 * otherwise, pass.
	 * 
	 * @param play
	 * @param b
	 * @return
	 */
	// @ requires ch != null;
	// @ requires strAy != null;
	public boolean handleMovesLeft(Player play, Board b) {
		boolean moveIsValid = false;
		List<Point> emptySpots = b.getEmptySpots();
		List<Tile> hand = play.getHand();
		for (int i = 0; i < hand.size() && !moveIsValid; i++) {
			Tile t = hand.get(i);
			for (Point p : emptySpots) {
				if (!moveIsValid) {
					Move m = new Move(p, t);
					moveIsValid = valid.isValidMove(m, b);
					if (moveIsValid) {
						return true;
					}
				}
			}
		}
		return false;

	}

	/**
	 * Tests whether a <code> Players </code> can do a valid <code> Move </code>
	 * otherwise pass.
	 * 
	 * @param ch
	 */
	// @ requires ch != null;
	public void handlePass(ClientHandler ch) {
		int counter = 0;
		while (!handleMovesLeft(ch.getGame().getPlayerTurn(), ch.getGame().getBoard())) {
			server.broadcast(ch.getGame().getPlayers(), protocol.serverPass(ch.getGame().getPlayerTurn()));
			server.broadcast(ch.getGame().getPlayers(), protocol.serverTurn(ch.getGame().getPlayerTurn()));
			ch.getGame().nextTurn();
			counter++;
			if (counter == ch.getGame().getPlayers().size()) {
				handleEndGame(ch);
				return;
			}
		}
	}
}
