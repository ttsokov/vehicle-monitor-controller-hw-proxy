package org.ttsokov.vehicle_monitor_controller.hw.proxy.web_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.enums.ParameterUnitsEnum;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.enums.ParamsEnum;
import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.ParamsParsingException;

public class WebClient {
	// TODO Support definition of backend URL into the config.xml file
	private String serverURL = "<add backend URL here>" + "/backend/AddMeasurement?";

	private String afrSensorId;
	private String mapSensorId;
	private String tempSensorId;
	private boolean isEcoState = false;

	public WebClient(String afrSensorId, String mapSensorId, String tempSensorId) {
		this.afrSensorId = afrSensorId;
		this.mapSensorId = mapSensorId;
		this.tempSensorId = tempSensorId;
	}

	public void sendRequest(Map<String, String> paramMap) throws Exception {

		String getRequestURL = buildRequestParamsStr(paramMap);

		// HttpClient client = new DefaultHttpClient();
		HttpClient client = HttpClientBuilder.create().build();
		// request.addHeader("User-Agent", USER_AGENT);

		System.out.println("URL= " + serverURL + getRequestURL);

		HttpGet request = new HttpGet(serverURL + getRequestURL);
		HttpResponse response = null;

		try {
			response = client.execute(request);
		} catch (Exception e) {
			throw e;
		}

		if (response == null) {
			throw new Exception();
		}

		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (IllegalStateException | IOException e) {
			throw e;
		}

		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println("Response Code : " + statusCode);

		StringBuffer result = new StringBuffer();

		try {
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			throw e;
		}

		if (statusCode != 200) {
			return;
		}

		String resultStr = result.toString();
		String[] tokens = resultStr.split("\\=");

		if (tokens.length != 2) {
			return;
		}
		if (!tokens[0].equals("isEcoState")) {
			return;
		}
		String isEcoStateStr = tokens[1];
		boolean isEcoState = Boolean.parseBoolean(isEcoStateStr);
		if (isEcoStateStr.equals("1")) {
			isEcoState = true;
		}
		this.setEcoState(isEcoState);
	}

	private String buildRequestParamsStr(Map<String, String> paramMap) throws ParamsParsingException {

		Set<String> paramTypesSet = paramMap.keySet();
		Iterator<String> paramTypesIterator = paramTypesSet.iterator();
		String paramType = null;
		int i = 0;
		while (paramTypesIterator.hasNext() && i == 0) {
			paramType = paramTypesIterator.next();
			i++;
		}

		if (paramType == null) {
			throw new ParamsParsingException();
		}

		String getRequestURL = "";
		String sensorUnit = ParameterUnitsEnum.MAP.getCode();
		String sensorIdUrlString = "sensorId=";
		String sensorValueMultiplier = "1";
		String sensorValueCalibration = "0";

		if (paramType.equals(ParamsEnum.AFR.getCode())) {
			sensorIdUrlString += afrSensorId;
			sensorUnit = ParameterUnitsEnum.AFR.getCode();
		} else if (paramType.equals(ParamsEnum.MAP.getCode())) {
			sensorIdUrlString += mapSensorId;
			sensorUnit = ParameterUnitsEnum.MAP.getCode();
		} else if (paramType.equals(ParamsEnum.TEMP.getCode())) {
			sensorIdUrlString += tempSensorId;
			sensorUnit = ParameterUnitsEnum.TEMP.getCode();
		} else {
			throw new ParamsParsingException();
		}

		getRequestURL += sensorIdUrlString;

		String sensorValue = paramMap.get(paramType);
		if ((sensorValue == null) || (sensorValue.isEmpty())) {
			sensorValue = "";
		}

		getRequestURL += "&sensorValue=" + sensorValue;
		getRequestURL += "&unit=" + sensorUnit;
		getRequestURL += "&sensorValueMultiplier=" + sensorValueMultiplier;
		getRequestURL += "&sensorValueCalibration=" + sensorValueCalibration;

		return getRequestURL;
	}

	public boolean isEcoState() {
		return isEcoState;
	}

	public void setEcoState(boolean isEcoState) {
		this.isEcoState = isEcoState;
	}
}
