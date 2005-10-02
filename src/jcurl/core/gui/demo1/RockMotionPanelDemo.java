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
package jcurl.core.gui.demo1;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jcurl.core.JCLoggerFactory;
import jcurl.core.PositionSet;
import jcurl.core.RockSet;
import jcurl.core.SpeedSet;
import jcurl.core.gui.RockMotionPanel;

import org.apache.ugli.ULogger;

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

    private static final Cursor Cdefault = Cursor
            .getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    private static final Cursor Cwait = Cursor
            .getPredefinedCursor(Cursor.WAIT_CURSOR);

    private static final ULogger log = JCLoggerFactory
            .getLogger(RockMotionPanelDemo.class);

    public static void main(String[] args) {
        final RockMotionPanelDemo frame = new RockMotionPanelDemo();
        frame.cmdNew();
        frame.show();
    }

    private final PositionSet model = new PositionSet();

    public RockMotionPanelDemo() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cmdExit();
            }
        });
        final RockMotionPanel panel = new RockMotionPanel(model, null, null,
                null);

        final Container con = getContentPane();
        con.add(panel, "Center");
        con.add(new RockSumPanel(model), "West");
        con.add(new RockSumPanel(model), "East");
        con.add(new JButton("Exit"), "South");
        setJMenuBar(createMenu());
        setTitle(this.getClass().getName());
        setSize(900, 400);

        new SetupController(model, panel);
    }

    void cmdAbout() {
        log.info("");
    }

    void cmdExit() {
        System.exit(0);
    }

    void cmdNew() {
        try {
            setCursor(Cwait);
            // initial state
            final PositionSet pos = PositionSet.allOut();
            pos.getDark(0).setLocation(0, 5, 0);
            pos.getDark(1).setLocation(0, 0, 0.5);
            pos.getLight(0).setLocation(0.2, 2.5);
            pos.getLight(1).setLocation(1.0, 1.5);
            final SpeedSet speed = new SpeedSet();
            speed.getDark(0).setLocation(0, -1.325, 0.75);
            // feed the model
            RockSet.copy(pos, model);
            model.notifyChange();
        } finally {
            setCursor(Cdefault);
        }
    }

    void cmdOpen() {
        log.info("");
    }

    void cmdSave() {
        log.info("");
    }

    void cmdSaveAs() {
        log.info("");
    }

    void cmdZoom() {
        log.info("");
    }

    private JMenuBar createMenu() {
        final JMenuBar bar = new JMenuBar();
        {
            final JMenu menu = bar.add(new JMenu("File"));
            menu.setMnemonic('F');
            menu.add(newMI("New", null, 'N', -1, this, "cmdNew"));
            menu.add(newMI("Open", null, 'O', KeyEvent.VK_O, this, "cmdOpen"));
            menu.addSeparator();
            menu.add(newMI("Save", null, 'S', KeyEvent.VK_S, this, "cmdSave"));
            menu.add(newMI("Save As", null, 'A', -1, this, "cmdSaveAs"));
            menu.addSeparator();
            menu.add(newMI("Exit", null, 'x', -1, this, "cmdExit"));
        }
        {
            final JMenu menu = bar.add(new JMenu("View"));
            menu.setMnemonic('V');
            menu.add(newMI("Zoom", null, 'z', -1, this, "cmdZoom"));
        }
        {
            final JMenu menu = bar.add(new JMenu("Help"));
            menu.setMnemonic('H');
            menu.add(newMI("About", null, 'a', -1, this, "cmdAbout"));
        }
        return bar;
    }

    /**
     * Create a new menu item.
     * 
     * @param name
     * @param icon
     * @param mnemonic
     * @param ctrlAccel
     * @param executor
     * @param action
     * 
     * @return
     */
    private JMenuItem newMI(final String name, final Icon icon,
            final char mnemonic, final int ctrlAccel, final Object executor,
            final String action) {
        final JMenuItem item = new JMenuItem(new AbstractAction(name, icon) {
            private Method m = null;

            public void actionPerformed(final ActionEvent evt) {
                if (m == null) {
                    try {
                        m = executor.getClass().getMethod(action, null);
                    } catch (Exception e) {
                        try {
                            m = executor.getClass().getDeclaredMethod(action,
                                    null);
                        } catch (SecurityException e1) {
                            throw new RuntimeException(e1);
                        } catch (NoSuchMethodException e1) {
                            throw new RuntimeException(e1);
                        }
                    }
                }
                try {
                    m.invoke(executor, null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        item.setMnemonic(mnemonic);
        if (ctrlAccel >= 0)
            item.setAccelerator(KeyStroke.getKeyStroke(ctrlAccel,
                    InputEvent.CTRL_MASK));
        return item;
    }
}