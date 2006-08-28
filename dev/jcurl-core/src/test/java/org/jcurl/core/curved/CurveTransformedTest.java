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

import junit.framework.TestCase;

import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.math.MathException;

public class CurveTransformedTest extends TestCase {

    public void testAffineTransformMemoryLayout() {
        Rock r = new RockDouble(1, 2, 3);
        AffineTransform at = new AffineTransform();
        double[] d = new double[6];
        at.getMatrix(d);
        assertEquals("", 1.0, d[0], 1e-9);
        assertEquals("", 0.0, d[1], 1e-9);
        assertEquals("", 0.0, d[2], 1e-9);
        assertEquals("", 1.0, d[3], 1e-9);
        assertEquals("", 0.0, d[4], 1e-9);
        assertEquals("", 0.0, d[5], 1e-9);
        at.transform(r = new RockDouble(2, 3, 4), r);
        TrivialCurlerTest.assertEquals(2, 3, 4, r, 1e-9);

        at = AffineTransform.getScaleInstance(0.5, 0.75);
        at.getMatrix(d);
        assertEquals("", 0.5, d[0], 1e-9);
        assertEquals("", 0.0, d[1], 1e-9);
        assertEquals("", 0.0, d[2], 1e-9);
        assertEquals("", 0.75, d[3], 1e-9);
        assertEquals("", 0.0, d[4], 1e-9);
        assertEquals("", 0.0, d[5], 1e-9);
        at.transform(r = new RockDouble(2, 3, 4), r);
        TrivialCurlerTest.assertEquals(1, 2.25, 4, r, 1e-9);

        at = AffineTransform.getTranslateInstance(0.5, 0.75);
        at.getMatrix(d);
        assertEquals("", 1.0, d[0], 1e-9);
        assertEquals("", 0.0, d[1], 1e-9);
        assertEquals("", 0.0, d[2], 1e-9);
        assertEquals("", 1.0, d[3], 1e-9);
        assertEquals("", 0.5, d[4], 1e-9);
        assertEquals("", 0.75, d[5], 1e-9);
        at.transform(r = new RockDouble(2, 3, 4), r);
        TrivialCurlerTest.assertEquals(2.5, 3.75, 4, r, 1e-9);
    }

    public void testAffineTransformRotate() {
        final Rock v0 = new RockDouble(1, 1.5, 0.3);
        final double[] d = { v0.getY(), -v0.getX(), v0.getX(), v0.getY(), 0, 0 };
        AffineTransform at = new AffineTransform(d);
        double v = v0.distance(0, 0);
        at.scale(1 / v, 1 / v);
        assertEquals(AffineTransform.TYPE_GENERAL_ROTATION, at.getType());
        assertEquals("", 1.0, at.getDeterminant(), 1e-9);
        assertEquals("", 0.8320502943378437, at.getScaleX(), 1e-9);
        assertEquals("", at.getScaleX(), at.getScaleY(), 1e-9);
        assertEquals("", 0.5547001962252291, at.getShearX(), 1e-9);
        assertEquals("", -at.getShearX(), at.getShearY(), 1e-9);
        assertEquals("", 0.0, at.getTranslateX(), 1e-9);
        assertEquals("", 0.0, at.getTranslateY(), 1e-9);
        Point2D p = null;
        p = at.transform(new Point2D.Double(1, 0), null);
        assertEquals("Point2D.Double[0.8320502943378437, -0.5547001962252291]",
                p.toString());
        assertEquals("", 1.0, p.distanceSq(0, 0), 1e-9);
        p = at.transform(new Point2D.Double(0, 1), null);
        assertEquals("Point2D.Double[0.5547001962252291, 0.8320502943378437]",
                p.toString());
        assertEquals("", 1.0, p.distanceSq(0, 0), 1e-9);
        p = at.transform(new Point2D.Double(0.75, 1.5), null);
        assertEquals("Point2D.Double[1.4560880150912265, 0.8320502943378438]",
                p.toString());
        p = at.transform(new Point2D.Double(1.5, 3.0), null);
        assertEquals("Point2D.Double[2.912176030182453, 1.6641005886756877]", p
                .toString());
    }

