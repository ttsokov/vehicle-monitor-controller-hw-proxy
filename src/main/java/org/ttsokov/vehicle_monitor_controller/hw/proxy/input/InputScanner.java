package org.ttsokov.vehicle_monitor_controller.hw.proxy.input;

import java.util.Scanner;

public class InputScanner {

	private Scanner scanner;
	private static InputScanner inputScanner;

	private InputScanner() {
		try {
			this.scanner = new Scanner(System.in);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Singleton
	public static InputScanner getInstance() {
		if (inputScanner == null) {
			inputScanner = new InputScanner();
		}
		return inputScanner;
	}

	public void closeScanner() {
		try {
			scanner.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String readLine() {
		return scanner.nextLine().trim();
	}
}
