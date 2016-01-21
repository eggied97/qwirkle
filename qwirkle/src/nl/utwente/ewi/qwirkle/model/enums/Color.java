package nl.utwente.ewi.qwirkle.model.enums;

public enum Color {
	PURPLE(0, 'p'), RED(1, 'r'), ORANGE(2, 'o'), YELLOW(3, 'y'), GREEN(4, 'g'), BLUE(5, 'b');

	public final int i;
	public final char c;

	private Color(int j, char cc) {
		this.i = j;
		this.c = cc;
	}

	public int getInt() {
		return i;
	}
	
	public char getChar(){
		return this.c;
	}

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
