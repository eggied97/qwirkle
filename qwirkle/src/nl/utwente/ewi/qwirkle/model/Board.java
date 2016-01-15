package nl.utwente.ewi.qwirkle.model;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.utwente.ewi.qwirkle.model.exceptions.PlaceOccupiedException;

public class Board {

	private Map<Dimension, Tile> map;

	public Board() {
		map = new HashMap<>();
	}

	/**
	 * @return <code> Tile </code> at given <code> x, y </code>
	 * @param x
	 * @param y
	 */
	public Tile getTile(int x, int y) {
		Dimension d  = new Dimension(x,y);
		return map.get(d);
	}
	
	public void putTile(int x, int y, Tile t) {
		Dimension d  = new Dimension(x,y);
		map.put(d, t);
	}

	public void putTile(List<Move> moves) {
		for (Move m : moves) {
			putTile(m);
		}
	}

	public void putTile(Move m) {
		Dimension d = m.getDimension();

		putTile((int)d.getWidth(), (int)d.getHeight(), m.getTile());
	}

	/**
	 * @return boundaries of the board in [left, bottom, right, top]
	 */
	private double[] getBoundaries() {
		double[] result = new double[4];

		for (Dimension d : map.keySet()) {
			result[2] = Math.max(result[2], d.getWidth());
			result[3] = Math.max(result[3], d.getHeight());
			result[0] = Math.min(result[0], d.getWidth());
			result[1] = Math.min(result[1], d.getHeight());
		}

		return result;
	}

	public boolean isSquare() {
		if (map.keySet().size() != 36) {
			return false;
		}

		double[] bound = getBoundaries();
		
		for(int i = (int)bound[0]; i < (int)bound[0] + 6; i++) {
			for(int j = (int)bound[1]; j < (int)bound[1] + 6; j++) {
				if(map.get(new Dimension(i,j)) == null) {
					return false;
				}
			}
		}
		return true;
	}

	public String toString() {
		if (this.isEmpty()) {
			return "(0,0)";
		}

		double[] bound = getBoundaries();
		String result = "";
		String testFormat1 = "";
		
		for (int y = (int) bound[1] - 1; y < bound[3] + 2; y++) {
			for (int x = (int) bound[0] - 1; x < bound[2] + 2; x++) {
				Tile t = getTile(x, y);
				if (t != null) {
					testFormat1 = "  " + t.toString();
					//result += getTile(x, y).toString();
				} else {
					testFormat1 = "(" + x + "," + y + ")";
					//result += "(" + x + "," + y + ")";
				}
				result += String.format("%-10s%-5s", testFormat1, "|");
				//result += " | ";
			}
			result = result.trim().substring(0, result.trim().length()-1);
			result += "\n";
		}

		return result;

	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public void setMap(Map<Dimension, Tile> m) {
		this.map.clear();
		this.map = m;
	}

	public Board deepCopy() {
		Map<Dimension, Tile> m = new HashMap<>();
		m.putAll(map);

		Board b = new Board();
		b.setMap(m);

		return b;
	}
	
	public Map<Dimension, Tile> getMap() {
		return map;
	}
	
	public boolean equals(Object b) {
		return ((b instanceof Board) && ((Board)b).getMap().equals(this.getMap()));
	}
	
	

}
