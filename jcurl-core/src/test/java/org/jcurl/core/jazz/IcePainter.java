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
package org.jcurl.core.jazz;

import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import org.jcurl.core.base.Zoomer;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * Strategy to paint the ice sheet in world coordinates.
 * 
 * @see org.jcurl.core.swing.RockPainter
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:IcePainter.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class IcePainter extends org.jcurl.core.swing.IcePainter {

    public static PNode create() {
        final PNode r = new PPath();
        r.scale(1.0 / Zoomer.SCALE);
        r.addChild(node(hog2hog, colors.hog2hog, colors.stroke));
        r.addChild(node(hog2tee, colors.hog2tee, colors.stroke));
        r.addChild(node(centerLe, colors.contours, colors.stroke));
        r.addChild(node(centerRi, colors.contours, colors.stroke));
        r.addChild(node(tee2back, colors.tee2back, colors.stroke));
        r.addChild(node(C12, colors.c12, null));
        r.addChild(node(C8, colors.c8, null));
        r.addChild(node(C4, colors.c4, null));
        r.addChild(node(C1, colors.c1, null));
        r.addChild(node(center, colors.contours, colors.stroke));
        r.addChild(node(centerLeft, colors.contours, colors.stroke));
        r.addChild(node(centerRight, colors.contours, colors.stroke));
        r.addChild(node(back, colors.contours, colors.stroke));
        r.addChild(node(tee, colors.contours, colors.stroke));
        r.addChild(node(nearHog, colors.contours, colors.stroke));
        r.addChild(node(farHog, colors.contours, colors.stroke));
        return r;
    }

    static PNode node(final Shape s, final Paint p, final Stroke l) {
        final PNode n = new PPath(s, l);
        n.setPaint(p);
        return n;
    }
}