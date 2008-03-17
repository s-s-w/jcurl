/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.UndoableEdit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcurl.core.ui.UndoRedoDocument;
import org.jcurl.core.zui.KeyboardZoom;

import edu.umd.cs.piccolo.PCamera;

/**
 * ...home of the {@link Action}s
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class Controller {

    static class MainController {

        private static final Log log = LogFactory.getLog(MainController.class);

        /**
         * tighten to a given point.
         * 
         * TODO play a shy slurping sound.
         * 
         * @param frame
         * @param steps
         * @param millis
         */
        private static void tighten(final JFrame frame, final int steps,
                final int millis) {
            if (false)
                // TODO on a wicked Mac do nothing more but exit...
                return;

            final Rectangle siz0 = frame.getBounds();
            // final BufferedImage img = new
            // BufferedImage(frame.getContentPane()
            // .getWidth(), frame.getContentPane().getHeight(),
            // BufferedImage.TYPE_INT_RGB);
            // frame.getContentPane().paint(img.getGraphics());
            // img.getGraphics().dispose();

            // why does this have no effect?
            frame.getContentPane().removeAll();
            frame.repaint();

            final Point2D dst = new Point2D.Double(siz0.x + siz0.width * 1 / 3,
                    siz0.y + siz0.height * 1 / 3);
            for (int i = steps - 1; i >= 0; i--) {
                final Rectangle siz = frame.getBounds();
                siz.x = (int) (dst.getX() + (siz0.x - dst.getX()) * i / steps);
                siz.y = (int) (dst.getX() + (siz0.y - dst.getY()) * i / steps);
                siz.height = (int) ((float) siz0.height * i / steps);
                siz.width = (int) ((float) siz0.width * i / steps);
                try {
                    Thread.sleep(millis / steps);
                } catch (final InterruptedException e) {
                    ;
                }
                frame.setBounds(siz);
            }
            frame.setVisible(false);
        }

        public final Action editProperties;
        final JFileChooser fcJcx;
        final JFileChooser fcPng;
        public final Action fileExit;
        public final Action fileNew;
        public final Action fileOpen;
        public final Action fileSave;
        public final Action fileSaveAs;
        public final Action fileScreenshot;
        private final JFrame frame;
        public final Action helpAbout;
        final JComponent zui;

        public MainController(final JComponent zui, final MainMod model,
                final JFrame frame) {
            this.zui = zui;
            this.frame = frame;
            fcJcx = new JFileChooser(model.getCurrentFile());
            fcJcx.setName("Open");
            fcJcx.setMultiSelectionEnabled(false);
            fcJcx.setAcceptAllFileFilterUsed(true);
            fcJcx.setFileFilter(new FileFilter() {
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
            fcPng = new JFileChooser(new File("."));
            fcPng.setName("Save Screenshot");
            fcPng.setMultiSelectionEnabled(false);
            fcPng.setAcceptAllFileFilterUsed(true);
            fcPng.setFileFilter(new FileFilter() {
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

            fileSave = null;
            fileSaveAs = new AbstractAction() {
                private static final long serialVersionUID = -5150063929213996925L;

                public void actionPerformed(final ActionEvent e) {
                    if (JFileChooser.APPROVE_OPTION == fcJcx
                            .showSaveDialog(frame))
                        model.save(fcJcx.getSelectedFile());
                }

            };
            fileOpen = new AbstractAction() {
                private static final long serialVersionUID = -5150063929213996925L;

                public void actionPerformed(final ActionEvent e) {
                    if (JFileChooser.APPROVE_OPTION == fcJcx
                            .showOpenDialog(frame))
                        model.open(fcJcx.getSelectedFile());
                }

            };
            fileNew = null;
            fileScreenshot = new AbstractAction() {
                private static final long serialVersionUID = -4623064479688132611L;

                public void actionPerformed(final ActionEvent e) {
                    log.debug("-");
                    if (JFileChooser.APPROVE_OPTION == fcPng
                            .showSaveDialog(frame)) {
                        final File dst = fcPng.getSelectedFile();
                        try {
                            // this.setCursor(Cwait);
                            exportPng(dst, zui);
                        } catch (final Exception e1) {
                            log.error("", e1);
                        } finally {
                            // this.setCursor(Cdefault);
                        }
                    }
                }
            };
            fileExit = new AbstractAction() {
                private static final long serialVersionUID = -4680813072205075958L;

                public void actionPerformed(final ActionEvent e) {
                    shutDown();
                }
            };

            editProperties = null;
            helpAbout = null;
        }

        private boolean discardUnsavedChanges() {
            // if (mod_locations.getLastChanged() <= lastSaved
            // && mod_speeds.getLastChanged() <= lastSaved)
            // return true;
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame,
                    "Discard unsaved changes?", "Warning",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))
                return true;
            return false;
        }

        void exportPng(File dst, final JComponent view) throws IOException {
            final BufferedImage img = new BufferedImage(view.getWidth(), view
                    .getHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics g = img.getGraphics();
            try {
                view.paintAll(g);
            } finally {
                g.dispose();
            }
            if (!dst.getName().endsWith(".png"))
                dst = new File(dst.getAbsoluteFile() + ".png");
            ImageIO.write(img, "png", dst);
        }

        public void shutDown() {
            if (!discardUnsavedChanges())
                fileSave.actionPerformed(null);
            tighten(frame, 10, 200);
            System.exit(0);
        }
    }

    static class UndoRedoCon implements UndoableEditListener {
        /**
         * Non-static to enable cross-usage and initialisation of the according
         * {@link Action}s.
         * 
         * http://www.javaworld.com/javaworld/jw-06-1998/jw-06-undoredo.html
         * 
         * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
         * @version $Id$
         */
        class UndoRedoAction extends AbstractAction {
            private static final long serialVersionUID = -4468864007790523205L;
            private final boolean undo;

            public UndoRedoAction(final boolean undo) {
                this.undo = undo;
                setEnabled(false);
            }

            public void actionPerformed(final ActionEvent e) {
                if (model == null)
                    return;
                try {
                    if (undo)
                        model.undo();
                    else
                        model.redo();
                } catch (final RuntimeException ex) {
                    lo.error("", ex);
                }
                editUndo.setEnabled(model.canUndo());
                editRedo.setEnabled(model.canRedo());
            }
        }

        private static final Log lo = LogFactory.getLog(UndoRedoCon.class);
        public final Action editRedo;
        public final Action editUndo;
        private UndoRedoDocument model;

        public UndoRedoCon() {
            editUndo = new UndoRedoAction(true);
            editRedo = new UndoRedoAction(false);
        }

        public UndoRedoDocument getModel() {
            return model;
        }

        public void setModel(final UndoRedoDocument model) {
            if (this.model != null)
                this.model.removeUndoableEditListener(this);
            this.model = model;
            if (this.model != null)
                this.model.addUndoableEditListener(this);
        }

        public void undoableEditHappened(final UndoableEditEvent e) {
            final UndoableEdit ed = e.getEdit();
            if (model == null || ed == null)
                return;
            editUndo.setEnabled(ed.canUndo());
            editRedo.setEnabled(ed.canRedo());
        }
    }

    static class ZuiController extends KeyboardZoom {

        public ZuiController(final PCamera cam) {
            super(cam);
        }
    }
}
