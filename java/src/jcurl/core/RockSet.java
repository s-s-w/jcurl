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
package jcurl.core;

import java.io.Serializable;

import jcurl.core.dto.RockFloat;

/**
 * A set of 8 light and 8 dark {@link jcurl.core.Rock}s.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: PositionSet.java 25 2005-03-19 21:42:40Z mrohrmoser $
 */
public abstract class RockSet implements Cloneable, Serializable {

    public static final int ALL_MASK = 0xFFFF;

    public static final int DARK_MASK = 0xAAAA;

    public static final int LIGHT_MASK = 0x5555;

    public static final int ROCKS_PER_COLOR = 8;

    public static final int ROCKS_PER_SET = 16;

    public static RockSet allZero(final RockSet ret) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
            ret.getRock(i).setLocation(0, 0, 0);
        return ret;
    }

    public static int countBits(int a) {
        int ret = 0;
        for (; a != 0; a >>= 1) {
            if ((a & 1) == 1)
                ret++;
        }
        return ret;
    }

    /**
     * Check which rocks are non-zero.
     * 
     * @param rocks
     * @return bitset of the rocks being non-zero
     */
    public static int nonZero(final RockSet rocks) {
        final double zero = 1e-6;
        int ret = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock r = rocks.getRock(i);
            if (r.getX() > zero || r.getX() < -zero)
                ret |= 1 << i;
            else if (r.getY() > zero || r.getY() < -zero)
                ret |= 1 << i;
        }
        return ret;
    }

    protected static double sqr(final double a) {
        return a * a;
    }

    protected final Rock[] dark = new Rock[ROCKS_PER_COLOR];

    protected final Rock[] light = new Rock[ROCKS_PER_COLOR];

    public RockSet() {
        this(true);
    }

    protected RockSet(boolean fill) {
        if (fill) {
            for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
                dark[i] = new RockFloat();
                light[i] = new RockFloat();
            }
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
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            this.dark[i] = (Rock) (b.dark[i].clone());
            this.light[i] = (Rock) (b.light[i].clone());
        }
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

    public Rock getLight(int i) {
        return light[i];
    }

    public Rock getRock(int i) {
        if (i % 2 == 0)
            return dark[i / 2];
        return light[i / 2];
    }
}