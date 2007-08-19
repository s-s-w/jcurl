/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005 M. Rohrmoser
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
package org.jcurl.demo.tactics;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MainApp extends JFrame {

    private static final long serialVersionUID = 3398372625156897223L;

    // FIXME What goes wrong if I put here 30?
    static final double tmax = 15;

    public static void main(final String[] args) {
        // PDebug.debugBounds = true;
        // PDebug.debugPrintUsedMemory = true;
        // PDebug.debugPrintFrameRate = true;
        // PDebug.debugPaintCalls = true;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainApp application = new MainApp();
                application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                application.setSize(800, 600);
                application.setVisible(true);
                application.center();
            }
        });
    }

    private final MainMod m;

    private final MainPanel p;

    public MainApp() {
        m = new MainMod();
        m.setCurrentTime(tmax);
        p = new MainPanel(m);
        getContentPane().add(p);
        // this.setJMenuBar(new MenuFactory().menu(c, this));
    }

    public void center() {
        p.center();
    }
}