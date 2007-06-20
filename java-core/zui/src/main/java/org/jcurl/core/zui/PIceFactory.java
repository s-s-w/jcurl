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
package org.jcurl.core.zui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.base.Factory;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.Unit;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * Creates a unpickable {@link PNode} displaying a sheet of ice with a <b>RIGHT
 * HANDED</b> coordinate system.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class PIceFactory implements Factory {

    public static class Fancy extends PIceFactory {

        /** IceSize colors */
        private static class ColorSet {

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

        private static final ColorSet colors = new ColorSet();

        /**
         * Do it!
         * 
         * @return the new PNode
         */
        @Override
        public PNode newInstance() {
            final PNode ice = new PNode();
            ice.addChild(node(hog2hog, colors.hog2hog, colors.stroke));
            ice.addChild(node(hog2tee, colors.hog2tee, colors.stroke));
            ice.addChild(node(centerLe, colors.contours, colors.stroke));
            ice.addChild(node(centerRi, colors.contours, colors.stroke));
            ice.addChild(node(tee2back, colors.tee2back, colors.stroke));
            ice.addChild(node(C12, colors.c12, null));
            ice.addChild(node(C8, colors.c8, null));
            ice.addChild(node(C4, colors.c4, null));
            ice.addChild(node(C1, colors.c1, null));
            ice.addChild(node(center, colors.contours, colors.stroke));
            ice.addChild(node(centerLeft, colors.contours, colors.stroke));
            ice.addChild(node(centerRight, colors.contours, colors.stroke));
            ice.addChild(node(back, colors.contours, colors.stroke));
            ice.addChild(node(tee, colors.contours, colors.stroke));
            ice.addChild(node(nearHog, colors.contours, colors.stroke));
            ice.addChild(node(farHog, colors.contours, colors.stroke));
            ice.setChildrenPickable(false);
            ice.setPickable(false);
            final PNode r = new PNode();
            r.addChild(ice);
            // Make coord-sys right-handed:
            r.setTransform(AffineTransform.getScaleInstance(1, -1));
            return r;
        }

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

    protected static final Line2D.Float farHog;

    protected static final Rectangle2D.Float hog2hog;

    protected static final Rectangle2D.Float hog2tee;

    protected static final Line2D.Float nearHog;

    protected static final Line2D.Float tee;

    protected static final Rectangle2D.Float tee2back;

    /** Define colors and the shapes to be filled and drawn */
    static {
        final float fhy = IceSize.FAR_HOG_2_TEE;
        final float nhy = IceSize.HOG_2_TEE;
        final float hy = IceSize.FAR_HACK_2_TEE;
        final float dx = IceSize.SIDE_2_CENTER;
        final float by = IceSize.BACK_2_TEE;
        final float c1 = Unit.f2m(0.5);
        final float c4 = Unit.f2m(2.0);
        final float c8 = Unit.f2m(4.0);
        final float c12 = Unit.f2m(6.0);

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
        final float RR = 4 * RockProps.DEFAULT.getRadius();
        centerLe = new Line2D.Float(-RR, fhy, -RR, -by);
        centerRi = new Line2D.Float(RR, fhy, RR, -by);
        centerLeft = new Line2D.Float(-dx, fhy, -dx, -by);
        centerRight = new Line2D.Float(dx, fhy, dx, -by);
    }

    protected static PNode node(final Shape s, final Paint p, final Stroke l) {
        final PNode n = new PPath(s, l);
        n.setPaint(p);
        n.setPickable(false);
        return n;
    }

    public abstract PNode newInstance();
}