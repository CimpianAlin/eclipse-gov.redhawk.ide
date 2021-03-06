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
package gov.redhawk.datalist.ui;

import BULKIO.PrecisionUTCTime;

/**
 * @since 2.0
 */
public class Sample {
	private final PrecisionUTCTime time;
	private final int index;
	private final Object data;

	public Sample(final PrecisionUTCTime time, final int index, final Object data) {
		this.time = time;
		this.index = index;
		this.data = data;
	}

	public PrecisionUTCTime getTime() {
		return this.time;
	}

	public int getIndex() {
		return this.index;
	}

	public Object getData() {
		return this.data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Sample other = (Sample) obj;
		if (index != other.index) {
			return false;
		}
		return true;
	}
}
