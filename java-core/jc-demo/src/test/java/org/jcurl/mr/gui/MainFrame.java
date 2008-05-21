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

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:MainFrame.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class MainFrame extends JFrame {

	private static final Log log = JCLoggerFactory.getLogger(MainFrame.class);

	private static final long serialVersionUID = 7094013076400484227L;

	private final PointList curve;

	public MainFrame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				log.info("Points collected: " + curve.size());
				System.exit(0);
			}
		});
		setTitle("FirstFrame");
		this.setSize(600, 600);
		final Container contentPane = getContentPane();
		// contentPane.add(new KeySketchPanel());
		// contentPane.add(new MouseRectPanel());
		final MouseSketchPanel mp = new MouseSketchPanel(' ');
		curve = mp.getCurve();
		contentPane.add(mp);
	}
}