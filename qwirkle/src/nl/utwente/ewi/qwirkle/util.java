package nl.utwente.ewi.qwirkle;

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
	 * Writes a prompt to standard out and tries to read an int value from
	 * standard in. This is repeated until an int value is entered.
	 * 
	 * @param prompt
	 *            the question to prompt the user
	 * @return the first int value which is entered by the user
	 */
	public static String readString(String prompt) {
		String value = "";
		boolean StringRead = false;
		do {
			System.out.print(prompt);
			try (Scanner line = new Scanner(System.in);) {
				if (line.hasNextLine()) {
					StringRead = true;
					value = line.nextLine();
				}
			}
		} while (!StringRead);

		return value;
	}

	public static boolean FeatureArrayContains(IProtocol.Feature[] s, IProtocol.Feature a) {
		for (IProtocol.Feature ftr : s) {
			if (ftr.equals(a)) {
				return true;
			}
		}

		return false;
	}

}
