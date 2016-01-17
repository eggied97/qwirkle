package nl.utwente.ewi.qwirkle.model.player.strategy;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;

public class SuperStrategy implements Strategy {

	@Override
	public String getName() {
		return "SuperStrategy";
	}

	@Override
	public List<Move> determineMove(Board b, List<Tile> hand) {

		return null;
	}

	@Override
	public List<Tile> determineTrade(List<Tile> hand) {
		//dont trade valid neighbours
		return null;
	}

	public List<Move> getBestPossibleMove(Board b, List<Tile> hand) {
		List<Move> bestMoves = new ArrayList<>();

		Board boardCopy = b.deepCopy();

		List<List<Tile>> tilePairs = getAllPossibleTilePairs(boardCopy, hand);
		List<Move> bestMoveSet = bestPossibleMoveSet(tilePairs);

		return bestMoves;
	}

	public List<List<Tile>> getAllPossibleTilePairs(Board b, List<Tile> hand) {
		List<List<Tile>> result = new ArrayList<>();

		for (Tile t : hand) {

		}

		return result;
	}

	public List<Move> bestPossibleMoveSet(List<List<Tile>> tilePairs) {
		List<Move> result = new ArrayList<>();

		return result;
	}

}
