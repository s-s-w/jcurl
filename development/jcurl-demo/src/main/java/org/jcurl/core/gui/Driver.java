/*
 * jcurl curling simulation framework 
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
package org.jcurl.core.gui;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;


import org.jcurl.core.gui.Zoomer;
import org.jcurl.core.swing.JCurlDisplay;
import org.jcurl.model.PositionSet;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Driver.java 330 2006-06-05 14:29:14Z mrohrmoser $
 */
public class Driver extends JFrame {

    private static final long serialVersionUID = 5974527099430727076L;

    // private static final Log log =
    // JCLoggerFactory.getLogger(Driver.class);

    public Driver() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle("CurlDemo");
        setSize(600, 400);
        Container contentPane = getContentPane();
        final PositionSet rs = PositionSet.allOut();
        final JCurlDisplay mp = new JCurlDisplay(rs, Zoomer.HOUSE2HACK, null,
                null);
        contentPane.add(mp);
    }

    public static void main(String[] args) {
        JFrame frame = new Driver();
        frame.show();
    }
}