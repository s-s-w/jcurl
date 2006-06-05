/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.model;

import junit.framework.TestCase;

import org.jcurl.core.dto.RockSet;

public class HitTimeMatrixTest extends TestCase {

    public void testIJ() {
        HitTimeMatrix m = new HitTimeMatrix();
        for (int i = RockSet.ROCKS_PER_SET - 1; i > 0; i--)
            for (int j = i - 1; j >= 0; j--)
                m.set(i, j, 1000 * i + j);
        for (int i = RockSet.ROCKS_PER_SET - 1; i > 0; i--)
            for (int j = i - 1; j >= 0; j--)
                assertEquals("", 1000 * i + j, m.get(i, j), 1e-11);
    }

    public void testMask() {
        HitTimeMatrix m = new HitTimeMatrix();
        for (int i = RockSet.ROCKS_PER_SET - 1; i > 0; i--) {
            final int a = 1 << i;
            for (int j = i - 1; j >= 0; j--) {
                final int b = 1 << j;
                assertEquals("i=" + i + ", j=" + j + ", a=" + a + ", b=" + b, m
                        .findIdx(i, j), m.findIdx(a | b));
            }
        }
    }
}
