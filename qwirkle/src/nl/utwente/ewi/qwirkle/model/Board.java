package nl.utwente.ewi.qwirkle.model;

import java.awt.Point;
import java.util.HashMap;
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

}
