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
	/*@ pure */ public static ScoreCalc getInstance() {
		return new ScoreCalc();
	}

	/**
	 * Calculates the score obtained by this move set
	 * @param b
	 * @param moves
	 * @return
	 */
	//@ requires b != null
	//@ requires moves != null
	//@ ensures \result >= 0
	public int calculate(Board b, List<Move> moves) {
		int score = 0;
		int adder = 0;

		Direction direction = Direction.HORIZONTAL;
		if (moves.size() > 1) {
			if (moves.get(0).getPoint().getX() == moves.get(1).getPoint().getX()) {
				direction = Direction.VERTICAL;
			}
		}

		
		
		if (direction.equals(Direction.HORIZONTAL)) {
			// Obtain first move
			int py = moves.get(0).getPoint().getY();
			int px = moves.get(0).getPoint().getX();
			int incounter = 1;
			
			// Count the tiles in the direction of the first move
			for (int i = px-1; i > px - 6; i--) {
				if (b.getTile(i, py) != null) {
					incounter++;
				} else {
					i = px - 6;
				}
			}
			// To the left as well as to the right
			for (int i = px + 1; i < px + 6; i++) {
				if (b.getTile(i, py) != null) {
					incounter++;
				} else {
					i = px + 6;
				}
			}
			
			// If complete row, multiply
			if(incounter == 6) {
				adder += 6;
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
						i = y - 6;
					}
				}

				for (int i = y + 1; i < y + 6; i++) {
					if (b.getTile(x, i) != null) {
						counter++;
					} else {
						i = y + 6;
					}
				}
				
				// If there are 5 tiles in the direction around it, its a full row so multiply by 2
				if (counter == 5) {
					adder += 6;
				}
				if(counter > 0) {
					counter++;
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
					i = py - 6;
				}
			}

			for (int i = py + 1; i < py + 6; i++) {
				if (b.getTile(px, i) != null) {
					incounter++;
				} else {
					i = py + 6;
				}
			}
			
			if(incounter == 6) {
				adder += 6;
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
						i = x - 6;
					}
				}

				for (int i = x + 1; i < x + 6; i++) {
					if (b.getTile(i, y) != null) {
						counter++;
					} else {
						i = x + 6;
					}
				}

				if (counter == 5) {
					adder += 6;
				}
				if(counter > 0) {
					counter++;
				}

				score += counter;

			}
		}

		return score + adder;
	}

}
