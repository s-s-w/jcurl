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

import java.awt.BorderLayout;
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
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.Zoomer;
import org.jcurl.core.helpers.Version;
import org.jcurl.core.io.SetupIO;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.swing.AboutDialog;
import org.jcurl.core.swing.JcxFileChooser;
import org.jcurl.core.swing.PngFileChooser;
import org.jcurl.core.swing.PositionDisplay;
import org.jcurl.core.swing.RockEditDisplay;
import org.jcurl.core.swing.SumOutDisplay;
import org.jcurl.core.swing.SumShotDisplay;
import org.jcurl.core.swing.SumWaitDisplay;
import org.xml.sax.SAXException;

/**
 * A simple editor that brings all together.
 * 
 * @see org.jcurl.core.swing.PositionDisplay
 * @see org.jcurl.demo.editor.LocationController
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: RockLocationDisplayDemo.java 135 2005-10-03 17:47:35Z
 *          mrohrmoser $
 */
public class TacticsApp extends JFrame {

    private static final long serialVersionUID = 2586556187989718873L;

    private static final Cursor Cdefault = Cursor
            .getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    private static final Cursor Cwait = Cursor
            .getPredefinedCursor(Cursor.WAIT_CURSOR);

    private static final Log log = JCLoggerFactory.getLogger(TacticsApp.class);

    /**
     * @param name
     * @param icon
     * @param executor
     * @param action
     * @return the generated action
     */
    private static AbstractAction createAction(final String name,
            final Icon icon, final Object executor, final String action) {
        return new AbstractAction(name, icon) {
            private static final long serialVersionUID = -6445283333165071107L;

            private Method m = null;

            public void actionPerformed(final ActionEvent evt) {
                if (m == null)
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
                try {
                    m.invoke(executor, null);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public static void main(String[] args) throws SAXException, IOException {
        log.info("Version: " + Version.find());
        final TacticsApp frame = new TacticsApp();
        frame.cmdNew();
        frame.load(args);
        frame.setVisible(true);
    }

    private JDialog about = null;

    private File currentFile = null;

    private long lastSaved = 0;

    private final RockEditDisplay master;

    private final PositionSet mod_locations = new PositionSet();

    private final SpeedSet mod_speeds = new SpeedSet();

    public TacticsApp() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                TacticsApp.this.cmdExit();
            }
        });
        master = new RockEditDisplay();
        master.setPos(mod_locations);
        master.setSpeed(mod_speeds);
        final PositionDisplay pnl2 = new PositionDisplay();
        pnl2.setPos(mod_locations);
        pnl2.setZoom(Zoomer.HOG2HACK);

        final Container con = getContentPane();
        {
            final JPanel p = new JPanel(new BorderLayout());
            p.add(new SumWaitDisplay(mod_locations), "West");
            p.add(master, "Center");
            p.add(new SumShotDisplay(mod_locations), "East");
            con.add(p, "Center");
        }
        // con.add(new SumWaitDisplay(mod_locations), "West");
        con.add(new SumOutDisplay(mod_locations), "West");
        {
            final Box b1 = Box.createHorizontalBox();
            b1.add(Box.createRigidArea(new Dimension(0, 75)));
            b1.add(pnl2);
            con.add(b1, "South");
        }
        final JTabbedPane t = new JTabbedPane();
        con.add(t, "East");
        {
            final Box b0 = Box.createHorizontalBox();
            t.add("Rock", b0);
            {
                final JPanel b1 = new JPanel(new BorderLayout());
                final Box b2 = Box.createVerticalBox();
                b2.add(new JComboBox(new String[] { "Dark", "Light" }));
                b2.add(new JLabel("Broom", SwingConstants.LEFT));
                b1.add(b2, "North");
                JSlider s = new JSlider(-2000, 2000, 0);
                s.setOrientation(SwingConstants.VERTICAL);
                s.setMajorTickSpacing(1000);
                s.setMinorTickSpacing(100);
                s.setPaintLabels(true);
                s.setPaintTicks(true);
                b1.add(s, "Center");

                final Box b3 = Box.createHorizontalBox();
                b3.add(new JFormattedTextField());
                b3.add(new JLabel("mm", SwingConstants.LEFT));
                b1.add(b3, "South");
                b0.add(b1);
            }
            {
                final JPanel b1 = new JPanel(new BorderLayout());
                final Box b2 = Box.createVerticalBox();
                b2.add(new JComboBox(new String[] { "1", "2", "3", "4", "5",
                        "6", "7", "8" }));
                b2.add(new JLabel("Splittime", SwingConstants.LEFT));
                b1.add(b2, "North");
                JSlider s = new JSlider(500, 2500, 1500);
                s.setOrientation(SwingConstants.VERTICAL);
                s.setMajorTickSpacing(1000);
                s.setMinorTickSpacing(100);
                s.setPaintLabels(true);
                s.setPaintTicks(true);
                b1.add(s, "Center");

                final Box b3 = Box.createHorizontalBox();
                b3.add(new JSpinner());
                b3.add(new JLabel("ms", SwingConstants.LEFT));
                b1.add(b3, "South");
                b0.add(b1);
            }
        }
        {
            final Box b0 = Box.createHorizontalBox();
            t.add("Ice", b0);
            {
                final JPanel b1 = new JPanel(new BorderLayout());
                b1.add(new JLabel("Curl"), "North");
                JSlider s = new JSlider(0, 5000, 0);
                s.setOrientation(SwingConstants.VERTICAL);
                s.setMajorTickSpacing(1000);
                s.setMinorTickSpacing(100);
                s.setPaintLabels(true);
                s.setPaintTicks(true);
                b1.add(s, "Center");

                final JSpinner s1 = new JSpinner();
                b1.add(s1, "South");
                b0.add(b1);
            }
            {
                final JPanel b1 = new JPanel(new BorderLayout());
                b1.add(new JLabel("DrawToTee"), "North");
                JSlider s = new JSlider(15000, 30000, 25000);
                s.setOrientation(SwingConstants.VERTICAL);
                s.setMajorTickSpacing(5000);
                s.setMinorTickSpacing(1000);
                s.setPaintLabels(true);
                s.setPaintTicks(true);
                b1.add(s, "Center");

                final JSpinner s1 = new JSpinner();
                b1.add(s1, "South");
                b0.add(b1);
            }
        }
        setJMenuBar(createMenu());
        refreshTitle();
        this.setSize(900, 400);

        new SpeedController(mod_locations, mod_speeds, master);
        new LocationController(mod_locations, pnl2);
        lastSaved = mod_locations.getLastChanged();
    }

