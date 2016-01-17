package nl.utwente.ewi.qwirkle.model.player.strategy;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.server.ValidMove;

public class DumbStrategy implements Strategy {

	@Override
	public String getName() {
		return "DumbStrategy";
	}

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

}
