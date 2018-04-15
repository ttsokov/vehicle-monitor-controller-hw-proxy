package org.ttsokov.vehicle_monitor_controller.hw.proxy.input;

import org.ttsokov.vehicle_monitor_controller.hw.proxy.validators.IValidator;

public class InputReader {

	private InputScanner inputScanner;

	public InputReader() {
		inputScanner = InputScanner.getInstance();
	}

	public String collectInput(IValidator validator, String infoMsg) {
		System.out.println(infoMsg);
		String inputStr = inputScanner.readLine();

		try {
			validator.validate(inputStr);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
			return collectInput(validator, infoMsg);// Recursion
		}

		return inputStr;
	}

	public void closeReader() {
		inputScanner.closeScanner();
	}
}
