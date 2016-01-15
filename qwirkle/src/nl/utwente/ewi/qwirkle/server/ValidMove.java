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
	
	private List<Move> moveSet;
	
	private boolean isValidMove(Move m, Board b) {
		List<Tile> tileSetXR = new ArrayList<>();
		List<Tile> tileSetXL = new ArrayList<>();
		List<Tile> tileSetYR = new ArrayList<>();
		List<Tile> tileSetYL = new ArrayList<>();
		
		Tile t = m.getTile();
		
		Point p = m.getPoint();
		
		int pX = (int)p.getX();
		int pY = (int)p.getY();
		
		// Check whether the position is empty
		if(b.getTile(pX, pY) != null) {
			return false;
		}
		
		
		// Horizontal line right of the point
		for(int i = pX; i < pX + 6; i++) {
			if(b.getTile(i, pY) != null) {
				tileSetXR.add(b.getTile(i, pY));
			} else {
				i = pX + 5;
			}
		}
		// Horizontal line left of the point
		for(int i = pX; i > pX - 6; i--) {
			if(b.getTile(i, pY) != null) {
				tileSetXL.add(b.getTile(i, pY));
			} else {
				i = pX - 5;
			}
		}
		// Test if the two horizontal rows can be combined
		for(Tile tile : tileSetXR) {
			for(Tile tile1 : tileSetXL) {
				if(!tile.isValidNeighbour(tile1)) {
					return false;
				}
			}
		}
		
		// Vertical line below the point
		for(int i = pY; i > pY - 6; i--) {
			if(b.getTile(pX, i) != null) {
				tileSetYR.add(b.getTile(pX, i));
			} else {
				i = pY - 5;
			}
		}
		// Vertical line above of the point
		for(int i = pY; i < pY + 6; i++) {
			if(b.getTile(pY, i) != null) {
				tileSetYL.add(b.getTile(pX, i));
			} else {
				i = pY + 5;
			}
		}
		// Test if the two vertical columns can be combined
		for(Tile tile : tileSetYR) {
			for(Tile tile1 : tileSetYL) {
				if(!tile.isValidNeighbour(tile1)) {
					return false;
				}
			}
		}
		
		List<Tile> tileSetX = new ArrayList<>();
		tileSetX.addAll(tileSetXL);
		tileSetX.addAll(tileSetXR);
		
		List<Tile> tileSetY = new ArrayList<>();
		tileSetX.addAll(tileSetYL);
		tileSetX.addAll(tileSetYR);
		
		// Check whether it connects with tiles
		if(tileSetX.isEmpty() && tileSetY.isEmpty()) {
			return false;
		}
		
		// Check whether the tile is a valid neighbour for the rows
		for(Tile til : tileSetX) {
			if(!til.isValidNeighbour(t)) {
				return false;
			}
		}
		 for(Tile til : tileSetY) {
			 if(!til.isValidNeighbour(t)) {
				 return false;
			 }
		 }

		return true;
	}
	
	
	public boolean validMoveSet(List<Move> moves, Board b) {
		if(!(validPointsX(moves) || validPointsY(moves))) {
			return false;
		}
		Board boardCopy = b.deepCopy();
		for(Move m : moves) {
			if(isValidMove(m, boardCopy)) {
				boardCopy.putTile((int)m.getPoint().getX(), (int)m.getPoint().getY(), m.getTile());
			} else {
				return false;
			}
		}
		return true;
	}
	
	public boolean validPointsX(List<Move> moves) {
		for(int i = 0; i < moves.size() - 1; i++) {
			if(moves.get(i).getPoint().getX() != moves.get(i+1).getPoint().getX()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean validPointsY(List<Move> moves) {
		for(int i = 0; i < moves.size() - 1; i++) {
			if(moves.get(i).getPoint().getY() != moves.get(i+1).getPoint().getY()) {
				return false;
			}
		}
		return true;
	}
}
