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
package jcurl.core.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import jcurl.core.PositionSet;
import jcurl.core.Source;
import jcurl.core.SpeedSet;
import jcurl.core.TargetDiscrete;
import jcurl.core.dto.RockSetProps;
import jcurl.sim.model.CollissionSimple;
import jcurl.sim.model.SlideStraight;

import org.apache.ugli.LoggerFactory;
import org.apache.ugli.ULogger;

/**
 * A simple viewer that brings all together.
 * 
 * @see jcurl.sim.model.SlideStraight
 * @see jcurl.sim.model.CollissionSimple
 * @see jcurl.core.gui.SimpleKeys
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DemoSimple extends JFrame {

    private static final ULogger log = LoggerFactory
            .getLogger(DemoSimple.class);

    private final TargetDiscrete dst;

    public DemoSimple() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle("CurlDemo");
        setSize(900, 400);

        final JCurlPanel mp = new JCurlPanel(null, null, null, null);
        getContentPane().add(mp);
        dst = mp;
    }

    public static void main(String[] args) {
        // initial state
        final PositionSet pos = PositionSet.allOut();
        pos.getDark(0).setLocation(0, 5, 0);
        pos.getLight(0).setLocation(0.2, 2.5);
        pos.getLight(1).setLocation(1.0, 1.5);
        final SpeedSet speed = new SpeedSet();
        speed.getDark(0).setLocation(0, -1.325, 0.75);
        // dynamics engines
        final Source src = new SlideStraight(new CollissionSimple());
        src.reset(0, pos, speed, RockSetProps.DEFAULT);
        final DemoSimple frame = new DemoSimple();
        // set up the keyboard handler
        frame.addKeyListener(new SimpleKeys(src, frame.dst));
        // display
        frame.show();
    }
}