package org.ttsokov.vehicle_monitor_controller.hw.proxy.parameter;

import java.util.Map;

import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.InvalidParamsCountException;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.ParamsParsingException;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.UnknownParameterTypeException;

public interface IParameterParser {
	public Map<String, String> parse(String receivedString)
			throws UnknownParameterTypeException, ParamsParsingException,
			InvalidParamsCountException;
}
