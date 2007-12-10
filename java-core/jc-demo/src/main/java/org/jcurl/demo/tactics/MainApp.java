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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jcurl.core.model.BroomPromptModel;
import org.jcurl.demo.tactics.Controller.MainController;
import org.jcurl.demo.tactics.Controller.UndoRedoCon;
import org.jcurl.demo.tactics.Controller.ZuiController;

public class MainApp extends JFrame {

    /**
     * Create menues and wire them up with {@link Action}s from
     * {@link MainController} and {@link ZuiController}.
     * 
     * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
     * @version $Id$
     */
    static class Menufactory {
        private final MainController mainc;
        private final UndoRedoCon undoc;
        private final ZuiController zuic;

        public Menufactory(final MainController mainc,
                final ZuiController zuic, final UndoRedoCon undoc) {
            this.mainc = mainc;
            this.zuic = zuic;
            this.undoc = undoc;
        }

        public JMenu editMenu() {
            final JMenu ret = new JMenu("Edit");
            ret.setMnemonic('E');

            JMenuItem i = ret.add(new JMenuItem(undoc.editUndo));
            i.setText("Undo");
            i.setMnemonic('U');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    InputEvent.CTRL_MASK));

            i = ret.add(new JMenuItem(undoc.editRedo));
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

            i = ret.add(new JMenuItem(mainc.fileOpen));
            i.setText("Open");
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0,
                    InputEvent.CTRL_MASK));
            i.setMnemonic('O');

            ret.addSeparator();

            i = ret.add(new JMenuItem());
            i.setText("Save");
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    InputEvent.CTRL_MASK));
            i.setMnemonic('S');
            i.setEnabled(false);

            i = ret.add(new JMenuItem(mainc.fileSaveAs));
            i.setText("Save As");
            i.setMnemonic('A');

            ret.addSeparator();

            i = ret.add(new JMenuItem(mainc.fileScreenshot));
            i.setText("Screenshot");
            i.setMnemonic('c');
            i
                    .setAccelerator(KeyStroke.getKeyStroke(
                            KeyEvent.VK_PRINTSCREEN, 0));

            ret.addSeparator();

            i = ret.add(new JMenuItem(mainc.fileExit));
            i.setText("Exit");
            i.setMnemonic('x');

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
            mb.add(viewMenu());
            mb.add(helpMenu());
            return mb;
        }

        public JMenu viewMenu() {
            final JMenu ret = new JMenu("View");
            ret.setMnemonic('V');
            JMenuItem i = null;

            i = ret.add(new JMenuItem(zuic.ZoomHouse));
            i.setText("House");
            i.setMnemonic('H');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));

            i = ret.add(new JMenuItem(zuic.Zoom12Foot));
            i.setText("12-foot");
            i.setMnemonic('2');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));

            i = ret.add(new JMenuItem(zuic.ZoomComplete));
            i.setText("Complete");
            i.setMnemonic('C');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,
                    InputEvent.CTRL_MASK));

            i = ret.add(new JMenuItem(zuic.ZoomSheet));
            i.setText("Active");
            i.setMnemonic('A');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END,
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

            ret.addSeparator();

            i = ret.add(new JMenuItem());
            i.setText("North");
            i.setMnemonic('N');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
            i.setEnabled(false);

            i = ret.add(new JMenuItem());
            i.setText("South");
            i.setMnemonic('S');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
            i.setEnabled(false);

            i = ret.add(new JMenuItem());
            i.setText("West");
            i.setMnemonic('W');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
            i.setEnabled(false);

            i = ret.add(new JMenuItem());
            i.setText("East");
            i.setMnemonic('E');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
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
        final MainApp application = new MainApp();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                application
                        .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                application.pack();
                application.setSize(600, 800);
                application.setVisible(true);
                application.center();
            }
        });
        final long dt = 20;
        while (application.m.getCurrentTime() < tmax) {
            // application.repaint();
            try {
                Thread.sleep(dt);
            } catch (final InterruptedException e1) {
                break;
            }
            application.m.setCurrentTime(application.m.getCurrentTime() + dt
                    * 1e-3);
        }
    }

    private final MainMod m;
    private final MainController mainc;
    private final ZuiController zuic;

    public MainApp() {
        m = new MainMod();
        final BroomPromptModel bpm = new BroomPromptModel();
        final UndoRedoCon undoc = new UndoRedoCon();
        undoc.setModel(m.undo);
        bpm.setIdx16(14);
        final ZuiPanel zui = new ZuiPanel(m.undo);
        final JTree tree = new JTree();
        getContentPane().add(tree, "West");
        getContentPane().add(zui, "Center");
        mainc = new MainController(zui, m, this);
        zuic = new ZuiController(zui.pico.getCamera());
        setJMenuBar(new Menufactory(mainc, zuic, undoc).menu());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                mainc.shutDown();
            }
        });
        // connect the data models:
        zui.setCurveStore(m.getCurveStore());
        zui.setInitialPos(m.getInitialPos());
        zui.setCurrentPos(m.getCurrentPos());
        zui.setBroomPrompt(bpm);
    }

    private void center() {
        zuic.ZoomComplete.actionPerformed(null);
    }
}