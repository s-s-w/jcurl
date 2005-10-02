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

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import jcurl.core.JCLoggerFactory;
import jcurl.core.PositionSet;
import jcurl.core.gui.RockMotionPanel;

import org.apache.ugli.ULogger;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SetupController implements MouseMotionListener {

    private static final ULogger log = JCLoggerFactory
            .getLogger(SetupController.class);

    private final Cursor CursorIn;

    private final Cursor CursorOut;

    private int hotRockIdx = -1;

    private final PositionSet model;

    private final RockMotionPanel panel;

    // avoid some instanciations. Cost: thread safety
    private final Point2D tmpWc = new Point2D.Double();

    /**
     * 
     * @param model
     *            final. Sets the <code>panel</code>'s model.
     * @param panel
     *            required for wc <->dc conversion. Repaint is triggered via
     *            {@link jcurl.core.RockSet#notifyChange()}.
     */
    public SetupController(final PositionSet model, final RockMotionPanel panel) {
        panel.addMouseMotionListener(this);
        panel.setPos(0, model);

        this.panel = panel;
        this.model = model;
        //this.model.addPropertyChangeListener(this);
        this.CursorOut = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        this.CursorIn = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    }

    private Point2D getWc(final MouseEvent e) {
        tmpWc.setLocation(e.getX(), e.getY());
        return panel.dc2wc(tmpWc, tmpWc);
    }

    public void mouseDragged(final MouseEvent e) {
        if (hotRockIdx < 0) {
            log.debug("no hot rock");
        } else {
            final Point2D wc = getWc(e);
            int idx = PositionSet.findRockIndexTouchingRockAtPos(model, wc,
                    hotRockIdx);
            if (idx >= 0) {
                log.debug("new position blocked");
            } else {
                model.getRock(hotRockIdx).setLocation(wc);
                model.notifyChange();
                log.debug("relocated");
            }
        }
    }

    public void mouseMoved(final MouseEvent e) {
        // check if the mouse is over any rock.
        final Point2D wc = getWc(e);
        if (log.isDebugEnabled())
            log.debug("wc: " + wc);
        int idx = PositionSet.findRockIndexAtPos(model, wc);
        if (idx >= 0 && log.isDebugEnabled())
            log.debug("rock " + idx);
        if (idx != hotRockIdx) {
            panel.setCursor(idx < 0 ? CursorOut : CursorIn);
            hotRockIdx = idx;
        }
    }
}