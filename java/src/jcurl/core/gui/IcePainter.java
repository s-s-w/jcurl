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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import jcurl.core.dto.Ice;
import jcurl.core.io.Dim;

/**
 * @see jcurl.core.gui.RockPainter
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class IcePainter {
    private static final Color outC = Color.BLUE;

    private static final Color frontC = Color.PINK;

    private static final Color backC = new Color(0xC9C9C9);

    private static final Color c1C = Color.RED;

    private static final Color c4C = Color.MAGENTA;

    private static final Color c8C = Color.ORANGE;

    private static final Color c12C = Color.CYAN;

    private static final Color linesC = Color.BLACK;

    protected static final Rectangle2D.Float hog2hog;

    protected static final Rectangle2D.Float hog2tee;

    protected static final Rectangle2D.Float tee2back;

    protected static final Arc2D.Float C12;

    protected static final Arc2D.Float C8;

    protected static final Arc2D.Float C4;

    protected static final Arc2D.Float C1;

    protected static final Line2D.Float back;

    protected static final Line2D.Float tee;

    protected static final Line2D.Float nearHog;

    protected static final Line2D.Float farHog;

    protected static final Line2D.Float center;

    protected static final Line2D.Float centerLeft;

    protected static final Line2D.Float centerRight;

    /** Define the shapes to be filled and drawn */
    static {
        final int f = JCurlPanel.SCALE;
        final int fhy = (int) (f * Ice.FAR_HOG_2_TEE);
        final int nhy = (int) (f * Ice.HOG_2_TEE);
        final int hy = (int) (f * Ice.FAR_HACK_2_TEE);
        final int dx = (int) (f * Ice.SIDE_2_CENTER);
        final int by = (int) (f * Ice.BACK_2_TEE);
        final int c1 = (int) (f * Dim.f2m(0.5));
        final int c4 = (int) (f * Dim.f2m(2.0));
        final int c8 = (int) (f * Dim.f2m(4.0));
        final int c12 = (int) (f * Dim.f2m(6.0));

        hog2hog = new Rectangle2D.Float(-dx, nhy, 2 * dx, fhy - nhy);
        hog2tee = new Rectangle2D.Float(-dx, 0, 2 * dx, nhy);
        tee2back = new Rectangle2D.Float(-dx, -by, 2 * dx, by);
        C12 = new Arc2D.Float(-c12, -c12, 2 * c12, 2 * c12, 0, 360, Arc2D.CHORD);
        C8 = new Arc2D.Float(-c8, -c8, 2 * c8, 2 * c8, 0, 360, Arc2D.CHORD);
        C4 = new Arc2D.Float(-c4, -c4, 2 * c4, 2 * c4, 0, 360, Arc2D.CHORD);
        C1 = new Arc2D.Float(-c1, -c1, 2 * c1, 2 * c1, 0, 360, Arc2D.CHORD);
        back = new Line2D.Float(-dx, -by, dx, -by);
        tee = new Line2D.Float(-dx, 0, dx, 0);
        nearHog = new Line2D.Float(-dx, nhy, dx, nhy);
        farHog = new Line2D.Float(-dx, fhy, dx, fhy);
        center = new Line2D.Float(0, hy, 0, -by);
        centerLeft = new Line2D.Float(-dx, fhy, -dx, -by);
        centerRight = new Line2D.Float(dx, fhy, dx, -by);
    }

    /**
     * 
     * 
     * @param g
     */
    public void paintIce(final Graphics2D g) {
        // filled stuff
        g.setColor(outC);
        g.fill(hog2hog);
        g.setColor(frontC);
        g.fill(hog2tee);
        g.setColor(backC);
        g.fill(tee2back);
        g.setColor(c12C);
        g.fill(C12);
        g.setColor(c8C);
        g.fill(C8);
        g.setColor(c4C);
        g.fill(C4);
        g.setColor(c1C);
        g.fill(C1);

        // contours
        g.setColor(linesC);
        g.draw(C12);
        g.draw(C8);
        g.draw(C4);
        g.draw(C1);
        g.draw(back);
        g.draw(tee);
        g.draw(nearHog);
        g.draw(farHog);
        g.draw(center);
        g.draw(centerLeft);
        g.draw(centerRight);
    }
}