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
package org.jcurl.demo.viewer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.ComputedSource;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.TargetDiscrete;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.SlideStraight;
import org.jcurl.core.swing.JCurlDisplay;
import org.jcurl.core.swing.SimpleKeys;
import org.xml.sax.SAXException;

/**
 * A simple viewer that brings all together.
 * 
 * @see org.jcurl.core.model.SlideStraight
 * @see org.jcurl.core.model.CollissionSimple
 * @see org.jcurl.core.swing.SimpleKeys
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ViewerApp extends JFrame {

    private static final long serialVersionUID = -5809346296249873005L;

    private static final Log log = JCLoggerFactory.getLogger(ViewerApp.class);

    public static void main(String[] args) throws MalformedURLException,
            ParserConfigurationException, SAXException, IOException {
        final ComputedSource src = new ComputedSource();
        if (true) {
            final URL url;
            {
                URL tmp = ViewerApp.class.getResource("/setup/dat.jcx");
                if (tmp == null) {
                    tmp = new File("./config/jcurl.jar/setup/hammy.jcx")
                            .toURL();
                }
                url = tmp;
            }
            log.info("Loading setup [" + url + "]");
            src.loadStart(url.openStream());
        } else {
            // initial state
            final PositionSet pos = PositionSet.allOut();
            pos.getDark(0).setLocation(0, 5, 0);
            pos.getLight(0).setLocation(0.2, 2.5);
            pos.getLight(1).setLocation(1.0, 1.5);
            final SpeedSet speed = new SpeedSet();
            speed.getDark(0).setLocation(0, -1.325, 0.75);
            // dynamics engines
            src.init(pos, speed, new SlideStraight(), new CollissionSpin());
        }
        final ViewerApp frame = new ViewerApp(src);
        // set up the keyboard handler
        frame.addKeyListener(new SimpleKeys(src, frame.dst));
        // display
        frame.show();
    }

    private final TargetDiscrete dst;

    public ViewerApp(ComputedSource src) {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle("CurlDemo");
        setSize(900, 400);

        final JCurlDisplay mp = new JCurlDisplay();
        mp.setPos(src.getPos());
        getContentPane().add(mp, "Center");
        // getContentPane().add(new SumShotDisplay(), "East");
        // getContentPane().add(new SumWaitDisplay(), "West");
        dst = mp;
    }
}