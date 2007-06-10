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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import org.jcurl.core.base.Zoomer;

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
public class RockPainter extends org.jcurl.core.swing.RockPainter {

    protected static final String[] labels = { "1", "2", "3", "4", "5", "6",
            "7", "8" };

    public static PNode create(final int idx16) {
        return create(idx16 / 2, idx16 % 2 == 0);
    }

    public static PNode create(final int idx8, final boolean isDark) {
        final PNode r = new PComposite();
        r.addChild(node(outer, colors.granite, null));
        r.addChild(node(inner, isDark ? colors.dark : colors.light, null));
        final PText t = new PText(labels[idx8]);
        t.setFont(org.jcurl.core.swing.RockPainter.fo);
        t.setTextPaint(colors.contour);
        // Make coord-sys left-handed again, as the ice is assumed to be
        // right-handed:
        t.setTransform(AffineTransform.getScaleInstance(1, -1));
        t.scale(1.0 / Zoomer.SCALE);
        t.translate(-0.5 * t.getWidth(), -0.5 * t.getHeight());
        t.setPickable(false);
        r.addChild(t);
        // r.setChildrenPickable(false);
        // r.setPickable(true);
        r.getChild(0).setPickable(true);
        return r;
    }

    static PNode node(final Shape s, final Paint p, final Stroke l) {
        final PNode n = new PPath(s, l);
        n.setPaint(p);
        n.setScale(1.0 / Zoomer.SCALE);
        n.setPickable(false);
        return n;
    }

    @Override
    public void paintRockRC(final Graphics2D g, final boolean isDark,
            final int idx) {
        throw new UnsupportedOperationException();
    }
}
