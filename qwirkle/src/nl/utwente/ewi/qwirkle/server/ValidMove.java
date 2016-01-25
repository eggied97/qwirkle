package nl.utwente.ewi.qwirkle.server;

import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * TODO maybe set this in the board.java? -Egbert
 * 
 * */
public class ValidMove {

	private static final boolean DEBUG = false;
	private List<Move> moveSet;

	private void printDEBUG(String msg) {
		if (DEBUG) {
			System.out.println(msg);
		}
	}

	/**
	 * Tests whether a single <code> Move </code> is valid on the current state
	 * of the <code> Board </code>
	 * 
	 * @param m
	 * @param b
	 * @return
	 */
	//@ requires m != null;
	//@ requires b != null;
	//@ ensures \result == (b.getTile(m.getPoint().getX(), m.getPoint().getY()) == null);
	public boolean isValidMove(Move m, Board b) {
		List<Tile> tileSetXR = new ArrayList<>();
		List<Tile> tileSetXL = new ArrayList<>();
		List<Tile> tileSetYR = new ArrayList<>();
		List<Tile> tileSetYL = new ArrayList<>();

		Tile t = m.getTile();

		Point p = m.getPoint();

		int dX = p.getX();
		int dY = p.getY();

		// Check whether the position is empty
		if (b.getTile(dX, dY) != null) {
			printDEBUG("Non empty position");
			return false;
		}

		// Horizontal line right of the point
		for (int i = dX + 1; i < dX + 7; i++) {
			if (b.getTile(i, dY) != null) {
				tileSetXR.add(b.getTile(i, dY));
			} else {
				i = dX + 6;
			}
		}
		// Horizontal line left of the point
		for (int i = dX - 1; i > dX - 7; i--) {
			if (b.getTile(i, dY) != null) {
				tileSetXL.add(b.getTile(i, dY));
			} else {
				i = dX - 6;
			}
		}
		// Test if the two horizontal rows can be combined
		for (Tile tile : tileSetXR) {
			for (Tile tile1 : tileSetXL) {
				if (!tile.isValidNeighbour(tile1)) {
					printDEBUG("No horizontal combination");
					return false;
				}
			}
		}
		// Vertical line above the point
		for (int i = dY - 1; i > dY - 7; i--) {
			if (b.getTile(dX, i) != null) {
				tileSetYR.add(b.getTile(dX, i));
			} else {
				i = dY - 6;
			}
		}
		// Vertical line below of the point
		for (int i = dY + 1; i < dY + 7; i++) {
			if (b.getTile(dX, i) != null) {
				tileSetYL.add(b.getTile(dX, i));
			} else {
				i = dY + 6;
			}
		}
		// Test if the two vertical columns can be combined
		for (Tile tile : tileSetYR) {
			for (Tile tile1 : tileSetYL) {
				if (!tile.isValidNeighbour(tile1)) {
					printDEBUG("No vertical combination");
					return false;
				}
			}
		}
		List<Tile> tileSetX = new ArrayList<>();
		tileSetX.addAll(tileSetXL);
		tileSetX.addAll(tileSetXR);

		List<Tile> tileSetY = new ArrayList<>();
		tileSetY.addAll(tileSetYL);
		tileSetY.addAll(tileSetYR);

		// Check whether it connects with tiles
		if (tileSetX.isEmpty() && tileSetY.isEmpty()) {
			if (!b.isEmpty()) {
				printDEBUG("No connection with other tiles");
				return false;
			}
		}
		// Check whether the tile is a valid neighbour for the rows
		for (Tile til : tileSetX) {
			if (!til.isValidNeighbour(t)) {
				printDEBUG("New tile cant fit in " + t.getIntOfTile() + " with " + til.getIntOfTile());
				return false;
			}
		}
		for (Tile til : tileSetY) {
			if (!til.isValidNeighbour(t)) {
				printDEBUG("New tile cant fit in " + t.getIntOfTile() + " with " + til.getIntOfTile());
				return false;
			}
		}
		return true;
	}

	/**
	 * Tests the entire moveset on the board whether its valid
	 * 
	 * @param moves
	 * @param b
	 * @return
	 */
	public boolean validMoveSet(List<Move> moves, Board b) {
		if (!(validPointsX(moves) || validPointsY(moves))) {
			return false;
		}

		Board boardCopy = b.deepCopy();
		// Validate every move and put it on the board if it is valid
		for (Move m : moves) {
			if (isValidMove(m, boardCopy)) {
				boardCopy.putTile(m.getPoint().getX(), m.getPoint().getY(), m.getTile());
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate the connection of the move
	 * 
	 * @param moves
	 * @return
	 */
	//@requires moves != null;

	public boolean validPointsX(List<Move> moves) {
		for (int i = 0; i < moves.size() - 1; i++) {
			if (moves.get(i).getPoint().getX() != moves.get(i + 1).getPoint().getX()) {
				printDEBUG("Tiles don't fit together");
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate the connection of the move
	 * 
	 * @param moves
	 * @return
	 */
	public boolean validPointsY(List<Move> moves) {
		for (int i = 0; i < moves.size() - 1; i++) {
			if (moves.get(i).getPoint().getY() != moves.get(i + 1).getPoint().getY()) {
				printDEBUG("Tiles don't fit together");
				return false;
			}
		}
		return true;
	}
}
