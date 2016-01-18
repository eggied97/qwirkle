package nl.utwente.ewi.qwirkle.model;

import java.util.List;

import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.Shape;

public class Tile {

	private Color c;
	private Shape s;
	private int iShape;
	private int iColor;

	public Tile(Color c, Shape s) {
		this.c = c;
		this.s = s;
	}

	public Tile(int i) {
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

	public Shape getShape() {
		return this.s;
	}

	public Color getColor() {
		return this.c;
	}

	@Override
	public String toString() {
		return String.valueOf(getIntOfTile());
	}

	public String getHumanReadableString() {
		return this.getColor().toString().substring(0, 1) + "_" + this.getShape().toString().substring(0,1);
	}

	public int getIntOfTile() {
		return getColor().getInt() * 6 + getShape().getInt();
	}

	public boolean isValidNeighbour(Tile t) {
		return this.getShape() == t.getShape() ^ this.getColor() == t.getColor();
	}

	@Override
	public boolean equals(Object t) {
		return (t instanceof Tile) && ((Tile) t).getShape() == this.getShape()
				&& ((Tile) t).getColor() == this.getColor();
	}

	@Override
	public int hashCode() {
		return getIntOfTile();
	}

}
