package nl.utwente.ewi.qwirkle.model.exceptions;

import java.awt.Point;

public class PlaceOccupiedException extends Exception {

	Point p;

	public PlaceOccupiedException(Point p) {
		this.p = p;
	}

	@Override
	public String getMessage() {
		return "error: there is already a tile on this point; (" + this.p.getX() + ", " + this.p.getY() + ")";
	}
}
