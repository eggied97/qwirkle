package nl.utwente.ewi.qwirkle.model.player.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

		List<List<Tile>> tilePairs = getAllPossibleTilePairs(hand);
		List<Move> bestMoveSet = bestPossibleMoveSet(tilePairs);

		return bestMoves;
	}

	public List<List<Tile>> getAllPossibleTilePairs(List<Tile> hand) {
		List<List<Tile>> result = new ArrayList<>();

		for (Tile t : hand) {
			List<Tile> mR = new ArrayList<Tile>();
			mR.add(t);
			
			result.add(mR);
		}
		
		boolean done = false;
		
		//stop pas als er voor elke steen in de hand niks meer bijgelegt kan worden
		while(!done){
			
			List<List<Tile>> tempStorage = new ArrayList<>();
			
			for(Tile t : hand){
				
				Iterator it = result.iterator();
				
				while(it.hasNext()){
					List<Tile> tiles = (List<Tile>)it.next();
					
					if(tiles.get(tiles.size() - 1).isValidNeighbour(t)){
						//past erachter
						done = false;
						List<Tile> middenResult = new ArrayList<Tile>();
						middenResult.addAll(tiles);
						middenResult.add(t);
																	
						tempStorage.add(middenResult);
						
					}else if(tiles.get(0).isValidNeighbour(t)){
						//past ervoor, wordt als het goed is niet aangeroepen als tiles.size() == 1;
						done = false;
						List<Tile> middenResult = new ArrayList<Tile>();
						middenResult.add(t);
						middenResult.addAll(tiles);						
						
						tempStorage.add(middenResult);
						
					}else{
						done = true;
					}
				}
			}
			
			result.addAll(tempStorage);
			tempStorage.clear();
		}

		return result;
	}

	private List<Move> bestPossibleMoveSet(List<List<Tile>> tilePairs) {
		List<Move> result = new ArrayList<>();

		return result;
	}

}
