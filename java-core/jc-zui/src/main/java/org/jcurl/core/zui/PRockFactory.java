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
package org.jcurl.core.zui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import org.jcurl.core.base.Factory;
import org.jcurl.core.base.RockProps;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * Creates a pickable node displaying one rock, assuming a <b>RIGHT HANDED</b>
 * parent coordinate system.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class PRockFactory implements Factory {

    public static class Fancy extends PRockFactory {
        /** Rock colors */
        protected static class ColorSet {
            public Color contour = Color.BLACK;

            public Color dark = new Color(0xFF0000);

            public Color granite = new Color(0x565755);

            public Color label = Color.BLACK;

            public Color light = new Color(0xFFFF00);
        }

        protected static final ColorSet colors = new ColorSet();

        protected static final Font fo;

        protected static final Arc2D.Float inner;

        protected static final Arc2D.Float outer;

        static {
            final float ro = RockProps.DEFAULT.getRadius();
            final float ri = 0.7F * ro;
            outer = new Arc2D.Float(-ro, -ro, 2 * ro, 2 * ro, 0, 360,
                    Arc2D.CHORD);
            inner = new Arc2D.Float(-ri, -ri, 2 * ri, 2 * ri, 0, 360,
                    Arc2D.CHORD);
            fo = new Font("SansSerif", Font.BOLD, 1);
        }

        static Color alpha(final Color base, final int alpha) {
            return new Color(base.getRed(), base.getGreen(), base.getBlue(),
                    alpha);
        }

        private final int alpha;

        public Fancy(final int alpha) {
            this.alpha = alpha;
        }

        @Override
        public PNode newInstance(final int i8, final boolean isDark) {
            final PNode r = new PComposite();
            r.addChild(node(outer, alpha(colors.granite, alpha), null, null));
            r.addChild(node(inner, alpha(isDark ? colors.dark : colors.light,
                    alpha), null, null));
            {
                final PText t = new PText(labels[i8]);
                t.setFont(fo);
                t.setTextPaint(alpha(colors.label, alpha));
                // Make coord-sys left-handed again, as the ice is assumed to be
                // right-handed:
                t.setTransform(AffineTransform.getScaleInstance(1, -1));
                t.scale(1.0 / 5.0); // implicit FontSize
                t.translate(-0.5 * t.getWidth(), -0.5 * t.getHeight());
                t.setPickable(false);
                r.addChild(t);
            }
            // r.setChildrenPickable(false);
            // r.setPickable(true);
            r.getChild(0).setPickable(true);
            return r;
        }
    }

    public static class Simple extends Fancy {
        private static final Stroke stroke = new BasicStroke(0.010F);

        public Simple() {
            super(128);
        }

        @Override
        public PNode newInstance(final int i8, final boolean isDark) {
            // fill to also make the body, not only the edge
            // pickable:
            final PNode r = node(outer, isDark ? colors.dark : colors.light,
                    stroke, Color.BLACK);
            r.setPickable(true);
            return r;
        }
    }

    protected static final String[] labels = { "1", "2", "3", "4", "5", "6",
            "7", "8" };

    protected static PNode node(final Shape s, final Paint p, final Stroke str,
            final Paint sp) {
        final PPath n = new PPath(s, str);
        n.setStrokePaint(sp);
        n.setPaint(p);
        n.setPickable(false);
        return n;
    }

    public PNode newInstance(final int i16) {
        return newInstance(i16 / 2, i16 % 2 == 0);
    }

    public abstract PNode newInstance(final int i8, final boolean isDark);
}
