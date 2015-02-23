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
package gov.redhawk.ide.graphiti.sad.ui.diagram.features.custom;

import gov.redhawk.ide.graphiti.sad.ext.ComponentShape;
import gov.redhawk.ide.graphiti.sad.ui.diagram.patterns.ComponentPattern;
import gov.redhawk.ide.graphiti.ui.diagram.util.DUtil;

import java.math.BigInteger;

import mil.jpeojtrs.sca.sad.SadComponentInstantiation;
import mil.jpeojtrs.sca.sad.SoftwareAssembly;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;

public class DecrementStartOrderFeature extends AbstractCustomFeature {

	public DecrementStartOrderFeature(IFeatureProvider fp) {
		super(fp);
	}

	public static final String NAME = "Move Start Order Earlier";
	public static final String DESCRIPTION = "Decrement the start order by 1";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * Returns true if linked business object is SadComponentInstantiation and Start Order is not zero
	 */
	@Override
	public boolean canExecute(ICustomContext context) {
		if (context.getPictogramElements() != null && context.getPictogramElements().length > 0) {
			Object obj = DUtil.getBusinessObject(context.getPictogramElements()[0]);
			if (obj instanceof SadComponentInstantiation) {
				// its a component
				SadComponentInstantiation ci = (SadComponentInstantiation) obj;
				
				// get sad from diagram
				final SoftwareAssembly sad = DUtil.getDiagramSAD(getDiagram());

				// don't allow decrement if its already the lowest
				EList<SadComponentInstantiation> sortedComponents = sad.getComponentInstantiationsInStartOrder();
				if (sortedComponents.get(0).equals(ci)) {
					return false;
				}
				
				// start order NOT zero
				if (ci.getStartOrder() != null) {
					if (ci.getStartOrder().compareTo(BigInteger.ZERO) != 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Decrements the start order for the Component
	 */
	@Override
	public void execute(ICustomContext context) {
		ComponentShape componentShape = (ComponentShape) context.getPictogramElements()[0];
		SadComponentInstantiation ci = (SadComponentInstantiation) DUtil.getBusinessObject(componentShape);

		// get sad from diagram
		final SoftwareAssembly sad = DUtil.getDiagramSAD(getDiagram());

		// get current we are swapping start order with
		SadComponentInstantiation swapCI = ComponentPattern.getComponentInstantiationViaStartOrder(sad, ci.getStartOrder().subtract(BigInteger.ONE));

		// swap start orders, also handle assembly controller changes
		ComponentPattern.swapStartOrder(sad, getFeatureProvider(), swapCI, ci);
	}

}