package nl.utwente.ewi.qwirkle.model;

import java.awt.Point;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.Shape;

public class Tile {
	
	private Color c;
	private Shape s;
	private int iShape;
	private int iColor;
	
	
	public Tile( Color c, Shape s){
		this.c = c;
		this.s= s;		
	}
	
	public Tile(int i){
		iShape = i % 6;
		iColor = (i - iShape) / 6;
		
		this.c = Color.getColorByInt(iColor);
		this.s = Shape.getShapeByInt(iShape);
	}
	
	public int getiShape() {
		return iShape;
	}

	public int getiColor() {
		return iColor;
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
	
	public int getIntOfTile(){
		return getColor().getInt() * 6 + getShape().getInt();
	}
	
	public boolean isValidNeighbour(Tile t) {
		return this.getShape() == t.getShape() ^ this.getColor() == t.getColor();
	}
	
}
