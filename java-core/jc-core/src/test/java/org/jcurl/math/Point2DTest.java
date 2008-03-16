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
package org.jcurl.math;

import java.awt.geom.Point2D;

import junit.framework.TestCase;

public class Point2DTest extends TestCase {

    public void testEqualsObject() {
        final Point2D pd = new Point2D.Double(0, 1);
        final Point2D pf = new Point2D.Float(0, 1);

        assertEquals(pd, pd);
        assertEquals(pf, pf);

        assertEquals(pd, pf);
        assertEquals(pf, pd);
    }
}
