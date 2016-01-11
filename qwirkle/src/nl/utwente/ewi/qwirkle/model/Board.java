package nl.utwente.ewi.qwirkle.model;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.model.exceptions.PlaceOccupiedException;

public class Board {

	Map<Point, Tile> map;

	public Board() {
		map = new HashMap<>();
	}

	/**
	 * @return <code> Tile </code> at given <code> x, y </code>
	 * @param x
	 * @param y
	 */
	public Tile getTile(int x, int y) {
		return map.get(new Point(x, y));
	}

	public void putTile(int x, int y, Tile t) {
		map.put(new Point(x, y), t);
		// TODO iets met een callback en update?
	}

	public void putTile(List<Move> moves) {
		for (Move m : moves) {
			Point p = m.getPoint();

			putTile((int) p.getX(), (int) p.getY(), m.getTile());
		}
	}
	
	public boolean isSquare() {
		if(map.keySet().size() != 36) {
			return false;
		}
		
		double maxX = 0;
		double maxY = 0;
		double minX = 1000;
		double minY = 1000;
		
		for(Point p :map.keySet()) {
			maxX = Math.max(maxX, p.getX());
			maxY = Math.max(maxY, p.getY());
			minX = Math.min(minX, p.getX());
			minY = Math.min(minY, p.getY());
		}
		
		if(maxX - minX == 6 && maxY - minY == 6) {
			return true;
		} else {
			return false;
		}
	}

}
