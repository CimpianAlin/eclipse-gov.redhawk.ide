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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.matchers.AbstractMatcher;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotToolbarDropDownButton;
import org.hamcrest.Description;
import org.junit.Assert;

import gov.redhawk.ide.swtbot.condition.WaitForWidgetEnablement;

public class ConsoleUtils {

	/** private to prevent instantiation since all functions are static. */
	private ConsoleUtils() {
	}

	public static class AnyMenuItemMatcher< T extends MenuItem > extends AbstractMatcher<T> {

		AnyMenuItemMatcher() {
		}

		@Override
		public void describeTo(Description description) {
		}

		@Override
		protected boolean doMatch(Object item) {
			return true;
		}
	}

	public static String[] getConsoleTitles(SWTWorkbenchBot bot) {
		SWTBotView view = ViewUtils.getConsoleView(bot);
		view.show();

		// Get the displayed console title
		String consoleText = view.bot().label().getText();
		List<String> consoleTitles = new ArrayList<String>();
		consoleTitles.add(consoleText);

		SWTBotToolbarDropDownButton consoleButton = (SWTBotToolbarDropDownButton) view.toolbarButton("Display Selected Console");
		if (!consoleButton.isEnabled()) {
			return consoleTitles.toArray(new String[1]);
		}

		// Cycle through the consoles
		consoleButton.click();
		for (String newConsoleText = view.bot().label().getText(); !consoleText.equals(newConsoleText); newConsoleText = view.bot().label().getText()) {
			consoleTitles.add(newConsoleText);
			consoleButton.click();
		}
		return consoleTitles.toArray(new String[consoleTitles.size()]);
	}

	public static void assertConsoleTitleExists(SWTWorkbenchBot bot, String titleRegex) {
		String[] titles = getConsoleTitles(bot);
		for (String title : titles) {
			if (title.matches(titleRegex)) {
				return;
			}
		}
		String errorMsg = String.format("Couldn't find a console title matching regex '%s'", titleRegex);
		Assert.fail(errorMsg);
	}

	/**
	 * Shows the console for a particular process.
	 * @param bot
	 * @param processName
	 * @return The SWTBot console view
	 */
	public static SWTBotView showConsole(SWTWorkbenchBot bot, final String processName) {
		SWTBotView view = ViewUtils.getConsoleView(bot);
		view.show();

		// We could already be on the correct console
		String consoleText = view.bot().label().getText();
		if (consoleText.contains(processName)) {
			return view;
		}

		SWTBotToolbarDropDownButton consoleButton = (SWTBotToolbarDropDownButton) view.toolbarButton("Display Selected Console");
		if (!consoleButton.isEnabled()) {
			throw new WidgetNotFoundException(String.format("Can't find console for %s", processName));
		}

		// Switch consoles until we hit the right one
		consoleButton.click();
		for (String newConsoleText = view.bot().label().getText(); !consoleText.equals(newConsoleText); newConsoleText = view.bot().label().getText()) {
			if (newConsoleText.contains(processName)) {
				return view;
			}
			consoleButton.click();
		}
		throw new WidgetNotFoundException(String.format("Can't find console for %s", processName));
	}

	/**
	 * Terminate a process via the console's terminate button
	 * @param bot
	 * @param processName
	 */
	public static void terminateProcess(SWTWorkbenchBot bot, final String processName) {
		SWTBotView view = showConsole(bot, processName);

		// Click terminate, wait for it to disable (indicating process ended)
		final SWTBotToolbarButton terminateButton = view.toolbarButton("Terminate");
		terminateButton.click();
		bot.waitUntil(new WaitForWidgetEnablement(terminateButton, false));
	}

	public static void removeTerminatedLaunches(SWTWorkbenchBot bot) {
		SWTBotView view = ViewUtils.getConsoleView(bot);
		view.show();

		SWTBotToolbarButton consoleButton = view.toolbarButton("Display Selected Console");
		String consoleText = view.bot().label().getText();
		String newConsoleText;

		do {
			try {
				SWTBotToolbarButton button = view.toolbarButton("Remove All Terminated Launches");
				if (button.isEnabled()) {
					button.click();
					return;
				}
			} catch (WidgetNotFoundException ex) {
				// PASS
			}
			if (consoleButton.isEnabled()) {
				consoleButton.click();
			}
			newConsoleText = view.bot().label().getText();
		} while (!consoleText.equals(newConsoleText));
	}
}
