package nl.utwente.ewi.qwirkle.model;

public class Point {

	private int x;
	private int y;

	/**
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Point) && ((Point) o).getX() == this.getX() && ((Point) o).getY() == this.getY();
	}

	/**
	 * For us to be able to use this class as a key in a hashmap we needed to
	 * provide a hashCode().
	 * 
	 * Because this is a point, we used the same hashCode() as the Point2D class
	 * from java itself.
	 * 
	 * This is the same hashCode() as <code>java.awt.geom.Point2D</code> (
	 * {@link http://developer.classpath.org/doc/java/awt/geom/Point2D-source.html}
	 */
	@Override
	public int hashCode() {
		long l = java.lang.Double.doubleToLongBits(getY());
		l = l * 31 ^ java.lang.Double.doubleToLongBits(getX());
		return (int) ((l >> 32) ^ l);
	}

}
