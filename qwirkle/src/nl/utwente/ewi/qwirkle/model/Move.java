package nl.utwente.ewi.qwirkle.model;

public class Move {

	private Point coordinate;
	private Tile tile;

	/**
	 * 
	 * @param p
	 * @param t
	 */
	public Move(Point p, Tile t) {
		this.coordinate = p;
		this.tile = t;
	}

	/**
	 * 
	 * @return returns the <code> 
	 *         {@link nl.utwente.ewi.qwirkle.model.Point} </code> belonging to
	 *         this move
	 */
	public Point getPoint() {
		return coordinate;
	}

	/**
	 * 
	 * @return returns the <code>  
	 *         {@link nl.utwente.ewi.qwirkle.model.Tile} </code> belonging to
	 *         this move
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * @return a human readable string, containing the information about the
	 *         move
	 */
	public String toHumanString() {
		return String.format(this.tile.getHumanReadableString() + "@%d,%d", this.getPoint().getX(),
				this.getPoint().getY());
	}

	/**
	 * @return a string containting information about the move (with the int of
	 *         the tile)
	 */
	public String toString() {
		return String.format(this.tile.toString() + "@%d,%d", this.getPoint().getX(), this.getPoint().getY());
	}

}
