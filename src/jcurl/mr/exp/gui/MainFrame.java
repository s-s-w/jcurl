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
package jcurl.mr.exp.gui;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import jcurl.mr.exp.math.PointList;

import org.apache.ugli.LoggerFactory;
import org.apache.ugli.ULogger;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MainFrame extends JFrame {

    private static final ULogger log = LoggerFactory.getLogger(MainFrame.class);

    private final PointList curve;

    public MainFrame() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                log.info("Points collected: " + curve.size());
                System.exit(0);
            }
        });
        setTitle("FirstFrame");
        setSize(600, 600);
        Container contentPane = getContentPane();
        //contentPane.add(new KeySketchPanel());
        //contentPane.add(new MouseRectPanel());
        final MouseSketchPanel mp = new MouseSketchPanel(' ');
        this.curve = mp.getCurve();
        contentPane.add(mp);
    }
}