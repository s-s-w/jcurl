/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.mr.gui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:MenuDemo.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class MenuDemo extends JFrame {

	private static class MyAboutDialog extends JDialog {

		private static final long serialVersionUID = -7533904080833071647L;

		public MyAboutDialog(final JFrame owner) {
			super(owner, "About", true);
			setResizable(false);
			setUndecorated(false);
			// ...

			pack();
		}

		@Override
		public void setVisible(boolean b) {
			if (!b)
				getOwner().requestFocus();
			super.setVisible(b);
		}
	}

	private static final long serialVersionUID = -2968454290313186955L;

	public static void main(final String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		final JFrame frame = new MenuDemo();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public MenuDemo() {
		final JFrame owner = this;
		final JMenuBar menu = new JMenuBar();
		{
			final JMenu animation = new JMenu("Animation");
			animation.setMnemonic('A');
			final Action toggleStartStop = new AbstractAction("Start/Stop") {

				private static final long serialVersionUID = 5755124125303854954L;

				private boolean started = false;

				public void actionPerformed(ActionEvent evt) {
					started = !started;
					System.err.println(started ? "Start" : "Stop");
				}
			};
			{
				final JMenuItem startAnimation = new JMenuItem(toggleStartStop);
				// startAnimation.setMnemonic('a');
				// startAnimation.setMnemonic(KeyEvent.VK_SPACE);
				startAnimation.setAccelerator(KeyStroke.getKeyStroke(' '));
				animation.add(startAnimation);
			}
			// {
			// final JMenuItem stopAnimation = new JMenuItem(toggleStartStop);
			// stopAnimation.setMnemonic('o');
			// stopAnimation.setAccelerator(KeyStroke.getKeyStroke(' '));
			// animation.add(stopAnimation);
			// }
			menu.add(animation);
		}
		{
			final JMenu help = new JMenu("Help");
			help.setMnemonic('H');
			final JMenuItem about = new JMenuItem(new AbstractAction("About") {

				private static final long serialVersionUID = -1288940924434544822L;

				public void actionPerformed(ActionEvent evt) {
					new MyAboutDialog(owner).setVisible(true);
				}
			});
			about.setMnemonic('A');
			about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
					InputEvent.CTRL_MASK));
			help.add(about);
			menu.add(help);
		}
		setJMenuBar(menu);
	}
}