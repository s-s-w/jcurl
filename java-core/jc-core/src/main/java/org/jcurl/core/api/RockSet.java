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

import java.io.Serializable;
import java.util.Hashtable;

import javax.swing.undo.StateEditable;

import org.jcurl.core.ui.ChangeSupport;
import org.jcurl.math.MathVec;

/**
 * A set of 8 light and 8 dark {@link org.jcurl.core.api.Rock}s.
 * 
 * <p>
 * Maybe this should be an interface to enable optimized implementations for 2D
 * and 3D screengraphs.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:RockSet.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class RockSet extends ChangeSupport implements Cloneable,
		Serializable, StateEditable {
	public static final int ALL_MASK = 0xFFFF;
	public static final int DARK_MASK = 0xAAAA;
	public static final int LIGHT_MASK = 0x5555;
	private static final Object mark = RockSet.class.getName() + ":allrocks";
	public static final int ROCKS_PER_COLOR = 8;
	public static final int ROCKS_PER_SET = 16;

	public static RockSet allZero(final RockSet dst) {
		for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
			dst.getRock(i).setLocation(0, 0, 0);
		dst.fireStateChanged();
		return dst;
	}

	public static RockSet copy(final RockSet src, final RockSet dst) {
		return dst.setLocation(src);
	}

	public static final int countBits(int a) {
		int ret = 0;
		for (; a != 0; a >>= 1)
			if ((a & 1) == 1)
				ret++;
		return ret;
	}

	/**
	 * Check if a bit is set
	 * 
	 * @param mask
	 * @param index16
	 * @return if the given rock's bit is set
	 */
	public static boolean isSet(final int mask, final int index16) {
		return 1 == (1 & mask >> index16);
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
	public static int nonZero(final RockSet rocks) {
		int ret = 0;
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
			if (rocks.getRock(i).isNotZero())
				ret |= 1 << i;
		return ret;
	}

	protected static double sqr(final double a) {
		return MathVec.sqr(a);
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

	protected final Rock[] dark = new Rock[ROCKS_PER_COLOR];
	protected final Rock[] light = new Rock[ROCKS_PER_COLOR];

	public RockSet(final Rock seed) {
		if (seed != null)
			for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
				dark[i] = (Rock) seed.clone();
				light[i] = (Rock) seed.clone();
			}
	}

	/**
	 * Copy constructor, useful e.g. for conversions position&lt;-&gt;speed.
	 * Makes a deep (cloned) copy.
	 * 
	 * @param b
	 */
	protected RockSet(final RockSet b) {
		this(b.getRock(0));
		copy(b, this);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		// TODO getClass
		// http://www.angelikalanger.com/Articles/JavaSpektrum/02.Equals-Part2/02.Equals2.html
		if (obj == null || !(obj instanceof RockSet))
			return false;
		final RockSet b = (RockSet) obj;
		for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
			if (!dark[i].equals(b.dark[i]))
				return false;
			if (!light[i].equals(b.light[i]))
				return false;
		}
		return true;
	}

	public Rock getDark(final int i8) {
		return dark[i8];
	}

	public Rock getLight(final int i8) {
		return light[i8];
	}

	public Rock getRock(final int i16) {
		return i16 % 2 == 0 ? dark[i16 / 2] : light[i16 / 2];
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

	public void restoreState(final Hashtable<?, ?> state) {
		final double[] arr = (double[]) state.get(mark);
		for (byte i16 = ROCKS_PER_SET - 1; i16 >= 0; i16--) {
			final int o = i16 * 3;
			getRock(i16).setLocation(arr[o], arr[o + 1], arr[o + 2]);
		}
		fireStateChanged();
	}

	public RockSet setLocation(final RockSet src) {
		for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
			getRock(i).setLocation(src.getRock(i));
		fireStateChanged();
		return this;
	}

	public void storeState(final Hashtable<Object, Object> state) {
		final double[] arr = new double[ROCKS_PER_SET * 3];
		for (byte i16 = ROCKS_PER_SET - 1; i16 >= 0; i16--) {
			final int o = i16 * 3;
			final Rock r = getRock(i16);
			arr[o] = r.getX();
			arr[o + 1] = r.getY();
			arr[o + 2] = r.getA();
		}
		state.put(mark, arr);
	}
}