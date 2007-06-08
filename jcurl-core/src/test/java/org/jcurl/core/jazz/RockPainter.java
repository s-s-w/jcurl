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
package org.jcurl.core.jazz;

import java.awt.Component;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import org.jcurl.core.base.Zoomer;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class RockPainter extends org.jcurl.core.swing.RockPainter {

    public static PNode create(final int i, final boolean isDark) {
        final PNode r = new PPath();
        r.scale(1.0 / Zoomer.SCALE);
        r.addChild(node(outer, colors.granite, null));
        r.addChild(node(inner, isDark ? colors.dark : colors.light, null));
        final PText t = new PText(Integer.toString(i + 1));
        t.setFont(org.jcurl.core.swing.RockPainter.fo);
        t.setTextPaint(colors.contour);
        t.setJustification(Component.CENTER_ALIGNMENT);
        r.addChild(t);
        return r;
    }

    static PNode node(final Shape s, final Paint p, final Stroke l) {
        final PNode n = new PPath(s, l);
        n.setPaint(p);
        return n;
    }
}
