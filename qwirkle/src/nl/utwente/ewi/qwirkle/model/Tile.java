package nl.utwente.ewi.qwirkle.model;

import java.awt.Point;

import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.Shape;

public class Tile {
	
	private Color c;
	private Shape s;
	private Point loc;
	
	
	public Tile(int x, int y, Color c, Shape s){
		this.c = c;
		this.s= s;
		
		this.loc = new Point(x, y);
	}
	
	public Shape getShape(){
		return this.s;
	}
	
	public Color getColor(){
		return this.c;
	}

	public String toString(){
		return getShape().i + " / " + getColor().i;
	}
}
