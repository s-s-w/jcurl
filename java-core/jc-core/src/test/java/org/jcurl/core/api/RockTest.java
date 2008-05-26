/*
 * jcurl java curling software framework http://www.jcurl.org
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
package org.jcurl.core.api;

import java.awt.geom.AffineTransform;

public class RockTest extends TestBase {

	static Rock m2r(final AffineTransform m, Rock r) {
		if (r == null)
			r = new RockDouble();
		r.setLocation(m.getTranslateX(), m.getTranslateY(), Math.asin(m
				.getShearY()));
		return r;
	}

	static AffineTransform r2m(final Rock r, AffineTransform m) {
		if (m == null)
			m = new AffineTransform();
		else
			m.setToIdentity();
		m.translate(r.getX(), r.getY());
		m.rotate(r.getA());
		return m;
	}

	private void assertEquals(final double x, final double y, final double z,
			final Rock was) {
		assertEquals(x, was.getX());
		assertEquals(y, was.getY());
		assertEquals(z, was.getA());
	}

	public void testMatrix() {
		final Rock r = new RockDouble(0, 0, 0);
		final AffineTransform m = new AffineTransform();
		r.setLocation(0, 0, 0);
		r2m(r, m);
		m2r(m, r);
		assertEquals(0, 0, 0, r);

		r.setLocation(1, 2, 1.5);
		r2m(r, m);
		m2r(m, r);
		System.out.println(m);
		assertEquals(1, 2, 1.5, r);
	}
}
