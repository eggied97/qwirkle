package nl.utwente.ewi.qwirkle.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.utwente.ewi.qwirkle.protocol.IProtocol;
import nl.utwente.ewi.qwirkle.protocol.protocol;
import nl.utwente.ewi.qwirkle.server.connect.ClientHandler;
import nl.utwente.ewi.qwirkle.server.connect.Server;

public class HandleCommand {
	
	private Server server;

	public HandleCommand(Server server) {
		this.server = server;
	}

	public static HandleCommand getInstance(Server server) {
		return new HandleCommand(server);
	}

	public String handleIdentifyName(String[] strAy) {
		String name = strAy[1];
		if (name.matches("^[a-zA-Z0-9-_]$")) {
			return name;
		} else {
			return null;
		}

	}

	public List<IProtocol.Feature> handleIdentifyFeatures(String[] strAy) {
		List<IProtocol.Feature> features = new ArrayList<>();
		
		for(int i = 2; i < strAy.length; i++) {
			switch(IProtocol.Feature.valueOf(strAy[i])) {
			case CHAT:
				features.add(IProtocol.Feature.CHAT);
				break;
			case CHALLENGE:
				features.add(IProtocol.Feature.CHALLENGE);
				break;
			case LEADERBOARD:
				features.add(IProtocol.Feature.LEADERBOARD);
				break;
			case LOBBY:
				features.add(IProtocol.Feature.LOBBY);
				break;
			default:
				break;
		}
			i++;
		}
		return features;
		
	}
	
	public void handleQueue(String[] strAy, ClientHandler ch) {
		String queue = strAy[1];
		String[] queueSpl = queue.split(",");
		Map<Integer, List<ClientHandler>> map = server.getQueues();
		for(int i = 0; i < queueSpl.length; i++) {
			switch(Integer.parseInt(queueSpl[i])) {
			case 2:
				map.get(2).add(ch);
				break;
				// TODO check if queue full
				// TODO test
			case 3:
				map.get(3).add(ch);
				break;
			case 4:
				map.get(4).add(ch);
				break;
			default:
				ch.sendMessage(protocol.SERVER_ERROR + IProtocol.Error.QUEUE_INVALID);
				break;
			}
		}
		
		
	}

	public void handleMoveTrade(String[] strAy, ClientHandler ch) {
		
	}
}
