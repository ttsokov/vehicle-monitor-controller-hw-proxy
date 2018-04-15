package org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions;

public class ParamsParsingException extends Exception {
	@Override
	public String getMessage() {
		return "Error in parsing of parameters.";
	}
}
