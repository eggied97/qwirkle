package nl.utwente.ewi.qwirkle.model.player;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.strategy.DumbStrategy;
import nl.utwente.ewi.qwirkle.model.player.strategy.Strategy;
import nl.utwente.ewi.qwirkle.server.ValidMove;

public class ComputerPlayer extends Player {

	private Strategy strat;

	public ComputerPlayer(String name) {
		this(name, new DumbStrategy());
	}

	public ComputerPlayer(String name, Strategy strat) {
		super(name);
		this.strat = strat;
	}

	@Override
	public Object determineMove(Board board) {

		List<Move> bestMove = this.strat.determineMove(board, getHand());

		if (bestMove.size() != 0) {
			return bestMove;
		} else {
			return strat.determineTrade(getHand());
		}
	}
}
