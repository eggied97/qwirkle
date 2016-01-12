package nl.utwente.ewi.qwirkle.model;

import java.awt.Point;

import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.Shape;

public class Tile {
	
	private Color c;
	private Shape s;
	
	
	public Tile( Color c, Shape s){
		this.c = c;
		this.s= s;		
	}
	
	public Tile(int i){
		int iShape = i % 6;
		int iColor = (i - iShape) / 6;
		
		this.c = Color.getColorByInt(iColor);
		this.s = Shape.getShapeByInt(iShape);
	}
	
	public Shape getShape(){
		return this.s;
	}
	
	public Color getColor(){
		return this.c;
	}

	@Override
	public String toString(){
		//TODO doe volgens protocol
		return getIntOfTile()+"";
	}
	
	private int getIntOfTile(){
		return getColor().getInt() * 6 + getShape().getInt();
	}
}
