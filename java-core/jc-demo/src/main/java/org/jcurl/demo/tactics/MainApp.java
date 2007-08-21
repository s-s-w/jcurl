/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.demo.tactics;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.jcurl.demo.tactics.Controller.MainController;
import org.jcurl.demo.tactics.Controller.ZuiController;

/**
 * Create menues and wire them up with {@link Action}s from
 * {@link MainController} and {@link ZuiController}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MainApp extends JFrame {

    static class Menufactory {
        private final MainController mainc;
        private final ZuiController zuic;

        public Menufactory(final MainController mainc, final ZuiController zuic) {
            this.mainc = mainc;
            this.zuic = zuic;
        }

        public JMenu editMenu() {
            final JMenu ret = new JMenu("Edit");
            ret.setMnemonic('E');

            JMenuItem i = ret.add(new JMenuItem(mainc.editUndo));
            i.setText("Undo");
            i.setMnemonic('U');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    InputEvent.CTRL_MASK));

            i = ret.add(new JMenuItem(mainc.editRedo));
            i.setText("Redo");
            i.setMnemonic('R');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                    InputEvent.CTRL_MASK));

            ret.addSeparator();

            i = ret.add(new JMenuItem());
            i.setText("Properties");
            // i.setMnemonic('P');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                    InputEvent.CTRL_MASK));
            i.setEnabled(false);

            return ret;
        }

        public JMenu fileMenu() {
            final JMenu ret = new JMenu("File");
            ret.setMnemonic('F');

            JMenuItem i = ret.add(new JMenuItem());
            i.setText("New");
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                    InputEvent.CTRL_MASK));
            i.setMnemonic('N');
            i.setEnabled(false);

            i = ret.add(new JMenuItem());
            i.setText("Open");
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0,
                    InputEvent.CTRL_MASK));
            i.setMnemonic('O');
            i.setEnabled(false);

            ret.addSeparator();

            i = ret.add(new JMenuItem());
            i.setText("Save");
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    InputEvent.CTRL_MASK));
            i.setMnemonic('S');
            i.setEnabled(false);

            i = ret.add(new JMenuItem());
            i.setText("Save As");
            i.setMnemonic('A');
            i.setEnabled(false);

            ret.addSeparator();

            i = ret.add(new JMenuItem());
            i.setText("Screenshot");
            i.setMnemonic('c');
            i
                    .setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_PRINTSCREEN, 0));
            i.setEnabled(false);

            ret.addSeparator();

            i = ret.add(new JMenuItem());
            i.setText("Exit");
            i.setMnemonic('x');
            i.setEnabled(false);

            return ret;
        }

        public JMenu helpMenu() {
            final JMenu ret = new JMenu("Help");
            ret.setMnemonic('H');

            final JMenuItem i = ret.add(new JMenuItem());
            i.setText("About");
            i.setMnemonic('A');
            i.setEnabled(false);

            return ret;
        }

        public JMenuBar menu() {
            final JMenuBar mb = new JMenuBar();
            mb.add(fileMenu());
            mb.add(editMenu());
            mb.add(zoomMenu());
            mb.add(helpMenu());
            return mb;
        }

        public JMenu zoomMenu() {
            final JMenu ret = new JMenu("Zoom");
            ret.setMnemonic('Z');

            JMenuItem i = ret.add(new JMenuItem(zuic.zoomSheet));
            i.setText("Sheet");
            i.setMnemonic('S');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));

            i = ret.add(new JMenuItem(zuic.zoomHouse));
            i.setText("House");
            i.setMnemonic('H');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));

            i = ret.add(new JMenuItem(zuic.zoom12Foot));
            i.setText("12-foot");
            i.setMnemonic('1');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,
                    InputEvent.CTRL_MASK));

            ret.addSeparator();

            i = ret.add(new JMenuItem());
            i.setText("In");
            i.setMnemonic('I');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
            i.setEnabled(false);

            i = ret.add(new JMenuItem());
            i.setText("Out");
            i.setMnemonic('O');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0));
            i.setEnabled(false);

            return ret;
        }
    }

    private static final long serialVersionUID = 3398372625156897223L;

    // FIXME What goes wrong if I put here 30?
    static final double tmax = 15;

    public static void main(final String[] args) {
        // PDebug.debugBounds = true;
        // PDebug.debugPrintUsedMemory = true;
        // PDebug.debugPrintFrameRate = true;
        // PDebug.debugPaintCalls = true;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainApp application = new MainApp();
                application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                application.setSize(800, 600);
                application.setVisible(true);
                application.center();
            }
        });
    }

    final MainController mainc;
    final ZuiController zuic;

    public MainApp() {
        final MainMod m = new MainMod();
        m.setCurrentTime(tmax);
        final ZuiPanel zui = new ZuiPanel(m);
        final MainPanel p = new MainPanel(m, zui);
        getContentPane().add(p);
        mainc = new MainController(p, m);
        zuic = new ZuiController(zui, m);
        setJMenuBar(new Menufactory(mainc, zuic).menu());
    }

    public void center() {
        zuic.zoomHouse.actionPerformed(null);
    }
}