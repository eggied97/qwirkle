package nl.utwente.ewi.qwirkle.model.player.strategy;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Tile;

public class test {

	public static void main(String[] args){
		List<Tile> tiles = new ArrayList<Tile>();
		
		for(int i = 0; i < 6; i++){
			int tileNo = (int)(Math.random() * 35);
			tiles.add(new Tile(tileNo));
		}
		
		System.out.println(tiles);
		
		List<List<Move>> waaat = new SuperStrategy().getAllMoves(new Board(), tiles);
		
		int heighest = 0;
		List<Move> lHeighest = null;
		
		for(List<Move> m : waaat){
			if(m.size() > heighest){
				heighest = m.size();
				lHeighest = m;
			}
		}
		
		Board b = new Board();
		b.putTile(lHeighest);
		
		System.out.println(waaat.size() + " / " + heighest);
		System.out.println(b.toString());
		
		List<Move> bestMove = new SuperStrategy().determineMove(new Board(), tiles);
		

		b = new Board();
		b.putTile(bestMove);
		
		System.out.println(b.toString());
		
	}
}
