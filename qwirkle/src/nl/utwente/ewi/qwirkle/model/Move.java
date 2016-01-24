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
	 *         {@link nl.utwente.ewi.qwirkle.model.Model} </code> corresponding
	 *         to the tile and Point
	 */
	public Move getMove() {
		return this;
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

	public String toString() {
		return String.format(this.tile.toString()+"@%d,%d", this.getPoint().getX(), this.getPoint().getY());
	}

}
