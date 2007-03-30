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
package org.jcurl.math.dom;

import java.util.Map;
import java.util.TreeMap;

import org.jcurl.math.R1RNFunction;

/**
 * A n-dimensional, continuous curve (R -&gt; R^n) based on a
 * {@link org.jcurl.math.dom.MathDom}.Node. Maybe it's better to add a
 * {@link org.jcurl.math.R1R1Function}based on
 * {@link org.jcurl.math.dom.MathDom}and use {@link org.jcurl.math.CurveFkt}rather
 * than this class.
 * 
 * @see org.jcurl.math.dom.MathDom
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveDom extends R1RNFunction {

    /** Internal helper - mutable number implementation */
    private static final class Num extends Number {

        private static final long serialVersionUID = -4493962870206362722L;

        public double v;

        @Override
        public double doubleValue() {
            return v;
        }

        @Override
        public float floatValue() {
            return (float) v;
        }

        @Override
        public int intValue() {
            return (int) v;
        }

        @Override
        public long longValue() {
            return (long) v;
        }

    }

    private final MathDom.Node[][] c;

    private final DomWalkerEval de;

    private final Map p = new TreeMap();

    private final Num t = new Num();

    public CurveDom(final MathDom.Node[] c0, final MathDom.Node[] c1,
            final String param) {
        super(c0.length);
        p.put(param, t);
        de = new DomWalkerEval(p);
        c = new MathDom.Node[2][dim()];
        for (int i = dim() - 1; i >= 0; i--) {
            c[0][i] = c0[i];
            c[1][i] = c1[i];
        }
    }

    public CurveDom(final MathDom.Node[] x, final String param) {
        this(x, null, param); // TODO compute and store the derivatives
    }

    @Override
    public double at(final int dim, final int c, final double t) {
        if (c == 0) {
            this.t.v = t;
            de.reset();
            de.walk(this.c[0][dim]);
            return de.doubleValue();
        }
        throw new UnsupportedOperationException("Not supported.");
    }
}