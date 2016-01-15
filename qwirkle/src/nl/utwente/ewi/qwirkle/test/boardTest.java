package nl.utwente.ewi.qwirkle.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Tile;

public class boardTest {
	
	private Board b;

	@Before
	public void setUp() throws Exception {
		b = new Board();
	}

	
	@Test
	public void testPutTile() {
		Tile t = new Tile(5);
		b.putTile(0, 0, t);
		assertEquals(b.getTile(0,0), t);
	}

}
