package nl.utwente.ewi.qwirkle.server;

import java.util.ArrayList;
import java.util.List;

import nl.utwente.ewi.qwirkle.protocol.IProtocol;

public class HandleCommand {

	public HandleCommand() {

	}

	public static HandleCommand getInstance() {
		return new HandleCommand();
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
	
}
