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
package jcurl.core.dto;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract superclass for enumeration-like object constants. Uses quick
 * {@link java.util.TreeMap}s for the flyweight lookup.
 * 
 * @see java.util.TreeMap
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class EnumBase extends Number implements Comparable,
        Serializable {

    /** Simple comparator based on the hash-value. */
    private static class HashCodeComp implements Comparator {

        public int compare(final Object o1, final Object o2) {
            if (o1 == null && o2 == null)
                return 0;
            if (o1 == null)
                return -1;
            if (o2 == null)
                return 1;
            final int a = o1.hashCode();
            final int b = o2.hashCode();
            if (a < b)
                return -1;
            if (a > b)
                return 1;
            return 0;
        }
    }

    private static final Map types = new TreeMap(new HashCodeComp());

    /**
     * Generic lookup.
     * 
     * @param type
     * @param state
     * @return the constant object
     * @throws IllegalArgumentException
     *             if the given state wasn't found.
     */
    public static EnumBase lookup(final Class type, final int state) {
        return lookup(type, new Integer(state));
    }

    /**
     * Generic lookup.
     * 
     * @param type
     * @param state
     * @return null ONLY if null was given
     * @throws IllegalArgumentException
     *             if the given state wasn't found.
     */
    public static EnumBase lookup(final Class type, final Integer state) {
        if (state == null)
            return null;
        Map values = (Map) types.get(type);
        if (values == null) {
            try {
                // ensure the class "type" is initialized...
                Class.forName(type.getName());
                values = (Map) types.get(type);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Couldn't load class [" + type + "]");
            }
        }
        if (values != null) {
            final EnumBase ret = (EnumBase) values.get(state);
            if (ret != null)
                return ret;
        }
        throw new IllegalArgumentException("Constant not found: [" + state
                + "] for " + type.getName());
    }

    private final Integer state;

    private final String text;

    protected EnumBase(final int state, final String text) {
        this.state = new Integer(state);
        this.text = text;
        final Class clazz = this.getClass();
        // register the value.
        Map values = (Map) types.get(clazz);
        if (values == null) {
            synchronized (types) {
                types.put(clazz, values = new TreeMap());
            }
        }
        synchronized (values) {
            if (values.containsKey(this.state))
                throw new IllegalArgumentException("Duplicate enum ["
                        + this.state + "] for " + clazz.getName());
            values.put(this.state, this);
        }
    }

    public int compareTo(Object o) {
        final EnumBase b = (EnumBase) o;
        return this.state.compareTo(b.state);
    }

    /**
     * @see java.lang.Number#doubleValue()
     */
    public double doubleValue() {
        return intValue();
    }

    public boolean equals(final Object o) {
        if (o == null || !this.getClass().equals(o.getClass()))
            return false;
        return this.state.equals(((EnumBase) o).state);
    }

    /**
     * @see java.lang.Number#floatValue()
     */
    public float floatValue() {
        return intValue();
    }

    private Integer getState() {
        return state;
    }

    /**
     * @return
     */
    public int getValue() {
        return intValue();
    }

    /**
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return state.hashCode();
    }

    /**
     * @see java.lang.Number#intValue()
     */
    public int intValue() {
        return state.intValue();
    }

    /**
     * @see java.lang.Number#longValue()
     */
    public long longValue() {
        return intValue();
    }

    /**
     * Resolve a read in object (de-serialization).
     * 
     * @return The resolved object read in.
     * 
     * @throws ObjectStreamException
     *             if there is a problem reading the object.
     * @throws RuntimeException
     *             If the read object doesn't exist.
     */
    protected Object readResolve() throws ObjectStreamException {
        return lookup(getClass(), getState());
    }

    public String toString() {
        return this.text;
    }
}