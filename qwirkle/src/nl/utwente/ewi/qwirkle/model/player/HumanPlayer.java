package nl.utwente.ewi.qwirkle.model.player;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;

public class HumanPlayer extends Player {

	public HumanPlayer(String name) {
		super(name);
	}

	@Override
	public List<Move> determineMove(Board board) {
		return null;
	}

}
