/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.core.api;

import java.util.HashMap;
import java.util.Map;

import org.jcurl.core.helpers.Unit;
import org.jcurl.core.helpers.Measure;

/**
 * Help accessing {@link PropModel} properties.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:PropModelHelper.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public final class PropModelHelper {
    public static final String DrawToTeeCurl = "drawToTeeCurl";

    public static final String DrawToTeeTime = "drawToTeeTime";

    public static final String FrictionRockRock = "frictionRockRock";

    public static final String Loss = "loss";

    private static final long serialVersionUID = -5959858338365408866L;

    public static Map<CharSequence, Measure> create() {
        return create(null);
    }

    public static Map<CharSequence, Measure> create(
            final Map<CharSequence, Measure> m) {
        return m == null ? new HashMap<CharSequence, Measure>()
                : new HashMap<CharSequence, Measure>(m);
    }

    public static double get(final Map<CharSequence, Measure> p,
            final CharSequence key, final Unit dim) {
        return p.get(key).to(dim).value;
    }

    public static double getDrawToTeeCurl(final Map<CharSequence, Measure> p) {
        return get(p, DrawToTeeCurl, Unit.METER);
    }

    /**
     * 
     * @param p
     * @return may be {@link Double#POSITIVE_INFINITY}
     */
    public static double getDrawToTeeTime(final Map<CharSequence, Measure> p) {
        return get(p, DrawToTeeTime, Unit.SECOND);
    }

    public static double getFrictionRockRock(final Map<CharSequence, Measure> p) {
        return get(p, FrictionRockRock, Unit.NONE);
    }

    public static double getLoss(final Map<CharSequence, Measure> p) {
        return get(p, Loss, Unit.JOULE);
    }

    public static void put(final Map<CharSequence, Measure> p,
            final String key, final double val, final Unit dim) {
        p.put(key, new Measure(val, dim));
    }

    public static void setDrawToTeeCurl(final Map<CharSequence, Measure> p,
            final double drawToTeeCurl) {
        put(p, DrawToTeeCurl, drawToTeeCurl, Unit.METER);
    }

    /**
     * 
     * @param p
     * @param drawToTeeTime
     *            may be {@link Double#POSITIVE_INFINITY}
     */
    public static void setDrawToTeeTime(final Map<CharSequence, Measure> p,
            final double drawToTeeTime) {
        put(p, DrawToTeeTime, drawToTeeTime, Unit.SECOND);
    }

    public static void setFrictionRockRock(final Map<CharSequence, Measure> p,
            final double frictionRockRock) {
        put(p, FrictionRockRock, frictionRockRock, Unit.NONE);
    }

    /**
     * 
     * @param p
     * @param loss
     *            may be {@link Double#POSITIVE_INFINITY}
     */
    public static void setLoss(final Map<CharSequence, Measure> p,
            final double loss) {
        put(p, Loss, loss, Unit.JOULE);
    }

    private PropModelHelper() {
    }
}
