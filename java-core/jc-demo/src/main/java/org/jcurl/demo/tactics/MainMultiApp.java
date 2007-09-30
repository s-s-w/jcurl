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

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcurl.core.base.JCurlSerializer;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.helpers.AnnoHelp;
import org.jcurl.core.io.XStreamSerializer;
import org.jcurl.core.model.CurveManager;

public class MainMultiApp extends JFrame {

    public static class Controller implements ChangeListener,
            UndoableEditListener {
        private static final Log log = LogFactory.getLog(Controller.class);

        public final Action aEditProperties = null;

        // http://www.javaworld.com/javaworld/jw-06-1998/jw-06-undoredo.html
        public final Action aEditRedo = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                final UndoRedoDocument um = getUndoer();
                if (um != null)
                    um.redo();
            }
        };

        // http://www.javaworld.com/javaworld/jw-06-1998/jw-06-undoredo.html
        public final Action aEditUndo = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                final UndoRedoDocument um = getUndoer();
                if (um != null)
                    um.undo();
            }
        };

        public final Action aFileClear = new AbstractAction() {
            private static final long serialVersionUID = -4623064479688132611L;

            public void actionPerformed(final ActionEvent e) {
                model.clear();
                desk.removeAll();
            }
        };

        public final Action aFileExit = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                view.setCursor(cWait);
                view.setVisible(false);
                System.exit(0);
            }
        };

        public final Action aFileOpen = new AbstractAction() {
            private static final long serialVersionUID = -5150063929213996925L;

            public void actionPerformed(final ActionEvent e) {
                if (JFileChooser.APPROVE_OPTION == zJcx.showOpenDialog(view))
                    try {
                        view.setCursor(cWait);
                        for (final CurveManager elem : model.load(zJcx
                                .getSelectedFile()))
                            addWindow(elem);
                    } finally {
                        view.setCursor(cDefa);
                    }
            }
        };

        public final Action aFileSave = new AbstractAction() {
            private static final long serialVersionUID = -5150063929213996925L;

            public void actionPerformed(final ActionEvent e) {
                try {
                    if (model.currentFile != null) {
                        view.setCursor(cWait);
                        model.save(model.currentFile);
                        return;
                    }
                    if (JFileChooser.APPROVE_OPTION == zJcx
                            .showSaveDialog(view)) {
                        view.setCursor(cWait);
                        model.save(zJcx.getSelectedFile());
                        return;
                    }
                } finally {
                    view.setCursor(cDefa);
                }
            }
        };

        public final Action aFileSaveAs = new AbstractAction() {
            private static final long serialVersionUID = -5150063929213996925L;

            public void actionPerformed(final ActionEvent e) {
                if (JFileChooser.APPROVE_OPTION == zJcx.showSaveDialog(view))
                    try {
                        view.setCursor(cWait);
                        model.save(zJcx.getSelectedFile());
                    } finally {
                        view.setCursor(cDefa);
                    }
            }

        };

        public final Action aHelpAbout = null;

        public final Action aView12Foot = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                zoom(TacticsPanel.twelveP);
            }
        };

        public final Action aViewComplete = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                zoom(TacticsPanel.completeP);
            }
        };

        public final Action aViewHouse = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                zoom(TacticsPanel.houseP);
            }
        };

        public final Action aViewSheet = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                zoom(TacticsPanel.sheetP);
            }
        };

        public final Action aWindowAdd = new AbstractAction() {
            private static final long serialVersionUID = -4623064479688132611L;

            public void actionPerformed(final ActionEvent e) {
                addWindow(MainMod.initHammy(null));
            }
        };

        public final Action aWindowDeleteCurrent = new AbstractAction() {
            private static final long serialVersionUID = -4623064479688132611L;

            public void actionPerformed(final ActionEvent e) {
                deleteCurrentWindow();
            }
        };

        public final Action aWindowScreenshot = new AbstractAction() {
            private static final long serialVersionUID = -4623064479688132611L;

            public void actionPerformed(final ActionEvent e) {
                log.debug("-");
                if (JFileChooser.APPROVE_OPTION == zPng.showSaveDialog(view))
                    try {
                        view.setCursor(cWait);
                        exportPng(getCurrentWindow(), zPng.getSelectedFile());
                    } catch (final Exception e1) {
                        log.error("", e1);
                    } finally {
                        view.setCursor(cDefa);
                    }
            }
        };

        private final Cursor cDefa = new Cursor(Cursor.DEFAULT_CURSOR);
        private final Cursor cWait = new Cursor(Cursor.WAIT_CURSOR);

        private final JTabbedPane desk;
        public final JMenu editMenu;
        private final FileController fico;
        private final Model<TrajectorySet> model = new Model<TrajectorySet>();
        private final MainMultiApp view;
        public final JMenu viewMenu;

        private final JFileChooser zJcx = new JFileChooser(model
                .getCurrentFile()) {
            private static final long serialVersionUID = -5499800990902865897L;
            {
                setName("Open");
                setMultiSelectionEnabled(false);
                setAcceptAllFileFilterUsed(true);
                setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(final File f) {
                        if (f == null)
                            return false;
                        return f.isDirectory() || f.getName().endsWith(".jcx")
                                || f.getName().endsWith(".jcz");
                    }

                    @Override
                    public String getDescription() {
                        return "JCurl Setup Files (.jcx) (.jcz)";
                    }
                });
            }
        };

        private final JFileChooser zPng = new JFileChooser((File) null) {
            private static final long serialVersionUID = -7839467661670577073L;
            {
                setName("Save Screenshot");
                setMultiSelectionEnabled(false);
                setAcceptAllFileFilterUsed(true);
                setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(final File f) {
                        if (f == null)
                            return false;
                        return f.isDirectory() || f.getName().endsWith(".png");
                    }

                    @Override
                    public String getDescription() {
                        return "Png Bitmap Images (.png)";
                    }
                });
            }
        };

        public Controller(final MainMultiApp frame) {
            view = frame;
            view.setCursor(cWait);

            // Set up the view:
            view.setTitle("JCurl Tactics Demo");
            {
                final JMenuBar mb = new JMenuBar();
                final Menufactory mf = new Menufactory(this);
                mb.add((fico = new FileController(this)).menu());
                mb.add(mf.fileMenu());
                mb.add(mf.windowMenu());
                mb.add(editMenu = mf.editMenu());
                mb.add(viewMenu = mf.viewMenu());
                mb.add(mf.helpMenu());
                view.setJMenuBar(mb);
            }
            desk = new JTabbedPane(SwingConstants.TOP);
            desk.addChangeListener(this);
            view.getContentPane().add(desk);
            view.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            view.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(final WindowEvent e) {
                    aFileExit.actionPerformed(null);
                }
            });
            view.setCursor(cDefa);
            stateChanged(new ChangeEvent(desk));
        }

        public void _fileClear() {
            try {
                view.setCursor(cWait);
                model.clear();
                desk.removeAll();
                fico.findAction("doSave").setEnabled(false);
                fico.findAction("doSaveAs").setEnabled(false);
            } finally {
                view.setCursor(cDefa);
            }
        }

        public void _fileExit() {
            view.setCursor(cWait);
            view.setVisible(false);
            System.exit(0);
        }

        public void _fileOpen() {
            try {
                view.setCursor(cWait);
                if (JFileChooser.APPROVE_OPTION == zJcx.showOpenDialog(view))
                    try {
                        view.setCursor(cWait);
                        for (final CurveManager elem : model.load(zJcx
                                .getSelectedFile()))
                            addWindow(elem);
                    } finally {
                        view.setCursor(cDefa);
                    }
            } finally {
                view.setCursor(cDefa);
            }
        }

        public void _fileSave() {
            try {
                if (model.currentFile != null) {
                    view.setCursor(cWait);
                    model.save(model.currentFile);
                    return;
                }
                if (JFileChooser.APPROVE_OPTION == zJcx.showSaveDialog(view)) {
                    view.setCursor(cWait);
                    model.save(zJcx.getSelectedFile());
                    return;
                }
            } finally {
                view.setCursor(cDefa);
            }
        }

        public void _fileSaveAs() {
            if (JFileChooser.APPROVE_OPTION == zJcx.showSaveDialog(view))
                try {
                    view.setCursor(cWait);
                    model.save(zJcx.getSelectedFile());
                } finally {
                    view.setCursor(cDefa);
                }
        }

        public void addWindow(final CurveManager elem) {
            model.add(elem);
            final TacticsPanel c = new TacticsPanel(elem);
            desk.add(buildTitle(elem.getAnnotations()), c);
            desk.setSelectedComponent(c);
            c.getModel().addUndoableEditListener(this);
            zoom(TacticsPanel.houseP);
        }

        public String buildTitle(final Map<String, Object> m) {
            final StringBuffer buf = new StringBuffer();
            final String[] keys = { AnnoHelp.DateK, AnnoHelp.EventK,
                    AnnoHelp.GameK, AnnoHelp.EndK };
            for (final String key : keys) {
                final Object val = m.get(key);
                if (val == null || !(val instanceof CharSequence))
                    continue;
                buf.append(val.toString()).append(" ");
            }
            return buf.toString();
        }

        public void deleteCurrentWindow() {
            view.setCursor(cWait);
            final TacticsPanel t = getCurrentWindow();
            model.remove(t.getModel().getModel());
            t.getModel().removeUndoableEditListener(this);
            desk.remove(t);
            view.setCursor(cDefa);
        }

        private void exportPng(final JComponent src, File dst)
                throws IOException {
            if (src == null || dst == null)
                return;
            final BufferedImage img = new BufferedImage(src.getWidth(), src
                    .getHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics g = img.getGraphics();
            try {
                src.paintAll(g);
            } finally {
                g.dispose();
            }
            if (!dst.getName().endsWith(".png"))
                dst = new File(dst.getAbsoluteFile() + ".png");
            ImageIO.write(img, "png", dst);
        }

        private TacticsPanel getCurrentWindow() {
            return (TacticsPanel) desk.getSelectedComponent();
        }

        private UndoRedoDocument getUndoer() {
            return getCurrentWindow() == null ? null : getCurrentWindow()
                    .getModel();
        }

        public void stateChanged(final ChangeEvent e) {
            log.info(e);
            if (e.getSource() == desk) {
                final boolean active = desk.getSelectedIndex() >= 0;
                editMenu.setEnabled(active);
                aWindowDeleteCurrent.setEnabled(active);
                aWindowScreenshot.setEnabled(active);
                viewMenu.setEnabled(active);
                aFileSave.setEnabled(active);
                aFileSaveAs.setEnabled(active);
                // view12Foot.setEnabled(active);
                // viewComplete.setEnabled(active);
                // viewHouse.setEnabled(active);
                // viewSheet.setEnabled(active);
                undoableEditHappened(null);
            }
        }

        public void undoableEditHappened(final UndoableEditEvent e) {
            log.info(e);
            final UndoRedoDocument um = getUndoer();
            if (um != null) {
                aEditUndo.setEnabled(um.canUndo());
                aEditRedo.setEnabled(um.canRedo());
            } else {
                aEditUndo.setEnabled(false);
                aEditRedo.setEnabled(false);
            }
        }

        private void zoom(final Rectangle2D r) {
            final TacticsPanel t = getCurrentWindow();
            if (t != null)
                t.zoom(r, 333);
        }
    }

    static class Menufactory {
        static final JMenuItem mi(final Action a, final String text,
                final int mnemonic) {
            return mi(a, text, mnemonic, null);
        }

        static final JMenuItem mi(final Action a, final String text,
                final int mnemonic, final String accelerator) {
            if (a == null) {
                final JMenuItem ret = new JMenuItem(text);
                if (mnemonic > 0)
                    ret.setMnemonic(mnemonic);
                if (accelerator != null)
                    ret.setAccelerator(KeyStroke.getKeyStroke(accelerator));
                ret.setEnabled(false);
                return ret;
            }
            a.putValue(Action.NAME, text);
            if (mnemonic > 0)
                a.putValue(Action.MNEMONIC_KEY, mnemonic);
            if (accelerator != null)
                a.putValue(Action.ACCELERATOR_KEY, KeyStroke
                        .getKeyStroke(accelerator));
            return new JMenuItem(a);
        }

        private final Controller mc;

        public Menufactory(final Controller mainc) {
            mc = mainc;
        }

        public JMenu editMenu() {
            final JMenu m = new JMenu("Edit");
            m.setMnemonic('E');
            m.add(mi(mc.aEditUndo, "Undo", KeyEvent.VK_U, "ctrl Z"));
            m.add(mi(mc.aEditRedo, "Redo", KeyEvent.VK_R, "ctrl R"));
            m.addSeparator();
            m.add(mi(mc.aEditProperties, "Properties", KeyEvent.VK_P));
            return m;
        }

        public JMenu fileMenu() {
            final JMenu m = new JMenu("File");
            m.setMnemonic('F');
            m.add(mi(mc.aFileClear, "Clear", KeyEvent.VK_C));
            m.add(mi(mc.aFileOpen, "Open", KeyEvent.VK_O, "ctrl O"));
            m.addSeparator();
            m.add(mi(mc.aFileSave, "Save", KeyEvent.VK_S, "ctrl S"));
            m.add(mi(mc.aFileSaveAs, "Save As", KeyEvent.VK_A));
            m.addSeparator();
            m.add(mi(mc.aFileExit, "Exit", KeyEvent.VK_X));
            return m;
        }

        public JMenu helpMenu() {
            final JMenu ret = new JMenu("Help");
            ret.setMnemonic('H');
            ret.add(mi(mc.aHelpAbout, "About", KeyEvent.VK_A));
            return ret;
        }

        public JMenu viewMenu() {
            final JMenu m = new JMenu("View");
            m.setMnemonic('V');
            m.add(mi(mc.aViewComplete, "Complete", KeyEvent.VK_C, "ctrl END"));
            m.add(mi(mc.aViewSheet, "Active", KeyEvent.VK_A, "ctrl HOME"));
            m.add(mi(mc.aViewHouse, "House", KeyEvent.VK_H, "END"));
            m.add(mi(mc.aView12Foot, "12-foot", KeyEvent.VK_2, "HOME"));
            m.addSeparator();
            m.add(mi(null, "In", KeyEvent.VK_I, "PLUS"));
            m.add(mi(null, "Out", KeyEvent.VK_O, "MINUS"));
            m.addSeparator();
            m.add(mi(null, "North", KeyEvent.VK_N, "UP"));
            m.add(mi(null, "South", KeyEvent.VK_S, "DOWN"));
            m.add(mi(null, "West", KeyEvent.VK_W, "LEFT"));
            m.add(mi(null, "East", KeyEvent.VK_E, "RIGHT"));
            return m;
        }

        public JMenu windowMenu() {
            final JMenu m = new JMenu("Window");
            m.setMnemonic('W');
            m.add(mi(mc.aWindowAdd, "Add New", KeyEvent.VK_N, "ctrl N"));
            m.add(mi(mc.aWindowDeleteCurrent, "Delete", KeyEvent.VK_D));
            m.addSeparator();
            m.add(mi(mc.aWindowScreenshot, "Screenshot", KeyEvent.VK_C,
                    "ctrl P"));
            return m;
        }
    }

    public static class Model<T extends TrajectorySet> {
        private static final Log log = LogFactory.getLog(Controller.class);

        private File currentFile = null;

        private final List<TrajectorySet> list = new ArrayList<TrajectorySet>();

        public T add(final T t) {
            list.add(t);
            return t;
        }

        public T addNew() {
            return null;
        }

        public void clear() {
            list.clear();
            currentFile = null;
        }

        public File getCurrentFile() {
            return currentFile;
        }

        public Iterable<CurveManager> load(final File src) {
            final CurveManager ts = new CurveManager();
            if (src.getName().endsWith(".jcz")
                    || src.getName().endsWith(".jcx")) {
                log.debug(src);
                final JCurlSerializer xs = new XStreamSerializer();
                final Collection<CurveManager> ret = new ArrayList<CurveManager>();
                try {
                    xs.read(src.toURL(), xs.wrap(null, ts));
                    ret.add(ts);
                } catch (final IOException e) {
                    log.error("", e);
                }
                currentFile = src;
                return ret;
            }
            return load(new File(src.getAbsoluteFile() + ".jcz"));
        }

        public void remove(final T t) {
        }

        public void save(final File dst) {
            if (dst.getName().endsWith(".jcz")
                    || dst.getName().endsWith(".jcx")) {
                log.debug(dst);
                final JCurlSerializer xs = new XStreamSerializer();
                try {
                    xs.write(xs.wrap(null, list), dst);
                } catch (final IOException e) {
                    log.error("", e);
                }
                currentFile = dst;
                return;
            }
            save(new File(dst.getAbsoluteFile() + ".jcz"));
        }
    }

    private static final long serialVersionUID = 5907776225462882883L;

    public static void main(final String[] args) {
        // PDebug.debugBounds = true;
        // PDebug.debugPrintUsedMemory = true;
        // PDebug.debugPrintFrameRate = true;
        // PDebug.debugPaintCalls = true;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainMultiApp application = new MainMultiApp();
                application.setSize(800, 600);
                application.setVisible(true);
            }
        });
    }

    public final Controller con;

    MainMultiApp() {
        if (true)
            con = new Controller(this);
        else {
            final JDesktopPane desktop = new JDesktopPane();
            getContentPane().add(desktop);
            for (int i = 0; i < 4; i++) {
                final JInternalFrame f = new JInternalFrame("Frame" + i, true,
                        true, true, true);
                f.setSize(200, 50);
                f.setVisible(true);
                desktop.add(f);
            }
        }
    }
}
