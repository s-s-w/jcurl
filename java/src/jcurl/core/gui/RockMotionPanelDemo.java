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
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import jcurl.core.JCLoggerFactory;
import jcurl.core.PositionSet;
import jcurl.core.Source;
import jcurl.core.SpeedSet;
import jcurl.core.dto.RockSetProps;
import jcurl.sim.model.CollissionSpin;
import jcurl.sim.model.SlideStraight;

import org.apache.ugli.ULogger;
import org.xml.sax.SAXException;

/**
 * A simple viewer that brings all together.
 * 
 * @see jcurl.sim.model.SlideStraight
 * @see jcurl.sim.model.CollissionSimple
 * @see jcurl.core.gui.SimpleKeys
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: DemoSimple.java 126 2005-10-01 19:26:12Z mrohrmoser $
 */
public class RockMotionPanelDemo extends JFrame {

    private static final ULogger log = JCLoggerFactory
            .getLogger(RockMotionPanelDemo.class);

    private final RockMotionPanel dst;

    private final PositionSet model = new PositionSet();

    public RockMotionPanelDemo() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle("CurlDemo");
        setSize(900, 400);

        this.dst = new RockMotionPanel(model, null, null, null);
        new SetupController(model, this.dst);
        getContentPane().add(this.dst);
    }

    public static void main(String[] args) throws MalformedURLException,
            ParserConfigurationException, SAXException, IOException {
        final RockMotionPanelDemo frame = new RockMotionPanelDemo();
        {
            final Source src;
            // initial state
            final PositionSet pos = PositionSet.allOut();
            pos.getDark(0).setLocation(0, 5, 0);
            pos.getDark(1).setLocation(0, 0, 0.5);
            pos.getLight(0).setLocation(0.2, 2.5);
            pos.getLight(1).setLocation(1.0, 1.5);
            final SpeedSet speed = new SpeedSet();
            speed.getDark(0).setLocation(0, -1.325, 0.75);
            // dynamics engines
            src = new SlideStraight(new CollissionSpin());
            src.reset(0, pos, speed, RockSetProps.DEFAULT);
            // feed the model
            src.getPos(0, frame.model);
        }
        // display
        frame.show();
    }
}