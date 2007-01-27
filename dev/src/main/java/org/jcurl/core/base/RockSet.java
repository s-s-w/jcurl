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
package org.jcurl.core.base;

import java.io.Serializable;

import org.jcurl.core.helpers.MutableObject;

/**
 * A set of 8 light and 8 dark {@link org.jcurl.core.base.Rock}s.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:RockSet.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class RockSet extends MutableObject implements Cloneable,
        Serializable {

    public static final int ALL_MASK = 0xFFFF;

    public static final int DARK_MASK = 0xAAAA;

    public static final int LIGHT_MASK = 0x5555;

    public static final int ROCKS_PER_COLOR = 8;

    public static final int ROCKS_PER_SET = 16;

    public static RockSet allZero(final RockSet dst) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
            dst.getRock(i).setLocation(0, 0, 0);
        dst.notifyChange();
        return dst;
    }

    public static RockSet copy(final RockSet src, final RockSet dst) {
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            dst.dark[i] = (Rock) src.dark[i].clone();
            dst.light[i] = (Rock) src.light[i].clone();
        }
        dst.notifyChange();
        return dst;
    }

    public static int countBits(int a) {
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
    public static boolean isSet(int mask, int index16) {
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
    public static boolean isSet(int mask, int index8, boolean isDark) {
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
        return a * a;
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

    private long lastChanged = 0;

    protected final Rock[] light = new Rock[ROCKS_PER_COLOR];

    public RockSet() {
        this(true);
    }

    protected RockSet(boolean fill) {
        if (fill)
            for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
                dark[i] = new RockFloat();
                light[i] = new RockFloat();
            }
    }

    /**
     * Copy constructor, useful e.g. for conversions position&lt;-&gt;speed.
     * Makes a deep copy.
     * 
     * @param b
     */
    protected RockSet(final RockSet b) {
        this(false);
        copy(b, this);
    }

    public abstract Object clone();

    public boolean equals(Object obj) {
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

    public Rock getDark(int i) {
        return dark[i];
    }

    public long getLastChanged() {
        return lastChanged;
    }

    public Rock getLight(int i) {
        return light[i];
    }

    public Rock getRock(int i) {
        if (i % 2 == 0)
            return dark[i / 2];
        return light[i / 2];
    }

    public void notifyChange() {
        propChange.firePropertyChange("rock", null, this);
        lastChanged = System.currentTimeMillis();
    }
}