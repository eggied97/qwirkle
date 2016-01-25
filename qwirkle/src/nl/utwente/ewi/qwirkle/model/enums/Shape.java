package nl.utwente.ewi.qwirkle.model.enums;

public enum Shape {
	CIRCLE(0, 'o'), CROSS(1, 'x'), DIAMOND(2, 'd'), RECTANGLE(3, 'r'), STAR(4, '*'), CLUBS(5, 'c');

	public final int i;
	public final char c;

	/** 
	 * @param j the <code> integer </code> value of the  <code> Shape </code>
	 * @param cc the <code> char </code> value of the <code> Shape </code>
	 */
	private Shape(int j, char cc) {
		this.i = j;
		this.c = cc;
	}

	/**
	 * @return the <code> integer </code> representation of this
	 *         <code> Shape </code>.
	 */
	public int getInt() {
		return i;
	}

	/**
	 * @return the <code> char </code> representation of this
	 *         <code> Shape </code>.
	 */
	public char getChar() {
		return this.c;
	}

	/**
	 * 
	 * @param j
	 *            the integer value of the <code> Shape </code>.
	 * @return a instance of <code> Spape </code> corresponding with the integer
	 *         j.
	 */
	public static Shape getShapeByInt(int j) {
		switch (j) {
		case 0:
			return CIRCLE;
		case 1:
			return CROSS;
		case 2:
			return DIAMOND;
		case 3:
			return RECTANGLE;
		case 4:
			return STAR;
		case 5:
			return CLUBS;

		default:
			return null;
		}
	}

}
