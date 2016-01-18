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
	public String determineAction() {
		return null;
	}

	@Override
	public List<Tile> determineTradeMove() {
		return strat.determineTrade(getHand());
	}

	@Override
	public List<Move> determinePutMove(Board board) {
		return this.strat.determineMove(board, getHand());
	}

	@Override
	public String sendChat() {
		// TODO Auto-generated method stub
		return null;
	}
}
