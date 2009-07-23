/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.api;

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.event.ChangeListener;

/**
 * A set of 8 light and 8 dark {@link Rock}s.
 * 
 * <p>
 * Maybe this should be an interface to enable optimized implementations for 2D
 * and 3D screengraphs.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:RockSet.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class RockSet<R extends RockType> implements Iterable<Rock<R>>,
		Cloneable, Serializable {

	public static final int ALL_MASK = 0xFFFF;
	public static final int DARK_MASK = 0xAAAA;
	public static final int LIGHT_MASK = 0x5555;
	private static final Object mark = RockSet.class.getName() + ":allrocks";
	public static final int ROCKS_PER_COLOR = 8;
	public static final int ROCKS_PER_SET = 16;
	private static final long serialVersionUID = -7154547850436886952L;

	public static <T extends RockType> RockSet<T> allZero(RockSet<T> dst) {
		if (dst == null)
			dst = new RockSet<T>(new RockDouble<T>());
		for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
			dst.getRock(i).setLocation(0, 0, 0);
		dst.fireStateChanged();
		return dst;
	}

	public static <T extends RockType> RockSet<T> copy(final RockSet<T> src,
			final RockSet<T> dst) {
		return dst.setLocation(src);
	}

	public static final int countBits(int a) {
		int ret = 0;
		for (; a != 0; a >>= 1)
			if ((a & 1) == 1)
				ret++;
		return ret;
	}

	public static final boolean isDark(final int i16) {
		return i16 % 2 == 0;
	}

	/**
	 * Check if a bit is set
	 * 
	 * @param mask
	 * @param i16
	 * @return if the given rock's bit is set
	 */
	public static boolean isSet(final int mask, final int i16) {
		return 1 == (1 & mask >> i16);
	}

	/**
	 * Check if a bit is set
	 * 
	 * @param mask
	 * @param index8
	 * @param isDark
	 * @return if the given rock's bit is set
	 */
	public static boolean isSet(final int mask, final int index8,
			final boolean isDark) {
		return isSet(mask, toIdx16(isDark, index8));
	}

	/**
	 * Check which rocks are non-zero.
	 * 
	 * @param rocks
	 * @return bitset of the rocks being non-zero
	 */
	public static <T extends RockType> int nonZero(final RockSet<T> rocks) {
		int ret = 0;
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
			if (rocks.getRock(i).isNotZero())
				ret |= 1 << i;
		return ret;
	}

	/**
	 * Convert rock color and index per color to index per set.
	 * 
	 * @param isDark
	 * @param idx8
	 *            [0-7]
	 * @return [0-15]
	 */
	public static int toIdx16(final boolean isDark, final int idx8) {
		return 2 * idx8 + (isDark ? 0 : 1);
	}

	public static final int toIdx8(final int idx16) {
		return idx16 / 2;
	}

	protected final Rock<R>[] dark = new Rock[ROCKS_PER_COLOR];
	protected final Rock<R>[] light = new Rock[ROCKS_PER_COLOR];
	private final transient Map<Object, Integer> r2i = new IdentityHashMap<Object, Integer>(
			RockSet.ROCKS_PER_SET);

	/**
	 * Create an instance populated with clones of the given <code>seed</code>.
	 * 
	 * @param seed
	 */
	public RockSet(final Rock<R> seed) {
		if (seed != null)
			for (int i8 = ROCKS_PER_COLOR - 1; i8 >= 0; i8--) {
				dark[i8] = (Rock<R>) seed.clone();
				light[i8] = (Rock<R>) seed.clone();
			}
		for (int i16 = ROCKS_PER_SET - 1; i16 >= 0; i16--)
			r2i.put(getRock(i16), i16);
	}

	/**
	 * Copy constructor, useful e.g. for conversions position&lt;-&gt;speed.
	 * Makes a deep (cloned) copy.
	 * 
	 * @param b
	 */
	protected RockSet(final RockSet<R> b) {
		this(b.getRock(0));
		copy(b, this);
	}

	/**
	 * Add <code>l</code> to all rocks.
	 * 
	 * @see Rock#addChangeListener(ChangeListener)
	 */
	public void addRockListener(final ChangeListener l) {
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
			getRock(i).addChangeListener(l);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		// TODO getClass
		// http://www.angelikalanger.com/Articles/JavaSpektrum/02.Equals-Part2/02.Equals2.html
		if (obj == null || !(obj instanceof RockSet))
			return false;
		final RockSet<R> b = (RockSet<R>) obj;
		for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
			if (!dark[i].equals(b.dark[i]))
				return false;
			if (!light[i].equals(b.light[i]))
				return false;
		}
		return true;
	}

	/**
	 * Look up a {@link Rock} reference.
	 * 
	 * @return -1 if not found.
	 */
	public int findI16(final Object r) {
		final Integer i16 = r2i.get(r);
		return i16 == null ? -1 : i16.intValue();
	}

	@Deprecated
	void fireStateChanged() {
		; // do nothing!!
	}

	public Rock<R> getDark(final int i8) {
		return dark[i8];
	}

	public Rock<R> getLight(final int i8) {
		return light[i8];
	}

	public Rock<R> getRock(final int i16) {
		return isDark(i16) ? dark[toIdx8(i16)] : light[toIdx8(i16)];
	}

	@Override
	public int hashCode() {
		// http://www.angelikalanger.com/Articles/JavaSpektrum/03.HashCode/03.HashCode.html
		// hashcode N = hashcode N-1 * multiplikator + feldwert N
		int hash = 17;
		final int fact = 59;
		for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
			hash *= fact;
			hash += dark[i].hashCode();
			hash *= fact;
			hash += light[i].hashCode();
		}
		return hash;
	}

	public Iterator<Rock<R>> iterator() {
		return new Iterator<Rock<R>>() {
			private int i = 0;

			public boolean hasNext() {
				return i < ROCKS_PER_SET;
			}

			public Rock<R> next() {
				if (i >= ROCKS_PER_SET)
					throw new NoSuchElementException();
				return getRock(i++);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Remove <code>l</code> from all rocks.
	 * 
	 * @see Rock#removeChangeListener(ChangeListener)
	 */
	public void removeRockListener(final ChangeListener l) {
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
			getRock(i).removeChangeListener(l);
	}

	public RockSet<R> setLocation(final RockSet<R> src) {
		for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
			getRock(i).setLocation(src.getRock(i));
		fireStateChanged();
		return this;
	}
}