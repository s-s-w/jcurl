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

import junit.framework.TestCase;

import org.jcurl.core.dto.Rock;
import org.jcurl.core.dto.RockDouble;
import org.jcurl.core.math.MathException;

public class CurveTransformedTest extends TestCase {
    public void testAffineTransform() {
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
