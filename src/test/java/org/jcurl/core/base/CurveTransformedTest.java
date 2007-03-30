/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.base;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.apache.commons.math.MathException;
import org.jcurl.core.swing.Zoomer;
import org.jcurl.math.Point3D;

public class CurveTransformedTest extends TestShowBase {

    private static void assertEquals(final double x, final double y,
            final double z, final Point3D p, final double delta) {
        final String txt = "";
        assertEquals(txt, x, p.getX(), delta);
        assertEquals(txt, y, p.getY(), delta);
        assertEquals(txt, z, p.getZ(), delta);
    }

    public void testAffineTransformMemoryLayout() {
        Rock r = new RockDouble(1, 2, 3);
        AffineTransform at = new AffineTransform();
        final double[] d = new double[6];
        at.getMatrix(d);
        assertEquals("", 1.0, d[0], 1e-9);
        assertEquals("", 0.0, d[1], 1e-9);
        assertEquals("", 0.0, d[2], 1e-9);
        assertEquals("", 1.0, d[3], 1e-9);
        assertEquals("", 0.0, d[4], 1e-9);
        assertEquals("", 0.0, d[5], 1e-9);
        at.transform(r = new RockDouble(2, 3, 4), r);
        assertEquals(2, 3, 4, r, 1e-9);

        at = AffineTransform.getScaleInstance(0.5, 0.75);
        at.getMatrix(d);
        assertEquals("", 0.5, d[0], 1e-9);
        assertEquals("", 0.0, d[1], 1e-9);
        assertEquals("", 0.0, d[2], 1e-9);
        assertEquals("", 0.75, d[3], 1e-9);
        assertEquals("", 0.0, d[4], 1e-9);
        assertEquals("", 0.0, d[5], 1e-9);
        at.transform(r = new RockDouble(2, 3, 4), r);
        assertEquals(1, 2.25, 4, r, 1e-9);

        at = AffineTransform.getTranslateInstance(0.5, 0.75);
        at.getMatrix(d);
        assertEquals("", 1.0, d[0], 1e-9);
        assertEquals("", 0.0, d[1], 1e-9);
        assertEquals("", 0.0, d[2], 1e-9);
        assertEquals("", 1.0, d[3], 1e-9);
        assertEquals("", 0.5, d[4], 1e-9);
        assertEquals("", 0.75, d[5], 1e-9);
        at.transform(r = new RockDouble(2, 3, 4), r);
        assertEquals(2.5, 3.75, 4, r, 1e-9);
    }

    public void testAffineTransformRotate() {
        final Rock v0 = new RockDouble(1, 1.5, 0.3);
        final double[] d = { v0.getY(), -v0.getX(), v0.getX(), v0.getY(), 0, 0 };
        final AffineTransform at = new AffineTransform(d);
        final double v = v0.distance(0, 0);
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
        final AffineTransform at = new AffineTransform(d);
        at.scale(1 / v, 1 / v);
        assertEquals(AffineTransform.TYPE_GENERAL_ROTATION
                + AffineTransform.TYPE_UNIFORM_SCALE, at.getType());
        assertEquals(1.0, at.getDeterminant());
        assertEquals(0.9028605188239303, at.getScaleX());
        assertEquals(at.getScaleX(), at.getScaleY());
        assertEquals(0.42993358039234775, at.getShearX());
        assertEquals(-at.getShearX(), at.getShearY());
        assertEquals(0, at.getTranslateX());
        assertEquals(0, at.getTranslateY());
        Point2D p = null;
        p = at.transform(new Point2D.Double(5, 1.3), null);
        assertEquals("Point2D.Double[5.073216248629703, -0.9759492274906292]",
                p.toString());

        at.preConcatenate(AffineTransform.getTranslateInstance(p0_wc.getX(),
                p0_wc.getY()));
        assertEquals(AffineTransform.TYPE_GENERAL_ROTATION
                + AffineTransform.TYPE_TRANSLATION
                + AffineTransform.TYPE_UNIFORM_SCALE, at.getType());
        assertEquals(1.0, at.getDeterminant());
        assertEquals(0.9028605188239303, at.getScaleX());
        assertEquals(at.getScaleX(), at.getScaleY());
        assertEquals(0.42993358039234775, at.getShearX());
        assertEquals(-at.getShearX(), at.getShearY());
        assertEquals(p0_wc.getX(), at.getTranslateX());
        assertEquals(p0_wc.getY(), at.getTranslateY());

        p = at.transform(new Point2D.Double(5, 1.3), null);
        assertEquals("Point2D.Double[8.073216248629702, 2.524050772509371]", p
                .toString());
    }

