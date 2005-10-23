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

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import jcurl.core.JCLoggerFactory;
import jcurl.core.gui.RockLocationDisplayBase;
import jcurl.math.MathVec;
import jcurl.model.PositionSet;
import jcurl.model.SpeedSet;

import org.apache.ugli.ULogger;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SpeedController extends LocationController implements
        MouseListener {

    private static final ULogger log = JCLoggerFactory
            .getLogger(SpeedController.class);

    protected int selectedIdx = -1;

    private final SpeedSet speeds;

    /**
     * @param locations
     * @param speeds
     * @param panel
     */
    public SpeedController(PositionSet locations, SpeedSet speeds,
            RockLocationDisplayBase panel) {
        super(locations, panel);
        this.speeds = speeds;
        panel.addMouseListener(this);
    }

    protected void changedFocus(final int oldIdx, final int newIdx) {
        super.changedFocus(oldIdx, newIdx);
        if (speeds == null)
            return;
        if (newIdx < 0) {
            // remove the speed paint when loosing focus:
            panel.repaint();
            return;
        }
        paintSpeed(newIdx);
    }

    protected void changedSelected(int oldIdx, int newIdx) {
        log.info("new " + newIdx);
        ;//paintSpeed(newIdx);
    }

    public void mouseClicked(MouseEvent e) {
        log.info("" + hotRockIdx);
        if (e.getButton() == MouseEvent.BUTTON1)
            if (selectedIdx != hotRockIdx) {
                changedSelected(selectedIdx, hotRockIdx);
                selectedIdx = hotRockIdx;
            }
    }

    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if (hotRockIdx < 0) {
            log.debug("no hot rock");
            return;
        }
        if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
            // move a rock
            final Point2D wc = getWc(e, tmpWc);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        // do speed stuff
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    protected void paintSpeed(final Graphics2D g2, final int idx) {
        if (idx < 0)
            return;
        final Point2D cwc = locations.getRock(idx);
        final Point2D cdc = panel.wc2dc(cwc, null);
        // prepare a direction beam (line) 5 Meters long from cwc
        final Point2D dir_dc;
        final Point2D normwc;
        final double vwc_abs;
        {
            Point2D norm = speeds.getRock(idx);
            vwc_abs = MathVec.abs(norm);
            if (vwc_abs == 0.0)
                normwc = new Point2D.Double(0, -1);
            else
                normwc = MathVec.mult(1.0 / vwc_abs, norm, null);
            norm = MathVec.mult(5, normwc, null);
            MathVec.add(cwc, norm, norm);
            panel.wc2dc(norm, norm);
            dir_dc = norm;
        }
        // prepare a perpendicular line for the "strength"
        final Point2D abs1_dc;
        final Point2D abs2_dc;
        {
            final double len = 0.5;
            Point2D base = MathVec.add(cwc, speeds.getRock(idx), null);
            // get a perpendicular
            abs2_dc = new Point2D.Double(-normwc.getY(), normwc.getX());
            MathVec.mult(len, abs2_dc, abs2_dc);
            abs1_dc = MathVec.add(base, abs2_dc, null);
            MathVec.mult(-1, abs2_dc, abs2_dc);
            MathVec.add(base, abs2_dc, abs2_dc);
            panel.wc2dc(abs1_dc, abs1_dc);
            panel.wc2dc(abs2_dc, abs2_dc);
        }

        final int r = 30;
        // direction beam:
        g2.drawLine((int) cdc.getX(), (int) cdc.getY(), (int) dir_dc.getX(),
                (int) dir_dc.getY());
        // perpendicular line for the "strength"
        g2.drawLine((int) abs1_dc.getX(), (int) abs1_dc.getY(), (int) abs2_dc
                .getX(), (int) abs2_dc.getY());
    }

    /**
     * paint the speed stuff.
     * 
     * @param newIdx
     */
    private void paintSpeed(int newIdx) {
        final Graphics2D g2 = (Graphics2D) panel.getGraphics();
        try {
            g2.setRenderingHints(panel.hints);
            paintSpeed(g2, newIdx);
        } finally {
            g2.dispose();
        }
    }
}