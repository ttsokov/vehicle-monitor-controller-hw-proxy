package org.ttsokov.vehicle_monitor_controller.hw.proxy.enums;

public enum ParamsEnum {
	MAP("MAP"), AFR("AFR"), TEMP("TEMP");

	private final String code;

	public String getCode() {
		return code;
	}

	private ParamsEnum(String code) {
		this.code = code;
	}
}