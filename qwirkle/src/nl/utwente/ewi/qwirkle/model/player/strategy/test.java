package nl.utwente.ewi.qwirkle.model.player.strategy;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Tile;

public class test {

	public static void main(String[] args){
		List<Tile> tiles = new ArrayList<Tile>();
		
		for(int i = 0; i < 50; i++){
			int tileNo = (int)(Math.random() * i) % 35;
			tiles.add(new Tile(tileNo));
		}
		
		List<List<Tile>> waaat = new SuperStrategy().getAllPossibleTilePairs(tiles);
		
		int heighest = 0;
		List<Tile> lHeighest = null;
		
		for(List<Tile> t : waaat){
			if(t.size() > heighest){
				heighest = t.size();
				lHeighest = t;
			}
		}
		
		System.out.println(waaat.size() + " / " + heighest);
		
	}
}
