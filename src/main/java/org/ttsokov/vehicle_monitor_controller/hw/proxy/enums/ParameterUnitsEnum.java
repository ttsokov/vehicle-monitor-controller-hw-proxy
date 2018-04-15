package org.ttsokov.vehicle_monitor_controller.hw.proxy.enums;

public enum ParameterUnitsEnum {
	MAP("Bar"), AFR("Pts"), TEMP("Celsius");

	private final String code;

	public String getCode() {
		return code;
	}

	private ParameterUnitsEnum(String code) {
		this.code = code;
	}
}
