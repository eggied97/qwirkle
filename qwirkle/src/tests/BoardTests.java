package tests;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;

public class BoardTests {

	/*
	 * CLASS NOT FOUND :
	 * 
	 * UP THE JUNIT AND HAMCREST JARS TO THE TOP IN BUILDORDER
	 * 
	 * 
	 */

	Board b;

	@Before
	public void setUp() throws Exception {
		b = new Board();
	}

	@Test
	public void deepCopyTest() {
		Board dcb = b.deepCopy();

		assertTrue(dcb.equals(b));

		dcb.putTile(2, 4, new Tile(5));
		assertFalse(dcb.equals(b));
	}

	@Test
	public void setMapTest() {
		Map<Point, Tile> m = new HashMap<>();
		m.put(new Point(12, 34), new Tile(12));
		m.put(new Point(67, 89), new Tile(23));

		b.setMap(m);

		assertTrue(b.getMap().equals(m));
	}

	@Test
	public void isEmptyTest() {
		assertTrue(b.isEmpty());

		b.putTile(2, 4, new Tile(5));
		assertFalse(b.isEmpty());
	}

	@Test
	public void putAndGetTileTest() {
		b.putTile(12, 34, new Tile(30));
		assertTrue(b.getTile(12, 34).equals(new Tile(30)));
	}

	@Test
	public void putSingleMove() {
		Move m = new Move(new Point(123, 53), new Tile(16));

		b.putTile(m);

		assertTrue(b.getTile(123, 53).equals(new Tile(16)));
	}

	@Test
	public void putMultipleMoves() {
		List<Move> moves = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			moves.add(new Move(new Point(0 + i, 0 + i), new Tile(1 + i)));
		}

		b.putTile(moves);

		for (int i = 0; i < 10; i++) {
			assertTrue(b.getTile(i, i).equals(new Tile(1 + i)));
		}

	}

	@Test
	public void isSquareTest() {
		b.putTile(43, 65, new Tile(1));

		assertTrue(!b.isSquare());

		Board newBoard = new Board();

		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 6; y++) {
				newBoard.putTile(x, y, new Tile(y + x));
			}
		}

		assertTrue(newBoard.isSquare());
		
		newBoard = new Board();

		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 6; y++) {
				newBoard.putTile(x+y, y, new Tile(y + x));
			}
		}

		assertTrue(!newBoard.isSquare());
	}

	@Test
	public void toStringTest() {
		assertTrue(b.toString().equals("(0,0)"));

		b.putTile(0, 0, new Tile(12));

		assertTrue(b.toString().trim().equals("(-1,-1) |    (0,-1)  |    (1,-1)  \n"
				+ "(-1,0)  |      o_o   |    (1,0)   \n" + "(-1,1)  |    (0,1)   |    (1,1)   \n".trim()));

	}

	@Test
	public void getEmptySpotTest() {
		List<Point> emptySpots = new ArrayList<>();

		emptySpots = b.getEmptySpots();

		assertTrue(emptySpots.size() == 1 && emptySpots.get(0).equals(new Point(0, 0)));

		b.putTile(0, 0, new Tile(12));

		emptySpots = new ArrayList<>();
		emptySpots = b.getEmptySpots();

		assertTrue(emptySpots.size() == 8);

		for (int y = -1; y < 2; y++) {
			for (int x = -1; x < 2; x++) {
				if (x == 0 && y == 0) {
					assertTrue(!emptySpots.contains(new Point(x, y)));
				} else {
					assertTrue(emptySpots.contains(new Point(x, y)));
					emptySpots.remove(new Point(x, y));
				}
			}
		}

		assertTrue(emptySpots.size() == 0);
	}

	@Test
	public void testGetButtonBoard() {
		Tile t = new Tile(30);

		b.putTile(12, 34, t);

		Map<Point, Tile> result = b.getButtonBoard();

		assertTrue(result.get(new Point(12 + 144, 34 + 144)).equals(t));
	}

}
