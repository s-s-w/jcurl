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

/**
 * A n-dimensional, continuous curve <code>R -&gt; R^n</code> based on
 * {@link org.jcurl.math.R1R1Function}s.
 * 
 * @see org.jcurl.math.R1R1Function
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveFkt extends R1RNFunction {

    private final R1R1Function[] fkt;

    public CurveFkt(final R1R1Function[] fkt) {
        super(fkt.length);
        this.fkt = new R1R1Function[dim()];
        for (int i = dim() - 1; i >= 0; i--)
            this.fkt[i] = fkt[i];
    }

    @Override
    public double at(final int dim, final int c, final double t) {
        return fkt[dim].at(0, c, t);
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append('[');
        for (int i = 0; i < dim(); i++)
            buf.append(fkt[i]).append(", ");
        if (dim() > 0)
            buf.setLength(buf.length() - 2);
        buf.append(']');
        return buf.toString();
    }
}