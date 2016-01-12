package nl.utwente.ewi.qwirkle.model.enums;

public enum Color {
	PURPLE(0), RED(1), ORANGE(2), YELLOW(3), GREEN(4), BLUE(5);

	public final int i;

	private Color(int j) {
		this.i = j;
	}

	public int getInt() {
		return i;
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
