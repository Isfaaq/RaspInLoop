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
package org.raspinloop.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;


public  class  BoardHardwareDelegate implements BoardHardware {	
	
	private String guid;

	public BoardHardwareDelegate(String guid) {

		this.setGuid(guid);
	}
	
	public BoardHardwareDelegate() {
	}

	@Override
	public String getType() {		
		throw new RuntimeException("getType not implemented by BoardHardwareDelegate");
	}

	@Override
	public String getSimulatedProviderName() {		
		throw new RuntimeException("getSimulatedProviderName not implemented by BoardHardwareDelegate");
	}

	@Override
	public String getComponentName() {		
		throw new RuntimeException("getName not implemented by BoardHardwareDelegate");
	}

	@Override
	public String getImplementationClassName() {
		throw new RuntimeException("getImplementationClassName not implemented by BoardHardwareDelegate");
	}	
	
	@Override
	public BoardHardwareDelegate setComponentName(String string) {
		throw new RuntimeException("getImplementationClassName not implemented by BoardHardwareDelegate");
	}
	
	protected ArrayList<PinImpl> outputPins = new ArrayList<PinImpl>();

	protected ArrayList<PinImpl> inputPins = new ArrayList<PinImpl>();

	protected ArrayList<BoardExtentionHardware> extentionComponents = new ArrayList<BoardExtentionHardware>();
	protected ArrayList<UARTComponent> uartComponents = new ArrayList<UARTComponent>();
	protected ArrayList<SPIComponent> spiComponents = new ArrayList<SPIComponent>();
	protected ArrayList<I2CComponent> i2cComponents = new ArrayList<I2CComponent>();

	boolean usedByComp(Pin pin) {
		if (pin == null)
			return false;
		
		for (HardwareProperties simulatedComponent : getAllComponents()) {
			for (Pin usedpin : simulatedComponent.getUsedPins()) {
				if (usedpin != null && usedpin.equals(pin))
					return true;
			}			
		}
	
		return false;
	}
	
	@Override
	public void useInputPin(Pin pin) throws AlreadyUsedPin {
		if (usedByComp(pin))
			throw new AlreadyUsedPin(pin);

		inputPins.add(new PinImpl(pin));
	}

	@Override
	public void useOutputPin(Pin pin) throws AlreadyUsedPin {
		if (usedByComp(pin))
			throw new AlreadyUsedPin(pin);
		outputPins.add(new PinImpl(pin));
	}

	@Override
	public void unUsePin(Pin pin) {
		if (pin == null)
			return;
		outputPins.remove(pin);
		inputPins.remove(pin);
	}

	@Override
	public void addComponent(BoardExtentionHardware sd ) {
		extentionComponents.add(sd);
	}

	@Override
	public void removeComponent(BoardExtentionHardware sd) {
		extentionComponents.remove(sd);
	}

	@Override
	public Collection<Pin> getInputPins() {
		return Collections.<Pin>unmodifiableCollection(inputPins);
	}

	@Override
	public Collection<Pin> getOutputPins() {
		return Collections.<Pin>unmodifiableCollection(outputPins);
	}

	@Override
	public Collection<Pin> getUsedByCompPins() {
		LinkedList<Pin> usedPins = new LinkedList<Pin>();
		
		for (HardwareProperties simulatedComponent : getAllComponents()) {
			usedPins.addAll(simulatedComponent.getUsedPins());		
		}		
		
		return Collections.unmodifiableCollection(usedPins);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Pin> getUnUsedPins() {
		return Collections.EMPTY_LIST;
	}
	
	
	@Override
	public Collection<BoardExtentionHardware> getGPIOComponents() {
		return Collections.unmodifiableCollection(extentionComponents);
	}

	@Override
	public Collection<I2CComponent> getI2CComponent() {
		return Collections.unmodifiableCollection(i2cComponents);		
	}

	@Override
	public void addComponent(I2CComponent comp) {
		i2cComponents.add(comp);
	}

	@Override
	public void removeComponent(I2CComponent comp) {
		i2cComponents.remove(comp);
	}

	@Override
	public Collection<UARTComponent> getUARTComponent() {
		return Collections.unmodifiableCollection(uartComponents);
	}

	@Override
	public void addComponent(UARTComponent comp) {
		uartComponents.add(comp);
	}

	@Override
	public void removeComponent(UARTComponent comp) {
		uartComponents.remove(comp);
	}

	@Override
	public Collection<SPIComponent> getSPIComponent() {
		return Collections.unmodifiableCollection(spiComponents);
	}

	@Override
	public void addComponent(SPIComponent comp) {
		spiComponents.add(comp);
	}

	@Override
	public void removeComponent(SPIComponent comp) {
		spiComponents.remove(comp);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Pin> getSupportedPin() {
		return Collections.EMPTY_LIST;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	@Override
	public Collection<HardwareProperties> getAllComponents() {
		HashSet<HardwareProperties> col = new HashSet<HardwareProperties>(getGPIOComponents());
		col.addAll(getSPIComponent());
		col.addAll(getI2CComponent());
		col.addAll(getUARTComponent());
		return col;
	}

	@Override
	public Collection<Pin> getUsedPins() {
		return getUsedByCompPins();
	}

	@Override
	public Collection<Pin> getSpiPins() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Pin> getI2CPins() {
		return Collections.emptyList();
	}
}
