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
 * JUnit Test
 * 
 * @see jcurl.math.Polynome
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PolynomeTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PolynomeTest.class);
    }

    public void test010() {
        Polynome po = Polynome.getPoly(1.0, 2.0, 3.0, 4.0);
        assertEquals("", 2.0, po.getC(0, 1.0), 1e-9);
        assertEquals("", 3.0, po.getC(0, 1.5), 1e-9);
        assertEquals("", 4.0, po.getC(0, 2.0), 1e-9);

        assertEquals("", 5.0, po.getC(1, 1.0), 1e-9);
        assertEquals("", 6.0, po.getC(1, 1.5), 1e-9);
        assertEquals("", 7.0, po.getC(1, 2.0), 1e-9);

        assertEquals("", 2.0, po.getC(2, 1.0), 1e-9);
        assertEquals("", 2.0, po.getC(2, 1.5), 1e-9);
        assertEquals("", 2.0, po.getC(2, 2.0), 1e-9);
    }
}