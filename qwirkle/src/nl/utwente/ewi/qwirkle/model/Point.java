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
	public boolean equals(Object o){
		return (o instanceof Point) && ((Point) o).getX() == this.getX() && ((Point)o).getY() == this.getY();
	}

}
