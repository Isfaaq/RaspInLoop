/*******************************************************************************
 * Copyright 2018 RaspInLoop
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.raspinloop.agent.internal.aspect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.raspinloop.config.PinEdge;
import org.raspinloop.hwemulation.PinDigitalStateChangeEvent;
import org.raspinloop.hwemulation.PinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.PinEventType;
import com.pi4j.io.gpio.event.PinListener;
import com.pi4j.io.gpio.impl.PinImpl;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiMode;

public class Adapters {

	private static com.pi4j.io.gpio.event.PinListener listener;
	final static Logger logger = LoggerFactory.getLogger(Adapters.class);

	public static class Pi4jPin implements org.raspinloop.config.Pin {

		
		private int address;
		private String name;
		private String provider;
		EnumSet<org.raspinloop.config.PinMode> supportedPinMode = EnumSet.noneOf(org.raspinloop.config.PinMode.class);
		EnumSet<org.raspinloop.config.PinPullResistance> pullResistance = EnumSet.noneOf(org.raspinloop.config.PinPullResistance.class);


		

		@Override
		public int getAddress() {
			return address;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getProvider() {
			return provider;
		}

		@Override
		public EnumSet<org.raspinloop.config.PinMode> getSupportedPinModes() {
			return supportedPinMode ;
		}

		@Override
		public EnumSet<org.raspinloop.config.PinPullResistance> getSupportedPinPullResistance() {
			return pullResistance ;
		}

		@Override
		public void setAddress(int arg0) {
			this.address = arg0;

		}

		@Override
		public void setName(String name) {
			this.name = name;	
		}

		@Override
		public void setProvider(String provider) {
			this.provider = provider;
		}

	}

	public static class Pi4jGpioProvider implements com.pi4j.io.gpio.GpioProvider {

		private org.raspinloop.hwemulation.GpioProvider provider;

		public Pi4jGpioProvider(org.raspinloop.hwemulation.GpioProvider provider) {
			this.provider = provider;
		}

		@Override
		public void addListener(Pin pin, PinListener listener) {
			provider.addListener(forPi4j(pin), forPi4j(listener));
		}

		@Override
		public void export(Pin arg0, PinMode arg1) {
			provider.export(forPi4j(arg0), forPi4j(arg1));
		}

		@Override
		public void export(Pin arg0, PinMode arg1, PinState arg2) {
			provider.export(forPi4j(arg0), forPi4j(arg1), forPi4j(arg2));
		}

		@Override
		public PinMode getMode(Pin arg0) {
			return forPi4j(provider.getMode(forPi4j(arg0)));
		}

		@Override
		public String getName() {
			return provider.getName();
		}

		@Override
		public PinPullResistance getPullResistance(Pin arg0) {
			return forPi4j(provider.getPullResistance(forPi4j(arg0)));
		}

		@Override
		public int getPwm(Pin arg0) {
			return provider.getPwm(forPi4j(arg0));
		}

		@Override
		public PinState getState(Pin pin) {
			PinState state = forPi4j(provider.getState(forPi4j(pin)));
			logger.trace("getstate {} retuned  for {}", state, pin);
			return state;
		}

		@Override
		public double getValue(Pin arg0) {
			return provider.getValue(forPi4j(arg0));
		}

		@Override
		public boolean hasPin(Pin arg0) {
			return provider.hasPin(forPi4j(arg0));
		}

		@Override
		public boolean isExported(Pin arg0) {
			return provider.isExported(forPi4j(arg0));
		}

		@Override
		public boolean isShutdown() {
			return provider.isShutdown();
		}

		@Override
		public void removeAllListeners() {
			provider.removeAllListeners();
		}

		@Override
		public void removeListener(Pin arg0, PinListener arg1) {
			provider.removeListener(forPi4j(arg0), forPi4j(arg1));
		}

		@Override
		public void setMode(Pin arg0, PinMode arg1) {
			provider.setMode(forPi4j(arg0), forPi4j(arg1));
		}

		@Override
		public void setPullResistance(Pin arg0, PinPullResistance arg1) {
			provider.setPullResistance(forPi4j(arg0), forPi4j(arg1));
		}

		@Override
		public void setPwm(Pin arg0, int arg1) {
			provider.setPwm(forPi4j(arg0), arg1);
		}

		@Override
		public void setPwmRange(Pin arg0, int arg1) {
			provider.setPwmRange(forPi4j(arg0), arg1);
		}

		@Override
		public void setState(Pin pin, PinState state) {
			logger.trace("setstate to {} for {}", state, pin);
			provider.setState(forPi4j(pin), forPi4j(state));
		}

		@Override
		public void setValue(Pin arg0, double arg1) {
			provider.setValue(forPi4j(arg0), arg1);
		}

		@Override
		public void shutdown() {
			provider.shutdown();
		}

		@Override
		public void unexport(Pin arg0) {
			provider.unexport(forPi4j(arg0));
		}

	}

	public static class Pi4jI2CDevice implements I2CDevice {

		private org.raspinloop.hwemulation.I2CDevice device;

		public Pi4jI2CDevice(org.raspinloop.hwemulation.I2CDevice device) {
			this.device = device;
		}

		@Override
		public int getAddress() {
			return device.getAddress();
		}

		@Override
		public int read() throws IOException {
			return device.read();
		}

		@Override
		public int read(int arg0) throws IOException {
			return device.read(arg0);
		}

		@Override
		public int read(byte[] arg0, int arg1, int arg2) throws IOException {
			return device.read(arg0, arg1, arg2);
		}

		@Override
		public int read(int arg0, byte[] arg1, int arg2, int arg3) throws IOException {
			return device.read(arg0, arg1, arg2, arg3);
		}

		@Override
		public int read(byte[] arg0, int arg1, int arg2, byte[] arg3, int arg4, int arg5) throws IOException {
			return device.read(arg0, arg1, arg2, arg3, arg4, arg5);
		}

		@Override
		public void write(byte arg0) throws IOException {
			device.write(arg0);
		}

		@Override
		public void write(byte[] arg0) throws IOException {
			device.write(arg0);
		}

		@Override
		public void write(int arg0, byte arg1) throws IOException {
			device.write(arg0, arg1);
		}

		@Override
		public void write(int arg0, byte[] arg1) throws IOException {
			device.write(arg0, arg1, 0, arg1.length	);
		}

		@Override
		public void write(byte[] arg0, int arg1, int arg2) throws IOException {
			device.write(arg0, arg1, arg2);
		}

		@Override
		public void write(int arg0, byte[] arg1, int arg2, int arg3) throws IOException {
			device.write(arg0, arg1, arg2, arg3);
		}

	}

	public static class Pi4jI2CBus implements I2CBus {

		private org.raspinloop.hwemulation.I2CBus instance;

		public Pi4jI2CBus(org.raspinloop.hwemulation.I2CBus instance) {
			this.instance = instance;
		}

		@Override
		public void close() throws IOException {
			instance.close();
		}

		@Override
		public int getBusNumber() {
			return instance.getBusNumber();
		}

		@Override
		public I2CDevice getDevice(int arg0) throws IOException {
			return Adapters.forPi4j(instance.getDevice(arg0));
		}

	}

	public static class Pi4jSpiDevice implements SpiDevice {

		private org.raspinloop.hwemulation.SpiDevice instance;
		private Pi4jSpiDevice(org.raspinloop.hwemulation.SpiDevice instance) {
			this.instance = instance;
		}

		@Override
		public ByteBuffer write(ByteBuffer arg0) throws IOException {
			return instance.write(arg0);
		}

		@Override
		public byte[] write(InputStream arg0) throws IOException {
			return instance.write(arg0);
		}

		@Override
		public byte[] write(byte... arg0) throws IOException {
			return instance.write(arg0);
		}

		@Override
		public short[] write(short... arg0) throws IOException {
			return instance.write(arg0);
		}

		@Override
		public String write(String arg0, Charset arg1) throws IOException {
			return instance.write(arg0, arg1);
		}

		@Override
		public String write(String arg0, String arg1) throws IOException {
			return instance.write(arg0, arg1);
		}

		@Override
		public int write(InputStream arg0, OutputStream arg1) throws IOException {
			return instance.write(arg0, arg1);
		}

		@Override
		public byte[] write(byte[] arg0, int arg1, int arg2) throws IOException {
			return instance.write(arg0);
		}

		@Override
		public short[] write(short[] arg0, int arg1, int arg2) throws IOException {
			return instance.write(arg0);
		}
	}

	public static SpiDevice forPi4j(org.raspinloop.hwemulation.SpiDevice instance) {
		return new Pi4jSpiDevice(instance);
	}




	public static PinState forPi4j(org.raspinloop.config.PinState state) {
		if (state == null)
			return null;
		switch(state){
		case HIGH:
			return PinState.HIGH;
		case LOW:
			return PinState.LOW;
		default:
			return null;
		}
	}




	public static org.raspinloop.config.PinState forPi4j(PinState state) {
		if (state == null)
			return null;
		switch(state){
		case HIGH:
			return org.raspinloop.config.PinState.HIGH;
		case LOW:
			return org.raspinloop.config.PinState.LOW;
		default:
			return null;
		}
	}




	public static org.raspinloop.hwemulation.PinListener forPi4j(PinListener arg1) {		
		Adapters.listener = arg1;
		return new org.raspinloop.hwemulation.PinListener() {			
			@Override
			public void handlePinEvent(org.raspinloop.hwemulation.PinEvent event) {
				listener.handlePinEvent(forPi4j(event));
			}
		};
	}


	protected static com.pi4j.io.gpio.event.PinEvent forPi4j(PinEvent event) {
		if (event instanceof PinDigitalStateChangeEvent)
			return new com.pi4j.io.gpio.event.PinDigitalStateChangeEvent(new Object(), forPi4j(event.getPin()), forPi4j(((PinDigitalStateChangeEvent) event).getState()));
		else
			return null; //TODO: throw error ? log ? report?
	}

	private static PinEventType forPi4j(org.raspinloop.hwemulation.PinEventType eventType) {
		if (eventType == null)
			return null;
		switch (eventType) {
		case ANALOG_VALUE_CHANGE:
			return PinEventType.ANALOG_VALUE_CHANGE;
		case DIGITAL_STATE_CHANGE:
			return PinEventType.ANALOG_VALUE_CHANGE;
		default:
			return null;
		}		
	}

	private static Map<Pin, org.raspinloop.config.Pin> pins = new HashMap<>();
	synchronized public static org.raspinloop.config.Pin forPi4j(Pin pin) {
		if (! pins.containsKey(pin)) {
			pins.put(pin, new  org.raspinloop.config.PinImpl(pin.getProvider(), 
															 pin.getAddress(),
															 pin.getName(), 
															 enumSetPinModeForPi4j(pin.getSupportedPinModes()),
															 enumSetPinPullupForPi4j(pin.getSupportedPinPullResistance()),
															 enumSetPinEdgeForPi4j(pin.getSupportedPinEdges())
															 
					));
		}
		return pins.get(pin);
	}

	private static EnumSet<org.raspinloop.config.PinEdge> enumSetPinEdgeForPi4j(EnumSet<com.pi4j.io.gpio.PinEdge> supportedPinEdges) {
		EnumSet<org.raspinloop.config.PinEdge> result = EnumSet.noneOf(org.raspinloop.config.PinEdge.class);
		for (com.pi4j.io.gpio.PinEdge pinEdge : supportedPinEdges) {
			result.add(forPi4j(pinEdge));
		}		
		return result;
	}

	private static PinEdge forPi4j(com.pi4j.io.gpio.PinEdge pinEdge) {
		if (pinEdge == null)
			return null;
		switch(pinEdge){
		case BOTH:
			return PinEdge.BOTH;
		case FALLING:
			return PinEdge.FALLING;
		case NONE:
			return PinEdge.NONE;
		case RISING:
			return PinEdge.RISING;
		default:
			return null;		
		}
	}




	private static EnumSet<org.raspinloop.config.PinPullResistance> enumSetPinPullupForPi4j(EnumSet<PinPullResistance> supportedPinPullResistance) {
		EnumSet<org.raspinloop.config.PinPullResistance> result = EnumSet.noneOf(org.raspinloop.config.PinPullResistance.class);
		for (PinPullResistance pinMode : supportedPinPullResistance) {
			result.add(forPi4j(pinMode));
		}		
		return result;
	}




	private static EnumSet<org.raspinloop.config.PinMode> enumSetPinModeForPi4j(EnumSet<PinMode> supportedPinModes) {
		EnumSet<org.raspinloop.config.PinMode> result = EnumSet.noneOf(org.raspinloop.config.PinMode.class);
		for (PinMode pinMode : supportedPinModes) {
			result.add(forPi4j(pinMode));
		}		
		return result;
	}
	
//	
//	private static EnumSet<org.raspinloop.config.PinPullResistance> forPi4j(EnumSet<PinPullResistance> supportedPinPullResistance) {
//		EnumSet<org.raspinloop.config.PinPullResistance> result = EnumSet.noneOf(org.raspinloop.config.PinPullResistance.class);
//		for (PinPullResistance pinPullResistance : supportedPinPullResistance) {
//			result.add(forPi4j(pinPullResistance));
//		}		
//		return result;
//	}


	
	

	private static Map<org.raspinloop.config.Pin, Pin> raspinloopPins = new HashMap<>();
	synchronized public static Pin forPi4j(org.raspinloop.config.Pin pin) {
		if (! raspinloopPins.containsKey(pin)) {
			raspinloopPins.put(pin, new PinImpl( pin.getProvider(), 
												 pin.getAddress(), 
												 pin.getName(), 
												 forPi4jPinModeSet(pin.getSupportedPinModes()), 
												 forPi4j(pin.getSupportedPinPullResistance())));
		}
		return raspinloopPins.get(pin);
	}
	
	private static EnumSet<PinPullResistance> forPi4j(EnumSet<org.raspinloop.config.PinPullResistance> supportedPinPullResistance) {
		EnumSet<PinPullResistance> result = EnumSet.noneOf(PinPullResistance.class);
		for (org.raspinloop.config.PinPullResistance pinPullResistance : supportedPinPullResistance) {
			result.add(forPi4j(pinPullResistance));
		}		
		return result;
	}

	private static PinPullResistance forPi4j(org.raspinloop.config.PinPullResistance pinPullResistance) {
		if (pinPullResistance == null)
			return null;
		switch (pinPullResistance) {
		case OFF:
			return PinPullResistance.OFF;
		case PULL_DOWN:
			return PinPullResistance.PULL_DOWN;
		case PULL_UP:
			return PinPullResistance.PULL_UP;
		default:
			return null;
		}		
	}




	public static EnumSet<PinMode> forPi4jPinModeSet(EnumSet<org.raspinloop.config.PinMode> supportedPinModes) {
		EnumSet<PinMode> result = EnumSet.noneOf(PinMode.class);
		for (org.raspinloop.config.PinMode pinmode : supportedPinModes) {
			result.add(forPi4j(pinmode));
		}		
		return result;
	}


	private static PinMode forPi4j(org.raspinloop.config.PinMode pinmode) {
		if (pinmode == null)
			return null;
		switch(pinmode){
		case DIGITAL_INPUT:
			return PinMode.DIGITAL_INPUT;
		case DIGITAL_OUTPUT:
			return PinMode.DIGITAL_OUTPUT;
		default:
			return null;		
		}
	}

	private static Map<org.raspinloop.hwemulation.I2CDevice, I2CDevice> i2CDevices = new HashMap<>();
	synchronized public static I2CDevice forPi4j(org.raspinloop.hwemulation.I2CDevice device) {
		if (! i2CDevices.containsKey(device)) {
			i2CDevices.put(device, new Pi4jI2CDevice(device));
		}
		return i2CDevices.get(device);
	}
	
	private static Map<org.raspinloop.hwemulation.I2CBus, I2CBus> i2CBuses = new HashMap<>();
	synchronized public static I2CBus forPi4j(org.raspinloop.hwemulation.I2CBus instance) {
		
		if (! i2CBuses.containsKey(instance)) {
			i2CBuses.put(instance, new Pi4jI2CBus(instance));
		}
		return i2CBuses.get(instance);
	}

	public static GpioProvider forPi4j(org.raspinloop.hwemulation.GpioProvider provider) {
		return new Pi4jGpioProvider(provider);
	}


	public static org.raspinloop.hwemulation.SpiChannel forPi4j(SpiChannel spiChannel) {
		if (spiChannel == null)
			return null;
		switch(spiChannel){
		case CS0:
			return org.raspinloop.hwemulation.SpiChannel.CS0;
		case CS1:
			return org.raspinloop.hwemulation.SpiChannel.CS1;
		default:
			return null;
		}
	}

	public static org.raspinloop.hwemulation.SpiMode forPi4j(SpiMode spiMode) {
		if (spiMode == null)
			return null;
		switch(spiMode){
		case MODE_0:
			return org.raspinloop.hwemulation.SpiMode.MODE_0;
		case MODE_1:
			return org.raspinloop.hwemulation.SpiMode.MODE_1;
		case MODE_2:
			return org.raspinloop.hwemulation.SpiMode.MODE_2;
		case MODE_3:
			return org.raspinloop.hwemulation.SpiMode.MODE_3;
		default:
			return null;
		}
	}
	

	public static org.raspinloop.config.PinPullResistance forPi4j(PinPullResistance enum1) {
		if (enum1 == null)
			return null;
		switch(enum1){
		case OFF:
			return org.raspinloop.config.PinPullResistance.OFF;
		case PULL_DOWN:
			return org.raspinloop.config.PinPullResistance.PULL_DOWN;
		case PULL_UP:
			return org.raspinloop.config.PinPullResistance.PULL_UP;
		default:
			return null;
		
		}
	}

	public static org.raspinloop.config.PinMode forPi4j(PinMode enum1) {
		if (enum1 == null)
			return null;
		switch(enum1){
		case ANALOG_INPUT:
		case DIGITAL_INPUT:
			return (org.raspinloop.config.PinMode.DIGITAL_INPUT);
		case ANALOG_OUTPUT:
		case DIGITAL_OUTPUT:
		case GPIO_CLOCK:
		case PWM_OUTPUT:
		case PWM_TONE_OUTPUT:
		case SOFT_PWM_OUTPUT:
		case SOFT_TONE_OUTPUT:
			return (org.raspinloop.config.PinMode.DIGITAL_OUTPUT);
		default:
			return null;
		}
	}
}
