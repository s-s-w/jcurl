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
package jcurl.core.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ugli.LoggerFactory;
import org.apache.ugli.ULogger;

/**
 * A value with attached unit of measurement {@link Dim}.
 * 
 * @see jcurl.core.io.Dim
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class DimVal {
    private static final ULogger log = LoggerFactory.getLogger(DimVal.class);

    public static final Pattern pat = Pattern
            .compile("^(-?[0-9]+([.][0-9]+)?(e-?[0-9]+)?)(.*)$");

    public static DimVal parse(final String txt) {
        // split the string
        final Matcher mat = pat.matcher(txt);
        if (mat.matches()) {
//            for (int i = 0; i < mat.groupCount(); i++)
//                log.debug(i + "=" + mat.group(i));
            final String val = mat.group(1);
            final String dim = mat.group(4);
            try {
                return new DimVal(Double.parseDouble(val), Dim.find(dim));
            } catch (RuntimeException e) {
                final IllegalArgumentException a = new IllegalArgumentException(
                        "Not a dimension: [" + txt + "]");
                a.initCause(e);
                throw a;
            }
        }
        throw new IllegalArgumentException("Not a dimension: [" + txt + "]");
    }

    public final Dim dim;

    public final double val;

    public DimVal(double value, final Dim dim) {
        this.val = value;
        this.dim = dim;
    }

    public boolean equals(final Object o) {
        if (o == null || !(o instanceof DimVal))
            return false;
        final DimVal b = (DimVal) o;
        if (!this.dim.equals(b.dim))
            return false;
        if (this.val == b.val)
            return true;
        return false;
    }

    public DimVal to(final Dim dst) {
        if (this.dim.BaseDim.getValue() != dst.BaseDim.getValue())
            throw new IllegalArgumentException("Units are not convertible ("
                    + this.dim.toString() + "->" + dst.toString() + ")");
        // this -> si -> dst
        return new DimVal(this.val * (this.dim.Factor / dst.Factor), dst);
    }

    public String toString() {
        return Double.toString(val) + dim.toString();
    }
}