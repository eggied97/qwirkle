package nl.utwente.ewi.qwirkle.model.player.strategy;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;

import java.util.List;

public interface Strategy {

	/**
	 * @return name of the strategy
	 */
	public String getName();

	/**
	 * determines the best move
	 * 
	 * @param b
	 *            the playing board
	 * @param m
	 *            the mark of the player
	 * @return the best move set, null if we need to trade
	 */
	public List<Move> determineMove(Board b, List<Tile> hand);
	
	public List<Tile> determineTrade(List<Tile> hand);
}
