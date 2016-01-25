package nl.utwente.ewi.qwirkle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import nl.utwente.ewi.qwirkle.protocol.IProtocol;

public class util {

	/**
	 * Writes a prompt to standard out and tries to read an int value from
	 * standard in. This is repeated until an int value is entered.
	 * 
	 * @param prompt
	 *            the question to prompt the user
	 * @return the first int value which is entered by the user
	 */
	public int readInt(String prompt) {
		int value = 0;
		boolean intRead = false;
		do {
			System.out.print(prompt);
			try (Scanner line = new Scanner(System.in); Scanner scannerLine = new Scanner(line.nextLine());) {
				if (scannerLine.hasNextInt()) {
					intRead = true;
					value = scannerLine.nextInt();
				}
			}
		} while (!intRead);

		return value;
	}

	/**
	 * 
	 * @param prompt
	 *            the question to prompt the user
	 * @return the first int value which is entered by the user
	 */
	public static String readString(String tekst) {
		System.out.print(tekst);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}

	/**
	 * checks if Feature is inside the array
	 * 
	 * @param s
	 * @param a
	 * @return true if {@link nl.utwente.ewi.qwirkle.protocol.IProtocol.Feature}
	 *         is inside <code>s</code>.
	 */
	public static boolean FeatureArrayContains(IProtocol.Feature[] s, IProtocol.Feature a) {
		for (IProtocol.Feature ftr : s) {
			if (ftr.equals(a)) {
				return true;
			}
		}

		return false;
	}

}
