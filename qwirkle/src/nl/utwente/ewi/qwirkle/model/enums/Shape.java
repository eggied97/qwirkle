package nl.utwente.ewi.qwirkle.model.enums;

public enum Shape {	
	CIRCLE(0), CROSS(1), DIAMOND(2), RECTANGLE(3), STAR(4), CLUBS(5);
	
	public final int i;
	
	private Shape(int j){
		this.i = j;
	}
	
	public int getInt(){
		return i;
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
