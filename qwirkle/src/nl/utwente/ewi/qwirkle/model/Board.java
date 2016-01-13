package nl.utwente.ewi.qwirkle.model;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.model.exceptions.PlaceOccupiedException;

public class Board {

	private Map<Point, Tile> map;

	public Board() {
		map = new HashMap<>();
	}
	
	public void main(String[] args){
		Board b = new Board();
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

	/**
	 * @return boundaries of the board in [left, top, right, bottom]
	 */
	private double[] getBoundaries() {
		double[] result = new double[4];

		for (Point p : map.keySet()) {
			result[2] = Math.max(result[2], p.getX());
			result[3] = Math.max(result[3], p.getY());
			result[0] = Math.min(result[0], p.getX());
			result[1] = Math.min(result[1], p.getY());
		}

		return result;
	}

	public boolean isSquare() {
		if (map.keySet().size() != 36) {
			return false;
		}

		double[] bound = getBoundaries();

		if (bound[2] - bound[0] == 6 && bound[3] - bound[1] == 6) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		double[] bound = getBoundaries();
		String result = "";

		for (int y = (int) bound[1]; y < bound[3]; y++) {
			for (int x = (int) bound[0]; x < bound[2]; x++) {
				Tile t = getTile(x, y);
				if (t != null) {
					result += getTile(x, y).toString();
				} else {
					result += "(" + x + "," + y + ")";
				}
				result += " | ";
			}
			result += "\n";
		}

		return result;

	}
	
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

}
