package nl.utwente.ewi.qwirkle.model.enums;

public enum Shape {
	
	CIRCLE(0), CROSS(1), DIAMOND(2), RECTANGLE(3), STAR(4), CLUBS(5);
	
	public final int i;
	
	private Shape(int j){
		this.i = j;
	}

}
