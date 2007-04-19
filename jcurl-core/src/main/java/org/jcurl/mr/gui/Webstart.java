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
package org.jcurl.mr.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jcurl.mr.gui.TacticsApplet.MenuFactory;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: Driver.java 483 2007-03-30 17:56:46Z mrohrmoser $
 */
public class Webstart extends JFrame {

    private static final long serialVersionUID = 3398372625156897223L;

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final Webstart application = new Webstart();
                application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                application.setSize(400, 400);
                application.setVisible(true);
            }
        });
    }

    private final TacticsController c;

    private final Model m;

    private final DetailPanel p;

    public Webstart() {
        m = new Model();
        p = new DetailPanel(m);
        c = new TacticsController(this, p, m);
        getContentPane().add(p);
        final MenuFactory mf = new MenuFactory();
        setJMenuBar(mf.menu(c, this));
    }

}