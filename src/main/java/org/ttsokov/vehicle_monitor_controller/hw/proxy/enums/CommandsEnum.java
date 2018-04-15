package org.ttsokov.vehicle_monitor_controller.hw.proxy.enums;

public enum CommandsEnum {
	MODE("mode"), NORMAL_MODE("normal"), SIMULATION_MODE("simulation"), HELP("help");

	private final String code;

	public String getCode() {
		return code;
	}

	private CommandsEnum(String code) {
		this.code = code;
	}
}
