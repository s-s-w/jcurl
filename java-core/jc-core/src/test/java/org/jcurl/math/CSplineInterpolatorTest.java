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
package org.jcurl.math;

import junit.framework.TestCase;

/**
 * @see org.jcurl.math.CSplineInterpolator
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id: CSplineInterpolatorTest.java 136 2005-10-03 22:05:16Z
 *          mrohrmoser $
 */
public class CSplineInterpolatorTest extends TestCase {

	public void test010() {
		final CSplineInterpolator ip = new CSplineInterpolator();
		ip.add(0, 0);
		ip.add(1, 1);
		ip.add(2, 0);
		ip.add(3, 1);
		ip.add(4, 0);
		ip.add(5, 1);
		ip.add(6, 0);
		ip.add(7, 1);
		ip.add(8, 0);
		assertEquals("", 0.0, ip.at(0, 0), 1e-6);
		assertEquals("", 0.774484, ip.at(0.5, 0), 1e-6);
		assertEquals("", 1.0, ip.at(1, 0), 1e-6);
		assertEquals("", 0.426546, ip.at(1.5, 0), 1e-6);
		assertEquals("", 0.0, ip.at(2, 0), 1e-6);
		assertEquals("", 0.519329, ip.at(2.5, 0), 1e-6);
		assertEquals("", 1.0, ip.at(3, 0), 1e-6);
		assertEquals("", 0.496134, ip.at(3.5, 0), 1e-6);
		assertEquals("", 0.0, ip.at(4, 0), 1e-6);
		assertEquals("", 0.496134, ip.at(4.5, 0), 1e-6);
		assertEquals("", 1.0, ip.at(5, 0), 1e-6);
		assertEquals("", 0.519329, ip.at(5.5, 0), 1e-6);
		assertEquals("", 0.0, ip.at(6, 0), 1e-6);
		assertEquals("", 0.426546, ip.at(6.5, 0), 1e-6);
		assertEquals("", 1.0, ip.at(7, 0), 1e-6);
		assertEquals("", 0.774484, ip.at(7.5, 0), 1e-6);
		assertEquals("", 0.0, ip.at(8, 0), 1e-6);
		try {
			ip.at(-0.1, 0);
			fail("Too small");
		} catch (final ArrayIndexOutOfBoundsException e) {
			;
		}
		try {
			ip.at(8.1, 0);
			fail("Too large");
		} catch (final ArrayIndexOutOfBoundsException e) {
			;
		}
		for (int i = 1000000; i >= 0; i--)
			ip.at(2, 0);
	}

	public void testBinarySearch() {
		final double[] a = { 0, 1, 2, 3, 4 };
		assertEquals("", 0, CurveCombined.binarySearch(a, 0, a.length - 1, 0));
		assertEquals("", 1, CurveCombined.binarySearch(a, 0, a.length - 1, 1));
		assertEquals("", 4, CurveCombined.binarySearch(a, 0, a.length - 1, 4));
		assertEquals("", -1, CurveCombined.binarySearch(a, 2, a.length - 1, 1));
		assertEquals("", -2, CurveCombined
				.binarySearch(a, 0, a.length - 1, 0.5));
	}
}