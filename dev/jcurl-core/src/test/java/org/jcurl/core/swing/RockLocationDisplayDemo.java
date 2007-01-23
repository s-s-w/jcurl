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
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jcurl.core.base.Trajectory;
import org.jcurl.core.curved.CurveTrajectory;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: RockLocationDisplayDemo.java 365 2006-08-28 21:01:40Z
 *          mrohrmoser $
 */
public class RockLocationDisplayDemo extends JFrame {

    private static final long serialVersionUID = 6821533592009529458L;

    public static void main(String[] args) {
        final RockLocationDisplayDemo frame = new RockLocationDisplayDemo();
        // TODO set up the keyboard handler
        frame.show();
    }

    private final RockLocationDisplay dst;

    private final Trajectory src = new CurveTrajectory();

    public RockLocationDisplayDemo() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (false && dst != null)
                    try {
                        dst.exportPng(new File("/tmp/jcurl.png"));
                    } catch (IOException e1) {
                        throw new RuntimeException(e1);
                    }
                System.exit(0);
            }
        });
        setTitle("CurlDemo");
        setSize(900, 400);

        // src.setCollider(new ColliderSimple());
        // src.setCurler(new CurlerStraight());

        dst = new RockLocationDisplay(src.getCurrentPos(), null, null, null);
        getContentPane().add(dst, "Center");
    }
}
