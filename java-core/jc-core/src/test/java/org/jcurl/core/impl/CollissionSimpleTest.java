/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.core.impl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.math.MathVec;

/**
 * @see org.jcurl.core.impl.CollissionSimple
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CollissionSimpleTest.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CollissionSimpleTest extends TestCase {

	private static final CollissionSimple hit = new CollissionSimple();

	final Rock<Vel> av;

	final Rock<Pos> ax;

	final Rock<Vel> bv;

	final Rock<Pos> bx;

	final RockSet<Pos> pos;

	final RockSet<Vel> speed;

	public CollissionSimpleTest() {
		pos = RockSetUtils.allHome();
		speed = RockSet.allZero(null);
		ax = pos.getDark(0);
		bx = pos.getLight(0);
		av = speed.getDark(0);
		bv = speed.getLight(0);
	}

	public void _test010_straight() {
		ax.setLocation(0, -2 * RockProps.DEFAULT.getRadius(), 0);
		bx.setLocation(0, 0, 0);
		av.setLocation(0, 1, 0);

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
		RockSetUtils.allHome(pos);
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
		ax.setLocation(0, -2 * RockProps.DEFAULT.getRadius(), 0);
		bx.setLocation(0, 0, 0);
		av.setLocation(1, 1, 0);

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