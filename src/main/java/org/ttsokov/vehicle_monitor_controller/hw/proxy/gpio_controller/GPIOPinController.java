package org.ttsokov.vehicle_monitor_controller.hw.proxy.gpio_controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GPIOPinController {

	private static GPIOPinController instance;
	private final GpioController gpio;
	private final GpioPinDigitalOutput pin;

	private GPIOPinController() {
		this.gpio = GpioFactory.getInstance();
		this.pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
		this.pin.setShutdownOptions(true, PinState.LOW);// PinPullResistance.OFF

		// setShutdownOptions(Boolean unexport, PinState state,
		// PinPullResistance resistance, GpioPin... pin);
		// setShutdownOptions(Boolean unexport, PinState state,
		// PinPullResistance resistance, PinMode mode, GpioPin... pin);

		// pin.toggle();
		// pin.toggle();
		// pin.pulse(1000, true); // set second argument to 'true' use a
		// blocking
		// call

		// stop all GPIO activity/threads by shutting down the GPIO controller
		// (this method will forcefully shutdown all GPIO monitoring threads and
		// scheduled tasks)
		// gpio.shutdown();
	}

	public static GPIOPinController getInstance() {
		if (instance == null) {
			instance = new GPIOPinController();
		}
		return instance;
	}

	public void high() {
		pin.high();
	}

	public void low() {
		pin.low();
	}

	// Must be called when the app is terminated
	public void shutdown() {
		gpio.shutdown();
	}
}
