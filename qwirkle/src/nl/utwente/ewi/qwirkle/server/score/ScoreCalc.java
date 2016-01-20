package nl.utwente.ewi.qwirkle.server.score;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;

public class ScoreCalc {

	public ScoreCalc() {

	}
	
	/**
	 * Returns an instance of ScoreCalc
	 * @return
	 */
	public static ScoreCalc getInstance() {
		return new ScoreCalc();
	}

	/**
	 * Calculates the score obtained by this move set
	 * @param b
	 * @param moves
	 * @return
	 */
	public int calculate(Board b, List<Move> moves) {
		int multiplier = 1;
		int score = 0;

		// direction of the moves 0 is horizontal, 1 is vertical, default is 0
		//TODO enums?
		int direction = 0;
		if (moves.size() > 1) {
			if (moves.get(0).getPoint().getX() == moves.get(1).getPoint().getX()) {
				direction = 1;
			}
		}

		
		
		if (direction == 0) {
			// Obtain first move
			int py = moves.get(0).getPoint().getY();
			int px = moves.get(0).getPoint().getX();
			int incounter = 1;
			
			// Count the tiles in the direction of the first move
			for (int i = px - 1; i > px - 6; i--) {
				if (b.getTile(i, py) != null) {
					incounter++;
				} else {
					i = px - 5;
				}
			}
			// To the left as well as to the right
			for (int i = px + 1; i < px + 6; i++) {
				if (b.getTile(i, py) != null) {
					incounter++;
				} else {
					i = px + 5;
				}
			}
			
			// If complete row, multiply
			if(incounter == 6) {
				multiplier *= 2;
			}
			
			// Add to score the amount of tiles in the direction of the move
			score += incounter;
			
			// Calculate score in the direction other than the direction of the move
			for (Move m : moves) {
				int counter = 0;
				int y = m.getPoint().getY();
				int x = m.getPoint().getX();

				// Look at tiles around the moved tile
				for (int i = y - 1; i > y - 6; i--) {
					if (b.getTile(x, i) != null) {
						counter++;
					} else {
						i = y - 5;
					}
				}

				for (int i = y + 1; i < y + 6; i++) {
					if (b.getTile(x, i) != null) {
						counter++;
					} else {
						i = y + 5;
					}
				}
				
				// If there are 5 tiles in the direction around it, its a full row so multiply by 2
				if (counter == 5) {
					multiplier *= 2;
				}

				score += counter;

			}
		} else {
			// The same as above but for the other direction
			
			int py = moves.get(0).getPoint().getY();
			int px = moves.get(0).getPoint().getX();
			int incounter = 1;
			
			for (int i = py - 1; i > py - 6; i--) {
				if (b.getTile(px, i) != null) {
					incounter++;
				} else {
					i = py - 5;
				}
			}

			for (int i = py + 1; i < py + 6; i++) {
				if (b.getTile(px, i) != null) {
					incounter++;
				} else {
					i = py + 5;
				}
			}
			
			if(incounter == 6) {
				multiplier *= 2;
			}
			
			score += incounter;

			
			for (Move m : moves) {
				int counter = 0;

				int y = m.getPoint().getY();
				int x = m.getPoint().getX();

				for (int i = x - 1; i > x - 6; i--) {
					if (b.getTile(i, y) != null) {
						counter++;
					} else {
						i = x - 5;
					}
				}

				for (int i = x + 1; i < x + 6; i++) {
					if (b.getTile(i, y) != null) {
						counter++;
					} else {
						i = x + 5;
					}
				}

				if (counter == 5) {
					multiplier *= 2;
				}

				score += counter;

			}
		}

		return score * multiplier;
	}

}
