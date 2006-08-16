/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.model;

import org.jcurl.core.dto.RockSet;

/**
 * Keep track of the time of the next hit of ALL pairs of positions.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class HitTimeMatrix {

    private final double[] pairs;

    public HitTimeMatrix() {
        this(RockSet.ROCKS_PER_SET * RockSet.ROCKS_PER_SET);
    }

    protected HitTimeMatrix(int v) {
        pairs = new double[v];
        reset(Double.NaN);
    }

    /** Mark all combinations Unknown */
    public void dirty(int index, double v) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i > index; i--)
            set(i, index, v);
        for (int i = index - 1; i >= 0; i--)
            set(index, i, v);
    }

    /**
     * Looks up the two set bits and calls {@link #findIdx(int, int)}.
     * 
     * @param bitmask
     * @return {@link #findIdx(int, int)}
     */
    int findIdx(int bitmask) {
        // find the lowest bit set
        bitmask &= RockSet.ALL_MASK;
        if (bitmask == 0)
            throw new IllegalArgumentException(
                    "Must have 2 bits set but has none.");
        int j = 0;
        while ((bitmask & 1) != 1) {
            bitmask >>= 1;
            j++;
        }
        bitmask >>= 1;
        // find the 2nd lowest bit set
        if (bitmask == 0)
            throw new IllegalArgumentException(
                    "Must have 2 bits set but has only one.");
        int i = j + 1;
        while ((bitmask & 1) != 1) {
            bitmask >>= 1;
            i++;
        }
        bitmask >>= 1;
        if (bitmask != 0)
            throw new IllegalArgumentException(
                    "Must have 2 bits set but has more than that.");
        return findIdx(i, j);
    }

    int findIdx(int i, int j) {
        if (i == j)
            throw new IllegalArgumentException("Won't ever hit myself: " + i);
        if (j > i)
            throw new IllegalArgumentException("j>i: " + j + ">" + i);
        return i * RockSet.ROCKS_PER_SET + j;
    }

    public double get(int i, int j) {
        return pairs[findIdx(i, j)];
    }

    public void reset(double v) {
        for (int i = pairs.length - 1; i >= 0; i--)
            pairs[i] = v;
    }

    public void set(int i, int j, double v) {
        pairs[findIdx(i, j)] = v;
    }
}
