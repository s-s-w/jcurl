/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.base;

import java.util.Iterator;
import java.util.Map.Entry;

import org.jcurl.math.CurveCombined;
import org.jcurl.math.R1RNFunction;

/**
 * Manage rock trajectory segments for a complete set of rocks over time.
 * <p>
 * This implementation is based on {@link CurveCombined}.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveStore implements
        Iterable<Iterable<Entry<Double, R1RNFunction>>> {

    private static final byte DIM = 3;

    private final CurveCombined[] curve;

    public CurveStore(final int capacity) {
        curve = new CurveCombined[capacity];
        for (int i = curve.length - 1; i >= 0; i--)
            curve[i] = new CurveCombined(DIM);
    }

    public void add(final int i, final double t, final R1RNFunction f) {
        curve[i].add(t, f);
    }

    public R1RNFunction getCurve(final int i) {
        return curve[i];
    }

    /**
     * Ascending iterator over the cuves returning each segment.
     * 
     * @return iterator over the cuves returning each segment.
     */
    public Iterator<Iterable<Entry<Double, R1RNFunction>>> iterator() {
        return new Iterator<Iterable<Entry<Double, R1RNFunction>>>() {
            int current = 0;

            public boolean hasNext() {
                return current < curve.length;
            }

            public Iterable<Entry<Double, R1RNFunction>> next() {
                return curve[current++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void reset(final int i) {
        curve[i].clear();
    }
}
