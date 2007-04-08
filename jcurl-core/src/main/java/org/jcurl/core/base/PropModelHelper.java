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

import java.util.HashMap;
import java.util.Map;

import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;

public final class PropModelHelper {
    public static final CharSequence DrawToTeeCurl = "drawToTeeCurl";

    public static final CharSequence DrawToTeeTime = "drawToTeeTime";

    public static final CharSequence FrictionRockRock = "frictionRockRock";

    public static final CharSequence Loss = "loss";

    private static final long serialVersionUID = -5959858338365408866L;

    public static Map<CharSequence, DimVal> create() {
        return create(null);
    }

    public static Map<CharSequence, DimVal> create(
            final Map<CharSequence, DimVal> m) {
        return m == null ? new HashMap<CharSequence, DimVal>()
                : new HashMap<CharSequence, DimVal>(m);
    }

    public static double get(final Map<CharSequence, DimVal> p,
            final CharSequence key, final Dim dim) {
        return p.get(key).to(dim).val;
    }

    public static double getDrawToTeeCurl(final Map<CharSequence, DimVal> p) {
        return get(p, DrawToTeeCurl, Dim.METER);
    }

    /**
     * 
     * @return may be {@link Double#POSITIVE_INFINITY}
     */
    public static double getDrawToTeeTime(final Map<CharSequence, DimVal> p) {
        return get(p, DrawToTeeTime, Dim.SECOND);
    }

    public static double getFrictionRockRock(final Map<CharSequence, DimVal> p) {
        return get(p, FrictionRockRock, Dim.NONE);
    }

    public static double getLoss(final Map<CharSequence, DimVal> p) {
        return get(p, Loss, Dim.JOULE);
    }

    public static void put(final Map<CharSequence, DimVal> p,
            final CharSequence key, final double val, final Dim dim) {
        p.put(key, new DimVal(val, dim));
    }

    public static void setDrawToTeeCurl(final Map<CharSequence, DimVal> p,
            final double drawToTeeCurl) {
        put(p, DrawToTeeCurl, drawToTeeCurl, Dim.METER);
    }

    /**
     * 
     * @param drawToTeeTime
     *            may be {@link Double#POSITIVE_INFINITY}
     */
    public static void setDrawToTeeTime(final Map<CharSequence, DimVal> p,
            final double drawToTeeTime) {
        put(p, DrawToTeeTime, drawToTeeTime, Dim.SECOND);
    }

    public static void setFrictionRockRock(final Map<CharSequence, DimVal> p,
            final double frictionRockRock) {
        put(p, FrictionRockRock, frictionRockRock, Dim.NONE);
    }

    /**
     * 
     * @param loss
     *            may be {@link Double#POSITIVE_INFINITY}
     */
    public static void setLoss(final Map<CharSequence, DimVal> p,
            final double loss) {
        put(p, Loss, loss, Dim.JOULE);
    }

    private PropModelHelper() {
    }
}
