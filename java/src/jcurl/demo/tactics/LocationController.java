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
package jcurl.demo.tactics;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import jcurl.core.gui.RockLocationDisplayBase;

import org.apache.ugli.ULogger;
import org.jcurl.core.PositionSet;
import org.jcurl.core.helpers.JCLoggerFactory;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class LocationController implements MouseMotionListener {

    private static final ULogger log = JCLoggerFactory
            .getLogger(LocationController.class);

    protected final Cursor CursorDefault;

    private final Cursor CursorIn;

    protected int hotRockIdx = -1;

    protected final PositionSet locations;

    protected final RockLocationDisplayBase panel;

    // avoid some instanciations. Cost: thread safety
    protected final Point2D tmpWc = new Point2D.Double();

    /**
     * 
     * @param model
     *            final. Sets the <code>panel</code>'s model.
     * @param panel
     *            required for wc <->dc conversion. Repaint is triggered via
     *            {@link org.jcurl.core.RockSet#notifyChange()}.
     */
    public LocationController(final PositionSet model,
            final RockLocationDisplayBase panel) {
        panel.addMouseMotionListener(this);
        panel.setPos(0, model);

        this.panel = panel;
        this.locations = model;
        // this.model.addPropertyChangeListener(this);
        this.CursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        this.CursorIn = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    }

    /**
     * @param oldIdx
     * @param newIdx
     */
    protected void changedFocus(final int oldIdx, final int newIdx) {
        log.info("new " + newIdx);
        panel.setCursor(newIdx < 0 ? CursorDefault : CursorIn);
    }

    /**
     * Get the world-coordinates where the mouse event happened.
     * 
     * @param e
     * @param dst
     *            wc container. <code>null</code> creates a
     *            <code>Point2D.Float</code>
     * @return the world-coordinate location
     */
    protected Point2D getWc(final MouseEvent e, Point2D dst) {
        if (dst == null)
            dst = new Point2D.Float(e.getX(), e.getY());
        else
            dst.setLocation(e.getX(), e.getY());
        return panel.dc2wc(dst, dst);
    }

    public void mouseDragged(final MouseEvent e) {
        if (hotRockIdx < 0) {
            log.debug("no hot rock");
            return;
        }
        if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
            // move a rock
            final Point2D wc = getWc(e, tmpWc);
            int idx = PositionSet.findRockIndexTouchingRockAtPos(locations, wc,
                    hotRockIdx);
            if (idx >= 0) {
                log.debug("new position blocked");
            } else {
                locations.getRock(hotRockIdx).setLocation(wc);
                locations.notifyChange();
                log.debug("relocated");
            }
        }
    }

    public void mouseMoved(final MouseEvent e) {
        // check if the mouse is over any rock.
        final Point2D wc = getWc(e, tmpWc);
        if (log.isDebugEnabled())
            log.debug("wc: " + wc);
        int idx = PositionSet.findRockIndexAtPos(locations, wc);
        if (idx >= 0 && log.isDebugEnabled())
            log.debug("rock " + idx);
        if (idx != hotRockIdx) {
            changedFocus(hotRockIdx, idx);
            hotRockIdx = idx;
        }
    }
}