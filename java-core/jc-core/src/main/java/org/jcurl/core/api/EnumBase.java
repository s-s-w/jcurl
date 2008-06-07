/*
 * jcurl java curling software framework http://www.jcurl.org
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

import java.io.ObjectStreamException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Abstract superclass for enumeration-like object constants. Uses quick
 * {@link java.util.TreeMap}s for the flyweight lookup.
 * 
 * @see java.util.TreeMap
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class EnumBase extends Number implements Comparable<EnumBase> {

	/** Simple comparator based on the hash-value. */
	private static class HashCodeComp implements
			Comparator<Class<? extends EnumBase>> {

		public int compare(final Class<? extends EnumBase> o1,
				final Class<? extends EnumBase> o2) {
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

	private static final long serialVersionUID = -5618394067421447401L;
	private static final Map<Class<? extends EnumBase>, Map<Integer, EnumBase>> types = new TreeMap<Class<? extends EnumBase>, Map<Integer, EnumBase>>(
			new HashCodeComp());

	/**
	 * Generic lookup.
	 * 
	 * @param type
	 * @param state
	 * @return null ONLY if null was given
	 * @throws IllegalArgumentException
	 *             if the given state wasn't found.
	 */
	public static EnumBase lookup(final Class<? extends EnumBase> type,
			final Integer state) {
		if (state == null)
			return null;
		Map<Integer, EnumBase> values = types.get(type);
		if (values == null)
			try {
				// ensure the class "type" is initialized...
				Class.forName(type.getName());
				values = types.get(type);
			} catch (final ClassNotFoundException e) {
				throw new RuntimeException("Couldn't load class [" + type + "]");
			}
		if (values != null) {
			final EnumBase ret = values.get(state);
			if (ret != null)
				return ret;
		}
		throw new IllegalArgumentException("Constant not found: [" + state
				+ "] for " + type.getName());
	}

	private final Integer state;
	private final String text;

	protected EnumBase(final int state, final String text) {
		this.state = Integer.valueOf(state);
		this.text = text;
		final Class<? extends EnumBase> clazz = getClass();
		// register the value.
		Map<Integer, EnumBase> values = types.get(clazz);
		if (values == null)
			synchronized (types) {
				types.put(clazz, values = new TreeMap<Integer, EnumBase>());
			}
		synchronized (values) {
			if (values.containsKey(this.state))
				throw new IllegalArgumentException("Duplicate enum ["
						+ this.state + "] for " + clazz.getName());
			values.put(this.state, this);
		}
	}

	public int compareTo(final EnumBase o) {
		return state.compareTo(o.state);
	}

	@Override
	public double doubleValue() {
		return intValue();
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || !getClass().equals(o.getClass()))
			return false;
		return state.equals(((EnumBase) o).state);
	}

	@Override
	public float floatValue() {
		return intValue();
	}

	@Override
	public int hashCode() {
		return state.hashCode();
	}

	@Override
	public int intValue() {
		return state.intValue();
	}

	@Override
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
		return lookup(getClass(), state);
	}

	@Override
	public String toString() {
		return text;
	}
}