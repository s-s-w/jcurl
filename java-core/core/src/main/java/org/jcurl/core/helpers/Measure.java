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
package org.jcurl.core.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A scalar value with attached {@link Unit} of measurement.
 * 
 * Similar to <code>javax.measure.Measure</code> from <a
 * href="http://www.jcp.org/en/jsr/detail?id=275">JSR-275</a> but faaaar
 * simpler.
 * 
 * @see org.jcurl.core.helpers.Unit
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Measure {

    public static final Pattern pat = Pattern
            .compile("^(-?[0-9]+([.][0-9]+)?(e-?[0-9]+)?)[ \t]*([\\S]*)$");

    public static Measure parse(final CharSequence measure) {
        // split the string
        final Matcher mat = pat.matcher(measure);
        if (mat.matches()) {
            // for (int i = 0; i < mat.groupCount(); i++)
            // log.debug(i + "=" + mat.group(i));
            final String val = mat.group(1);
            final String dim = mat.group(4);
            try {
                return new Measure(Double.parseDouble(val), Unit.find(dim));
            } catch (final RuntimeException e) {
                final IllegalArgumentException a = new IllegalArgumentException(
                        "Not a measure: [" + measure + "]");
                a.initCause(e);
                throw a;
            }
        }
        throw new IllegalArgumentException("Not a measure: [" + measure + "]");
    }

    public final Unit unit;

    public final double quantity;

    public Measure(final double quantity, final Unit unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof Measure))
            return false;
        final Measure b = (Measure) o;
        if (!unit.equals(b.unit))
            return false;
        if (quantity == b.quantity)
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        // http://www.angelikalanger.com/Articles/JavaSpektrum/03.HashCode/03.HashCode.html
        // hashcode N = hashcode N-1 * multiplikator + feldwert N
        int hash = 17;
        final int fact = 59;
        hash *= fact;
        hash += unit.hashCode();
        hash *= fact;
        final long tmp = quantity == 0.0 ? 0L : java.lang.Double
                .doubleToLongBits(quantity);
        hash += (int) (tmp ^ tmp >>> 32);
        return hash;
    }

    public Measure to(final Unit dst) {
        if (unit.BaseUnit.intValue() != dst.BaseUnit.intValue())
            throw new IllegalArgumentException("Units are not convertible ("
                    + unit.toString() + "->" + dst.toString() + ")");
        // this -> si -> dst
        return new Measure(quantity * unit.Factor / dst.Factor, dst);
    }

    @Override
    public String toString() {
        if (unit == null)
            return Double.toString(quantity);
        return Double.toString(quantity) + unit.toString();
    }
}