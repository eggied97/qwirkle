package nl.utwente.ewi.qwirkle.model.player;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;

//online opponent
public class SocketPlayer extends Player {

	public SocketPlayer(String name) {
		super(name);
	}

	@Override
	public String determineAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tile> determineTradeMove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Move> determinePutMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sendChat() {
		// TODO Auto-generated method stub
		return null;
	}

}
