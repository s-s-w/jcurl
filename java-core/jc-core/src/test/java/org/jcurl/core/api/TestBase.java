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
package org.jcurl.core.api;

import junit.framework.TestCase;

public abstract class TestBase extends TestCase {

	protected void assertEquals(final double expected, final double found) {
		final double precision = 1e-9;
		assertEquals("expected:<" + expected + "> +/-:<" + precision
				+ "> but was:<" + found + ">", expected, found, precision);
	}

	protected double rad2deg(final double rad) {
		return 180 * rad / Math.PI;
	}
}
