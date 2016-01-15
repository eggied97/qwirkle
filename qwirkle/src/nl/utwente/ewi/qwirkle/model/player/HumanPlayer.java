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

	public void setGame(Game g) {
		this.g = g;
	}

	@Override
	public Object determineMove(Board board) {
		if (this.g == null) {
			// TODO throw error
		}

		String pORe = this.g.getUI().askForPlayOrExchange();

		switch (pORe) {
		case "p":
			String awnser = this.g.getUI().askForMove();

			List<Move> list = parseMoveAwnser(awnser);

			if (list.equals(null) || list.size() == 0) {
				return determineMove(board);
			} else {
				return list;
			}

		case "e":
			String awnserQ = this.g.getUI().askForTrade();

			List<Tile> rList = parseTradeAwnser(awnserQ);
			
			if(rList.size() == 0){
				this.g.getUI().showError("Invalid input.");
				return determineMove(board);
			}else{
				return rList;
			}

		default:
			this.g.getUI().showError("Wrong argument.");
			return determineMove(board);
		}
	}

	private List<Tile> parseTradeAwnser(String trades){
		List<Tile> result = new ArrayList<>();
		
		String[] mulTrades = trades.split(" ");
		
		for(String trade : mulTrades){
			result.add(this.getHand().get(Integer.parseInt(trade)));
		}
		
		return result;
	}

	private List<Move> parseMoveAwnser(String a) {
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
	
	public String printHand(){
		String result = "";
		
		List<Tile> hand = this.getHand();
		
		result += "Current hand ("+hand.size()+") : \n";
		
		for(int i = 0; i < hand.size(); i++){
			result += hand.get(i).toString()+"("+i+") \t";
		}
		
		result += "\n";
		
		return result;
	}

}
