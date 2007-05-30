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
package org.jcurl.mr.gui;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.swing.WCComponent;

/**
 * Simple (mouse) controller to place rocks by dragging them. Cursors are
 * hardcoded.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockPositionMouseController implements MouseMotionListener {
    private static final Log log = JCLoggerFactory
            .getLogger(RockPositionMouseController.class);

    protected final Cursor CursorDefault;

    private final Cursor CursorIn;

    protected final WCComponent dc2wc;

    private boolean enabled = false;

    private boolean fire = false;

    private final boolean fireOnMove;

    protected int hotRockIdx = -1;

    protected final PositionSet locations;

    // avoid some instanciations. Cost: thread safety
    protected final Point2D tmpWc = new Point2D.Double();

    /**
     * 
     * @param model
     *            Real world rock coordinates.
     * @param panel
     *            required for wc&lt;-&gt;dc conversion. Repaint is triggered
     *            via {@link org.jcurl.core.base.RockSet#notifyChange()}.
     * @param fireOnMove
     */
    public RockPositionMouseController(final PositionSet model,
            final WCComponent panel, final boolean fireOnMove) {
        this.fireOnMove = fireOnMove;
        dc2wc = panel;
        locations = model;
        // this.model.addPropertyChangeListener(this);
        CursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        CursorIn = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        setEnabled(true);
    }

    /**
     * @param oldIdx
     * @param newIdx
     */
    protected void changedFocus(final int oldIdx, final int newIdx) {
        if (log.isDebugEnabled())
            log.debug("new " + newIdx);
        dc2wc.setCursor(newIdx < 0 ? CursorDefault : CursorIn);
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
        return dc2wc.dc2wc(dst, dst);
    }

    public void mouseDragged(final MouseEvent e) {
        if (!enabled || !dc2wc.isVisible() || !dc2wc.isEnabled())
            return;
        if (hotRockIdx < 0) {
            log.debug("no hot rock");
            return;
        }
        if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
            // move a rock
            final Point2D wc = getWc(e, tmpWc);
            final int idx = PositionSet.findRockIndexTouchingRockAtPos(
                    locations, wc, hotRockIdx);
            if (idx >= 0)
                log.debug("new position blocked");
            else {
                locations.getRock(hotRockIdx).setLocation(wc);
                if (fireOnMove)
                    locations.notifyChange();
                else
                    fire = true;
                log.debug("relocated");
            }
            return;
        }
    }

    public void mouseMoved(final MouseEvent e) {
        if (!enabled || !dc2wc.isVisible() || !dc2wc.isEnabled())
            return;
        if (!fireOnMove && fire) {
            locations.notifyChange();
            fire = false;
        }
        // check if the mouse is over any rock.
        final Point2D wc = getWc(e, tmpWc);
        if (log.isDebugEnabled())
            log.debug("wc: " + wc);
        final int idx = PositionSet.findRockIndexAtPos(locations, wc);
        if (idx >= 0 && log.isDebugEnabled())
            if (log.isDebugEnabled())
                log.debug("rock " + idx);
        if (idx != hotRockIdx) {
            changedFocus(hotRockIdx, idx);
            hotRockIdx = idx;
        }
    }

    public void setEnabled(final boolean enabled) {
        if (this.enabled == enabled)
            return;
        if (enabled)
            dc2wc.addMouseMotionListener(this);
        else
            dc2wc.removeMouseMotionListener(this);
        hotRockIdx = -1;
        this.enabled = enabled;
    }
}
