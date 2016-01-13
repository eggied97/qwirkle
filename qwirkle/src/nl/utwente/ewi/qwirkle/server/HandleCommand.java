package nl.utwente.ewi.qwirkle.server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.server.connect.ClientHandler;
import nl.utwente.ewi.qwirkle.server.connect.Server;

public class HandleCommand {
	
	private Server server;
	private protocol protocol;
	private ValidMove valid;
	
	public HandleCommand(Server server) {
		this.server = server;
	}

	public static HandleCommand getInstance(Server server) {
		return new HandleCommand(server);
	}

	public void handleIdentifyName(String[] strAy, ClientHandler ch) {
		String name = strAy[1];
		if (name.matches("^[a-zA-Z0-9-_]{2,16}$") || !name.equals(null)) {
			for(ClientHandler handler : server.getAll() ) {
				if(handler.getClientName().equals(name)) {
					ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.NAME_USED);
					return;
				}
			}
			ch.setClientName(name);
		} else {
			ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.NAME_INVALID);
		}

	}

	public void handleIdentifyFeatures(String[] strAy, ClientHandler ch) {
		List<IProtocol.Feature> features = new ArrayList<>();
		
		for(int i = 2; i < strAy.length; i++) {
			switch(IProtocol.Feature.valueOf(strAy[i])) {
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
		ch.sendMessage(protocol.serverConnectOk((IProtocol.Feature[])features.toArray()));
	}
	
	public void handleQueue(String[] strAy, ClientHandler ch) {
		String queue = strAy[1];
		String[] queueSpl = queue.split(",");
		Map<Integer, List<ClientHandler>> map = server.getQueues();
		for(int i = 0; i < queueSpl.length; i++) {
			switch(Integer.parseInt(queueSpl[i])) {
			case 2:
				map.get(2).add(ch);
				break;
				// TODO check if queue full
				// TODO test
			case 3:
				map.get(3).add(ch);
				break;
			case 4:
				map.get(4).add(ch);
				break;
			default:
				ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.QUEUE_INVALID);
				break;
			}
		}
		
		
	}

	public void handleMoveTrade(String[] strAy, ClientHandler ch) {
		List<Tile> tiles = new ArrayList<>();
		for(int i = 1; i < strAy.length; i++) {
			tiles.add(new Tile(Integer.parseInt(strAy[i])));
		}
		List<Integer> tilesInt = new ArrayList<>();
		for (Tile t : tiles) {
			tilesInt.add(t.getIntOfTile());		
		}
		
		if(ch.getGame().getBag().isEmpty() || ch.getGame().getBag().getAmountOfTiles() - tiles.size() < 0) {
			ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.DECK_EMPTY);
		} else if(ch.getGame().getBoard().isEmpty()) {
			ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.TRADE_FIRST_TURN);
		} else if(!checkTiles(tiles, ch)) {
			ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.MOVE_TILES_UNOWNED);
		}
		
		List<Tile> newTiles = ch.getGame().getBag().getRandomTile(tiles.size());
		List<Integer> newTilesInt = new ArrayList<>();;
		for (Tile t : newTiles) {
			newTilesInt.add(t.getIntOfTile());		
		}
		ch.getGame().getBag().addTiles(tilesInt);
		
		ch.sendMessage(protocol.serverDrawTile(newTiles));
	}
	
	public boolean checkTiles(List<Tile> tiles, ClientHandler ch) {
		List<Tile> playerTiles = ch.getPlayer().getHand();
		for(Tile t : tiles) {
			if(!playerTiles.contains(t)) {
				return false;
			}
		}
		return true;
	}

	public void handleMovePut(String[] strAy, ClientHandler ch) {
		List<Move> moves = new ArrayList<>();
		List<Tile> tiles = new ArrayList<>();
		for(int i = 1; i < strAy.length; i++) {
			String[] a = strAy[i].split("@");
			int tileNo = Integer.parseInt(a[0]);
			String[] b = a[1].split(",");
			int x = Integer.parseInt(b[0]);
			int y = Integer.parseInt(b[1]);
			Tile t = new Tile(tileNo);
			Move m = new Move(new Point(x,y), t);
			tiles.add(t);
			moves.add(m);
		}
		if(!checkTiles(tiles, ch)) {
			ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.MOVE_TILES_UNOWNED);
			return;
		}
		if(!valid.validMoveSet(moves)) {
			ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.MOVE_INVALID);
			return;
		}
		ch.getGame().getBoard().putTile(moves);
	}
}
