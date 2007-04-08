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
package org.jcurl.math;

import java.util.Map.Entry;

/**
 * @see CurveCombined2
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurvePart implements Entry<Double, R1RNFunction> {

    private final R1RNFunction curve;

    private final Double t0;

    public CurvePart(final double t0, final R1RNFunction f) {
        this.t0 = new Double(t0);
        curve = f;
    }

    public Double getKey() {
        return t0;
    }

    public R1RNFunction getValue() {
        return curve;
    }

    public R1RNFunction setValue(final R1RNFunction value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return new StringBuffer().append("[").append(getKey())
                .append(" : ").append(getValue()).append("]").toString();
    }
}