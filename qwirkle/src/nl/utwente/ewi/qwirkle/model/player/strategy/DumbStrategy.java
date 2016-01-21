package nl.utwente.ewi.qwirkle.model.player.strategy;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.server.ValidMove;

public class DumbStrategy implements Strategy {

	private static final String name = "DumbStrategy";

	@Override
	public String getName() {
		return name;
	}

	/*
	 * Dumb startegy for determining a move
	 * 
	 * First we get all of the empty spots, visible on the board
	 * 
	 * Then we go through our tiles, point by point, and ask if it is a valid
	 * move.
	 * 
	 * Ifso we do that move, if not we continue with the points/tiles
	 * 
	 */
	@Override
	public List<Move> determineMove(Board b, List<Tile> hand) {
		List<Move> result = new ArrayList<>();

		boolean moveIsValid = false;
		List<Point> emptySpots = b.getEmptySpots();
		ValidMove vm = new ValidMove();

		for (int i = 0; i < hand.size() && !moveIsValid; i++) {

			Tile t = hand.get(i);

			// check every empty spot
			for (Point p : emptySpots) {
				if (!moveIsValid) {
					Move m = new Move(p, t);

					moveIsValid = vm.isValidMove(m, b);

					if (moveIsValid) {
						result.add(m);
					}
				}
			}
		}

		return result;
	}

	/*
	 * Dumb strategy for trading:
	 * 
	 * We first get a random number for how many tiles we need to trade.
	 * 
	 * Then we will get `x` random tiles, and trade those.
	 * 
	 */
	@Override
	public List<Tile> determineTrade(List<Tile> hand) {
		List<Tile> result = new ArrayList<>();
		List<Tile> handCopy = new ArrayList<>(hand);

		int numOfTilesNeededToTrade = 0;

		while (numOfTilesNeededToTrade < 1) {
			numOfTilesNeededToTrade = (int) (Math.random() * handCopy.size());
		}

		for (int i = 0; i < numOfTilesNeededToTrade; i++) {
			int index = (int) (Math.random() * (handCopy.size() - 1));

			result.add(hand.get(index));
			handCopy.remove(index);
		}

		return result;
	}

}
