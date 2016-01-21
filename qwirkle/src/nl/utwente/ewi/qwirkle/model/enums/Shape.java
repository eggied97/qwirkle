package nl.utwente.ewi.qwirkle.model.enums;

public enum Shape {	
	CIRCLE(0, 'o'), CROSS(1, 'x'), DIAMOND(2, 'd'),
	RECTANGLE(3, 'r'), STAR(4, '*'), CLUBS(5, 'c');
	
	public final int i;
	public final char c;
	
	private Shape(int j, char cc) {
		this.i = j;
		this.c = cc;
	}
	
	public int getInt() {
		return i;
	}
	
	public char getChar() {
		return this.c;
	}
	
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
