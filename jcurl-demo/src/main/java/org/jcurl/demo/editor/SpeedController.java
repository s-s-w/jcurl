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
package org.jcurl.demo.editor;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.core.swing.RockEditDisplay;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SpeedController.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class SpeedController implements MouseMotionListener, MouseListener {

    private static final Log log = JCLoggerFactory
            .getLogger(SpeedController.class);

    private final RockEditDisplay.HotStuff hot = new RockEditDisplay.HotStuff();

    private final RockEditDisplay panel;

    private final PositionSet rocks;

    // private final SpeedSet speeds;

    private final Point2D tmp = new Point2D.Double();

    /**
     * @param locations
     * @param speeds
     * @param panel
     */
    public SpeedController(PositionSet locations, SpeedSet speeds,
            RockEditDisplay panel) {
        rocks = locations;
        // this.speeds = speeds;
        this.panel = panel;
        this.panel.addMouseListener(this);
        this.panel.addMouseMotionListener(this);
        this.panel.setPos(rocks);
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

    public void mouseClicked(MouseEvent e) {
        log.info("" + hot.idx);
        if (e.getButton() == MouseEvent.BUTTON1) {
            int mask = 1 << hot.idx;
            if (e.isShiftDown()) {
                int old = panel.getSelectedMask();
                if ((mask & old) == 0)
                    mask |= old;
                else
                    mask = ~mask & old;
            }
            panel.setSelectedMask(mask);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
            if (RockEditDisplay.HotObject.ROCK.equals(hot.what)) {
                // move a rock
                final Point2D wc = getWc(e, tmp);
                int idx = PositionSet.findRockIndexTouchingRockAtPos(rocks, wc,
                        hot.idx);
                if (idx >= 0)
                    log.debug("new position blocked");
                else {
                    rocks.getRock(hot.idx).setLocation(wc);
                    rocks.notifyChange();
                    log.debug("relocated");
                }
            }
            if (RockEditDisplay.HotObject.SPEED.equals(hot.what)) {
                // change the speed of a rock
                final Point2D wc = getWc(e, tmp);
                panel.setSpeedSpot(hot.idx, wc);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        // check if the mouse is over something hot:
        // - a speed handle
        // - a rock
        tmp.setLocation(e.getX(), e.getY());
        panel.findHotDc(tmp, hot);
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}