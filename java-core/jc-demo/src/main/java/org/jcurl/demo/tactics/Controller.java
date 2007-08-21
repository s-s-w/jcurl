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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcurl.core.zui.KeyboardZoom;

/**
 * ...home of the {@link Action}s
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class Controller {

    static class MainController {
        /**
         * Non-static to enable cross-usage and initialisation of the according
         * {@link Action}s.
         * 
         * http://www.javaworld.com/javaworld/jw-06-1998/jw-06-undoredo.html
         * 
         * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
         * @version $Id: MainApp.java 701 2007-08-21 00:22:00Z mrohrmoser $
         */
        class UndoRedoAction extends AbstractAction {
            private static final long serialVersionUID = -4468864007790523205L;
            private final boolean undo;

            public UndoRedoAction(final boolean undo) {
                this.undo = undo;
            }

            public void actionPerformed(final ActionEvent e) {
                try {
                    if (undo)
                        model.getUndoer().undo();
                    else
                        model.getUndoer().redo();
                } catch (final RuntimeException ex) {
                    log.error("", ex);
                }
                editUndo.setEnabled(model.getUndoer().canUndo());
                editRedo.setEnabled(model.getUndoer().canRedo());
            }
        }

        private static final Log log = LogFactory.getLog(MainController.class);

        public final Action editProperties;
        public final Action editRedo;
        public final Action editUndo;
        public final Action fileNew;
        public final Action fileOpen;
        public final Action fileSave;
        public final Action fileSaveAs;
        public final Action fileScreenshot;
        public final Action helpAbout;
        private final MainMod model;
        private final MainPanel view;

        public MainController(final MainPanel view, final MainMod model) {
            this.view = view;
            this.model = model;
            fileSave = null;
            fileSaveAs = null;
            fileOpen = null;
            fileNew = null;
            fileScreenshot = null;
            editUndo = new UndoRedoAction(true);
            editRedo = new UndoRedoAction(false);
            editProperties = null;
            helpAbout = null;
        }
    }

    static class ZuiController {
        private final ZuiMod model;
        private final ZuiPanel view;
        public final Action zoom12Foot;
        public final Action zoomHouse;
        public final Action zoomIn;
        public final Action zoomOut;
        public final Action zoomSheet;

        public ZuiController(final ZuiPanel view, final ZuiMod model) {
            this.view = view;
            this.model = model;

            zoomSheet = new AbstractAction() {
                private static final long serialVersionUID = -4680813072205075958L;

                public void actionPerformed(final ActionEvent e) {
                    view.zoom(KeyboardZoom.sheetP);
                }
            };
            zoomHouse = new AbstractAction() {
                private static final long serialVersionUID = -4680813072205075958L;

                public void actionPerformed(final ActionEvent e) {
                    view.zoom(KeyboardZoom.houseP);
                }
            };
            zoom12Foot = new AbstractAction() {
                private static final long serialVersionUID = -4680813072205075958L;

                public void actionPerformed(final ActionEvent e) {
                    view.zoom(KeyboardZoom.twelveP);
                }
            };
            zoomIn = null;
            zoomOut = null;
        }
    }
}
