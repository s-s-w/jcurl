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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;

import jcurl.core.TargetDiscrete;
import jcurl.core.gui.JCurlDisplay;
import jcurl.core.gui.SimpleKeys;
import jcurl.sim.core.ComputedSource;
import jcurl.sim.model.CollissionSpin;
import jcurl.sim.model.SlideStraight;

import org.apache.commons.logging.Log;
import org.jcurl.core.PositionSet;
import org.jcurl.core.SpeedSet;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ViewerApplet extends JApplet {

    private static final long serialVersionUID = 3098543777055838932L;

    private static final Log log = JCLoggerFactory
            .getLogger(ViewerApplet.class);

    public String getAppletInfo() {
        return "JCurl Viewer Demo";
    }

    public void init() {
        setFocusable(true);
        // resize(200, 100);
        // final Container contentPane = getContentPane();
        final JCurlDisplay mp = new JCurlDisplay(null, null, null, null);
        getContentPane().add(mp);
        final TargetDiscrete dst = mp;

        final ComputedSource src = new ComputedSource();
        try {
            if (true) {
                final URL url;
                {
                    URL tmp = ViewerApp.class.getResource("/setup/hammy.jcx");
                    if (tmp == null)
                        tmp = new URL("file", "localhost",
                                "/home/m/eclipse/berlios/jcurl/config/jcurl.jar/setup/hammy.jcx");
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
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set up the keyboard handler
        this.addKeyListener(new SimpleKeys(src, dst));
    }
}