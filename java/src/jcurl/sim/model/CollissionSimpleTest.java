/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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
package jcurl.sim.model;

import java.awt.geom.Point2D;

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.RockProps;
import jcurl.math.MathVec;
import junit.framework.TestCase;

/**
 * @see jcurl.sim.model.CollissionSimple
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CollissionSimpleTest extends TestCase {

    final RockSet pos;

    final RockSet speed;

    final Rock ax;

    final Rock bx;

    final Rock av;

    final Rock bv;

    public CollissionSimpleTest() {
        pos = RockSet.allHome();
        speed = new RockSet();
        ax = pos.getDark(0);
        bx = pos.getLight(0);
        av = speed.getDark(0);
        bv = speed.getLight(0);
    }

    public void setUp() {
        RockSet.allHome(pos);
        RockSet.allZero(speed);
    }

    private static final CollissionSimple hit = new CollissionSimple();

    public static void main(String[] args) {
        junit.textui.TestRunner.run(CollissionSimpleTest.class);
    }

    public void test005_math() {
        Point2D a = new Point2D.Double(1, 1);
        Point2D b = new Point2D.Double(0.3, 0);

        double scal = MathVec.scal(a, b);
        double bb = MathVec.scal(b, b);

        MathVec.mult(scal / bb, b, b);
        assertEquals("", 1, b.getX(), 1e-6);
        assertEquals("", 0, b.getY(), 1e-6);
    }

    public void _test010_straight() {
        ax.setLocation(0, -2 * RockProps.DEFAULT.getRadius());
        bx.setLocation(0, 0);
        av.setLocation(0, 1);

        int ret = hit.compute(pos, speed);
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

    public void test020_45() {
        ax.setLocation(0, -2 * RockProps.DEFAULT.getRadius());
        bx.setLocation(0, 0);
        av.setLocation(1, 1);

        int ret = hit.compute(pos, speed);
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
}