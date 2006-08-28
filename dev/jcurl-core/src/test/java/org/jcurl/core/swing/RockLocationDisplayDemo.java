/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.core.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.jcurl.core.base.ColliderSimple;
import org.jcurl.core.curved.CurveTrajectory;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockLocationDisplayDemo extends JFrame {

    public static void main(String[] args) {
        final RockLocationDisplayDemo frame = new RockLocationDisplayDemo();
        // set up the keyboard handler
        // display
        frame.show();
    }

    private final RockLocationDisplay dst;

    private final CurveTrajectory src = new CurveTrajectory();

    public RockLocationDisplayDemo() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle("CurlDemo");
        setSize(900, 400);

        src.setCollider(new ColliderSimple());
        src.setCurler(null);

        dst = new RockLocationDisplay(src.getCurrentPos(), null, null, null);
        getContentPane().add(dst, "Center");
    }
}
