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
package org.jcurl.core.base;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.undo.StateEditable;

import org.jcurl.core.helpers.MutableObject;
import org.jcurl.math.MathVec;

/**
 * A set of 8 light and 8 dark {@link org.jcurl.core.base.Rock}s.
 * 
 * <p>
 * Maybe this should be an interface to enable optimized implementations for 2D
 * and 3D screengraphs.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:RockSet.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class RockSet extends MutableObject implements Cloneable,
        Serializable, StateEditable {

    private class REntry implements Entry<Integer, Rock> {

        private final Integer i;

        public REntry(final int i) {
            this.i = boxIdx16(i);
        }

        public Integer getKey() {
            return i;
        }

        public Rock getValue() {
            return getRock(i);
        }

        public Rock setValue(final Rock value) {
            getValue().setLocation(value);
            return getValue();
        }

        @Override
        public String toString() {
            return new StringBuilder().append("[").append(getKey())
                    .append(" : ").append(getValue()).append("]").toString();
        }
    }

    public static final int ALL_MASK = 0xFFFF;

    public static final int DARK_MASK = 0xAAAA;

    private static Integer[] idx;

    public static final int LIGHT_MASK = 0x5555;

    private static final Object mark = RockSet.class.getName() + ":allrocks";

    public static final int ROCKS_PER_COLOR = 8;

    public static final int ROCKS_PER_SET = 16;

    public static RockSet allZero(final RockSet dst) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
            dst.getRock(i).setLocation(0, 0, 0);
        dst.notifyChange();
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
            if (rocks.getRock(i).nonZero())
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

    private transient long lastChanged = 0;

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

    /**
     * Fast Boxing. Is this faster than {@link Integer#valueOf(int)}?
     * 
     * @param i
     * @return {@link Integer}
     */
    private final Integer boxIdx16(final int i) {
        if (idx == null) {
            idx = new Integer[ROCKS_PER_SET];
            for (int j = ROCKS_PER_SET - 1; j >= 0; j--)
                idx[j] = j;
        }
        return idx[i];
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

    public long getLastChanged() {
        return lastChanged;
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

    /**
     * Careful - this is about 20 times slower than a for loop over int.
     * 
     * @return a (very slow) iterator
     */
    Iterator<Entry<Integer, Rock>> itEntries() {
        return new Iterator<Entry<Integer, Rock>>() {
            int current = 0;

            public boolean hasNext() {
                return current < RockSet.ROCKS_PER_SET;
            }

            public Entry<Integer, Rock> next() {
                return new REntry(current++);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Iterator over this {@link RockSet}.
     * <p>
     * Doesn't return {@link Entry} - which could be very convenient - to save
     * instanciations in inner loops. But maybe cached transient references
     * could help here?
     * </p>
     * 
     * @return {@link Integer} rather than {@link Rock} as the latter doesn't
     *         know it's own index or color.
     */
    Iterator<Integer> iterator() {
        return itIdx();
    }

    /**
     * Careful - this is several times slower than a for loop over int.
     * <p>
     * The unbeateable fastest way to iterate is:
     * 
     * <pre>
     * for (int i = ROCKS_PER_SET - 1; i &gt;= 0; i--)
     *     ;
     * </pre>
     * 
     * But this iterator here only requires one additional instanciation.
     * </p>
     * 
     * @return a (slow) iterator
     */
    Iterator<Integer> itIdx() {
        return new Iterator<Integer>() {
            int current = 0;

            public boolean hasNext() {
                return current < RockSet.ROCKS_PER_SET;
            }

            public Integer next() {
                return boxIdx16(current++);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Careful - this is about 10 times slower than a for loop over int.
     * 
     * @return a (slow) iterator
     */
    Iterator<Rock> itRocks() {
        return new Iterator<Rock>() {
            int current = 0;

            public boolean hasNext() {
                return current < RockSet.ROCKS_PER_SET;
            }

            public Rock next() {
                return getRock(current++);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void notifyChange() {
        propChange.firePropertyChange("rock", null, this);
        lastChanged = System.currentTimeMillis();
    }

    public void restoreState(final Hashtable<?, ?> state) {
        final double[] arr = (double[]) state.get(mark);
        for (byte i16 = ROCKS_PER_SET - 1; i16 >= 0; i16--) {
            final int o = i16 * 3;
            getRock(i16).setLocation(arr[o], arr[o + 1], arr[o + 2]);
        }
        notifyChange();
    }

    public RockSet setLocation(final RockSet src) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
            getRock(i).setLocation(src.getRock(i));
        notifyChange();
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