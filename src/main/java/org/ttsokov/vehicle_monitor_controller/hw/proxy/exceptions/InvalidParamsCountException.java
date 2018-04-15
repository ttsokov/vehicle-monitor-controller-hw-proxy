package org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions;

public class InvalidParamsCountException extends Exception {

	@Override
	public String getMessage() {
		return "Number of parameters is incorrect.";
	}
}
