package nl.utwente.ewi.qwirkle.tests;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.Before;
import org.junit.Test;

import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;

public class moveTest {

	Move m;
	
	@Before
	public void setUp() throws Exception {
		m = new Move(new Dimension(0, 0), new Tile(0));
	}
	
	@Test
	public void testToString(){
		assertTrue(m.toString().equals("0@0,0"));
		
		Move m2 = new Move(new Dimension(123, 456), new Tile(21));
		assertTrue(m2.toString().equals("21@123,456"));
	}
	
	@Test
	public void getPointTest(){
		assertTrue(m.getDimension().equals(new Dimension(0,0)));
	}
	
	@Test
	public void testGetTile(){
		assertTrue(m.getTile().equals(new Tile(0)));
	}


}
