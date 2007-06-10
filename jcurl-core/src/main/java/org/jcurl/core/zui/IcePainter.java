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

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import org.jcurl.core.base.Zoomer;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * Creates a unpickable {@link PNode} displaying a sheet of ice with a <b>RIGHT
 * HANDED</b> coordinate system.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class IcePainter extends org.jcurl.core.swing.IcePainter {

    /**
     * Do it!
     * 
     * @return the new PNode
     */
    public static PNode create() {
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

    private static PNode node(final Shape s, final Paint p, final Stroke l) {
        final PNode n = new PPath(s, l);
        n.setPaint(p);
        n.setScale(1.0 / Zoomer.SCALE);
        n.setPickable(false);
        return n;
    }

    @Override
    public void paintIceWC(final Graphics2D g) {
        throw new UnsupportedOperationException();
    }
}