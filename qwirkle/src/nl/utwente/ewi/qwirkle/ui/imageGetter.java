package nl.utwente.ewi.qwirkle.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.SwitchPoint;

import javax.imageio.ImageIO;
import javax.swing.SwingConstants;

import nl.utwente.ewi.qwirkle.model.Tile;

public class imageGetter {
	

	BufferedImage wholeImage;
	private final static int widthTile = 62;
	private final static int heightTile = 62;
	
	public imageGetter() {
		try {
			wholeImage = ImageIO.read(new File("tiles.PNG"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BufferedImage getImageByTile(Tile t) {
		int x = 0;
		int y = 0;

		switch (t.getColor()) {
			case RED:
				y = 2;
				break;
			case ORANGE:
				y = 68;
				break;
			case YELLOW:
				y = 134;
				break;
	
			case GREEN:
				y = 200;
				break;
			case BLUE:
				y = 267;
				break;
			case PURPLE:
				y = 332;
				break;
		}

		switch (t.getShape()) {
		case CIRCLE:
			x = 5;
			break;

		case CLUBS:
			x = 336;
			break;

		case CROSS:
			x = 71;
			break;

		case DIAMOND:
			x = 137;
			break;

		case RECTANGLE:
			x = 203;
			break;

		case STAR:
			x = 269;
			break;

		default:
			break;
		}
		
		return wholeImage.getSubimage(x, y, widthTile, heightTile);
	}

}
