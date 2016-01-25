package nl.utwente.ewi.qwirkle.ui.tui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import nl.utwente.ewi.qwirkle.util;
import nl.utwente.ewi.qwirkle.callback.ResultCallback;
import nl.utwente.ewi.qwirkle.callback.UserInputCallback;
import nl.utwente.ewi.qwirkle.callback.UserInterfaceCallback;
import nl.utwente.ewi.qwirkle.client.Game;
import nl.utwente.ewi.qwirkle.client.UserInputThread;
import nl.utwente.ewi.qwirkle.model.Move;
import nl.utwente.ewi.qwirkle.model.Point;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.model.enums.InputState;
import nl.utwente.ewi.qwirkle.model.player.ComputerPlayer;
import nl.utwente.ewi.qwirkle.model.player.HumanPlayer;
import nl.utwente.ewi.qwirkle.model.player.Player;
import nl.utwente.ewi.qwirkle.model.player.strategy.SuperStrategy;
import nl.utwente.ewi.qwirkle.protocol.Protocol;
import nl.utwente.ewi.qwirkle.ui.UserInterface;

public class TUIView implements UserInterface, UserInputCallback {

	private static final String QUESTION_FOR_SERVERINFORMATION = "With what server doe you want to connect? (format : ip@port )";
	private static final String QUESTION_ASK_NAME = "What is your name? ";
	private static final String QUESTION_QUEUE = "With how many players would you like to play (2-4)? ( format: 2,3 )";
	private static final String QUESTION_PLAY_OR_EXHANGE = "Do you want to play a Tile, or exhange a Tile, see the score, chat or quit? (p/e/s/c/q)";
	private static final String QUESTION_ASK_FOR_MOVE = "Which Tiles do you want to lay down?(h for a hint, or b to go back) \n format: [no_tile@x,y] :";
	private static final String QUESTION_ASK_FOR_TRADE = "Which Tiles do you want to trade?(or b to go back) \n format: [no_tile] :";
	private static final String QUESTION_ASK_FOR_CHAT = "The format for a chat message is as follow: \n global/@username message";
	private static final String QUESTION_ASK_FORAI_TIME = "How long should the AI be able to think (in seconds)? ";

	private UserInterfaceCallback callback;	

	private UserInputThread UIT;
	
	public TUIView() {
		UIT = new UserInputThread(this);
		UIT.start();
	}
	
	@Override
	public void askForServerInformation() {
		this.UIT.setInputState(InputState.FORSERVERINFORMATION);
		this.printMessage(QUESTION_FOR_SERVERINFORMATION);		
	}

	@Override
	public void askForLogin() {
		UIT.setInputState(InputState.FORLOGIN);
		this.printMessage(QUESTION_ASK_NAME);

	}

	@Override
	public void askQueueWithHowManyPlayers() {
		UIT.setInputState(InputState.FORQUEUE);
		this.printMessage(QUESTION_QUEUE);		
	}

	@Override
	public void changeTurn(Player p) {
		printMessage("It is " + p.getName() + "'s turn");
	}

	@Override
	public void playerTraded(Player p, int noOfTilesTraded) {
		printMessage(p.getName() + " has traded " + noOfTilesTraded + " tiles");
	}

	@Override
	public void showError(String message) {
		System.err.println(message);
	}

	@Override
	public void printMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void showScore(Map<Player, Integer> scoreMap, boolean fromGameEnd) {
		
		if(fromGameEnd){
			printMessage("Game was ended, score:");
		}
		
		for (Entry e : scoreMap.entrySet()) {
			printMessage(((Player) e.getKey()).getName() + " has a score of: " + e.getValue());
		}
	}

	@Override
	public void showHand(List<Tile> tiles) {
		
		printMessage("Your hand :");
		
		String result = "";
		
		for (int i = 0; i < tiles.size(); i++) {
			result += tiles.get(i).getHumanReadableString() + " (" + i+")";
				result += " ";
		}
		
		printMessage(result);
	}

	@Override
	public void askForPlayOrExchange() {
		UIT.setInputState(InputState.IDLE);
		this.printMessage(QUESTION_PLAY_OR_EXHANGE);
	}

	@Override
	public void askForMove() {
		UIT.setInputState(InputState.FORMOVE);
		this.printMessage(QUESTION_ASK_FOR_MOVE);
	}

	@Override
	public void askForTrade() {
		UIT.setInputState(InputState.FORTRADE);
		this.printMessage(QUESTION_ASK_FOR_TRADE);
	}

	@Override
	public void askForChatMessage() {
		UIT.setInputState(InputState.FORCHAT);
		this.printMessage(QUESTION_ASK_FOR_CHAT);
	}

	@Override
	public void setCallback(UserInterfaceCallback callback) {
		this.callback = callback;
	}

	@Override
	public void handlePlayerInput(String input, InputState state) {
		switch (state) {
			case FORSERVERINFORMATION:
				callback.setupServer(input);				
				break;
		
			case FORLOGIN:
				String name = input;
				Player p;
	
				if(name.equals("COMPUTERMAN")){
					p =  new ComputerPlayer("pcman" + (int)(Math.random() * 4));
				}else if(name.equals("COMPUTERMANSLIM")){
					p =  new ComputerPlayer("pcmanslim" + (int)(Math.random() * 4), new SuperStrategy());				
				}else{
					p = new HumanPlayer(name);
				}
				
				callback.login(p);
				
				break;
				
			case FORASKINGAITIME:
				
				try{
					int time = Integer.parseInt(input);
					
					callback.setAITime(time);					
				}catch (NumberFormatException e) {
					this.showError("Input should be an integer.");
					this.askForAITime();
				}
				
				break;
				
			case FORQUEUE:
				String[] queues = input.split(",");
				
				int[] result = new int[0];

				//TODO check if this check is needed or not
				if (queues.length == 0 || queues.length > 3) {
					showError("Wrong input: " + QUESTION_QUEUE);					
					callback.queue(result);
				} else {
					result = new int[queues.length];

					for (int i = 0; i < result.length; i++) {
						try {
							result[i] = Integer.parseInt(queues[i].trim());
						} catch (NumberFormatException e) {
							showError("You have to enter an integer");
							result = new int[0];
						}
					}
					
					callback.queue(result);
				}
				break;
			
			case IDLE:
				callback.determinedAction(input);
				break;
	
			case FORMOVE:
				if (input.equals("b")) {
					this.askForPlayOrExchange();
				}else if(input.equals("h")){
					callback.printHint();
				} else {	
					callback.putMove(input);
				}
				break;
			case FORTRADE:
				if (input.equals("b")) {
					this.askForPlayOrExchange();
				} else {
					callback.putTrade(input);					
				}
				break;
			case FORCHAT:
				if (input.equals("b")) {
					this.askForPlayOrExchange();
				} else {
					callback.sendChat(input);
				}
				break;
		}
	}

	@Override
	public void askForAITime() {
		UIT.setInputState(InputState.FORASKINGAITIME);
		this.printMessage(QUESTION_ASK_FORAI_TIME);		
	}


	
	
}
