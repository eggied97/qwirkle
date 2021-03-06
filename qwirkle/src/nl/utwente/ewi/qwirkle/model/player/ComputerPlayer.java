package nl.utwente.ewi.qwirkle.model.player;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.player.strategy.DumbStrategy;
import nl.utwente.ewi.qwirkle.model.player.strategy.Strategy;

public class ComputerPlayer extends Player {

	private Strategy strat;
	private int time;

	public ComputerPlayer(String name) {
		this(name, new DumbStrategy());
	}

	public ComputerPlayer(String name, Strategy strat) {
		super(name);
		this.strat = strat;
	}

	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}



	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
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
