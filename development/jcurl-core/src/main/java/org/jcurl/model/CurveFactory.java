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

import org.jcurl.core.Rock;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.math.analysis.R1R1Function;

/**
 * Base class to create "curled" curves in rock coordinates. Each
 * {@link org.jcurl.model.PathSegment} is responsible for RC&lt;-&gt;WC
 * transformation.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CurveFactory extends MutableObject {

    private double curl;

    private double tee;

    /**
     * Default curl is 1m and default draw-to-tee-time is 24s.
     * 
     */
    public CurveFactory() {
        curl = 1;
        tee = 24;
    }

    /**
     * Do the real work and get the path in rock-coordinates.
     * 
     * @param v0Square
     * @param w0
     * @return -
     */
    abstract R1R1Function[] compute(final double v0Square, final double w0);

    PathSegment compute(final double t0, final double x0, final double y0,
            final double a0, final double vx0, final double vy0,
            final double w0, final double sweepFactor) {
        return new PathSegment(t0, x0, y0, vx0, vy0, compute(vx0 * vx0 + vy0
                * vy0, w0));
    }

    public PathSegment compute(final double t0, final Rock x0, final Rock v0,
            final double sweepFactor) {
        return compute(t0, x0.getX(), x0.getY(), x0.getZ(), v0.getX(), v0
                .getY(), v0.getZ(), sweepFactor);
    }

    public double getCurl() {
        return curl;
    }

    public double getTee() {
        return tee;
    }

    public void setCurl(double curl) {
        propChange.firePropertyChange("curl", this.curl, curl);
        this.curl = curl;
    }

    public void setTee(double tee) {
        propChange.firePropertyChange("tee", this.tee, tee);
        this.tee = tee;
    }

    /**
     * The split-time
     * 
     * @param y0
     */
    public double splittime(double y0) {
        throw new NotImplementedYetException();
    }
}
