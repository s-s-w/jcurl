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

import java.util.Map.Entry;

import junit.framework.TestCase;

public class CurveCombinedTest extends TestCase {
	public void testIterator() {
		final CurveCombined<R1RNFunction> cc = new CurveCombined<R1RNFunction>(
				1);
		cc.add(0, new Polynome(new double[] { 0 }), true);
		cc.add(1, new Polynome(new double[] { 1 }), true);
		cc.add(2, new Polynome(new double[] { 2 }), true);

		int i = 0;
		for (final Entry<Double, R1RNFunction> element : cc) {
			System.out.println(element);
			i++;
		}
	}
}
