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

import jcurl.core.dto.Ice;
import jcurl.core.dto.RockFloat;

/**
 * A set of 8 light and 8 dark {@link jcurl.core.Rock}s.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockSet implements Cloneable, Serializable {

    /**
     * Check which rocks are non-zero.
     * 
     * @param rocks
     * @return bitset of the rocks beeing non-zero
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

    public static final int ALL_MASK = 0xFFFF;

    public static final int ROCKS_PER_COLOR = 8;

    public static final int ROCKS_PER_SET = 16;

    public static RockSet allHome() {
        return allHome(null);
    }

    public static RockSet allHome(RockSet ret) {
        ret = ret != null ? ret : new RockSet();
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            Ice.setHome(ret.dark[i], true, i);
            Ice.setHome(ret.light[i], false, i);
        }
        return ret;
    }

    public static RockSet allOut() {
        return allOut(null);
    }

    public static RockSet allOut(RockSet ret) {
        ret = ret != null ? ret : new RockSet();
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            Ice.setOut(ret.dark[i], true, i);
            Ice.setOut(ret.light[i], false, i);
        }
        return ret;
    }

    public static RockSet allZero(RockSet ret) {
        ret = ret != null ? ret : new RockSet();
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--) {
            final Rock ro = ret.getRock(i);
            ro.setLocation(0, 0);
            ro.setZ(0);
        }
        return ret;
    }

    private final RockFloat[] dark = new RockFloat[ROCKS_PER_COLOR];

    private final RockFloat[] light = new RockFloat[ROCKS_PER_COLOR];

    public RockSet() {
        this(true);
    }

    private RockSet(boolean fill) {
        if (fill) {
            for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
                dark[i] = new RockFloat();
                light[i] = new RockFloat();
            }
        }
    }

    public Object clone() {
        final RockSet b = new RockSet(false);
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            b.dark[i] = (RockFloat) (this.dark[i].clone());
            b.light[i] = (RockFloat) (this.light[i].clone());
        }
        return b;
    }

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

    public RockFloat getDark(int i) {
        return dark[i];
    }

    public RockFloat getLight(int i) {
        return light[i];
    }

    public Rock getRock(int i) {
        if (i % 2 == 0)
            return dark[i / 2];
        return light[i / 2];
    }
}