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

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Simple class to paint graphical objects with custom parameter semantics.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Painter {

    static void rectDC(final Graphics g, final Point2D.Float a,
            final Point2D.Float b) {
        final float x = a.x < b.x ? a.x : b.x;
        final float y = a.y < b.y ? a.y : b.y;
        float width = Math.abs(b.x - a.x);
        float height = Math.abs(b.y - a.y);
        g.drawRect((int) x, (int) y, (int) width, (int) height);
    }

    static void circleDC(final Graphics g, final Point2D.Float a,
            final Point2D.Float b) {
        float width = 2 * Math.abs(b.x - a.x);
        float height = 2 * Math.abs(b.y - a.y);
        float tlx = a.x - width / 2;
        float tly = a.y - height / 2;
        g.drawArc((int) (tlx), (int) (tly), (int) (width), (int) (height), 0,
                360);
    }

    static void circleDC(final Graphics g, final Point2D c, final double r) {
        circleDC(g, c.getX(), c.getY(), r, r);
    }

    static void circleDC(final Graphics g, final Point2D c, final double rx,
            final double ry) {
        circleDC(g, c.getX(), c.getY(), rx, ry);
    }

    static void circleDC(final Graphics g, final double cx, final double cy,
            final double rx, final double ry) {
        final int x = (int) (cx - rx);
        final int y = (int) (cy - ry);
        final int dx = (int) (2 * rx);
        final int dy = (int) (2 * ry);
        g.drawArc(x, y, dx, dy, 0, 360);
    }

    static void lineDC(final Graphics g, final Point2D.Float a,
            final Point2D.Float b) {
        g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
    }

    static void wc2dc(final AffineTransform mat, final Point2D[] wc,
            final Point2D[] dc) {
        for (int i = wc.length - 1; i >= 0; i--) {
            final Point2D pwc = wc[i];
            if (pwc == null)
                continue;
            dc[i] = mat.transform(pwc, dc[i]);
        }
    }
}