package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.Shape;

public class TileTest {
	
	Tile t1, t2;

	@Before
	public void setUp() throws Exception {
		t1 = new Tile(Color.BLUE, Shape.CIRCLE);
		t2 = new Tile(Color.GREEN, Shape.CLUBS);
	}
	
	@Test
	public void tileEqualsTest(){
		Tile t = new Tile(0);
		assertTrue(t.equals(new Tile(0)));
		assertTrue(!t.equals(new Tile(2)));
	}
	
	@Test
	public void tileInitTest(){
		Tile t = new Tile(0);
		Tile t5 = new Tile(28);
		
		int iShape = 28 % 6;
		int iColor = (28 - iShape) / 6;
		
		assertTrue(t.getiColor() == 0);
		assertTrue(t.getiShape() == 0);
		
		assertTrue(t5.getiShape() == iShape);
		assertTrue(t5.getiColor() == iColor);
	}
	
	@Test
	public void tileToHumanReadableStringTest(){
		String tussenstap = Color.BLUE.toString() + "_" + Shape.CIRCLE.toString();
		
		assertTrue(tussenstap.equals(t1.getHumanReadableString()));
		assertFalse(tussenstap.equals(t2.getHumanReadableString()));		
	}
	
	@Test
	public void isValidNeighbourTest(){
		assertFalse(t1.isValidNeighbour(t2));
		assertTrue(t1.isValidNeighbour(new Tile(Color.BLUE, Shape.CLUBS)));
		assertTrue(t1.isValidNeighbour(new Tile(Color.RED, Shape.CIRCLE)));
	}
	
	@Test
	public void getIntOfTileTest(){
		assertTrue(t1.getIntOfTile() == 30);
		assertTrue(new Tile(12).getIntOfTile() == 12);
		assertTrue(new Tile(35).getIntOfTile() == 35);
	}
	

}
