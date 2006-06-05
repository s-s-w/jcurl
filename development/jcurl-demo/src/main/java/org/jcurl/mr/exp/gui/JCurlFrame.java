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
package org.jcurl.mr.exp.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.jcurl.core.dto.PositionSet;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.core.swing.RockLocationDisplay;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:MainFrame.java 330 2006-06-05 14:29:14Z mrohrmoser $
 */
public class JCurlFrame extends JFrame {

    private static final Log log = JCLoggerFactory.getLogger(JCurlFrame.class);

    private static final long serialVersionUID = 7094013076400484227L;

    public static void main(String[] args) throws InterruptedException {
        JCurlFrame f = new JCurlFrame();
        f.show();
        //Thread.sleep(2000);
        //PositionSet.allOut(f.rld.getPositions());
    }

    private RockLocationDisplay rld;

    public JCurlFrame() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setTitle(this.getClass().getName());
        setSize(800, 600);
        
        rld = new RockLocationDisplay();
        rld.setDoubleBuffered(false);
        rld.setPositions(null);
        rld.setZoom(null);
        
        getContentPane().add(rld);
    }
}