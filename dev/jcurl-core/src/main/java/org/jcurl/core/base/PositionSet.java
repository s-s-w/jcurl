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

import java.awt.geom.Point2D;

import org.jcurl.core.helpers.Dim;

/**
 * A {@link org.jcurl.core.base.RockSet}&nbsp;with location semantics.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PositionSet extends RockSet {

    private static final long serialVersionUID = -4106725355290612312L;

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
        ret.notifyChange();
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
        ret.notifyChange();
        return ret;
    }

    /**
     * @param rocks
     * @param pos
     * @return <code>-1</code> if none
     */
    public static int findRockIndexAtPos(final PositionSet rocks,
            final Point2D pos) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--) {
            if (rocks.getRock(i).distanceSq(pos) <= RR)
                return i;
        }
        return -1;
    }

    /**
     * 
     * @param rocks
     * @param pos
     * @param myself
     * @return <code>-1</code> if none
     */
    public static int findRockIndexTouchingRockAtPos(final PositionSet rocks,
            final Point2D pos, int myself) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--) {
            if (i == myself)
                continue;
            if (rocks.getRock(i).distanceSq(pos) <= RR4)
                return i;
        }
        return -1;
    }

    /**
     * Get the "out" positions (as bitmask).
     * 
     * @param a
     * @return bitmask of the out positions.
     */
    public static int getOutRocks(final PositionSet a) {
        final double xmin = Ice.SIDE_2_CENTER + RockProps.DEFAULT.getRadius();
        final double ymin = -Ice.BACK_2_TEE - RockProps.DEFAULT.getRadius();
        int ret = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Point2D rock = a.getRock(i);
            final double x = Math.abs(rock.getX());
            final double y = rock.getY();
            if (x > xmin || y < ymin)
                ret |= 1 << i;
        }
        return ret;
    }

    /**
     * Get the "shot" positions (as bitmask).
     * 
     * @param a
     * @return bitmask of the shot positions.
     */
    public static int getShotRocks(final PositionSet a) {
        final int scorer;
        final double scoreDistSq;
        {
            // first get the best rock's distance square for each dark and
            // light.
            double distDarkSq = MaxScoreDistSq;
            double distLightSq = MaxScoreDistSq;
            for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
                double distSq = a.getDark(i).distanceSq(0, 0);
                if (distSq < distDarkSq)
                    distDarkSq = distSq;
                distSq = a.getLight(i).distanceSq(0, 0);
                if (distSq < distLightSq)
                    distLightSq = distSq;
            }
            if (distDarkSq == distLightSq)
                return 0;
            // who scores?
            scorer = distDarkSq < distLightSq ? 0 : 1;
            // limiting distance
            scoreDistSq = distDarkSq > distLightSq ? distDarkSq : distLightSq;
        }
        int ret = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (i % 2 != scorer)
                continue;
            double distSq = a.getRock(i).distanceSq(0, 0);
            if (distSq <= scoreDistSq)
                ret |= 1 << i;
        }
        return ret;
    }

    /**
     * Get the "waiting" positions (as bitmask).
     * 
     * @param a
     * @return bitmask of the waiting positions.
     */
    public static int getWaitRocks(final PositionSet a) {
        final double xmax = Ice.SIDE_2_CENTER + RockProps.DEFAULT.getRadius();
        final double ymax = Ice.HOG_2_TEE - RockProps.DEFAULT.getRadius();
        int ret = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            final Point2D rock = a.getRock(i);
            final double x = Math.abs(rock.getX());
            final double y = rock.getY();
            if (x < xmax && y > ymax)
                ret |= 1 << i;
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
