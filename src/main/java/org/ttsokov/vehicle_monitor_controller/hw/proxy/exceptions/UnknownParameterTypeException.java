package org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions;

public class UnknownParameterTypeException extends Exception {
	@Override
	public String getMessage() {
		return "Received parameter is unknown.";
	}
}
