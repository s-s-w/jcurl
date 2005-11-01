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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import jcurl.core.JCLoggerFactory;
import jcurl.core.gui.RockLocationDisplay;
import jcurl.core.gui.RockLocationDisplayBase;
import jcurl.core.gui.Zoomer;
import jcurl.core.io.SetupIO;
import jcurl.model.PositionSet;
import jcurl.model.RockSet;
import jcurl.model.SpeedSet;

import org.apache.ugli.ULogger;
import org.xml.sax.SAXException;

/**
 * A simple editor that brings all together.
 * 
 * @see jcurl.core.gui.RockLocationDisplay
 * @see jcurl.core.gui.demo1.LocationController
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: RockLocationDisplayDemo.java 135 2005-10-03 17:47:35Z
 *          mrohrmoser $
 */
public class EditorApp extends JFrame {

    public static class JCurlFileChooser extends JFileChooser {
        public JCurlFileChooser(File currentDirectory) {
            super(currentDirectory);
            this.setMultiSelectionEnabled(false);
            this.setAcceptAllFileFilterUsed(true);
            this.setFileFilter(new FileFilter() {
                public boolean accept(final File f) {
                    if (f == null)
                        return false;
                    return f.isDirectory() || f.getName().endsWith(".jcx")
                            || f.getName().endsWith(".jcz");
                }

                public String getDescription() {
                    return "JCurl Setup Files (.jcx) (.jcz)";
                }
            });
        }
    }

    private static final Cursor Cdefault = Cursor
            .getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    private static final Cursor Cwait = Cursor
            .getPredefinedCursor(Cursor.WAIT_CURSOR);

    private static final ULogger log = JCLoggerFactory
            .getLogger(EditorApp.class);

    public static void main(String[] args) {
        final EditorApp frame = new EditorApp();
        frame.cmdNew();
        frame.setVisible(true);
    }

    private File currentFile = null;

    private long lastSaved = 0;

    private final PositionSet mod_locations = new PositionSet();

    private final SpeedSet mod_speeds = new SpeedSet();

    public EditorApp() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cmdExit();
            }
        });
        final RockLocationDisplayBase pnl1 = new RockLocationDisplay(
                mod_locations, null, null, null);
        final RockLocationDisplayBase pnl2 = new RockLocationDisplay(
                mod_locations, Zoomer.HOG2HACK, null, null);

        final Container con = getContentPane();

        con.add(pnl1, "Center");
        con.add(new SumWaitDisplay(mod_locations), "West");
        con.add(new SumShotDisplay(mod_locations), "East");
        {
            final Box b = Box.createHorizontalBox();
            b.add(Box.createRigidArea(new Dimension(0, 75)));
            b.add(pnl2);
            con.add(b, "South");
        }

        setJMenuBar(createMenu());
        refreshTitle();
        setSize(900, 400);

        new SpeedController(mod_locations, mod_speeds, pnl1);
        new LocationController(mod_locations, pnl2);
    }

    boolean chooseLoadFile(final File def) {
        final JFileChooser fc = new JCurlFileChooser(def);
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            setCurrentFile(fc.getSelectedFile());
            return true;
        }
        return false;
    }

    boolean chooseSaveFile(final File def) {
        final JFileChooser fc = new JCurlFileChooser(def);
        if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this)) {
            setCurrentFile(fc.getSelectedFile());
            return true;
        }
        return false;
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
            speed.getDark(0).setLocation(0.1, -1.325, 0.75);
            // feed the model
            RockSet.copy(pos, mod_locations);
            RockSet.copy(speed, mod_speeds);

            mod_locations.notifyChange();
        } finally {
            setCursor(Cdefault);
        }
    }

    void cmdOpen() throws FileNotFoundException, SAXException, IOException {
        if (!chooseLoadFile(getCurrentFile() == null ? new File(".")
                : getCurrentFile()))
            return;
        SetupIO.load(getCurrentFile(), mod_locations, null, null, null);
    }

    void cmdSave() throws SAXException, IOException {
        if (getCurrentFile() == null) {
            if (!chooseSaveFile(new File(".")))
                return;
        }
        save(getCurrentFile(), mod_locations);
    }

    void cmdSaveAs() throws SAXException, IOException {
        if (!chooseSaveFile(getCurrentFile() == null ? new File(".")
                : getCurrentFile()))
            return;
        save(getCurrentFile(), mod_locations);
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

    public File getCurrentFile() {
        return currentFile;
    }

    private void load(final File f, final PositionSet pos)
            throws FileNotFoundException, SAXException, IOException {
        SetupIO.load(f, pos, null, null, null);
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
     * @return the new menu item
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

    private void refreshTitle() {
        setTitle(getClass().getName() + " - "
                + (currentFile == null ? "" : currentFile.getAbsolutePath()));
    }

    private void save(File f, PositionSet pos) throws SAXException, IOException {
        SetupIO.save(f, mod_locations, null, null, null);
        lastSaved = System.currentTimeMillis();
        refreshTitle();
    }

    public void setCurrentFile(File currentFile) {
        if (currentFile == null || currentFile.getName().endsWith(".jcx"))
            this.currentFile = currentFile;
        else
            this.currentFile = new File(currentFile.getName() + ".jcx");
        refreshTitle();
    }
}