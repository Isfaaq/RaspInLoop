/*******************************************************************************
 * Copyright (C) 2018 RaspInLoop
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.raspinloop.pi4j.io.spi;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.raspinloop.config.HardwareProperties;
import org.raspinloop.fmi.plugin.Images;
import org.raspinloop.fmi.plugin.preferences.extension.AbstractHWConfigPage;
import org.raspinloop.hwemulation.SpiChannel;



public class MCP3008PropertiesPage extends AbstractHWConfigPage {

	

	private IStatus[] fFieldStatus = new IStatus[1];
	private Text fHWName;
	private MCP3008Properties fHW;
	private Combo fHWChannel;
	private Spinner fHWVref;


	public MCP3008PropertiesPage() {
		super("MCP3008 configuration");
		for (int i = 0; i < fFieldStatus.length; i++) {
			fFieldStatus[i] = Status.OK_STATUS;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite p) {
		// create a composite with standard margins and spacing
		Composite composite = new Composite(p, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		addLabel(composite, "MCP3008 Name");
		fHWName = addText(composite);
					
		addLabel(composite, "Channel");
		fHWChannel = new Combo(composite, SWT.READ_ONLY);
		fHWChannel.setItems(new String[] { SpiChannel.CS0.toString() , SpiChannel.CS1.toString() });
		
		addLabel(composite, "Use fixed Vref");
		
		
		fHWVref = new Spinner(composite, SWT.NONE);
		fHWVref.setDigits(2);
		fHWVref.setMinimum(0);
		fHWVref.setMaximum(330);
		fHWVref.setIncrement(10);
		fHWVref.setSelection(330);
		
		Composite pinBlockComposite = new Composite(composite, SWT.NONE);
		GridLayout pinBlockLayout = new GridLayout();
		pinBlockLayout.numColumns = 4;
		pinBlockLayout.marginLeft=0;
		pinBlockComposite.setLayout(pinBlockLayout);
		pinBlockComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Dialog.applyDialogFont(composite);
		setControl(composite);
		initializeFields();
	}

	/**
	 * Initialize the dialogs fields
	 */
	private void initializeFields() {
		fHWName.setText(fHW.getComponentName());	
		fHWChannel.setText((fHW.getChannel() == null)?SpiChannel.CS0.toString():fHW.getChannel().toString());		
		nameChanged(fHWName.getText());
	}

	private void setFieldValuesToHW() {
		fHW.setComponentName(fHWName.getText());
		
		fHW.setChannel(SpiChannel.valueOf(fHWChannel.getText()));
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#getImage()
	 */
	@Override
	public Image getImage() {
		return Images.get(Images.IMG_HARDWARE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.debug.ui.launchConfigurations.AbstractVMInstallPage#
	 * getVMStatus()
	 */
	@Override
	protected IStatus[] getHWStatus() {
		return fFieldStatus;
	}

	@Override
	public boolean finish() {
		setFieldValuesToHW();
		return true;
	}
	
	@Override
	public void setSelection(HardwareProperties hw) {
		super.setSelection(hw);
		if (hw instanceof MCP3008Properties)
			fHW = (MCP3008Properties)hw;		
		setTitle("Configure MCP3008");
		setDescription("Use this page to configure your MCP3008.");
	}

	@Override
	public HardwareProperties getSelection() {
		return fHW;
	}
}
