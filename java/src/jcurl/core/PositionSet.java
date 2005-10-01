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

import java.awt.geom.Point2D;

import jcurl.core.dto.Ice;
import jcurl.core.dto.RockProps;
import jcurl.core.io.Dim;
import jcurl.math.MathVec;

/**
 * A {@link jcurl.core.RockSet}%nbsp;with location semantics.
 * 
 * @see jcurl.core.PositionSetTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PositionSet extends RockSet {

    private static final double MaxScoreDistSq = sqr(RockProps.DEFAULT
            .getRadius()
            + Dim.f2m(6));

    private static final double RR = sqr(RockProps.DEFAULT.getRadius());

    private static final double RR4 = RR * 4;

    public static PositionSet allHome() {
        return allHome(null);
    }

    public static PositionSet allHome(PositionSet ret) {
        ret = ret != null ? ret : new PositionSet();
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            Ice.setHome(ret.dark[i], true, i);
            Ice.setHome(ret.light[i], false, i);
        }
        return ret;
    }

    public static PositionSet allOut() {
        return allOut(null);
    }

    public static PositionSet allOut(PositionSet ret) {
        ret = ret != null ? ret : new PositionSet();
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            Ice.setOut(ret.dark[i], true, i);
            Ice.setOut(ret.light[i], false, i);
        }
        return ret;
    }

    /**
     * @see PositionSetTest#test020_findRockAtPos()
     * @param rocks
     * @param pos
     * @return
     */
    public static int findRockIndexAtPos(final PositionSet rocks,
            final Point2D pos) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--) {
            if (MathVec.distSqr(rocks.getRock(i), pos) <= RR)
                return i;
        }
        return -1;
    }

    /**
     * @param rocks
     * @param pos
     * @param myself
     *            TODO
     * @see PositionSetTest#test020_findRockAtPos()
     * @return
     */
    public static int findRockIndexTouchingRockAtPos(final PositionSet rocks,
            final Point2D pos, int myself) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--) {
            if (i == myself)
                continue;
            if (MathVec.distSqr(rocks.getRock(i), pos) <= RR4)
                return i;
        }
        return -1;
    }

    /**
     * Get the "shot" rocks (as bitmask).
     * 
     * @param a
     * @return bitmask of the shot rocks.
     */
    public static int getShotRocks(final PositionSet a) {
        int ret = 0;
        int scoring = 0;
        double scoreDistSq = MaxScoreDistSq;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final double distSq = a.getRock(i).distanceSq(0, 0);
            if (distSq < scoreDistSq) {
                if (i % 2 != scoring) {
                    // if the scoring color changes start anew
                    ret = 0;
                    scoring = i % 2;
                }
                ret |= 1 << i;
                scoreDistSq = distSq;
            }
        }
        return ret;
    }

    public PositionSet() {
        super();
    }

    protected PositionSet(boolean fill) {
        super(fill);
    }

    public PositionSet(final RockSet b) {
        super(b);
    }

    public Object clone() {
        return new PositionSet(this);
    }
}