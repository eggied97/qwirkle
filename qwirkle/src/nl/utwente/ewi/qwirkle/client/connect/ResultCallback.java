package nl.utwente.ewi.qwirkle.client.connect;

public interface ResultCallback {

	/**
	 * 
	 * @param result
	 *            result string that is comming from the server
	 */
	public void resultFromServer(String result);
}
