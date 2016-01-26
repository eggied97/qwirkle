package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.server.score.ScoreCalc;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.server.ValidMove;

public class ScoreTest {

	private ScoreCalc calc;
	private Board b;

	@Before
	public void setUp() throws Exception {
		calc = new ScoreCalc();
		b = new Board();
	}

	@Test
	public void firstMoveTest() {
		List<Move> moveSet = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			moveSet.add(new Move(new Point(0, i), new Tile(i)));
		}
		b.putTile(moveSet);
		assertEquals(calc.calculate(b, moveSet), 5);
	}

	@Test
	public void fullRowTest() {
		List<Move> moveSet = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			moveSet.add(new Move(new Point(0, i), new Tile(i)));
		}
		b.putTile(moveSet);
		assertEquals(calc.calculate(b, moveSet), 12);
	}

	@Test
	public void multipleRowTest() {
		List<Move> moveSet = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			moveSet.add(new Move(new Point(0, i), new Tile(i)));
		}

		b.putTile(-1, 0, new Tile(3));
		b.putTile(-2, 0, new Tile(4));
		b.putTile(moveSet);
		assertEquals(calc.calculate(b, moveSet), 5);
	}

	@Test
	public void doubleFullRowTest() {
		List<Move> moveSet = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			b.putTile(new Move(new Point(0, i), new Tile(i)));
		}
		for (int i = 0; i < 5; i++) {
			b.putTile(new Move(new Point(i + 1, 5), new Tile(i)));
		}
		moveSet.add(new Move(new Point(0, 5), new Tile(5)));
		b.putTile(moveSet);
		assertEquals(calc.calculate(b, moveSet), 24);
	}

}
