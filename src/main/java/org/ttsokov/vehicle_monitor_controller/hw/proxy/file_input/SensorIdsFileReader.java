package org.ttsokov.vehicle_monitor_controller.hw.proxy.file_input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ttsokov.vehicle_monitor_controller.hw.proxy.enums.ParamsEnum;

public class SensorIdsFileReader {
	private static final String VEHICLE_ID = "100";
	private static final String AFR_SENSOR_ID = "200";
	private static final String MAP_SENSOR_ID = "201";
	private static final String TEMP_SENSOR_ID = "202";
	private static final String sensorIdsFilename = "./vehicle_monitor_controller.conf";
	private Map<ParamsEnum, String> sensorIds;

	public SensorIdsFileReader() {
		sensorIds = new HashMap<ParamsEnum, String>();
		getSensorIds().put(ParamsEnum.AFR, AFR_SENSOR_ID);
		getSensorIds().put(ParamsEnum.MAP, MAP_SENSOR_ID);
		getSensorIds().put(ParamsEnum.TEMP, TEMP_SENSOR_ID);
	}

	// AFR=<Id>
	// MAP=<Id>
	// TEMP=<Id>
	public Map<ParamsEnum, String> getSensorIdsMapFromConfFile() {
		String afrSensorId, mapSensorId;
		FileReader sensorIdsFileReader = null;
		try {
			sensorIdsFileReader = new FileReader(sensorIdsFilename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return sensorIds;
		}

		BufferedReader sensorIdsFileBR = new BufferedReader(sensorIdsFileReader);
		try {
			StringBuilder sb = new StringBuilder();
			String line;

			do {
				line = sensorIdsFileBR.readLine();
				if (line == null) {
					continue;
				}

				String[] tokens = line.split("\\=");

				if (tokens == null) {
					System.out.println("Parse error while reading configuration file: " + sensorIdsFilename);
					continue;
				}

				if (tokens.length == 0) {
					System.out.println("Parse error while reading configuration file: " + sensorIdsFilename);
					continue;
				}

				if (tokens.length != 2) {
					System.out.println("Parse error while reading configuration file: " + sensorIdsFilename);
					continue;
				}

				if (tokens[0].equals(ParamsEnum.AFR.getCode())) {
					sensorIds.put(ParamsEnum.AFR, tokens[1]);
				}

				if (tokens[0].equals(ParamsEnum.MAP.getCode())) {
					sensorIds.put(ParamsEnum.MAP, tokens[1]);
				}

				if (tokens[0].equals(ParamsEnum.TEMP.getCode())) {
					sensorIds.put(ParamsEnum.TEMP, tokens[1]);
				}
			} while (line != null);

		} catch (IOException e) {
			e.printStackTrace();
			return sensorIds;
		} finally {
			try {
				sensorIdsFileBR.close();
			} catch (IOException e) {
				e.printStackTrace();
				return sensorIds;
			}
		}

		return sensorIds;
	}

	public Map<ParamsEnum, String> getSensorIds() {
		return sensorIds;
	}
}
