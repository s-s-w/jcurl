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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.dto.Rock;
import org.jcurl.core.dto.RockDouble;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.math.analysis.R1R1Function;
import org.jcurl.math.analysis.R1RnCurve;

/**
 * R^3 Curve with one component for x and y each plus one for rotation. Manages
 * Rock Coordinates &lt;-&gt; World Coordinates transformation.
 * 
 * @see java.awt.geom.AffineTransform
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PathSegment extends R1RnCurve implements JCurlCurve {

    private final boolean isRockCoordinates;

    private final AffineTransform rc2wc;

    private final double t0;

    private transient double[] tmp = null;

    protected PathSegment(boolean isRockCoordinates, double t0, double x_0,
            double y_0, double vx0, double vy0, final R1R1Function[] c) {
        this(isRockCoordinates, t0, new Point2D.Double(x_0, y_0),
                new Point2D.Double(vx0, vy0), c);
    }

    protected PathSegment(boolean isRockCoordinates, double t0, Point2D x0,
            Point2D v0, final R1R1Function[] c) {
        super(c);
        if (c.length != 3)
            throw new IllegalArgumentException(
                    "rock path curve must have 3 dimensions, but had "
                            + c.length);
        this.isRockCoordinates = isRockCoordinates;
        this.rc2wc = new AffineTransform();
        rc2wc.rotate(-Math.acos((v0.getX() * 0 + v0.getY() * 1)
                / v0.distance(0, 0)), x0.getX(), x0.getY());
        rc2wc.translate(x0.getX(), x0.getY());
        this.t0 = t0;
    }

    /**
     * 
     * @param t0
     *            [0,x[
     * @param x0
     * @param y0
     * @param vx0
     * @param vy0
     * @param c
     *            curves in rock coordinates
     */
    public PathSegment(double t0, double x0, double y0, double vx0, double vy0,
            final R1R1Function[] c) {
        this(true, t0, x0, y0, vx0, vy0, c);
    }

    /**
     * @param t0
     *            [0,x[
     * @param c
     *            curves in world coordinates
     */
    public PathSegment(double t0, final R1R1Function[] c) {
        super(c);
        throw new NotImplementedYetException();
    }

    /**
     * @see #valueWC(double, Rock)
     */
    public Rock value(double t, Rock dst) throws FunctionEvaluationException {
        return valueWC(t, dst);
    }

    /**
     * @param t
     *            [t0, x[
     */
    public Rock valueRC(double t, Rock dst) throws FunctionEvaluationException {
        try {
            tmp = super.value(t - t0, tmp);
            if (dst == null)
                dst = new RockDouble();
            dst.setLocation(tmp[0], tmp[1], tmp[2]);
            if (!isRockCoordinates)
                rc2wc.inverseTransform(dst, dst);
            return dst;
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException("Trafo MUST be invertible.", e);
        }
    }

    /**
     * @param t
     *            [t0, x[
     */
    public Rock valueWC(double t, Rock dst) throws FunctionEvaluationException {
        tmp = super.value(t - t0, tmp);
        if (dst == null)
            dst = new RockDouble();
        dst.setLocation(tmp[0], tmp[1], tmp[2]);
        if (isRockCoordinates)
            rc2wc.transform(dst, dst);
        return dst;
    }

    public double getT0() {
        return t0;
    }
}