    boolean chooseLoadFile(final File def) {
        final JFileChooser fc = new JcxFileChooser(def);
        if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
            setCurrentFile(fc.getSelectedFile());
            return true;
        }
        return false;
    }

    boolean chooseSaveFile(final File def) {
        final JFileChooser fc = new JcxFileChooser(def);
        if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this)) {
            setCurrentFile(fc.getSelectedFile());
            return true;
        }
        return false;
    }

    void cmdAbout() {
        log.info("");
        if (about == null)
            about = new AboutDialog(this);
        about.setVisible(true);
    }

    void cmdExit() {
        if (!discardUnsavedChanges())
            return;
        System.exit(0);
    }

    void cmdExportPng() throws IOException {
        log.debug("-");
        final JFileChooser fc = new PngFileChooser(currentFile);
        if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this)) {
            final File dst = fc.getSelectedFile();
            try {
                this.setCursor(Cwait);
                master.exportPng(dst);
            } finally {
                this.setCursor(Cdefault);
            }
        }
    }

    void cmdNew() {
        if (!discardUnsavedChanges())
            return;
        try {
            this.setCursor(Cwait);
            // initial state
            PositionSet.allHome(mod_locations);
            RockSet.allZero(mod_speeds);
            lastSaved = System.currentTimeMillis();
        } finally {
            this.setCursor(Cdefault);
        }
    }

    void cmdOpen() throws FileNotFoundException, SAXException, IOException {
        if (!discardUnsavedChanges())
            return;
        // try {
        // final FileOpenService fos = (FileOpenService) ServiceManager
        // .lookup("javax.jnlp.BasicService");
        // final FileContents fc = fos.openFileDialog("/home/m", new
        // String[]{".jcx", ".jcz"});
        // if(fc != null)
        // log.info(fc.getName());
        // } catch (UnavailableServiceException e) {
        // throw new RuntimeException("Uncaught exception", e);
        // }
        if (!chooseLoadFile(getCurrentFile() == null ? new File(".")
                : getCurrentFile()))
            return;
        SetupIO.load(getCurrentFile(), mod_locations, mod_speeds, null, null);
        lastSaved = System.currentTimeMillis();
    }

    void cmdSave() throws SAXException, IOException {
        if (getCurrentFile() == null)
            if (!chooseSaveFile(new File(".")))
                return;
        save(getCurrentFile());
    }

    void cmdSaveAs() throws SAXException, IOException {
        if (!chooseSaveFile(getCurrentFile() == null ? new File(".")
                : getCurrentFile()))
            return;
        save(getCurrentFile());
    }

    void cmdZoom() {
        log.info("");
    }

    private JMenuBar createMenu() {
        final JMenuBar bar = new JMenuBar();
        {
            final JMenu menu = bar.add(new JMenu("File"));
            menu.setMnemonic('F');
            menu.add(this.newMI("New", null, 'N', -1, this, "cmdNew"));
            menu.add(this.newMI("Open", null, 'O', KeyEvent.VK_O, this,
                    "cmdOpen"));
            menu.addSeparator();
            menu.add(this.newMI("Export Png", null, 'P', KeyEvent.VK_E, this,
                    "cmdExportPng"));
            menu.addSeparator();
            menu.add(this.newMI("Save", null, 'S', KeyEvent.VK_S, this,
                    "cmdSave"));
            menu.add(this.newMI("Save As", null, 'A', -1, this, "cmdSaveAs"));
            menu.addSeparator();
            menu.add(this.newMI("Exit", null, 'x', -1, this, "cmdExit"));
        }
        {
            final JMenu menu = bar.add(new JMenu("View"));
            menu.setMnemonic('V');
            menu.add(this.newMI("Zoom", null, 'z', -1, this, "cmdZoom"));
        }
        {
            final JMenu menu = bar.add(new JMenu("Play"));
            menu.setMnemonic('P');
            menu.setEnabled(false);
            // menu.add(newMI('a', -1, bStart.getAction()));
            // menu.add(newMI('P', -1, bPause.getAction()));
            // menu.add(newMI('o', -1, bStop.getAction()));
        }
        {
            final JMenu menu = bar.add(new JMenu("Help"));
            menu.setMnemonic('H');
            menu.add(this.newMI("About", null, 'a', -1, this, "cmdAbout"));
        }
        return bar;
    }

    private boolean discardUnsavedChanges() {
        if (mod_locations.getLastChanged() <= lastSaved
                && mod_speeds.getLastChanged() <= lastSaved)
            return true;
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
                "Discard unsaved changes?", "Warning",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
            return true;
        return false;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    private void load(final File f, final PositionSet pos)
            throws FileNotFoundException, SAXException, IOException {
        SetupIO.load(f, pos, null, null, null);
    }

    private void load(final String[] args) throws SAXException, IOException {
        if (args.length <= 0)
            return;
        final File f = new File(args[0]);
        if (!f.exists())
            return;
        this.load(f, mod_locations);
    }

    private JMenuItem newMI(final char mnemonic, final int ctrlAccel,
            final Action action) {
        final JMenuItem item = new JMenuItem(action);
        item.setMnemonic(mnemonic);
        if (ctrlAccel >= 0)
            item.setAccelerator(KeyStroke.getKeyStroke(ctrlAccel,
                    InputEvent.CTRL_MASK));
        return item;
    }

    private JMenuItem newMI(final String name, final Icon icon,
            final char mnemonic, final int ctrlAccel, final Object executor,
            final String action) {
        return this.newMI(mnemonic, ctrlAccel, createAction(name, icon,
                executor, action));
    }

    private void refreshTitle() {
        setTitle(getClass().getName() + " - "
                + (currentFile == null ? "" : currentFile.getAbsolutePath()));
    }

    private void save(File f) throws SAXException, IOException {
        SetupIO.save(f, mod_locations, mod_speeds, null, null);
        lastSaved = System.currentTimeMillis();
        refreshTitle();
    }

    public void setCurrentFile(File currentFile) {
        if (currentFile == null || currentFile.getName().endsWith(".jcx")
                || currentFile.getName().endsWith(".jcz"))
            this.currentFile = currentFile;
        else
            this.currentFile = new File(currentFile.getName() + ".jcx");
        refreshTitle();
    }
}