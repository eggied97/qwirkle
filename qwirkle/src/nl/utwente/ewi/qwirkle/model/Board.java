package nl.utwente.ewi.qwirkle.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;

import nl.utwente.ewi.qwirkle.model.exceptions.PlaceOccupiedException;
import nl.utwente.ewi.qwirkle.server.score.ScoreCalc;

public class Board {

	private Map<Point, Tile> map;

	public Board() {
		map = new HashMap<>();
	}

	/**
	 * @return <code> Tile </code> at given <code> x, y </code>
	 * @param x
	 * @param y
	 */
	public Tile getTile(int x, int y) {
		Point d  = new Point(x,y);
		return map.get(d);
	}
	
	/**
	 * Creates a new <code> Point </code> and put the <code> Tile </code> at this position
	 * @param x
	 * @param y
	 * @param t
	 */
	public void putTile(int x, int y, Tile t) {
		Point d  = new Point(x,y);
		map.put(d, t);
	}
	
	/**
	 * Put all specified moves on the <code> Board </code>
	 * @param moves
	 */
	public int putTile(List<Move> moves) {
		for (Move m : moves) {
			putTile(m);
		}
		
		return new ScoreCalc().calculate(this, moves);
	}

	/**
	 * Put the specified <code> Move </code> on the <code> Board </code>
	 * @param m
	 */
	public void putTile(Move m) {
		Point p = m.getPoint();

		putTile(p.getX(), p.getY(), m.getTile());
	}

	/**
	 * @return boundaries of the board in [left, bottom, right, top]
	 */
	private double[] getBoundaries() {
		double[] result = new double[4];

		for (Point d : map.keySet()) {
			result[2] = Math.max(result[2], d.getX());
			result[3] = Math.max(result[3], d.getY());
			result[0] = Math.min(result[0], d.getX());
			result[1] = Math.min(result[1], d.getY());
		}

		return result;
	}

	/**
	 * Returns true if the tiles on the <code> Board </code> form a square
	 * @return
	 */
	public boolean isSquare() {
		if (map.keySet().size() != 36) {
			return false;
		}

		double[] bound = getBoundaries();
		
		for(int i = (int)bound[0]; i < (int)bound[0] + 6; i++) {
			for(int j = (int)bound[1]; j < (int)bound[1] + 6; j++) {
				if(map.get(new Point(i,j)) == null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Creates a String representing the <code> Board </code>
	 */
	public String toString() {
		if (this.isEmpty()) {
			return "(0,0)";
		}

		double[] bound = getBoundaries();
		String result = "";
		String cellText = "";
		
		for (int y = (int) bound[1] - 1; y < bound[3] + 2; y++) {
			for (int x = (int) bound[0] - 1; x < bound[2] + 2; x++) {
				Tile t = getTile(x, y);
				
				if (t != null) {
					cellText = "  " + t.getHumanReadableString();
					//result += getTile(x, y).toString();
				} else {
					cellText = "(" + x + "," + y + ")";
					//result += "(" + x + "," + y + ")";
				}
				result += String.format("%-8s%-5s", cellText, "|");
				//result += " | ";
			}
			result = result.trim().substring(0, result.trim().length()-1);
			result += "\n";
		}

		return result;

	}
	
	/**
	 * Returns a <code> List </code> of points where a <code> Tile </code> can be put
	 * @return
	 */
	public List<Point> getEmptySpots(){
		List<Point> result = new ArrayList<>();
		
		if (this.isEmpty()) {
			result.add(new Point(0, 0));
		}else{
	
			double[] bound = getBoundaries();
			
			
			for (int y = (int) bound[1] - 1; y < bound[3] + 2; y++) {
				for (int x = (int) bound[0] - 1; x < bound[2] + 2; x++) {
					Tile t = getTile(x, y);
					if (t == null) {
						result.add(new Point(x, y));
					}
				}				
			}
		}
		
		return result;
	}

	/**
	 * Returns true if the <code> Board </code> is still empty
	 * @return
	 */
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	/**
	 * Set the map 
	 * @param m
	 */
	public void setMap(Map<Point, Tile> m) {
		this.map.clear();
		this.map = m;
	}

	/**
	 * Returns a deep copy of the <code> Board </code>
	 * @return
	 */
	public Board deepCopy() {
		Map<Point, Tile> m = new HashMap<>();
		m.putAll(map);

		Board b = new Board();
		b.setMap(m);

		return b;
	}
	
	/**
	 * Returns the map
	 * @return
	 */
	public Map<Point, Tile> getMap() {
		return map;
	}
	
	@Override
	public boolean equals(Object b) {
		return ((b instanceof Board) && ((Board)b).getMap().equals(this.getMap()));
	}
	

}
