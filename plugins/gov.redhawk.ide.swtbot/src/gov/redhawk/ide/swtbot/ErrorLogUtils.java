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
package gov.redhawk.ide.swtbot;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Assert;

/**
 * 
 */
public class ErrorLogUtils {

	/** private to prevent instantiation since all functions are static. */
	private ErrorLogUtils() {
	}

	public static void clearErrorLog(SWTGefBot bot) {
		bot.menu("Window").menu("Show View").menu("Error Log").click();
		SWTBotView errorLogView = bot.viewById("org.eclipse.pde.runtime.LogView");
		SWTBotToolbarButton clearButton = errorLogView.toolbarButton("Clear Log Viewer");
		clearButton.click();
		checkErrorLogIsEmpty(bot);
	}

	/**
	 * Checks error log for a specific message. If found, the message text is returned. Otherwise returns null. </br>
	 * View is closed after the search is complete.
	 * @param bot
	 * @param message - All or part of the message that is being checked for
	 */
	public static String checkErrorLogForMessage(SWTGefBot bot, String message) {
		bot.menu("Window").menu("Show View").menu("Error Log").click();
		SWTBotView errorLogView = bot.viewById("org.eclipse.pde.runtime.LogView");

		// Make sure the message does not exist in the ErrorLog view
		for (SWTBotTreeItem treeItem : errorLogView.bot().tree().getAllItems()) {
			String treeItemText = treeItem.getText();
			if (treeItemText.matches(".*" + message + ".*")) {
				closeView(bot);
				return treeItemText;
			}
		}

		closeView(bot);
		return null;
	}

	public static void checkErrorLogIsEmpty(SWTGefBot bot) {
		bot.menu("Window").menu("Show View").menu("Error Log").click();
		SWTBotView errorLogView = bot.viewById("org.eclipse.pde.runtime.LogView");
		SWTBotTree tree = errorLogView.bot().tree();
		Assert.assertTrue("Errors were reported in Error Log View", tree.getAllItems().length == 0);
	}

	public static void closeView(SWTGefBot bot) {
		bot.viewById("org.eclipse.pde.runtime.LogView").close();
	}
}
