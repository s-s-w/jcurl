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

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.RockProps;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockPainter {

    final static Point2D.Float RadiusWC = new Point2D.Float(-2
            * RockProps.DEFAULT.getRadius(), -2 * RockProps.DEFAULT.getRadius());

    private static void paintRock(final Graphics g, final AffineTransform mat,
            final Rock rock, final boolean isDark, final int idx) {
        final Point2D centerwc = rock;
        final Point2D.Float center = new Point2D.Float();
        mat.transform(centerwc, center);
        final Point2D.Float radius = new Point2D.Float();
        mat.deltaTransform(RadiusWC, radius);
        g.drawArc((int) center.x, (int) center.y, (int) radius.x,
                (int) radius.y, 0, 360);
    }

    void paintRocks(final Graphics g, final AffineTransform mat,
            final RockSet rocks) {
        for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
            paintRock(g, mat, rocks.getDark(i), true, i);
            paintRock(g, mat, rocks.getLight(i), false, i);
        }
    }

}