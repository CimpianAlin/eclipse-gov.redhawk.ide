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
package gov.redhawk.ide.swtbot.diagram;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.hamcrest.Matcher;

/**
 * Bot editor class to use RHTestBotEditor for better dragging functionality with Graphiti diagrams.
 */
public class RHBotGefEditor extends SWTBotGefEditor {

	private RHTestBotViewer dragViewer;

	/**
	 * @param reference
	 * @param bot
	 * @throws WidgetNotFoundException
	 */
	public RHBotGefEditor(IEditorReference reference, SWTWorkbenchBot bot) throws WidgetNotFoundException {
		super(reference, bot);
		GraphicalViewer graphicalViewer = UIThreadRunnable.syncExec(new Result<GraphicalViewer>() {
			public GraphicalViewer run() {
				final IEditorPart editorPart = partReference.getEditor(true);
				return (GraphicalViewer) editorPart.getAdapter(GraphicalViewer.class);
			}
		});
		dragViewer = new RHTestBotViewer(graphicalViewer);
		dragViewer.init();
	}

	/**
	 * 
	 * @return The RHTestBotViewer used for drag operations
	 */
	public RHTestBotViewer getDragViewer() {
		return dragViewer;
	}

	/**
	 * Uses dragViewer instead of viewer
	 */
	@Override
	public void drag(final int fromXPosition, final int fromYPosition, final int toXPosition, final int toYPosition) {
		getDragViewer().drag(fromXPosition, fromYPosition, toXPosition, toYPosition);
	}

	public void activateNamespacedTool(final String[] labels) {
		getDragViewer().activateNamespacedTool(labels);
	}

	public void activateNamespacedTool(final String[] labels, final int index) {
		getDragViewer().activateNamespacedTool(labels, index);
	}

	/**
	 * Finds {@link org.eclipse.gef.EditPart}s in the palette.
	 * @param matcher the matcher that matches on {@link org.eclipse.gef.EditPart}
	 * @return a collection of {@link SWTBotGefEditPart}
	 * @throws WidgetNotFoundException
	 */
	public List<SWTBotGefEditPart> editPartsPalette(Matcher< ? extends EditPart> matcher) throws WidgetNotFoundException {
		return getDragViewer().editPartsPalette(matcher);
	}

}
