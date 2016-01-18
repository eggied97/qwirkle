package nl.utwente.ewi.qwirkle.server.score;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;

public class ScoreCalc {

	public ScoreCalc() {

	}

	// TODO FIX THAT SHIT
	
	public static ScoreCalc getInstance() {
		return new ScoreCalc();
	}

	public int calculate(Board b, List<Move> moves) {
		int multiplier = 1;
		int score = 0;

		// direction 0 is horizontal, 1 is vertical

		int direction = 0;
		if (moves.size() > 1) {
			if (moves.get(0).getPoint().getX() == moves.get(1).getPoint().getX()) {
				direction = 1;
			}
		}

		if (direction == 0) {
			int py = moves.get(0).getPoint().getY();
			int px = moves.get(0).getPoint().getX();
			int incounter = 1;
			
			for (int i = px - 1; i > px - 6; i--) {
				if (b.getTile(i, py) != null) {
					incounter++;
				} else {
					i = px - 5;
				}
			}

			for (int i = px + 1; i < px + 6; i++) {
				if (b.getTile(i, py) != null) {
					incounter++;
				} else {
					i = px + 5;
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

				if (counter == 5) {
					multiplier *= 2;
				}

				score += counter;

			}
		} else {
			
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
