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
package org.jcurl.core.swing;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.EnumBase;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.MathVec;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:RockEditDisplay.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class RockEditDisplay extends PositionDisplay {

    private static final long serialVersionUID = 8273672905059423985L;

    public static class HotObject extends EnumBase {

        private static final long serialVersionUID = -1579660062770601846L;

        public static final HotObject NONE = new HotObject(0, "None",
                Cursor.DEFAULT_CURSOR);

        public static final HotObject ROCK = new HotObject(1, "Rock",
                Cursor.HAND_CURSOR);

        public static final HotObject SPEED = new HotObject(2, "Speed",
                Cursor.MOVE_CURSOR);

        public final Cursor cursor;

        private HotObject(final int state, final String text, final Cursor cs) {
            super(state, text);
            cursor = cs;
        }

        private HotObject(final int state, final String text, final int cs) {
            this(state, text, Cursor.getPredefinedCursor(cs));
        }
    }

    public static class HotStuff {
        // public boolean changed = false;

        public int idx;

        public HotObject what;
    }

    private static final int hotRadiusDC = 5;

    private static final Log log = JCLoggerFactory
            .getLogger(RockEditDisplay.class);

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(570, 140));

    }

    protected static void circleDC(final Graphics2D g, final Point2D center,
            final int radius) {
        final int cx = (int) center.getX();
        final int cy = (int) center.getY();
        g.drawArc(cx - radius, cy - radius, 2 * radius, 2 * radius, 0, 360);
    }

    /**
     * @param g
     * @param abs1_dc
     * @param abs2_dc
     */
    protected static void lineDC(final Graphics2D g, final Point2D abs1_dc,
            final Point2D abs2_dc) {
        // perpendicular line for the "strength"
        g.drawLine((int) abs1_dc.getX(), (int) abs1_dc.getY(), (int) abs2_dc
                .getX(), (int) abs2_dc.getY());
    }

    private int focus = -1;

    public HotStuff hot = null;

    private int selectedMask = 0;

    private SpeedSet speed;

    public RockEditDisplay() {
        super();
        initialize();
        speed = new SpeedSet();
        speed.addPropertyChangeListener(this);
    }

    public void findHotDc(final Point2D dc, final HotStuff hot) {
        final Point2D wc = dc2wc(dc, null);
        try {
            hot.what = HotObject.SPEED;
            hot.idx = findHotSpeed(wc, dc);
            if (hot.idx >= 0)
                return;
            hot.what = HotObject.ROCK;
            hot.idx = PositionSet.findRockIndexAtPos(getPos(), wc);
            if (hot.idx >= 0)
                return;
            hot.what = HotObject.NONE;
        } finally {
            setCursor(hot.what.cursor);
        }
    }

    private int findHotSpeed(final Point2D wc, final Point2D dc) {
        Point2D tmp = new Point2D.Float();
        final double RR = hotRadiusDC * hotRadiusDC;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if ((1 << i & selectedMask) == 0)
                continue;
            tmp = getSpeedSpotWC(i, tmp);
            wc2dc(tmp, tmp);
            final double dist = tmp.distanceSq(dc);
            if (log.isDebugEnabled())
                log.debug("i=" + i + " dist=" + dist);
            if (dist <= RR)
                return i;
        }
        return -1;
    }

    public int getFocus() {
        return focus;
    }

    public int getSelectedMask() {
        return selectedMask;
    }

    private Point2D getSpeedSpotWC(final int idx, final Point2D dst) {
        return MathVec.add(getPos().getRock(idx), speed.getRock(idx), dst);
    }

    public void paint(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        super.paint(g2);
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            if ((selectedMask & 1 << i) != 0)
                paintSpeed(g2, i);
    }

    protected void paintSpeed(final Graphics2D g2, final int idx) {
        if (idx < 0)
            return;
        final Point2D cwc = getPos().getRock(idx);
        final Point2D cdc = wc2dc(cwc, null);
        // prepare a direction beam (line) 5 Meters long from cwc
        final Point2D dir_dc;
        final Point2D normwc;
        final double vwc_abs;
        {
            Point2D norm = speed.getRock(idx);
            vwc_abs = MathVec.abs(norm);
            if (vwc_abs == 0.0)
                normwc = new Point2D.Double(0, -1);
            else
                normwc = MathVec.mult(1.0 / vwc_abs, norm, null);
            norm = MathVec.mult(5, normwc, null);
            MathVec.add(cwc, norm, norm);
            wc2dc(norm, norm);
            dir_dc = norm;
        }
        // prepare a perpendicular line for the "strength"
        final Point2D abs1_dc;
        final Point2D abs2_dc;
        final Point2D spotWc;
        {
            final double len = 0.5;
            spotWc = getSpeedSpotWC(idx, null);
            // get a perpendicular
            abs2_dc = new Point2D.Double(-normwc.getY(), normwc.getX());
            MathVec.mult(len, abs2_dc, abs2_dc);
            abs1_dc = MathVec.add(spotWc, abs2_dc, null);
            MathVec.mult(-1, abs2_dc, abs2_dc);
            MathVec.add(spotWc, abs2_dc, abs2_dc);
            wc2dc(abs1_dc, abs1_dc);
            wc2dc(abs2_dc, abs2_dc);
        }

        lineDC(g2, cdc, dir_dc);
        lineDC(g2, abs1_dc, abs2_dc);
        // circle at the hot "spot" for changing speed
        circleDC(g2, wc2dc(spotWc, null), hotRadiusDC);
    }

    /**
     * Property (speed) changed.
     * 
     * @param evt
     * @see PositionDisplay#propertyChange(PropertyChangeEvent)
     */
    public void propertyChange(final PropertyChangeEvent evt) {
        final Object tmp = evt.getNewValue();
        if (SpeedSet.class.isAssignableFrom(tmp.getClass()))
            this.repaint();
        else
            super.propertyChange(evt);
    }

    public void setFocus(final int focus) {
        if (this.focus == focus)
            return;
        this.focus = focus & 0xF;
        this.repaint();
    }

    public void setSelectedMask(final int selectedMask) {
        if (this.selectedMask == selectedMask)
            return;
        this.selectedMask = selectedMask & 0xFFFF;
        if (log.isDebugEnabled())
            log.debug("selectedMask=" + this.selectedMask);
        this.repaint();
    }

    public void setSpeedSpot(final int idx, final Point2D spot) {
        MathVec.sub(spot, getPos().getRock(idx), speed.getRock(idx));
        speed.notifyChange();
    }

    public SpeedSet getSpeed() {
        return speed;
    }

    public void setSpeed(final SpeedSet speed) {
        this.speed = speed;
    }
} // @jve:decl-index=0:visual-constraint="10,10"
