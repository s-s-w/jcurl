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
            final CurveBase[] c = new CurveBase[siz];
            System.arraycopy(fkt, 0, c, 0, parts);
        }
        // add
        this.t0[parts] = _t0;
        this.fkt[parts++] = fkt;
    }

    public double getC(int dim, int c, double t) {
        for (int i = 0; i <= parts; i++) {
            if (i == parts)
                return fkt[i - 1].getC(dim, c, t);
            if (t >= t0[i])
                return fkt[i].getC(dim, c, t);
        }
        throw new IllegalArgumentException("t >= tmin");
    }
}