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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.Strategy;
import org.jcurl.core.helpers.Unit;

/**
 * Strategy to paint the ice sheet in world coordinates.
 * 
 * @see org.jcurl.core.swing.RockPainter
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:IcePainter.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
class IcePainter implements Strategy {
    /** IceSize colors */
    public static class ColorSet {

        public Paint backGround = new Color(0xF0F0FF);

        public Paint c1 = new Color(0xFFFFFF);

        public Paint c12 = new Color(0xFF3131);

        public Paint c4 = new Color(0x3131FF);

        public Paint c8 = new Color(0xFFFFFF);

        public Paint contours = Color.BLACK;

        public Paint hog2hog = new Color(0xF8F8F8);

        public Paint hog2tee = new Color(0xFFFFFF);

        /** (wc) millimiters */
        public Stroke stroke = new BasicStroke(0.005F);

        public Paint tee2back = new Color(0xFFFFFF);
    }

    protected static final Line2D.Float back;

    protected static final Arc2D.Float C1;

    protected static final Arc2D.Float C12;

    protected static final Arc2D.Float C4;

    protected static final Arc2D.Float C8;

    protected static final Line2D.Float center;

    protected static final Line2D.Float centerLe;

    protected static final Line2D.Float centerLeft;

    protected static final Line2D.Float centerRi;

    protected static final Line2D.Float centerRight;

    protected static final ColorSet colors = new ColorSet();

    protected static final Line2D.Float farHog;

    protected static final Rectangle2D.Float hog2hog;

    protected static final Rectangle2D.Float hog2tee;

    protected static final Line2D.Float nearHog;

    protected static final Line2D.Float tee;

    protected static final Rectangle2D.Float tee2back;

    /** Define colors and the shapes to be filled and drawn */
    static {
        final int f = 1;
        final float fhy = f * IceSize.FAR_HOG_2_TEE;
        final float nhy = f * IceSize.HOG_2_TEE;
        final float hy = f * IceSize.FAR_HACK_2_TEE;
        final float dx = f * IceSize.SIDE_2_CENTER;
        final float by = f * IceSize.BACK_2_TEE;
        final float c1 = f * Unit.f2m(0.5);
        final float c4 = f * Unit.f2m(2.0);
        final float c8 = f * Unit.f2m(4.0);
        final float c12 = f * Unit.f2m(6.0);

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
        final float RR = 4 * f * RockProps.DEFAULT.getRadius();
        centerLe = new Line2D.Float(-RR, fhy, -RR, -by);
        centerRi = new Line2D.Float(RR, fhy, RR, -by);
        centerLeft = new Line2D.Float(-dx, fhy, -dx, -by);
        centerRight = new Line2D.Float(dx, fhy, dx, -by);
    }

    public void paintIceWC(final Graphics2D g) {
        g.setStroke(colors.stroke);
        // filled stuff
        g.setPaint(colors.hog2hog);
        g.fill(hog2hog);
        g.setPaint(colors.hog2tee);
        g.fill(hog2tee);
        g.setPaint(colors.contours);
        g.draw(centerLe);
        g.draw(centerRi);
        g.setPaint(colors.tee2back);
        g.fill(tee2back);
        g.setPaint(colors.c12);
        g.fill(C12);
        g.setPaint(colors.c8);
        g.fill(C8);
        g.setPaint(colors.c4);
        g.fill(C4);
        g.setPaint(colors.c1);
        g.fill(C1);
        // contours
        g.setPaint(colors.contours);
        // g.draw(C12);
        // g.draw(C8);
        // g.draw(C4);
        // g.draw(C1);
        g.draw(back);
        g.draw(tee);
        g.draw(nearHog);
        g.draw(farHog);
        g.draw(center);
        g.draw(centerLeft);
        g.draw(centerRight);
    }
}