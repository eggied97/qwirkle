package nl.utwente.ewi.qwirkle.model;

import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.Shape;

public class Tile {

	private Color c;
	private Shape s;
	private int iShape;
	private int iColor;

	/**
	 * creates a <code> Tile </code> object.
	 * 
	 * @param c
	 *            the {@link nl.utwente.ewi.qwirkle.model.enums.Color} instance
	 *            of the tile
	 * @param s
	 *            the {@link nl.utwente.ewi.qwirkle.model.enums.Shape} instance
	 *            of the tile
	 */
	public Tile(Color c, Shape s) {
		this.c = c;
		this.s = s;

		this.iColor = c.getInt();
		this.iShape = s.getInt();
	}

	/**
	 * creates a <code> Tile </code> object.
	 * 
	 * @param i
	 *            the integer of the tile (See
	 *            {@link nl.utwente.ewi.qwirkle.protocol.IProtocol} for the
	 *            translation).
	 */
	public Tile(int i) {
		iShape = i % 6;
		iColor = (i - iShape) / 6;

		this.c = Color.getColorByInt(iColor);
		this.s = Shape.getShapeByInt(iShape);
	}

	/**
	 * @return the integer value of the
	 *         {@link nl.utwente.ewi.qwirkle.model.enums.Shape} enum
	 */
	public int getiShape() {
		return iShape;
	}

	/**
	 * @return the integer value of the
	 *         {@link nl.utwente.ewi.qwirkle.model.enums.Color} enum
	 */
	public int getiColor() {
		return iColor;
	}

	/**
	 * @return the {@link nl.utwente.ewi.qwirkle.model.enums.Shape} enum
	 *         corresponding with this <code> Tile </code>
	 */
	public Shape getShape() {
		return this.s;
	}

	/**
	 * @return the {@link nl.utwente.ewi.qwirkle.model.enums.Color} enum
	 *         corresponding with this <code> Tile </code>
	 */
	public Color getColor() {
		return this.c;
	}

	@Override
	public String toString() {
		return String.valueOf(getIntOfTile());
	}

	/**
	 * @return the value of this <code> Tile </code>, in a human readable form.
	 */
	public String getHumanReadableString() {
		return this.getColor().getChar() + "_" + this.getShape().getChar();
	}

	/**
	 * @return the integer of the tile (See
	 *         {@link nl.utwente.ewi.qwirkle.protocol.IProtocol} for the
	 *         translation).
	 */
	public int getIntOfTile() {
		return getColor().getInt() * 6 + getShape().getInt();
	}

	/**
	 * checks if the given <code> Tile </code> is a valid neighbour of this
	 * instance.
	 * 
	 * @param t
	 *            the <code> Tile </code> that needs to be checked against this
	 *            instance.
	 * @return true if this <code> Tile </code> is a valid neighbour of
	 *         <code> t</code>
	 */
	public boolean isValidNeighbour(Tile t) {
		// Use the XOR operator
		return this.getShape() == t.getShape() ^ this.getColor() == t.getColor();
	}

	@Override
	public boolean equals(Object t) {
		return (t instanceof Tile) && ((Tile) t).getShape() == this.getShape()
				&& ((Tile) t).getColor() == this.getColor();
	}

	@Override
	public int hashCode() {
		// This implementation is enough, because the int of the tile is unique.
		return getIntOfTile();
	}

}
