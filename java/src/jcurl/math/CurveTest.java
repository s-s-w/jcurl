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
package jcurl.math;

import junit.framework.TestCase;

/**
 * @see jcurl.math.CurveBase
 * @see jcurl.math.CurveDom
 * @see jcurl.math.CurveParts
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(CurveTest.class);
    }

    public void test010() {
        Polynome po = Polynome.getPoly(1.0, 2.0, 3.0, 4.0);
        assertEquals("", 2.0, po.getC(0, 1.0), 1e-9);
        assertEquals("", 6.0, po.getC(0, 1.5), 1e-9);
        assertEquals("", 11.0, po.getC(0, 2.0), 1e-9);
    }
}