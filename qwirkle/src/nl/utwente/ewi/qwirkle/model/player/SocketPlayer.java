package nl.utwente.ewi.qwirkle.model.player;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;

/**
 * Online opponent, so no implementation other then the implementation of
 * {@link nl.utwente.ewi.qwirkle.model.player.Player}.
 */
public class SocketPlayer extends Player {

	public SocketPlayer(String name) {
		super(name);
	}

	@Override
	public String determineAction() {
		return null;
	}

	@Override
	public List<Tile> determineTradeMove() {
		return null;
	}

	@Override
	public List<Move> determinePutMove(Board board) {
		return null;
	}

	@Override
	public String sendChat() {
		return null;
	}

}
