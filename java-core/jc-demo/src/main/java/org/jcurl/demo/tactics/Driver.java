/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.demo.tactics;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Driver extends JApplet {

    private static final long serialVersionUID = -2524747372821750530L;

    public static void main(final String[] args) {
        // PDebug.debugBounds = true;
        // PDebug.debugPrintUsedMemory = true;
        // PDebug.debugPrintFrameRate = true;
        // PDebug.debugPaintCalls = true;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFrame application = new JFrame();
                application.setTitle("JCurl Tactics Demo");
                application.setJMenuBar(new JMenuBar());
                final MainMultiPanel m = new MainMultiPanel(application
                        .getJMenuBar(), application.getContentPane());
                application
                        .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                application.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(final WindowEvent e) {
                        m.con.aFileExit.actionPerformed(null);
                    }
                });
                application.setSize(800, 600);
                application.setVisible(true);
            }
        });
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() {
        super.init();
        setJMenuBar(new JMenuBar());
        new MainMultiPanel(getJMenuBar(), getContentPane());
    }

    @Override
    public void start() {
        super.start();
        setSize(800, 600);
        setVisible(true);
    }

    @Override
    public void stop() {
        super.stop();
    }
}