    /**
     * Test the transformation from a Rock Coordinates (rc) System at wc(3,3.5)
     * with positive y axis along wc(2,4.2) into World Coordinates (wc). Uses a
     * Point rc(5,1.3) = wc(8,2.5).
     */
    public void testAffineTransformRotateShift() {
        final Point2D p0_wc = new Point2D.Double(3, 3.5);
        final Rock v0_wc = new RockDouble(2, 4.2, 0.3);
        final double v = v0_wc.distance(0, 0);
        final double[] d = { v0_wc.getY(), -v0_wc.getX(), v0_wc.getX(),
                v0_wc.getY(), 0, 0 };
        AffineTransform at = new AffineTransform(d);
        at.scale(1 / v, 1 / v);
        assertEquals(AffineTransform.TYPE_GENERAL_ROTATION
                + AffineTransform.TYPE_UNIFORM_SCALE, at.getType());
        assertEquals("", 1.0, at.getDeterminant(), 1e-9);
        assertEquals("", 0.9028605188239303, at.getScaleX(), 1e-9);
        assertEquals("", at.getScaleX(), at.getScaleY(), 1e-9);
        assertEquals("", 0.42993358039234775, at.getShearX(), 1e-9);
        assertEquals("", -at.getShearX(), at.getShearY(), 1e-9);
        assertEquals("", 0, at.getTranslateX(), 1e-9);
        assertEquals("", 0, at.getTranslateY(), 1e-9);
        Point2D p = null;
        p = at.transform(new Point2D.Double(5, 1.3), null);
        assertEquals("Point2D.Double[5.073216248629703, -0.9759492274906292]",
                p.toString());

        at.preConcatenate(AffineTransform.getTranslateInstance(p0_wc.getX(),
                p0_wc.getY()));
        assertEquals(AffineTransform.TYPE_GENERAL_ROTATION
                + AffineTransform.TYPE_TRANSLATION
                + AffineTransform.TYPE_UNIFORM_SCALE, at.getType());
        assertEquals("", 1.0, at.getDeterminant(), 1e-9);
        assertEquals("", 0.9028605188239303, at.getScaleX(), 1e-9);
        assertEquals("", at.getScaleX(), at.getScaleY(), 1e-9);
        assertEquals("", 0.42993358039234775, at.getShearX(), 1e-9);
        assertEquals("", -at.getShearX(), at.getShearY(), 1e-9);
        assertEquals("", p0_wc.getX(), at.getTranslateX(), 1e-9);
        assertEquals("", p0_wc.getY(), at.getTranslateY(), 1e-9);

        p = at.transform(new Point2D.Double(5, 1.3), null);
        assertEquals("Point2D.Double[8.073216248629702, 2.524050772509371]", p
                .toString());
    }

    /**
     * Test the transformation from a Rock Coordinates (rc) System at wc(3,3.5)
     * with positive y axis along wc(2,4.2) into World Coordinates (wc). Uses a
     * Point rc(5,1.3) = wc(8,2.5).
     * 
     * @see CurveTransformed#create(Point2D, Point2D)
     */
    public void testCreate() {
        final Point2D p0_wc = new Point2D.Double(3, 3.5);
        final Rock v0_wc = new RockDouble(2, 4.2, 0.3);
        AffineTransform at = CurveTransformed.create(p0_wc, v0_wc);
        assertEquals(AffineTransform.TYPE_GENERAL_ROTATION
                + AffineTransform.TYPE_TRANSLATION, at.getType());
        assertEquals("", 1.0, at.getDeterminant(), 1e-9);
        assertEquals("", 0.9028605188239303, at.getScaleX(), 1e-9);
        assertEquals("", at.getScaleX(), at.getScaleY(), 1e-9);
        assertEquals("", 0.42993358039234775, at.getShearX(), 1e-9);
        assertEquals("", -at.getShearX(), at.getShearY(), 1e-9);
        assertEquals("", p0_wc.getX(), at.getTranslateX(), 1e-9);
        assertEquals("", p0_wc.getY(), at.getTranslateY(), 1e-9);

        Point2D rc = new Point2D.Double(5, 1.3);
        Point2D wc = at.transform(rc, null);
        assertEquals("Point2D.Double[8.073216248629704, 2.524050772509371]", wc
                .toString());

        // angle in rc:
        double ang = Math.atan2(rc.getY(), rc.getX());
        assertEquals("", 14.574216198038739, ang * 180 / Math.PI, 1e-9);

        // wc rotation:
        ang = Math.atan2(at.getShearY(), at.getScaleY());
        assertEquals("", -25.463345061871614, ang * 180 / Math.PI, 1e-9);
        final double[] d = new double[6];
        at.getMatrix(d);
        ang = Math.atan2(-d[2], d[3]);
        assertEquals("", -25.463345061871614, ang * 180 / Math.PI, 1e-9);

        // angle in wc:
        ang = Math.atan2(wc.getY(), wc.getX());
        assertEquals("", 17.36159358309492, ang * 180 / Math.PI, 1e-9);
    }

    public void testValueC0() throws MathException {
        Rock ret = null;
        AffineTransform at = AffineTransform.getScaleInstance(0.75, 1.25);
        CurveTransformed cw = new CurveTransformed(new CurveRockBase() {
            protected double value(double t, int derivative, int component) {
                switch (component) {
                case 0:
                    return t * 2.0;
                case 1:
                    return t * 1.5;
                case 2:
                    return t * 0.5;
                default:
                    throw new IllegalArgumentException();
                }
            }
        }, at, 0);
        ret = cw.value(0, ret);
        TrivialCurlerTest.assertEquals(0, 0, 0, ret, 1e-9);
        ret = cw.value(0.5, ret);
        TrivialCurlerTest.assertEquals(0.75, 0.9375, 0.25, ret, 1e-9);
        ret = cw.value(1.0, ret);
        TrivialCurlerTest.assertEquals(1.5, 1.875, 0.5, ret, 1e-9);
        ret = cw.value(1.5, ret);
        TrivialCurlerTest.assertEquals(2.25, 2.8125, 0.75, ret, 1e-9);
    }
}
