/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
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
package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.jcurl.core.ui.TaskExecutor.SwingEDT;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class MainApp extends JFrame {

	private static final long serialVersionUID = 3398372625156897223L;

	public static void main(final String[] args) {
		final JFrame f = new MainApp();
		f.setSize(600, 800);
		f.setVisible(true);
		// zoom to the house smoothely
		ActionRegistry.invoke(MenuView.class, "zoomHouse", SwingEDT.class);
		if (false) {
			try {
				Thread.sleep(500);
			} catch (final InterruptedException e1) {
				return;
			}
			// load hammy by default
			ActionRegistry.invoke(MenuFile.Controller.class, "showHammy",
					SwingEDT.class);
		}
		{
			final Preferences p = Preferences.userNodeForPackage(MainApp.class);
			p.putLong("lastStartMillis", System.currentTimeMillis());
			try {
				p.flush();
			} catch (final BackingStoreException e) {
				throw new RuntimeException("Unhandled", e);
			}
		}
	}

	private MainApp() {
		setTitle("JCurl Shot Planner - www.jcurl.org");
		final JMenuBar mb = new JMenuBar();
		final TrajectoryPiccolo tp = new TrajectoryPiccolo();
		tp.setBackground(new Color(0xE8E8FF));
		final ActionRegistry ah = ActionRegistry.getInstance();
		{
			final MenuFile.Controller con = new MenuFile.Controller(this, tp,
					tp);
			ah.registerController(con);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(final WindowEvent e) {
					con.exitFile();
				}
			});
			mb.add(ah.createJMenu(con));
			ah.findAction(con, "clear").setEnabled(true);
			ah.findAction(con, "showHammy").setEnabled(true);
			ah.findAction(con, "openFile").setEnabled(true);
			ah.findAction(con, "newFile").setEnabled(true);
			ah.findAction(con, "screenShot").setEnabled(true);
			// ah.findAction(con, "exportSvg").setEnabled(true);
			ah.findAction(con, "exitFile").setEnabled(true);
			ah.findAction(con, "saveAsFile").setEnabled(true);
		}
		{
			final MenuEdit.Controller con = new MenuEdit.Controller();
			ah.registerController(con);
			mb.add(ah.createJMenu(con));
		}
		{
			final MenuView con = new MenuView();
			ah.registerController(con);
			mb.add(ah.createJMenu(con));
			con.setModel(tp);
		}
		{
			final MenuHelp con = new MenuHelp();
			ah.registerController(con);
			mb.add(ah.createJMenu(con));
			ah.findAction(con, "helpAbout").setEnabled(true);
		}
		setJMenuBar(mb);
		final Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(tp, BorderLayout.CENTER);

		// add the swing widget based trajectory manipulation
		final Box b = Box.createVerticalBox();

		final JTabbedPane t = new JTabbedPane(SwingConstants.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		final TrajectorySwing ts = new TrajectorySwing();
		t.add("Rock", ts);
		t.setMnemonicAt(0, 'R');
		t.add("Ice", new JLabel("Todo"));
		t.setMnemonicAt(1, 'I');
		t.add("Collission", new JLabel("Todo"));
		t.setMnemonicAt(2, 'C');

		b.add(t);
		b.add(new Box.Filler(new Dimension(0, 0), new Dimension(1,
				Integer.MAX_VALUE), new Dimension(Integer.MAX_VALUE,
				Integer.MAX_VALUE)));

		c.add(b, BorderLayout.EAST);

		pack();
	}
}
