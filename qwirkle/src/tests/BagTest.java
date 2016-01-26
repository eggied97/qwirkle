package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.Bag;

public class BagTest {

	Bag b;
	
	@Before
	public void setUp() throws Exception {
		b = new Bag();
	}
	
	@Test
	public void initTest(){
		assertTrue(b.getBag().size() == 108);
	}
	
	@Test
	public void testGetRandomTile(){
		b.getRandomTile();
		
		assertTrue(b.getBag().size() == 107);
		
		for(int i = 0; i < 10; i++){
			b.getRandomTile();
		}
		
		assertTrue(b.getBag().size() == 97);		
	}
	
	@Test
	public void testGetMultipleRandomTiles(){
		final int noOfTiles = 45;
		
		b.getRandomTile(noOfTiles);
		
		assertTrue(b.getBag().size() == 108 - noOfTiles);
		
		b.getRandomTile(109 - noOfTiles);
		
		//should be one to many, should be a empty bag now
		
		assertTrue(b.getBag().size() == 0);
	}
	
	@Test
	public void testIsEmpty(){
		assertTrue(!b.isEmpty());
		
		b.getRandomTile(108);
		
		assertTrue(b.isEmpty());
	}
	
	@Test
	public void testGetBag(){
		List<Tile> tiles = b.getBag();
		
		Tile t = b.getRandomTile();
		tiles.remove(t);
		
		assertTrue(tiles.equals(b.getBag()));
		
	}
	
	@Test
	public void testGetAmount(){
		assertTrue(b.getAmountOfTiles() == b.getBag().size());
		
		b.getRandomTile(12);
		
		assertTrue(b.getAmountOfTiles() == b.getBag().size());
	}
	
	@Test
	public void testAddTile(){
		assertTrue(b.getAmountOfTiles() == 108);
		
		List<Integer> toAdd = new ArrayList<>();
		
		for(int i = 0; i < 10; i++){
			toAdd.add(i);
		}
		
		b.addTiles(toAdd);
		
		assertTrue(b.getAmountOfTiles() == 118);
	}

	
}
