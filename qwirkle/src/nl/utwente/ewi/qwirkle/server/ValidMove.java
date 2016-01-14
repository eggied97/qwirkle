package nl.utwente.ewi.qwirkle.server;

import java.awt.Point;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;

import java.util.ArrayList;

public class ValidMove {
	
	private List<Move> moveSet;
	
	private boolean isValidMove(Move m, Board b) {
		List<Tile> tileSetX = new ArrayList<>();
		List<Tile> tileSetY = new ArrayList<>();
		
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
				tileSetX.add(b.getTile(i, pY));
			} else {
				i = pX + 5;
			}
		}
		// Horizontal line left of the point
		for(int i = pX; i > pX - 6; i--) {
			if(b.getTile(i, pY) != null) {
				tileSetX.add(b.getTile(i, pY));
			} else {
				i = pX - 5;
			}
		}
		// Vertical line below the point
		for(int i = pY; i > pY - 6; i--) {
			if(b.getTile(pX, i) != null) {
				tileSetY.add(b.getTile(pX, i));
			} else {
				i = pY - 5;
			}
		}
		// Vertical line above of the point
		for(int i = pY; i < pY + 6; i++) {
			if(b.getTile(pY, i) != null) {
				tileSetY.add(b.getTile(pX, i));
			} else {
				i = pY + 5;
			}
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
		Board fake = b;
		for(Move m : moves) {
			if(isValidMove(m, fake)) {
				fake.putTile((int)m.getPoint().getX(), (int)m.getPoint().getY(), m.getTile());
			} else {
				return false;
			}
		}
		return true;
	}
	
	
	
	

}
