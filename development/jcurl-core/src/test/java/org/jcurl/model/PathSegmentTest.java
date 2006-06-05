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
import org.jcurl.math.analysis.Polynome;
import org.jcurl.math.analysis.R1R1Function;
import org.jcurl.math.analysis.R1RnCurve;

import junit.framework.TestCase;

public class PathSegmentTest extends TestCase {

    public void testTrafo() throws NoninvertibleTransformException {
        final Point2D x0 = new Point2D.Double(1.5, 2.5);
        // final Point2D x0 = new Point2D.Double(0, 0);
        final Point2D v0 = new Point2D.Double(2, 1);
        // build the trafo
        final AffineTransform rc2wc = new AffineTransform();
        {
            rc2wc.rotate(-Math.acos((v0.getX() * 0 + v0.getY() * 1)
                    / v0.distance(0, 0)), x0.getX(), x0.getY());
            rc2wc.translate(x0.getX(), x0.getY());
        }
        // check some points.
        // wc(x0) -> rc(0,0)
        Point2D tmp = rc2wc.inverseTransform(x0, null);
        assertEquals("", 0, tmp.getX(), 1e-9);
        assertEquals("", 0, tmp.getY(), 1e-9);

        // rc(0,1) -> wc(x0)
        tmp = rc2wc.transform(new Point2D.Double(0, 1), null);
        assertEquals("", x0.getX() + 0.8944271909999, tmp.getX(), 1e-6);
        assertEquals("", x0.getY() + 0.4472135954999, tmp.getY(), 1e-6);
    }

    public void testRC2RC() throws FunctionEvaluationException {
        final PathSegment p;
        {
            final R1R1Function[] f = new R1R1Function[3];
            final R1R1Function[] tmp = R1RnCurve.straightLine(0.5, 1.25)
                    .components();
            f[0] = tmp[0];
            f[1] = tmp[1];
            f[2] = new Polynome(new double[] { 0 });
            p = new PathSegment(3, 1, 2, 3, 4, f);
        }
        assertEquals("", 3.0, p.getT0(), 1e-9);
        final Rock x = new RockDouble();

        p.valueRC(0, x);
        assertEquals("", -3.0, x.getX(), 1e-9);
        assertEquals("", -3.25, x.getY(), 1e-9);
        assertEquals("", 0.0, x.getZ(), 1e-9);

        p.valueRC(p.getT0(), x);
        assertEquals("", 0.0, x.getX(), 1e-9);
        assertEquals("", 0.5, x.getY(), 1e-9);
        assertEquals("", 0, x.getZ(), 1e-9);

        p.valueRC(2 * p.getT0(), x);
        assertEquals("", 3.0, x.getX(), 1e-9);
        assertEquals("", 4.25, x.getY(), 1e-9);
        assertEquals("", 0, x.getZ(), 1e-9);
    }

    public void testRC2WC() throws FunctionEvaluationException {
        final PathSegment p;
        {
            final R1R1Function[] f = new R1R1Function[3];
            final R1R1Function[] tmp = R1RnCurve.straightLine(0.5, 1.25)
                    .components();
            f[0] = tmp[0];
            f[1] = tmp[1];
            f[2] = new Polynome(new double[] { 0 });
            p = new PathSegment(3, 1, 2, 3, 4, f);
        }
        assertEquals("", 3.0, p.getT0(), 1e-9);
        final Rock x = new RockDouble();

        p.valueWC(0, x);
        assertEquals("", -3.349999999999999, x.getX(), 1e-9);
        assertEquals("", 1.19999999999999, x.getY(), 1e-9);
        assertEquals("", 0, x.getZ(), 1e-9);

        p.valueWC(p.getT0(), x);
        assertEquals("", 1.299999999999999999, x.getX(), 1e-9);
        assertEquals("", 2.4, x.getY(), 1e-9);
        assertEquals("", 0, x.getZ(), 1e-9);

        p.valueWC(2 * p.getT0(), x);
        assertEquals("", 5.94999999999999, x.getX(), 1e-9);
        assertEquals("", 3.60, x.getY(), 1e-9);
        assertEquals("", 0, x.getZ(), 1e-9);
    }
}
