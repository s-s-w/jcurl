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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.jcurl.core.ui.TaskExecutor.SwingEDT;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id: MainApp.java 822 2008-03-31 17:00:22Z mro $
 */
public class Main extends JFrame {

	private static final long serialVersionUID = 3398372625156897223L;

	public static void main(final String[] args) {
		final JFrame f = new Main();
		f.setSize(600, 800);
		f.setVisible(true);
		// zoom to the house smoothely
		ActionRegistry.invoke(MenuView.class, "zoomHouse", SwingEDT.class);
		// load hammy by default
		
		{
			final Preferences p = Preferences.userNodeForPackage(Main.class);
			p.putLong("lastStartMillis", System.currentTimeMillis());
			try {
				p.flush();
			} catch (final BackingStoreException e) {
				throw new RuntimeException("Unhandled", e);
			}
		}
	}

	private Main() {
		setTitle("JCurl Shot Planner");
		final JMenuBar mb = new JMenuBar();
		final TrajectoryPiccoloPanel tp = new TrajectoryPiccoloPanel();
		final ActionRegistry ah = ActionRegistry.getInstance();
		{
			final MenuFile.Controller con = new MenuFile.Controller(this, tp);
			ah.registerController(con);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(final WindowEvent e) {
					con.exitFile();
				}
			});
			mb.add(ah.createJMenu(con));
			ah.findAction(con, "openFile").setEnabled(true);
			ah.findAction(con, "newFile").setEnabled(true);
			ah.findAction(con, "screenShot").setEnabled(true);
			ah.findAction(con, "exitFile").setEnabled(true);
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
		getContentPane().add(tp);
		pack();
	}
}
