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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;

public class ModelProps implements Map<CharSequence, DimVal> {

    public static final CharSequence DrawToTeeCurl = "drawToTeeCurl";

    public static final CharSequence DrawToTeeTime = "drawToTeeTime";

    private static final long serialVersionUID = -5959858338365408866L;

    private final Map<CharSequence, DimVal> p;

    public ModelProps() {
        this(null);
    }

    public ModelProps(final Map<CharSequence, DimVal> p) {
        this.p = p == null ? new HashMap<CharSequence, DimVal>() : p;
    }

    public void clear() {
        p.clear();
    }

    public boolean containsKey(final Object arg0) {
        return p.containsKey(arg0);
    }

    public boolean containsValue(final Object arg0) {
        return p.containsValue(arg0);
    }

    public Set<Entry<CharSequence, DimVal>> entrySet() {
        return p.entrySet();
    }

    @Override
    public boolean equals(final Object arg0) {
        return p.equals(arg0);
    }

    public double get(final CharSequence key, final Dim dim) {
        return get(key).to(dim).val;
    }

    public DimVal get(final Object arg0) {
        return p.get(arg0);
    }

    public double getDrawToTeeCurl() {
        return get(DrawToTeeCurl, Dim.METER);
    }

    /**
     * 
     * @return may be {@link Double#POSITIVE_INFINITY}
     */
    public double getDrawToTeeTime() {
        return get(DrawToTeeTime, Dim.SECOND);
    }

    @Override
    public int hashCode() {
        return p.hashCode();
    }

    public boolean isEmpty() {
        return p.isEmpty();
    }

    public Set<CharSequence> keySet() {
        return p.keySet();
    }

    public DimVal put(final CharSequence arg0, final DimVal arg1) {
        return p.put(arg0, arg1);
    }

    public void put(final CharSequence key, final double val, final Dim dim) {
        put(key, new DimVal(val, dim));
    }

    public void putAll(final Map<? extends CharSequence, ? extends DimVal> arg0) {
        p.putAll(arg0);
    }

    public DimVal remove(final Object arg0) {
        return p.remove(arg0);
    }

    public void setDrawToTeeCurl(final double drawToTeeCurl) {
        put(DrawToTeeCurl, drawToTeeCurl, Dim.METER);
    }

    /**
     * May be {@link Double#POSITIVE_INFINITY}
     * 
     * @param drawToTeeTime
     */
    public void setDrawToTeeTime(final double drawToTeeTime) {
        put(DrawToTeeTime, drawToTeeTime, Dim.SECOND);
    }

    public int size() {
        return p.size();
    }

    public Collection<DimVal> values() {
        return p.values();
    }
}
