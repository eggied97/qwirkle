package nl.utwente.ewi.qwirkle.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.enums.Color;
import nl.utwente.ewi.qwirkle.model.enums.Shape;

public class Bag {
	/**
	 * Number of color-shape combinations
	 * */
	private final static int NUMBER_OF_EACH_COMBO = 4;

	List<Tile> tiles;

	public Bag() {
		tiles = new ArrayList<>();
		initBag();
	}

	/**
	 * Initializes the bag by putting all the tiles into it, and shuffles it
	 * afterwords.
	 */
	private void initBag() {
		for (Color c : Color.values()) {
			for (Shape s : Shape.values()) {
				Tile t = new Tile(c, s);

				for (int i = 0; i < NUMBER_OF_EACH_COMBO; i++) {
					tiles.add(t);
				}
			}
		}
		Collections.shuffle(tiles);
	}

	/**
	 * @return random <code> Tile </code> from the bag
	 */
	public Tile getRandomTile() {
		Tile t = tiles.get((int) (Math.random() * tiles.size()));
		tiles.remove(t); //TODO maybey removes 4 tiles
		return t;
	}

	/**
	 * Get a amount of random tiles from the bag
	 * 
	 * @param amount
	 *            - the amount of random tiles from the bag
	 * @return <code> List<Tile> </code> of random tiles
	 **/
	public List<Tile> getRandomTile(int amount) {
		List<Tile> l = new ArrayList<>();

		for (int i = 0; i < amount; i++) {
			l.add(getRandomTile());
		}

		return l;
	}

	/**
	 * @return <code> List<tile> </code> containing the tiles from the bag
	 */
	public List<Tile> getBag() {
		return this.tiles;
	}
	
	/**
	 * 
	 * @return returns true if the bag is empty
	 */
	public boolean isEmpty() {
		return getBag().isEmpty();
	}
	
	public int getAmountOfTiles() {
		return this.tiles.size();
	}


}
