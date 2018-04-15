package org.ttsokov.vehicle_monitor_controller.hw.proxy.main;

import java.util.HashMap;
import java.util.Map;

import org.ttsokov.vehicle_monitor_controller.hw.proxy.enums.CommandsEnum;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.enums.ParamsEnum;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.InvalidCommandException;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.file_input.SensorIdsFileReader;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.gpio_controller.GPIOPinController;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.input.InputCommandParser;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.input.InputReader;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.serial_communication.IRunnableStoppable;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.serial_communication.SerialCommunicationController;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.validators.MenuEntryValidator;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.web_client.WebClient;

public class Main {

	private static final String label = "Engine Params Logger\n";
	private static final String enterChoiceMsg = "Enter choice:\n" + "[1] Receive/transmit (Normal Mode)\n"
			+ "[2] Send simulated data to server (Simulation Mode)\n" + "[3] Exit\n";

	private static final int engineParamsCount = 3;

	private static InputReader inputReader = null;
	private static IRunnableStoppable serialComControllerRunnable = null;
	private static GPIOPinController gpioPinController;

	private volatile static boolean isSerialControllerThreadStarted = false;
	private static SensorIdsFileReader sensorIdsReader;
	static Map<ParamsEnum, String> sensorIds;

	public static void main(String[] args) throws InterruptedException {
		attachShutdownHook();

		if (args.length == 0) {
			interactiveMode();
			System.exit(0);
		}
		CommandsEnum command = null;

		try {
			command = InputCommandParser.parseCMDInput(args);
		} catch (InvalidCommandException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}

		System.out.println("Batch Mode");

		switch (command) {
		case NORMAL_MODE:
			if (!isSerialControllerThreadStarted) {
				startSerialComController();
				while (true) {
					;
				}
			} else {
				while (true) {
					;
				}
			}
		case SIMULATION_MODE:
			if (!isSerialControllerThreadStarted) {
				sendSimulatedDataToServerWithoutArduino();
				while (true) {
					;
				}
			} else {
				while (true) {
					;
				}
			}
		default:
			break;
		}

		System.exit(0);
	}

	private static void attachShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {

				if (gpioPinController != null) {
					gpioPinController.shutdown();
				}

				if (serialComControllerRunnable != null) {
					serialComControllerRunnable.stop();
				}

				if (inputReader != null) {
					inputReader.closeReader();
				}
			}
		});
	}

	private static void interactiveMode() {
		inputReader = new InputReader();
		System.out.println(label);
		do {
			String menuEntryStr = inputReader.collectInput(new MenuEntryValidator(), enterChoiceMsg);

			switch (menuEntryStr) {
			case "1":
				if (!isSerialControllerThreadStarted) {
					startSerialComController();
				}
				break;
			case "2":
				sendSimulatedDataToServerWithoutArduino();
				break;
			default:
				break;
			}

			if (menuEntryStr.equalsIgnoreCase("3")) {
				break;
			}

		} while (true);
	}

	private static void sendSimulatedDataToServerWithoutArduino() {
		sensorIdsReader = new SensorIdsFileReader();
		sensorIds = sensorIdsReader.getSensorIdsMapFromConfFile();
		(new Thread(new SimulationRunnable())).start();
	}

	private static class SimulationRunnable implements Runnable {
		@Override
		public void run() {
			do {
				WebClient webClient = new WebClient(sensorIds.get(ParamsEnum.AFR), sensorIds.get(ParamsEnum.MAP),
						sensorIds.get(ParamsEnum.TEMP));
				Map<String, String> paramMap = new HashMap<>();
				paramMap.put("AFR", "11.8");

				try {
					webClient.sendRequest(paramMap);

				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				Boolean isEcoState = webClient.isEcoState();
				// System.out.println("SimulationRunnable WeClient.isEcoState() "
				// + isEcoState);

				gpioPinController = GPIOPinController.getInstance();
				if (gpioPinController == null) {
					return;
				}

				if (isEcoState) {
					gpioPinController.high();
				} else {
					gpioPinController.low();
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (true);

		}
	}

	private static void startSerialComController() {
		System.out.println("startSerialComController()");
		isSerialControllerThreadStarted = true;
		gpioPinController = GPIOPinController.getInstance();
		serialComControllerRunnable = SerialCommunicationController.getInstance(gpioPinController);
		(new Thread(serialComControllerRunnable)).start();
	}
}
