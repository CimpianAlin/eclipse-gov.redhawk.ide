/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.ide.debug.linux;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.RuntimeProcess;

/**
 * 
 */
public class PosixRuntimeProcess extends RuntimeProcess {

	/**
	 * @param launch
	 * @param process
	 * @param name
	 * @param attributes
	 */
	public PosixRuntimeProcess(ILaunch launch, Process process, String name, @SuppressWarnings("rawtypes") Map attributes) {
		super(launch, process, name, attributes);
	}
	
	@Override
	public synchronized void terminate() throws DebugException {
		if (isTerminated()) {
			return;
		}
		try {
			ProcessTree tree = new ProcessTree();
			Process p = getSystemProcess();
			if (p == null) {
				return;
			}
			Field pidField = p.getClass().getDeclaredField("pid");
			pidField.setAccessible(true);
			int pid = pidField.getInt(p);
			tree.killAll(String.valueOf(pid));
			super.terminate();
		} catch (SecurityException e) {
			super.terminate();
			throw new DebugException(new Status(Status.ERROR, "gov.redhawk.ide.debug.linux", "gov.redhawk.ide.debug.linux", e));
		} catch (NoSuchFieldException e) {
			super.terminate();
			throw new DebugException(new Status(Status.ERROR, "gov.redhawk.ide.debug.linux", "Failed to clean up process ", e));
		} catch (IllegalArgumentException e) {
			super.terminate();
			throw new DebugException(new Status(Status.ERROR, "gov.redhawk.ide.debug.linux", "Failed to clean up process ", e));
		} catch (IllegalAccessException e) {
			super.terminate();
			throw new DebugException(new Status(Status.ERROR, "gov.redhawk.ide.debug.linux", "Failed to clean up process ", e));
		} catch (IOException e) {
			super.terminate();
			throw new DebugException(new Status(Status.ERROR, "gov.redhawk.ide.debug.linux", "Failed to clean up process ", e));
		}
		
	}

}
