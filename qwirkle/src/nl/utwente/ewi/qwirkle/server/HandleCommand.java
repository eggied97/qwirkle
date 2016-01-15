package nl.utwente.ewi.qwirkle.server;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.client.connect.Client;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.server.connect.ClientHandler;
import nl.utwente.ewi.qwirkle.server.connect.Server;
import nl.utwente.ewi.qwirkle.server.score.ScoreCalc;

public class HandleCommand {

	private Server server;
	private protocol protocol = new protocol();
	private ValidMove valid = new ValidMove();
	private boolean wentWell = true;
	private final static String REGEX = "^[a-zA-Z0-9-_]{2,16}$";
	
	public HandleCommand(Server server) {
		this.server = server;
	}

	public boolean getWentWell() {
		return wentWell;
	}

	public void setWentWell(boolean t) {
		wentWell = t;
	}

	public static HandleCommand getInstance(Server server) {
		return new HandleCommand(server);
	}

	public void handleIdentifyName(String[] strAy, ClientHandler ch) {
		String name = strAy[1];
		if (name.matches(REGEX) && !name.equals(null)) { 
			for (ClientHandler handler : server.getAll()) {
				// Check if youre not trying to get the name of the object whose name you want to set
				if (handler.equals(ch)) {
				} else if (handler.getClientName().equals(name)) {
					ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.NAME_USED));
					wentWell = false;
					return;
				}
			}
			
			ch.setClientName(name);
		} else {
			ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.NAME_INVALID));
			wentWell = false;
			return;
		}

	}

	public void handleIdentifyFeatures(String[] strAy, ClientHandler ch) {
		List<IProtocol.Feature> features = new ArrayList<>();
		if (strAy.length > 2) {
			for (int i = 2; i < strAy.length; i++) {
				switch (IProtocol.Feature.valueOf(strAy[i])) {
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
					break;
				}
				i++;
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

	public void handleQueue(String[] strAy, ClientHandler ch) {
		String queue = strAy[1];
		String[] queueSpl = queue.split(",");
		Map<Integer, List<ClientHandler>> map = server.getQueues();
		for (int i = 0; i < queueSpl.length; i++) {
			switch (Integer.parseInt(queueSpl[i])) {
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
				ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.QUEUE_INVALID));
				break;
			}
		}

	}

	public void handleMoveTrade(String[] strAy, ClientHandler ch) {

		if(!ch.getGame().hasTurn(ch)) {
			return;
		}
		
		List<Tile> tiles = new ArrayList<>();
		for (int i = 1; i < strAy.length; i++) {
			tiles.add(new Tile(Integer.parseInt(strAy[i])));
		}
		
		List<Integer> tilesInt = new ArrayList<>();
		for (Tile t : tiles) {
			tilesInt.add(t.getIntOfTile());
		}

		if (ch.getGame().getBag().isEmpty() || ch.getGame().getBag().getAmountOfTiles() - tiles.size() < 0) {
			ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.DECK_EMPTY));
			return;
		} else if (ch.getGame().getBoard().isEmpty()) {
			ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.TRADE_FIRST_TURN));
			return;
		} else if (!checkTiles(tiles, ch)) {
			ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.MOVE_TILES_UNOWNED));
			return;
		}

		List<Tile> newTiles = ch.getGame().getBag().getRandomTile(tiles.size());
		List<Integer> newTilesInt = new ArrayList<>();
		
		for (Tile t : newTiles) {
			newTilesInt.add(t.getIntOfTile());
		}
		ch.getGame().getBag().addTiles(tilesInt);
		ch.getPlayer().bagToHand(newTiles);

		server.broadcast(ch.getGame().getPlayers(), protocol.serverMoveTrade(tilesInt.size()));
		ch.sendMessage(protocol.serverDrawTile(newTiles));
		
		handleTurn(ch);
	}

	public boolean checkTiles(List<Tile> tiles, ClientHandler ch) {
		List<Tile> playerTiles = ch.getPlayer().getHand();
		for (Tile t : tiles) {
			if (!playerTiles.contains(t)) {
				return false;
			}
		}
		return true;
	}

	public void handleMovePut(String[] strAy, ClientHandler ch) {
		if(!ch.getGame().hasTurn(ch)) {
			//TODO may be error? -Egbert
			return;
		}
		List<Move> moves = new ArrayList<>();
		List<Tile> tiles = new ArrayList<>();
		for (int i = 1; i < strAy.length; i++) {
			String[] a = strAy[i].split("@");
			int tileNo = Integer.parseInt(a[0]);
			String[] b = a[1].split(",");
			int x = Integer.parseInt(b[0]);
			int y = Integer.parseInt(b[1]);
			Tile t = new Tile(tileNo);
			Move m = new Move(new Dimension(x, y), t);
			tiles.add(t);
			moves.add(m);
		}
		if (!checkTiles(tiles, ch)) {
			ch.sendMessage(protocol.getInstance().serverError(IProtocol.Error.MOVE_TILES_UNOWNED));
			return;
		}
		if (!valid.validMoveSet(moves, ch.getGame().getBoard())) {
			ch.sendMessage(nl.utwente.ewi.qwirkle.protocol.protocol.getInstance().serverError(IProtocol.Error.MOVE_INVALID));
			return;
		}
		ch.getGame().getBoard().putTile(moves);
		ch.getPlayer().addScore(ScoreCalc.getInstance().calculate(ch.getGame().getBoard(), moves));
		server.broadcast(ch.getGame().getPlayers(), protocol.getInstance().serverMovePut(moves));
		List<Tile> newTiles = ch.getGame().getBag().getRandomTile(tiles.size());
		ch.getPlayer().bagToHand(newTiles);
		handleTurn(ch);
	}
	
	public void handleTurn(ClientHandler ch) {
		if(ch.getGame().gameEnd()) {
			handleEndGame(ch);
		} else if(ch.getGame().getBag().isEmpty()) {
			// TODO check for possible moves for next player
		}
		ch.getGame().nextTurn();
		
		server.broadcast(ch.getGame().getPlayers(), protocol.serverTurn(ch.getGame().getPlayerTurn()));
		
		
		List<Player> playersPlay = new ArrayList<>();
		for(ClientHandler player : ch.getGame().getPlayers()) {
			playersPlay.add(player.getPlayer());
		}
		
		server.print(protocol.serverEndGame(playersPlay, handleScores(ch), 1));
	}
	
	public void handleEndGame(ClientHandler ch) {
		List<ClientHandler> players = ch.getGame().getPlayers();
		List<Player> playersPlay = new ArrayList<>();
		int[] scores = new int[players.size()];
		int i = 0;
		for(ClientHandler player : players) {
			playersPlay.add(player.getPlayer());
			scores[i] = player.getPlayer().getScore();
			i++;
		}
		server.broadcast(players, protocol.serverEndGame(playersPlay, scores, 1));
		// TODO end the game
		
		
	}
	
	public int[] handleScores(ClientHandler ch) {
		List<ClientHandler> players = ch.getGame().getPlayers();
		List<Player> playersPlay = new ArrayList<>();
		int[] scores = new int[players.size()];
		int i = 0;
		for(ClientHandler player : players) {
			playersPlay.add(player.getPlayer());
			scores[i] = player.getPlayer().getScore();
			i++;
		}
		return scores;
	}


}
