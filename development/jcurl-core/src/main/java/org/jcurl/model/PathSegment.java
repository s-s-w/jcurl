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
package org.jcurl.model;

import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.Rock;
import org.jcurl.core.RockDouble;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.math.analysis.R1R1Function;
import org.jcurl.math.analysis.R1RnCurve;

/**
 * R^3 Curve with one component for x and y each plus one for rotation.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PathSegment extends R1RnCurve implements JCurlCurve {

    private final boolean doTransform;

    private final double t0;

    private transient double[] tmp = null;

    private final double vx0;

    private final double vy0;

    private final double x0;

    private final double y0;

    protected PathSegment(boolean tr, double t0, double x0, double y0,
            double vx0, double vy0, final R1R1Function[] c) {
        super(c);
        if (c.length != 3)
            throw new IllegalArgumentException(
                    "rock path curve must have 3 dimensions, but had "
                            + c.length);
        this.doTransform = tr;
        this.t0 = t0;
        this.x0 = x0;
        this.y0 = y0;
        this.vx0 = vx0;
        this.vy0 = vy0;
    }

    public PathSegment(double t0, double x0, double y0, double vx0, double vy0,
            final R1R1Function[] c) {
        this(true, t0, x0, y0, vx0, vy0, c);
    }

    public PathSegment(final R1R1Function[] c) {
        this(false, 0, 0, 0, 0, 0, c);
    }

    public Rock value(double t, Rock dst) throws FunctionEvaluationException {
        return valueWC(t, dst);
    }

    public Rock valueRC(double t, Rock dst) throws FunctionEvaluationException {
        throw new NotImplementedYetException();
    }

    public Rock valueWC(double t, Rock dst) throws FunctionEvaluationException {
        tmp = super.value(t, tmp);
        if (dst == null)
            dst = new RockDouble();
        dst.setLocation(tmp[0], tmp[1], tmp[2]);
        return dst;
    }
}
