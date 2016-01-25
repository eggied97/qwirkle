package nl.utwente.ewi.qwirkle.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;

public class HumanPlayer extends Player {

	Game g;

	public HumanPlayer(String name) {
		super(name);
	}

	/**
	 * sets the {@link nl.utwente.ewi.qwirkle.client.Game} instance.
	 * 
	 * @param g
	 *            the {@link nl.utwente.ewi.qwirkle.client.Game} instance
	 */
	public void setGame(Game g) {
		this.g = g;
	}

	/**
	 * Not used -> handled by the UI.
	 */
	@Override
	public String determineAction() {
		return null;
	}

	/**
	 * Not used -> handled by the UI.
	 */
	@Override
	public List<Tile> determineTradeMove() {
		return null;
	}

	/**
	 * Not used -> handled by the UI.
	 */
	@Override
	public List<Move> determinePutMove(Board board) {
		return null;

	}

	/**
	 * Not used -> handled by the UI.
	 */
	@Override
	public String sendChat() {
		return null;
	}

	/**
	 * converts a string (according to
	 * {@link nl.utwente.ewi.qwirkle.protocol.IProtocol}) to a
	 * <code> List </code> of {@link nl.utwente.ewi.qwirkle.model.Tile}s.
	 * 
	 * @param a
	 *            the <code> String </code> that needs to be parsed.
	 * @return a <code> List </code> of
	 *         {@link nl.utwente.ewi.qwirkle.model.Tile} s.
	 */
	public List<Tile> parseTradeAwnser(String trades) {
		List<Tile> result = new ArrayList<>();

		String[] mulTrades = trades.trim().split(" ");

		for (String trade : mulTrades) {
			result.add(this.getHand().get(Integer.parseInt(trade)));
		}

		return result;
	}

	/**
	 * converts a string (according to
	 * {@link nl.utwente.ewi.qwirkle.protocol.IProtocol}) to a
	 * <code> List </code> of {@link nl.utwente.ewi.qwirkle.model.Move}s.
	 * 
	 * @param a
	 *            the <code> String </code> that needs to be parsed.
	 * @return <code> NULL </code> if there was an error, <code> List </code> of
	 *         {@link nl.utwente.ewi.qwirkle.model.Move}s otherwise.
	 */
	public List<Move> parseMoveAwnser(String a) {
		List<Move> result = new ArrayList<>();

		for (String m : a.split(" ")) {
			String[] parts = m.split("@");

			if (parts.length != 2) {
				this.g.getUI().showError("Invalid input.");
				return null;
			}

			String[] coord = parts[1].split(",");

			if (coord.length != 2) {
				this.g.getUI().showError("Invalid input.");
				return null;
			}

			Point pResult = new Point(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
			Move mResult = new Move(pResult, this.getHand().get(Integer.parseInt(parts[0])));

			result.add(mResult);
		}

		return result;
	}

	/**
	 * @return a <code> String </code> respresntation of the Players hand. (with
	 *         index numbers behind it, for easy entering in the TUI)
	 */
	public String printHand() {
		String result = "";

		List<Tile> hand = this.getHand();

		result += "Current hand (" + hand.size() + ") : \n";

		for (int i = 0; i < hand.size(); i++) {
			result += hand.get(i).toString() + "(" + i + ") \t";
		}

		result += "\n";

		return result;
	}

}
