package nl.utwente.ewi.qwirkle.model.enums;

public enum Color {
	PURPLE(0, 'p'), RED(1, 'r'), ORANGE(2, 'o'), YELLOW(3, 'y'), GREEN(4, 'g'), BLUE(5, 'b');

	public final int i;
	public final char c;

	/** 
	 * @param j the <code> integer </code> value of the  <code> Color </code>
	 * @param cc the <code> char </code> value of the <code> Color </code>
	 */
	private Color(int j, char cc) {
		this.i = j;
		this.c = cc;
	}

	/** 
	 * @return the <code> integer </code> representation of this  <code> Color </code>.
	 */
	public int getInt() {
		return i;
	}

	/** 
	 * @return the <code> char </code> representation of this  <code> Color </code>.
	 */
	public char getChar() {
		return this.c;
	}

	/**
	 * 
	 * @param j
	 *            the integer value of the <code> Color </code>.
	 * @return a instance of <code> Color </code> corresponding with the integer
	 *         j.
	 */
	public static Color getColorByInt(int j) {
		switch (j) {
			case 0:
				return PURPLE;
			case 1:
				return RED;
			case 2:
				return ORANGE;
			case 3:
				return YELLOW;
			case 4:
				return GREEN;
			case 5:
				return BLUE;
	
			default:
				return null;
		}
	}
}
