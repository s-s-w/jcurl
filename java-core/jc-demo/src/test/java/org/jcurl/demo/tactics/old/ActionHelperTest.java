/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jcurl.demo.tactics.old;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class ActionHelperTest extends TestCase {

	public void testAccelerator() {
		final ActionRegistry ah = ActionRegistry.getInstance();

		// swing-like:
		assertEquals(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK), ah
				.findAccelerator("ctrl pressed S"));
		assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,
				InputEvent.CTRL_MASK), ah.findAccelerator("CTRL-HOME"));
		assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_END,
				InputEvent.CTRL_MASK), ah.findAccelerator("CTRL-END"));

		// non-swing-like:
		assertEquals(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK), ah
				.findAccelerator("CTRL-S"));
		assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,
				InputEvent.CTRL_MASK), ah.findAccelerator("CTRL-HOME"));
		assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_END,
				InputEvent.CTRL_MASK), ah.findAccelerator("CTRL-END"));
		assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,
				InputEvent.CTRL_MASK), ah.findAccelerator("CTRL-PGUP"));
		assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,
				InputEvent.CTRL_MASK), ah.findAccelerator("CTRL-PGDN"));
		assertEquals(KeyStroke
				.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK), ah
				.findAccelerator("ALT-F4"));
	}

	public void testGetMnemonic() {
		final ActionRegistry ah = ActionRegistry.getInstance();
		assertEquals(Character.valueOf('S'), ah.findMnemonic("&Save"));
		assertEquals(Character.valueOf('S'), ah.findMnemonic("&& &Save"));
		assertEquals(Character.valueOf('S'), ah.findMnemonic("&Save &&"));
	}

	public void testGetName() {
		final ActionRegistry ah = ActionRegistry.getInstance();
		assertEquals("Save", ah.stripMnemonic("&Save"));
		assertEquals("& Save", ah.stripMnemonic("&& &Save"));
		assertEquals("Save &", ah.stripMnemonic("&Save &&"));
	}
}
