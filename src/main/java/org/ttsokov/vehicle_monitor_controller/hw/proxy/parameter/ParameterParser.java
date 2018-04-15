package org.ttsokov.vehicle_monitor_controller.hw.proxy.parameter;

import java.util.HashMap;
import java.util.Map;

import org.ttsokov.vehicle_monitor_controller.hw.proxy.enums.ParamsEnum;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.InvalidParamsCountException;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.ParamsParsingException;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.UnknownParameterTypeException;

public class ParameterParser implements IParameterParser {

	final String[] paramsKeys = { ParamsEnum.MAP.getCode(), ParamsEnum.AFR.getCode() };

	// MAP,1.5,
	// AFR,20.05,
	// TEMP,32,
	// Map <"MAP", "1.5">
	public Map<String, String> parse(String receivedString) throws UnknownParameterTypeException, ParamsParsingException,
			InvalidParamsCountException {

		if (receivedString == null) {
			throw new ParamsParsingException();
		}

		if (receivedString.isEmpty()) {
			throw new ParamsParsingException();
		}

		String[] tokens = receivedString.split("\\,");

		if (tokens == null) {
			throw new ParamsParsingException();
		}

		if (tokens.length == 0) {
			throw new ParamsParsingException();
		}

		if (tokens.length != 2) {
			throw new InvalidParamsCountException();
		}

		if (!tokens[0].equals(ParamsEnum.MAP.getCode()) && !tokens[0].equals(ParamsEnum.AFR.getCode())
				&& !tokens[0].equals(ParamsEnum.TEMP.getCode())) {
			throw new UnknownParameterTypeException();
		}

		return buildMap(tokens[0], tokens[1]);
	}

	public Map<String, String> buildMap(String key, String value) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put(key, value);
		return paramMap;
	}

}
