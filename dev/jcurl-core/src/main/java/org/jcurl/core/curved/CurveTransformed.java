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

import org.jcurl.core.dto.Rock;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.math.MathException;

/**
 * Decorator to apply an {@link AffineTransform} and a time-shift.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveTransformed implements CurveRock {

    private final CurveRock c;

    private final double[] p = new double[6];

    private final double t0;

    private final double rot;

    public CurveTransformed(CurveRock c, AffineTransform t, double t0) {
        this.t0 = t0;
        this.c = c;
        t.getMatrix(this.p);
        rot = 0; // t.getDeterminant();
    }

    public int dimension() {
        return 3;
    }

    public double[] value(double t, double[] ret) throws MathException {
        return value(t, 0, ret);
    }

    public double[] value(double t, int derivative, double[] ret)
            throws MathException {
        t -= t0;
        throw new NotImplementedYetException();
    }

    public Rock value(double t, int derivative, Rock ret) throws MathException {
        t -= t0;
        ret = c.value(t, derivative, ret);
        if (derivative < 1) {
            final double x = p[0] * ret.getX() + p[2] * ret.getY() + p[4];
            final double y = p[1] * ret.getX() + p[3] * ret.getY() + p[5];
            final double z = ret.getZ() + rot;
            ret.setLocation(x, y, z);
        } else {
            final double x = p[0] * ret.getX() + p[2] * ret.getY();
            final double y = p[1] * ret.getX() + p[3] * ret.getY();
            ret.setLocation(x, y);
        }
        return ret;
    }

    public Rock value(double t, Rock ret) throws MathException {
        return value(t, 0, ret);
    }
}
