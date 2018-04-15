package org.ttsokov.vehicle_monitor_controller.hw.proxy.input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ttsokov.vehicle_monitor_controller.hw.proxy.enums.CommandsEnum;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.InvalidCommandException;

public class InputCommandParser {

	public static CommandsEnum parseCMDInput(String[] args) throws InvalidCommandException {

		if (args.length != 1) {
			throw new InvalidCommandException();
		}

		for (String arg : args) {

			if (arg.equals("--" + CommandsEnum.HELP.getCode())) {
				return CommandsEnum.HELP;
			}

			Pattern regex = Pattern.compile("^--\\w+=\\w+$");
			Matcher m = regex.matcher(arg);
			String[] command;

			if (!m.find()) {
				throw new InvalidCommandException();
			}

			command = arg.split("\\=");

			if (command == null) {
				throw new InvalidCommandException();
			}

			if (command.length == 0 || command.length > 2) {
				throw new InvalidCommandException();
			}

			command[0] = command[0].replace("--", "");
			System.out.println("command0=" + command[0] + " command1= " + command[1]);

			if (!command[0].equals(CommandsEnum.MODE.getCode())) {
				throw new InvalidCommandException();
			}

			if (command[1].equals(CommandsEnum.NORMAL_MODE.getCode())) {
				return CommandsEnum.NORMAL_MODE;
			}

			if (command[1].equals(CommandsEnum.SIMULATION_MODE.getCode())) {
				return CommandsEnum.SIMULATION_MODE;
			}

			throw new InvalidCommandException();
		}
		return null;
	}

	private static void showHelp() {
		// TODO showHelp
	}
}
