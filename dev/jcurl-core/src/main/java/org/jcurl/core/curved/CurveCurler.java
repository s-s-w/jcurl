/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.core.curved;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jcurl.core.dto.Curler;
import org.jcurl.core.dto.Rock;

public abstract class CurveCurler implements Curler {

    public CurveTransformed computeWC(double t0, final Rock x0, final Rock v0) {
        final double v = v0.distance(0, 0);
        final Point2D v1;
        if (v == 0)
            v1 = new Point2D.Double(0, 1);
        else
            v1 = new Point2D.Double(v0.getX() / v, v0.getY() / v);
        final AffineTransform at = new AffineTransform();
        at.rotate(0);
        at.translate(x0.getX(), x0.getY());
        return new CurveTransformed(computeRC(v, v0.getZ()), at, t0);
    }

    public abstract CurveRock computeRC(final double v, final double omega);
}
