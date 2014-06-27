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
package gov.redhawk.ide.sad.graphiti.ui.diagram.features.update;

import gov.redhawk.diagram.util.InterfacesUtil;
import gov.redhawk.ide.sad.graphiti.ui.diagram.patterns.SADConnectInterfacePattern;
import gov.redhawk.ide.sad.graphiti.ui.diagram.util.DUtil;
import gov.redhawk.sca.sad.validation.ConnectionsConstraint;
import mil.jpeojtrs.sca.sad.SadConnectInterface;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.IRemoveFeature;
import org.eclipse.graphiti.features.context.IRemoveContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.RemoveContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;

public class SADConnectionInterfaceUpdateFeature extends AbstractUpdateFeature {

	public SADConnectionInterfaceUpdateFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context) {
		Object obj = DUtil.getBusinessObject(context.getPictogramElement());

		if (obj != null && obj instanceof SadConnectInterface) {
			return true;
		}
		return false;
	}

	@Override
	public IReason updateNeeded(IUpdateContext context) {

		Connection connectionPE = (Connection) context.getPictogramElement();
		SadConnectInterface sadConnectInterface = (SadConnectInterface) DUtil.getBusinessObject(context.getPictogramElement());

		Reason requiresUpdate = internalUpdate(connectionPE, sadConnectInterface, getFeatureProvider(), false);

		return requiresUpdate;
	}

	@Override
	public boolean update(IUpdateContext context) {

		Connection connectionPE = (Connection) context.getPictogramElement();
		SadConnectInterface sadConnectInterface = (SadConnectInterface) DUtil.getBusinessObject(context.getPictogramElement());

		Reason updated = internalUpdate(connectionPE, sadConnectInterface, getFeatureProvider(), true);

		return updated.toBoolean();
	}

	/**
	 * Performs either an update or a check to determine if update is required.
	 * if performUpdate flag is true it will update the shape,
	 * otherwise it will return reason why update is required.
	 * @param ci
	 * @param performUpdate
	 * @return
	 */
	public Reason internalUpdate(Connection connectionPE, SadConnectInterface connectInterface, IFeatureProvider featureProvider, boolean performUpdate) {

		boolean updateStatus = false;

		// imgConnectionDecorator
		ConnectionDecorator imgConnectionDecorator = (ConnectionDecorator) DUtil.findFirstPropertyContainer(connectionPE,
			SADConnectInterfacePattern.SHAPE_IMG_CONNECTION_DECORATOR);
		// textConnectionDecorator
		ConnectionDecorator textConnectionDecorator = (ConnectionDecorator) DUtil.findFirstPropertyContainer(connectionPE,
			SADConnectInterfacePattern.SHAPE_TEXT_CONNECTION_DECORATOR);

		// if not compatible draw error/warning decorator
		if (connectInterface.getSource() != null && connectInterface.getTarget() != null) {
			// connection validation
			boolean uniqueConnection = ConnectionsConstraint.uniqueConnection(connectInterface);
			boolean compatibleConnection = InterfacesUtil.areCompatible(connectInterface.getSource(), connectInterface.getTarget());

			if ((!compatibleConnection || !uniqueConnection) && (imgConnectionDecorator == null || textConnectionDecorator == null)) {
				if (performUpdate) {
					updateStatus = true;
					// Incompatible connection without an error decorator! We need to add an error decorator
					SADConnectInterfacePattern.decorateConnection(connectionPE, connectInterface, getDiagram());
				} else {
					return new Reason(true, "Connection requires Error Decorator");
				}
			} else if ((compatibleConnection || uniqueConnection) && (imgConnectionDecorator != null || textConnectionDecorator != null)) {
				if (performUpdate) {
					updateStatus = true;
					// Compatible connection with an inappropriate error decorator! We need to remove the error decorator
					IRemoveContext rc = new RemoveContext(imgConnectionDecorator);
					IRemoveFeature removeFeature = featureProvider.getRemoveFeature(rc);
					if (removeFeature != null) {
						removeFeature.remove(rc);
					}
					rc = new RemoveContext(textConnectionDecorator);
					removeFeature = featureProvider.getRemoveFeature(rc);
					if (removeFeature != null) {
						removeFeature.remove(rc);
					}
				} else {
					return new Reason(true, "Error Decorator needs to be removed from Connection");
				}
			}
		}

		if (updateStatus && performUpdate) {
			return new Reason(true, "Update successful");
		}

		return new Reason(false, "No updates required");
	}

}