/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.core.model;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.jcurl.core.base.ColliderBase;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.math.MathVec;

/**
 * @see org.jcurl.core.model.CollissionSimple
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CollissionSimpleTest.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CollissionSimpleTest extends TestCase {

    private static final CollissionSimple hit = new CollissionSimple();

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(CollissionSimpleTest.class);
    }

    final Rock av;

    final Rock ax;

    final Rock bv;

    final Rock bx;

    final PositionSet pos;

    final SpeedSet speed;

    public CollissionSimpleTest() {
        pos = PositionSet.allHome();
        speed = new SpeedSet(pos);
        ax = pos.getDark(0);
        bx = pos.getLight(0);
        av = speed.getDark(0);
        bv = speed.getLight(0);
    }

    public void _test010_straight() {
        ax.setLocation(0, -2 * RockProps.DEFAULT.getRadius());
        bx.setLocation(0, 0);
        av.setLocation(0, 1);

        final int ret = hit.compute(pos, speed, null);
        assertEquals(3, ret);
        assertEquals("a.v.x", 0.0, av.getX(), 1e-6);
        assertEquals("a.v.y", 0.0, av.getY(), 1e-6);
        assertEquals("buf.v.x", 0.0, bv.getX(), 1e-6);
        assertEquals("buf.v.y", 1.0, bv.getY(), 1e-6);

        assertEquals("a.x.x", 0.0, ax.getX(), 1e-6);
        assertEquals("a.x.y", -0.3048, ax.getY(), 1e-6);
        assertEquals("buf.x.x", 0.0, bx.getX(), 1e-6);
        assertEquals("buf.x.y", 0.0, bx.getY(), 1e-6);
    }

    @Override
    public void setUp() {
        PositionSet.allHome(pos);
        RockSet.allZero(speed);
    }

    public void test005_math() {
        final Point2D a = new Point2D.Double(1, 1);
        final Point2D b = new Point2D.Double(0.3, 0);

        final double scal = MathVec.scal(a, b);
        final double bb = MathVec.scal(b, b);

        MathVec.mult(scal / bb, b, b);
        assertEquals("", 1, b.getX(), 1e-6);
        assertEquals("", 0, b.getY(), 1e-6);
    }

    public void test020_45() {
        ax.setLocation(0, -2 * RockProps.DEFAULT.getRadius());
        bx.setLocation(0, 0);
        av.setLocation(1, 1);

        final int ret = hit.compute(pos, speed, null);
        assertEquals(3, ret);
        assertEquals("a.v.x", 1.0, av.getX(), 1e-6);
        assertEquals("a.v.y", 0.0, av.getY(), 1e-6);
        assertEquals("buf.v.x", 0.0, bv.getX(), 1e-6);
        assertEquals("buf.v.y", 1.0, bv.getY(), 1e-6);

        assertEquals("a.x.x", 0.0, ax.getX(), 1e-6);
        assertEquals("a.x.y", -0.3048, ax.getY(), 1e-6);
        assertEquals("buf.x.x", 0.0, bx.getX(), 1e-6);
        assertEquals("buf.x.y", 0.0, bx.getY(), 1e-6);
    }

    public void test030_getTrafo() {
        final AffineTransform mat = new AffineTransform();
        final double[] flat = new double[6];
        Point2D a = new Point2D.Double(0, 0);
        Point2D b = new Point2D.Double(0, 1);
        ColliderBase.getInverseTrafo(a, a, b, mat);
        assertTrue(mat.isIdentity());

        a = new Point2D.Double(0, -1);
        b = new Point2D.Double(0, 1);
        ColliderBase.getInverseTrafo(a, a, b, mat);
        mat.getMatrix(flat);
        assertEquals("", 1, flat[0], 1e-9);
        assertEquals("", 0, flat[1], 1e-9);
        assertEquals("", 0, flat[2], 1e-9);
        assertEquals("", 1, flat[3], 1e-9);
        assertEquals("", 0, flat[4], 1e-9);
        assertEquals("", 1, flat[5], 1e-9);

        a = new Point2D.Double(1, 1);
        b = new Point2D.Double(1.1, 1.1);
        ColliderBase.getInverseTrafo(a, a, b, mat);
        mat.getMatrix(flat);
        double sq2 = Math.sqrt(2);
        assertEquals("", sq2 / 2, flat[0], 1e-9);
        assertEquals("", -sq2 / 2, flat[1], 1e-9);
        assertEquals("", sq2 / 2, flat[2], 1e-9);
        assertEquals("", sq2 / 2, flat[3], 1e-9);
        assertEquals("", -1.41421356237, flat[4], 1e-9);
        assertEquals("", 0, flat[5], 1e-9);
    }
}