    /**
     * Test the transformation from a Rock Coordinates (rc) System at wc(3,3.5)
     * with positive y axis along wc(2,4.2) into World Coordinates (wc). Uses a
     * Point rc(5,1.3) = wc(8,2.5).
     * 
     * @see CurveTransformed#createRc2Wc(AffineTransform, Point2D, Point2D)
     */
    public void testCreateRc2Wc() {
        final Point2D p0_wc = new Point2D.Double(3, 3.5);
        final Rock v0_wc = new RockDouble(2, 4.2, 0.3);
        final AffineTransform at = CurveTransformed.createRc2Wc(null, p0_wc,
                v0_wc);
        assertEquals(AffineTransform.TYPE_GENERAL_ROTATION
                + AffineTransform.TYPE_TRANSLATION, at.getType());
        assertEquals(1.0, at.getDeterminant());
        assertEquals(0.9028605188239303, at.getScaleX());
        assertEquals(at.getScaleX(), at.getScaleY());
        assertEquals(0.42993358039234775, at.getShearX());
        assertEquals(-at.getShearX(), at.getShearY());
        assertEquals(p0_wc.getX(), at.getTranslateX());
        assertEquals(p0_wc.getY(), at.getTranslateY());

        final Point2D rc = new Point2D.Double(5, 1.3);
        final Point2D wc = at.transform(rc, null);
        assertEquals("Point2D.Double[8.073216248629704, 2.524050772509371]", wc
                .toString());

        // angle in rc:
        double ang = Math.atan2(rc.getY(), rc.getX());
        assertEquals(14.574216198038739, rad2deg(ang));

        // wc rotation:
        ang = Math.atan2(at.getShearY(), at.getScaleY());
        assertEquals(-25.463345061871614, rad2deg(ang));
        final double[] d = new double[6];
        at.getMatrix(d);
        ang = Math.atan2(-d[2], d[3]);
        assertEquals(-25.463345061871614, rad2deg(ang));

        // angle in wc:
        ang = Math.atan2(wc.getY(), wc.getX());
        assertEquals(17.36159358309492, rad2deg(ang));
    }

    public void testValueC0() throws MathException {
        Rock ret = null;
        final AffineTransform at = AffineTransform.getScaleInstance(0.75, 1.25);
        final CurveTransformed cw = new CurveTransformed(new CurveRock() {
            public double at(int component, int derivative, double t) {
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
        ret = cw.at(0, 0, ret);
        assertEquals(0, 0, 0, ret, 1e-9);
        ret = cw.at(0, 0.5, ret);
        assertEquals(0.75, 0.9375, 0.25, ret, 1e-9);
        ret = cw.at(0, 1.0, ret);
        assertEquals(1.5, 1.875, 0.5, ret, 1e-9);
        ret = cw.at(0, 1.5, ret);
        assertEquals(2.25, 2.8125, 0.75, ret, 1e-9);
    }

    public void testStill() {
        final PositionSet p = PositionSet.allOut();
        final CurveTransformed[] c = new CurveTransformed[6];
        final AffineTransform[] m = new AffineTransform[c.length];
        int k = -1;
        // Plain & straight
        m[++k] = new AffineTransform();
        c[k] = new CurveTransformed(CurveRock.still(0.25, 1, 0), m[k], 0);
        m[++k] = new AffineTransform();
        m[k].translate(0.5, 1);
        c[k] = new CurveTransformed(CurveRock.still(0, 0, 0), m[k], 0);

        // Plain & looking left
        m[++k] = new AffineTransform();
        c[k] = new CurveTransformed(CurveRock.still(0.25, 1.5, 0.25 * Math.PI),
                m[k], 0);
        m[++k] = new AffineTransform();
        m[k].translate(0.5, 1.5);
        m[k].rotate(0.25 * Math.PI);
        c[k] = new CurveTransformed(CurveRock.still(0, 0, 0), m[k], 0);

        // createRc2Wc & looking left
        m[++k] = new AffineTransform();
        c[k] = new CurveTransformed(CurveRock.still(0.25, 2, 0.25 * Math.PI),
                m[k], 0);
        m[++k] = CurveTransformed.createRc2Wc(null, new Point2D.Double(0.5, 2),
                new Point2D.Double(-1, 1));
        c[k] = new CurveTransformed(CurveRock.still(0, 0, 0), m[k], 0);

        final double[] tmp = { 0, 0, 0 };
        for (int i = c.length - 1; i >= 0; i--)
            p.getRock(i).setLocation(c[i].at(0, 0, tmp));
        // Check if "sibling" rocks look alike:
        showPositionDisplay(p, Zoomer.C12, 5000, new TimeRunnable() {
            public void run(final double t) throws InterruptedException {
                p.notifyChange();
                Thread.sleep(1500);
            }
        });

        assertEquals(0.25, 1, 0, p.getRock(0), 1e-6);
        assertEquals(0.5, 1, 0, p.getRock(1), 1e-6);

        assertEquals(0.25, 1.5, 0.25 * Math.PI, p.getRock(2), 1e-6);
        assertEquals(0.5, 1.5, 0.25 * Math.PI, p.getRock(3), 1e-6);

        assertEquals(0.25, 2, 0.25 * Math.PI, p.getRock(4), 1e-6);
        assertEquals(0.5, 2, 0.25 * Math.PI, p.getRock(5), 1e-6);

    }
}
