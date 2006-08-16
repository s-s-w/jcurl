/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.math.analysis;

import java.io.Serializable;

/**
 * An interval (subset of real) with boundaries that can be in- or excluded.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Interval implements Comparable, Serializable {
    private static final long serialVersionUID = 4906509542928200609L;

    public static boolean isWithin(Interval a, double b) {
        if (Double.isNaN(b) || Double.isInfinite(b))
            return false;
        if (b < a.min || b > a.max)
            return false;
        if (b > a.min && b < a.max)
            return true;
        if (b == a.min && a.min_included)
            return true;
        if (b == a.max && a.max_included)
            return true;
        return false;
    }

    public static Interval overlap(Interval a, Interval b) {
        if (a.equals(b))
            return a;
        if (a.compareTo(b) > 0) {
            final Interval c = a;
            a = b;
            b = c;
        }
        if (a.max < b.min)
            return null;
        if (a.max == b.min && (!a.max_included || !b.min_included))
            return null;
        if (a.max > b.max)
            return new Interval(b.min, b.min_included, b.max, b.max_included);
        if (a.max < b.max)
            return new Interval(b.min, b.min_included, a.max, a.max_included);
        return new Interval(b.min, b.min_included, b.max, b.max_included);
    }

    public final double max;

    public final boolean max_included;

    public final double min;

    public final boolean min_included;

    public Interval(final double min, final boolean min_included,
            final double max, final boolean max_included) {
        if (min > max)
            throw new IllegalArgumentException("min > max");
        if (min == max && !(min_included && max_included))
            throw new IllegalArgumentException(
                    "min == max but not both included");
        if (Double.isNaN(min))
            throw new IllegalArgumentException("min == NaN");
        if (Double.isInfinite(min) && min_included)
            throw new IllegalArgumentException(
                    "min cannot be infinite and included");
        if (Double.isNaN(max))
            throw new IllegalArgumentException("max == NaN");
        if (Double.isInfinite(max) && max_included)
            throw new IllegalArgumentException(
                    "max cannot be infinite and included");
        this.min = min;
        this.min_included = min_included;
        this.max = max;
        this.max_included = max_included;
    }

    public int compareTo(final Interval b) {
        if (min < b.min)
            return -1;
        if (min > b.min)
            return 1;
        if (min_included && !b.min_included)
            return -1;
        if (!min_included && b.min_included)
            return 1;
        return 0;
    }

    public int compareTo(Object o) {
        return compareTo((Interval) o);
    }

    public boolean equals(Interval b) {
        if (b == null)
            return false;
        if (min != b.min)
            return false;
        if (min_included != b.min_included)
            return false;
        if (max != b.max)
            return false;
        if (max_included != b.max_included)
            return false;
        return true;
    }

    public boolean equals(Object o) {
        if (o instanceof Interval)
            return equals((Interval) o);
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

}