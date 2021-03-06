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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;

public class MCP3008PropertiesFactory implements IExecutableExtensionFactory {

	@Override
	public Object create() throws CoreException {		
		return new MCP3008Properties() ;
	}

}
