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

import org.apache.ugli.LoggerFactory;
import org.apache.ugli.ULogger;

/**
 * Combined curve.
 * 
 * @see jcurl.math.CurveTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveParts extends CurveBase {

    private static final int growth = 120;

    private static final int initialSize = 5;

    private static final ULogger log = LoggerFactory
            .getLogger(CurveParts.class);

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
    static int binarySearch(double[] a, final double key, int min, int max) {
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

    private CurveBase[] fkt;

    private int parts = 0;

    private double[] t0;

    /**
     * @param dim
     */
    public CurveParts(int dim) {
        super(dim);
        t0 = new double[initialSize];
        fkt = new CurveBase[initialSize];
    }

    public void add(final double _t0, final CurveBase fkt) {
        // re-alloc?
        if (parts == t0.length) {
            final int siz = 1 + parts * growth / 100;
            final double[] t = new double[siz];
            System.arraycopy(t0, 0, t, 0, parts);
            t0 = t;
            final CurveBase[] c = new CurveBase[siz];
            System.arraycopy(this.fkt, 0, c, 0, parts);
            this.fkt = c;
        }
        // add
        this.t0[parts] = _t0;
        this.fkt[parts++] = fkt;
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
    private int findFktIdx_BS(double t) {
        if (t < t0[0])
            throw new IllegalArgumentException("t < tmin");
        // find the correct index
        int idx = CurveParts.binarySearch(t0, t, 0, parts - 1);
        if (idx >= 0)
            return idx;
        if (idx == -1)
            return parts - 1;
        return -2 - idx;
    }

    /**
     * Linear search.
     * 
     * @param t
     * @return the curve index
     */
    private int findFktIdx_LS(double t) {
        for (int i = 0; i <= parts; i++) {
            if (i == parts)
                return i - 1;
            if (t >= t0[i])
                return i;
        }
        throw new IllegalArgumentException("t < tmin");
    }

    /**
     * Get the n-th derivative of one dimension.
     * 
     * @param dim
     *            dimension
     * @param c
     *            derivative
     * @param t
     * @return the value
     */
    public double getC(int dim, int c, double t) {
        final int idx = findFktIdx_BS(t);
        if (false && log.isDebugEnabled())
            log.debug("t=" + t + " idx=" + idx);
        return fkt[idx].getC(dim, c, t);
    }
}