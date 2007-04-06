/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.math;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.Map.Entry;

/**
 * Combined curve. Becomes more and more similar to {@link SortedMap} with some
 * restrictions and additions.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveCombined extends R1RNFunction implements
        Iterable<Entry<Double, R1RNFunction>> {

    private class CCEntry implements Entry<Double, R1RNFunction> {

        private final int i;

        public CCEntry(final int i) {
            this.i = i;
        }

        public Double getKey() {
            return t0[i];
        }

        public R1RNFunction getValue() {
            return fkt[i];
        }

        public R1RNFunction setValue(final R1RNFunction value) {
            throw new UnsupportedOperationException();
            // try {return v;} finally {v = value;}
        }

        @Override
        public String toString() {
            return new StringBuffer().append("[").append(getKey())
                    .append(" : ").append(getValue()).append("]").toString();
        }
    }

    private static final int growth = 120;

    private static final int initialSize = 5;

    /**
     * Search only part of an array.
     * 
     * @see java.util.Arrays#binarySearch(double[], double)
     * @param a
     * @param key
     * @param min
     * @param max
     * @return found index
     */
    static int binarySearch(final double[] a, final double key, int min, int max) {
        for (;;) {
            if (key == a[min])
                return min;
            if (key == a[max])
                return max;
            final int m = (max + min) / 2;
            if (key == a[m])
                return m;
            if (min + 1 >= max) {
                if (a[min] < key && key < a[max])
                    return -1 - max;
                return -1;
            }
            if (key < a[m]) {
                max = m;
                continue;
            } else if (key > a[m]) {
                min = m;
                continue;
            }
        }
    }

    private R1RNFunction[] fkt;

    private int parts = 0;

    private double[] t0;

    /**
     * @param dim
     */
    public CurveCombined(final int dim) {
        super(dim);
        t0 = new double[initialSize];
        fkt = new R1RNFunction[initialSize];
    }

    public void add(final double t0, final R1RNFunction fkt) {
        if (fkt.dim() != dim())
            throw new IllegalArgumentException();
        // re-alloc?
        if (parts == this.t0.length) {
            final int siz = 1 + parts * growth / 100;
            final double[] t = new double[siz];
            System.arraycopy(this.t0, 0, t, 0, parts);
            this.t0 = t;
            final R1RNFunction[] c = new R1RNFunction[siz];
            System.arraycopy(this.fkt, 0, c, 0, parts);
            this.fkt = c;
        }
        // add
        this.t0[parts] = t0;
        this.fkt[parts++] = fkt;
    }

    /**
     * Get the n-th derivative of all dimensions.
     * 
     * @param c
     *            derivative
     * @param t
     * @param ret
     *            <code>null</code> creates a new instance
     * @return the value
     * @see R1RNFunction#at(int, double, double[])
     */
    @Override
    public double[] at(final int c, final double t, final double[] ret) {
        return fkt[findFktIdx_BS(t)].at(c, t, ret);
    }

    @Override
    public double at(final int dim, final int c, final double t) {
        return fkt[findFktIdx_BS(t)].at(dim, c, t);
    }

    public void clear() {
        parts = 0;
    }

    /**
     * Binary search.
     * 
     * @param t
     * @return the curve index
     */
    private int findFktIdx_BS(final double t) {
        if (t < t0[0])
            throw new IllegalArgumentException("t < tmin");
        // find the correct index
        final int idx = CurveCombined.binarySearch(t0, t, 0, parts - 1);
        if (idx >= 0)
            return idx;
        if (idx == -1)
            return parts - 1;
        return -2 - idx;
    }

    public Iterator<Entry<Double, R1RNFunction>> iterator() {
        return new Iterator<Entry<Double, R1RNFunction>>() {
            int current = 0;

            public boolean hasNext() {
                return current < parts;
            }

            public Entry<Double, R1RNFunction> next() {
                return new CCEntry(current++);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}