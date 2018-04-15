package org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions;

public class InvalidMenuEntryException extends Exception {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Only values '1' or '2' are correct.";
	}
}
