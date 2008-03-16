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
package org.jcurl.core.model;

import java.util.Map.Entry;

import junit.framework.TestCase;

import org.jcurl.core.base.CurveStore;
import org.jcurl.math.R1RNFunction;

public class CurveStoreTest extends TestCase {

    public void testIterator() {
        final CurveStore cs = new CurveStoreImpl(null, 3);
        for (final Iterable<Entry<Double, R1RNFunction>> element : cs)
            for (final Entry<Double, R1RNFunction> e2 : element)
                System.out.println(e2.getValue());
    }
}
