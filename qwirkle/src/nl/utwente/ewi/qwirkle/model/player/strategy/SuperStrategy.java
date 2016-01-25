package nl.utwente.ewi.qwirkle.model.player.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.server.ValidMove;
import nl.utwente.ewi.qwirkle.server.score.ScoreCalc;

public class SuperStrategy implements Strategy {

	private static final String NAME = "SuperStrategy";

	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Supersmart strategy : determining move:
	 * 
	 * <p>
	 * We use the same principal as with the
	 * {@link nl.utwente.ewi.qwirkle.model.player.strategy.DumbStrategy}. But we
	 * iterate over those mvoes, with the remaining tiles in our hand, over and
	 * over again.
	 * </p>
	 * 
	 * <p>
	 * So we create a <code> List </code> of all possible moves. Now we itterate
	 * over this list and get the score of every move. At the end we return the
	 * move whoch gains us the most points
	 * </p>
	 * 
	 */
	@Override
	public List<Move> determineMove(Board b, List<Tile> hand) {
		List<List<Move>> alleMovesMogelijk = getAllMoves(b, hand);

		int hoogsteScore = 0;
		List<Move> lijstHoogsteScore = new ArrayList<>();

		ScoreCalc sc = new ScoreCalc();

		for (List<Move> moves : alleMovesMogelijk) {
			Board bCopy = b.deepCopy();
			bCopy.putTile(moves);

			int score = sc.calculate(bCopy, moves);

			if (score > hoogsteScore) {
				hoogsteScore = score;
				lijstHoogsteScore = new ArrayList<>();
				lijstHoogsteScore.addAll(moves);
			}
		}

		return lijstHoogsteScore;
	}

	/**
	 * 
	 * Generate a <code> List </code> of all possible <code> move lists </code>.
	 * 
	 * @param b
	 *            deepcopy of the {@link nl.utwente.ewi.qwirkle.model.Board}
	 *            instance
	 * @param hand
	 *            a <code> List </code> of
	 *            {@link nl.utwente.ewi.qwirkle.model.Tile}s , which the player
	 *            holds in his hand.
	 * 
	 * @return a <code> List </code> containing <code> List </code> of
	 *         {@link nl.utwente.ewi.qwirkle.model.Move} .
	 * 
	 */
	public List<List<Move>> getAllMoves(Board b, List<Tile> hand) {
		Map<Integer, List<List<Move>>> alleLijsten = new HashMap<>();

		List<List<Move>> result = checkHandForMoves(b, hand, null);
		alleLijsten.put(0, result);

		for (int i = 1; i < hand.size(); i++) {
			List<List<Move>> gelegdeMoves = alleLijsten.get(i - 1);
			List<Tile> handCopy = new ArrayList<>();
			handCopy.addAll(hand);

			List<List<Move>> tussenResult = new ArrayList<>();

			for (List<Move> moves : gelegdeMoves) {
				handCopy = new ArrayList<>();
				handCopy.addAll(hand);

				// then remove tiles from hand out of the move
				for (Move m : moves) {
					handCopy.remove(m.getTile());
				}

				// now check with the handCopy, where we can do a validMove
				tussenResult.addAll(checkHandForMoves(b, handCopy, moves));

			}

			alleLijsten.put(i, tussenResult);
		}

		result = new ArrayList<>();

		for (List<List<Move>> zetErIn : alleLijsten.values()) {
			result.addAll(zetErIn);
		}

		return result;

	}

	/**
	 * 
	 * generates a <code> List </code> of all possible <code> move lists </code>
	 * when you already did a move (used in getAllMoves()).
	 * 
	 * @param b
	 *            deepcopy of the {@link nl.utwente.ewi.qwirkle.model.Board}
	 *            instance
	 * @param hand
	 *            a <code> List </code> of
	 *            {@link nl.utwente.ewi.qwirkle.model.Tile}s , which the player
	 *            holds in his hand.
	 * 
	 * @param eerderGelegd
	 *            a <code> List </code> of
	 *            {@link nl.utwente.ewi.qwirkle.model.Move}s, which are already
	 *            laid.
	 * @return
	 */
	private List<List<Move>> checkHandForMoves(Board b, List<Tile> hand, List<Move> eerderGelegd) {
		List<List<Move>> result = new ArrayList<>();

		boolean moveIsValid = false;

		Board bCopy = b.deepCopy();

		if (eerderGelegd != null) {
			bCopy.putTile(eerderGelegd);
		}

		List<Point> emptySpots = bCopy.getEmptySpots();
		ValidMove vm = new ValidMove();

		for (int i = 0; i < hand.size(); i++) {

			Tile t = hand.get(i);

			// check every empty spot
			for (Point p : emptySpots) {

				List<Move> eerderGelegdCopy = new ArrayList<>();

				if (eerderGelegd != null) {
					eerderGelegdCopy.addAll(eerderGelegd);
				}

				Move m = new Move(p, t);

				eerderGelegdCopy.add(m);

				moveIsValid = vm.validMoveSet(eerderGelegdCopy, b);

				if (moveIsValid) {
					result.add(eerderGelegdCopy);
				}
			}
		}

		return result;
	}

	/**
	 * Smart strategy for trading:
	 * 
	 * First we determine the amount of possible neighbours a tile has (more
	 * points on the board) Then we go through those pairs (Tile -
	 * numberOfNeighbours)
	 * 
	 * If we have 1 or more with 0 neighbours, we will trade all of those. When
	 * we dont have Tiles with 0 neighbours, we will go up in no. of neighbours,
	 * and only trade one (So we lose less points).
	 * 
	 */
	@Override
	public List<Tile> determineTrade(List<Tile> hand) {
		Map<Tile, Integer> possibleTrade = getTilesWithNumberOfNeighbours(hand);

		boolean done = false;
		List<Tile> result = new ArrayList<>();

		for (int i = 0; i < hand.size() && !done; i++) {
			if (possibleTrade.values().contains(i)) {
				if (i == 0) {
					for (Entry<Tile, Integer> e : possibleTrade.entrySet()) {
						if (e.getValue() == i) {
							result.add(e.getKey());
						}
					}

					done = true;
				} else {
					for (Entry<Tile, Integer> e : possibleTrade.entrySet()) {
						if (e.getValue() == i && !done) {
							result.add(e.getKey());
							done = true;
						}
					}

				}
			}
		}

		return result;
	}

	/**
	 * Generates a <code> Map </code> with
	 * {@link nl.utwente.ewi.qwirkle.model.Tile} - number of neighboors
	 * relations. 
	 * 
	 * @param hand
	 *            a <code> List </code> of
	 *            {@link nl.utwente.ewi.qwirkle.model.Tile}s , which the player
	 *            holds in his hand.
	 * @return A <code> Map </code> with the
	 *         {@link nl.utwente.ewi.qwirkle.model.Tile} - number of neighboors
	 *         relations
	 */
	private Map<Tile, Integer> getTilesWithNumberOfNeighbours(List<Tile> hand) {
		Map<Tile, Integer> result = new HashMap<>();

		for (Tile t : hand) {
			result.put(t, 0);
		}

		for (int i = 0; i < hand.size(); i++) {
			for (int j = i + 1; j < hand.size(); j++) {
				if (j != i) {
					Tile ti = hand.get(i);
					Tile tj = hand.get(j);

					if (ti.isValidNeighbour(tj)) {
						result.put(ti, result.get(ti) + 1);
						result.put(tj, result.get(tj) + 1);
					}
				}
			}
		}

		return result;
	}
}
