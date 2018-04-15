package org.ttsokov.vehicle_monitor_controller.hw.proxy.validators;

import org.ttsokov.vehicle_monitor_controller.hw.proxy.exceptions.InvalidMenuEntryException;

public class MenuEntryValidator implements IValidator {

	@Override
	public void validate(String str) throws Exception {
		if (!((str.length() == 1) && (str.equalsIgnoreCase("1") || str.equalsIgnoreCase("2") || str.equalsIgnoreCase("3")))) {
			throw new InvalidMenuEntryException();
		}

	}
}
