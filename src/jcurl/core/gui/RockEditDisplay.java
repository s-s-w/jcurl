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
package jcurl.core.gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import jcurl.core.dto.EnumBase;
import jcurl.math.MathVec;
import jcurl.model.PositionSet;
import jcurl.model.RockSet;
import jcurl.model.SpeedSet;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockEditDisplay extends RockLocationDisplay {

    public static class HotObject extends EnumBase {
        public static final HotObject ROCK = new HotObject(1, "Rock");

        public static final HotObject SPEED = new HotObject(2, "Speed");

        private HotObject(int state, String text) {
            super(state, text);
        }
    }

    public static class HotStuff {
        public boolean changed = false;

        public int idx;

        public HotObject what;
    }

    protected final Cursor CursorDefault;

    private final Cursor CursorIn;

    private int focus = -1;

    public HotStuff hot = null;

    private int selectedMask = 0;

    private final SpeedSet speeds;

    /**
     * @param rocks
     * @param speeds
     * @param zoom
     */
    public RockEditDisplay(PositionSet rocks, SpeedSet speeds, Zoomer zoom) {
        this(rocks, speeds, zoom, null, null);
    }

    /**
     * @param rocks
     * @param speeds
     * @param zoom
     * @param iceP
     * @param rockP
     */
    public RockEditDisplay(PositionSet rocks, SpeedSet speeds, Zoomer zoom,
            IcePainter iceP, RockPainter rockP) {
        super(rocks, zoom, iceP, rockP);
        this.speeds = speeds;
        this.speeds.addPropertyChangeListener(this);
        this.CursorDefault = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        this.CursorIn = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    }

    public void findHotDc(final Point2D dc, final HotStuff hot) {
        final Point2D wc = this.dc2wc(dc, dc);
        hot.what = HotObject.ROCK;
        hot.idx = PositionSet.findRockIndexAtPos(rocks, wc);
        this.setCursor(hot.idx < 0 ? CursorDefault : CursorIn);
    }

    public int getFocus() {
        return focus;
    }

    public int getSelectedMask() {
        return selectedMask;
    }

    public void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if ((selectedMask & (1 << i)) != 0)
                paintSpeed(g2, i);
        }
    }

    protected void paintSpeed(final Graphics2D g2, final int idx) {
        if (idx < 0)
            return;
        final Point2D cwc = rocks.getRock(idx);
        final Point2D cdc = this.wc2dc(cwc, null);
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
            this.wc2dc(norm, norm);
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
            this.wc2dc(abs1_dc, abs1_dc);
            this.wc2dc(abs2_dc, abs2_dc);
        }

        final int r = 30;
        // direction beam:
        g2.drawLine((int) cdc.getX(), (int) cdc.getY(), (int) dir_dc.getX(),
                (int) dir_dc.getY());
        // perpendicular line for the "strength"
        g2.drawLine((int) abs1_dc.getX(), (int) abs1_dc.getY(), (int) abs2_dc
                .getX(), (int) abs2_dc.getY());
    }

    public void setFocus(int focus) {
        if (this.focus == focus)
            return;
        this.focus = focus;
        repaint();
    }

    public void setSelectedMask(int selectedMask) {
        if (this.selectedMask == selectedMask)
            return;
        this.selectedMask = selectedMask;
        repaint();
    }
}