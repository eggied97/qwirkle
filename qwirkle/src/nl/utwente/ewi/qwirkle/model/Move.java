package nl.utwente.ewi.qwirkle.model;

import java.awt.Dimension;

public class Move {
	
	private Dimension coordinate;
	private Tile tile;
	
	/**
	 * 
	 * @param p
	 * @param t
	 */
	public Move(Dimension d, Tile t) {
		this.coordinate = d;
		this.tile = t;
	}
	
	/**
	 * 
	 * @return returns the <code> Move </code> corresponding to the tile and dimension
	 */
	public Move getMove() {
		return this;
	}
	
	/**
	 * 
	 * @return returns the <code> Dimension </code> belonging to this move
	 */
	public Dimension getDimension() {
		return coordinate;
	}
	
	/**
	 * 
	 * @return returns the <code> Tile </code> belonging to this move
	 */
	public Tile getTile() {
		return tile;
	}
	
	public String toString(){
		return String.format("%i@%i,%i", this.tile.toString(), this.getDimension().getWidth(), this.getDimension().getHeight());
	}
	
	
	
	
	
}
