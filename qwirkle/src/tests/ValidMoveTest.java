package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.Shape;
import nl.utwente.ewi.qwirkle.server.ValidMove;

public class ValidMoveTest {
		
		
		ValidMove vm;
		Board b;
		
		@Before
		public void setUp() throws Exception {
			vm = new ValidMove();
			b = new Board();
		}
		
		@Test
		public void goodMoveStartTest(){
			List<Move> moveSet = new ArrayList<>();
			for(int i = 0; i < 5; i++) {
				moveSet.add(new Move(new Point(0, i), new Tile(i)));
			}
			assertTrue(vm.validMoveSet(moveSet, b));	
		}
		
		@Test
		public void moveTooBigTest() {
			List<Move> moveSet = new ArrayList<>();
			for(int i = 0; i < 7; i++) {
				moveSet.add(new Move(new Point(0, i), new Tile(i)));
			}
			assertFalse(vm.validMoveSet(moveSet, b));
		}
		
		@Test
		public void pointsNotInARowTest() {
			List<Move> moveSet = new ArrayList<>();
			for(int i = 0; i < 4; i++) {
				moveSet.add(new Move(new Point(0, i), new Tile(i)));
			}
			for(int i = 0; i < 2; i++) {
				moveSet.add(new Move(new Point(i, 0), new Tile(i)));
			}
			assertFalse(vm.validMoveSet(moveSet, b));
		}
		
		@Test
		public void notCorrespondingTilesTest() {
			List<Move> moveSet = new ArrayList<>();
			for(int i = 4; i < 9; i++) {
				moveSet.add(new Move(new Point(0, i), new Tile(i)));
			}
			
			assertFalse(vm.validMoveSet(moveSet, b));
		}
		
		@Test
		public void pointsNotContinuousTest() {
			List<Move> moveSet = new ArrayList<>();
			for(int i = 0; i < 4; i++) {
				moveSet.add(new Move(new Point(i*2, 0), new Tile(i)));
			}
			
			assertFalse(vm.validMoveSet(moveSet, b));
		}
		
		@Test
		public void pointsNotContinuousXTest() {
			List<Move> moveSet = new ArrayList<>();
			for(int i = 0; i < 4; i++) {
				moveSet.add(new Move(new Point(0, i*2), new Tile(i)));
			}
			
			assertFalse(vm.validMoveSet(moveSet, b));
		}
		
		@Test
		public void singlePoint() {
			List<Move> moveSet = new ArrayList<>();
			for(int i = 0; i < 1; i++) {
				moveSet.add(new Move(new Point(0, i), new Tile(i)));
			}
			
			assertTrue(vm.validMoveSet(moveSet, b));
		}
		
		@Test
		public void nextToRowTest() {
			b.putTile(0, -1, new Tile(5));
			b.putTile(0, 4, new Tile(4));
			b.putTile(1, 0, new Tile(3));
			b.putTile(-1, 0, new Tile(4));
			List<Move> moveSet = new ArrayList<>();
			for(int i = 0; i < 4; i++) {
				moveSet.add(new Move(new Point(0, i), new Tile(i)));
			}
			
			assertTrue(vm.validMoveSet(moveSet, b));
		}
}
