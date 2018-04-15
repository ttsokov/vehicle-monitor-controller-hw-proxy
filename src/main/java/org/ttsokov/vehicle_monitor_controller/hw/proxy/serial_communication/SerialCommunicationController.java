package org.ttsokov.vehicle_monitor_controller.hw.proxy.serial_communication;

import java.util.Map;

import org.ttsokov.vehicle_monitor_controller.hw.proxy.enums.ParamsEnum;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.InvalidParamsCountException;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.ParamsParsingException;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.UnknownParameterTypeException;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.file_input.SensorIdsFileReader;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.gpio_controller.GPIOPinController;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.parameter.IParameterParser;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.parameter.ParameterParser;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.web_client.WebClient;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;

public class SerialCommunicationController implements IRunnableStoppable {

	private static final int serialBaudRate = 9600;
	private final Serial serial;
	private SerialDataListener serialDataListener;

	// private static final String serialComLogFilename =
	// "./vehicle_monitor_controller.log";
	// private File serialComLogFile = null;
	// private BufferedWriter serialComLogFileBW = null;

	private static final String skippingMsg = "Skipping received data.";
	private GPIOPinController gpioPinController;

	private static SerialCommunicationController instance;

	private static SensorIdsFileReader sensorIdsReader;
	private static Map<ParamsEnum, String> sensorIds;

	private SerialCommunicationController(GPIOPinController gpioPinController) {
		this.gpioPinController = gpioPinController;
		this.serial = SerialFactory.createInstance();
		this.serialDataListener = new SerialListener();
	}

	public static SerialCommunicationController getInstance(GPIOPinController gpioPinController) {
		if (instance == null) {
			instance = new SerialCommunicationController(gpioPinController);
		}
		sensorIdsReader = new SensorIdsFileReader();
		sensorIds = sensorIdsReader.getSensorIdsMapFromConfFile();
		return instance;
	}

	@Override
	public void run() {
		// if (serialComLogFile == null) {
		// serialComLogFile = new File(serialComLogFilename);
		// }

		// if (serialComLogFileBW == null) {
		// try {
		// serialComLogFileBW = new BufferedWriter(new
		// FileWriter(serialComLogFile));
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// }

		serial.addListener(serialDataListener);

		try {
			serial.open(Serial.DEFAULT_COM_PORT, serialBaudRate);// /dev/ttyAMA0
		} catch (SerialPortException ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Override
	public void stop() {
		serial.removeListener(serialDataListener);

		try {
			serial.close();
		} catch (IllegalStateException ex) {
			System.out.println(ex.getMessage());
		}

		// if (serialComLogFileBW == null) {
		// return;
		// }

		// try {
		// serialComLogFileBW.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	private class SerialListener implements SerialDataListener {
		@Override
		public void dataReceived(SerialDataEvent event) {

			// if (serialComLogFileBW == null) {
			// System.out.println("FileWriter is not initialized");
			// return;
			// }

			String receivedDataStr = event.getData();

			// DateFormat dateFormat = new
			// SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// Date date = new Date();
			// String logMsg = "[" + dateFormat.format(date) + "] " +
			// receivedDataStr + System.getProperty("line.separator");
			// System.out.println("logMsg=" + logMsg);

			// try {
			// serialComLogFileBW.write(logMsg);
			// } catch (IOException e) {
			// System.out.println("Error while write to log:" + e.getMessage());
			// }

			IParameterParser parser = new ParameterParser();
			Map<String, String> paramMap = null;
			try {
				paramMap = parser.parse(receivedDataStr);
			} catch (ParamsParsingException ex1) {
				System.out.println(skippingMsg);
				System.out.println(ex1.getMessage());
			} catch (UnknownParameterTypeException ex2) {
				System.out.println(skippingMsg);
				System.out.println(ex2.getMessage());
				return;
			} catch (InvalidParamsCountException ex) {
				System.out.println(skippingMsg);
				System.out.println(ex.getMessage());
				return;
			}

			if (paramMap == null) {
				return;
			}

			sendRequestToServer(paramMap);
		}

		private void sendRequestToServer(Map<String, String> paramMap) {
			WebClient webClient = new WebClient(sensorIds.get(ParamsEnum.AFR), sensorIds.get(ParamsEnum.MAP),
					sensorIds.get(ParamsEnum.TEMP));
			try {
				webClient.sendRequest(paramMap);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			Boolean isEcoState = webClient.isEcoState();
			System.out.println("WeClient.isEcoState() " + isEcoState);
			controllGpioPin(isEcoState);
		}

		private void controllGpioPin(Boolean isEcoState) {
			if (gpioPinController == null) {
				return;
			}

			if (isEcoState) {
				gpioPinController.high();
			} else {
				gpioPinController.low();
			}
		}
	}
}
