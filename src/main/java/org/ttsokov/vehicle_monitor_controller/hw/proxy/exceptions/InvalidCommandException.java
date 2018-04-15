package org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions;

public class InvalidCommandException extends Exception {
	@Override
	public String getMessage() {
		return "Invalid command.";
	}
}
