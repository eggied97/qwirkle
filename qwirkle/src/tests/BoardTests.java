package tests;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;

public class BoardTests {

	
	/*CLASS NOT FOUND :
	 * 
	 * UP THE JUNIT AND HAMCREST JARS TO THE TOP IN BUILDORDER
	 * 
	 * 
	 * */
	
	Board b;
	
	@Before
	public void setUp() throws Exception {
		b = new Board();
	}
	
	@Test
	public void deepCopyTest(){
		Board dcb = b.deepCopy();
		
		assertTrue(dcb.equals(b));
		
		dcb.putTile(2, 4, new Tile(5));
		assertTrue(dcb.equals(b));
	}
	
	@Test
	public void setMapTest(){
		Map<Point, Tile> m = new HashMap<>();
		m.put(new Point(12, 34), new Tile(12));
		m.put(new Point(67, 89), new Tile(23));
		
		b.setMap(m);
		
		assertTrue(b.getMap().equals(m));		
	}
	
	@Test
	public void isEmptyTest(){
		assertTrue(b.isEmpty());
		
		b.putTile(2, 4, new Tile(5));
		assertFalse(b.isEmpty());		
	}

	
	@Test
	public void putAndGetTileTest(){
		b.putTile(12, 34, new Tile(30));
		assertTrue(b.getTile(12, 34).equals(new Tile(30)));
	}
	

}
