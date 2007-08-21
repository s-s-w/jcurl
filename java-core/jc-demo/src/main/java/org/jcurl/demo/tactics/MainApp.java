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

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MainApp extends JFrame {

    static class Menufactory {
        private final MainMod model;

        public Menufactory(final MainMod model) {
            this.model = model;
        }

        public JMenu editMenu() {
            final JMenu ret = new JMenu("Edit");
            ret.setMnemonic('E');

            JMenuItem i = ret.add(new JMenuItem(new UndoRedoAction(model
                    .getUndoer(), true)));
            i.setText("Undo");
            i.setMnemonic('U');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                    InputEvent.CTRL_MASK));

            i = ret.add(new JMenuItem(new UndoRedoAction(model.getUndoer(),
                    false)));
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

            JMenuItem i = ret.add(new JMenuItem());
            i.setText("Sheet");
            i.setMnemonic('S');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
            i.setEnabled(false);

            i = ret.add(new JMenuItem());
            i.setText("House");
            i.setMnemonic('H');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
            i.setEnabled(false);

            i = ret.add(new JMenuItem());
            i.setText("12-foot");
            i.setMnemonic('1');
            i.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_END,
                    InputEvent.CTRL_MASK));
            i.setEnabled(false);

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

    /**
     * http://www.javaworld.com/javaworld/jw-06-1998/jw-06-undoredo.html
     * 
     * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
     * @version $Id$
     */
    static class UndoRedoAction extends AbstractAction {
        private static final Log log = LogFactory.getLog(UndoRedoAction.class);
        private static final long serialVersionUID = -4468864007790523205L;
        private final boolean undo;
        private final UndoManager undoer;

        public UndoRedoAction(final UndoManager undoer, final boolean undo) {
            this.undoer = undoer;
            this.undo = undo;
        }

        public void actionPerformed(final ActionEvent e) {
            try {
                if (undo)
                    undoer.undo();
                else
                    undoer.redo();
            } catch (final RuntimeException ex) {
                log.error("", ex);
            }
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

    private final MainMod m;

    private final MainPanel p;

    public MainApp() {
        m = new MainMod();
        m.setCurrentTime(tmax);
        p = new MainPanel(m);
        getContentPane().add(p);
        setJMenuBar(new Menufactory(m).menu());
    }

    public void center() {
        p.center();
    }
}