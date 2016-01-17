package nl.utwente.ewi.qwirkle.model.player.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;

public class SuperStrategy implements Strategy {

	private static final String name = "SuperStrategy";

	@Override
	public String getName() {
		return name;
	}

	/*
	 * Smart strategy determine move:
	 * 
	 * First off we make a list of all the possible pairs we can create iwith
	 * the tiles in our hand.
	 * 
	 * Then we are going to loop through this list, and if it is a valid move on
	 * the board, determine how many points we get out of that. Finally we place
	 * the move wich gains us the most points -> INSTA WIN.
	 * 
	 */
	@Override
	public List<Move> determineMove(Board b, List<Tile> hand) {

		return null;
	}

	/*
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

	private List<Move> getBestPossibleMove(Board b, List<Tile> hand) {
		List<Move> bestMoves = new ArrayList<>();

		Board boardCopy = b.deepCopy();

		List<List<Tile>> tilePairs = getAllPossibleTilePairs(boardCopy, hand);
		List<Move> bestMoveSet = bestPossibleMoveSet(tilePairs);

		return bestMoves;
	}

	private List<List<Tile>> getAllPossibleTilePairs(Board b, List<Tile> hand) {
		List<List<Tile>> result = new ArrayList<>();

		for (Tile t : hand) {

		}

		return result;
	}

	private List<Move> bestPossibleMoveSet(List<List<Tile>> tilePairs) {
		List<Move> result = new ArrayList<>();

		return result;
	}

}
