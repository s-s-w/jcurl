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

import java.awt.geom.Point2D;

import org.jcurl.core.helpers.Unit;

/**
 * A {@link RockSet} with location semantics.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:PositionSet.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class PositionSet extends RockSet {

    private static final double MaxScoreDistSq = sqr(RockProps.DEFAULT
            .getRadius()
            + Unit.f2m(6));

    private static final double RR = sqr(RockProps.DEFAULT.getRadius());

    private static final double RR4 = RR * 4;

    private static final long serialVersionUID = -4106725355290612312L;

    public static PositionSet allHome() {
        return allHome(new PositionSet(new RockDouble()));
    }

    public static PositionSet allHome(final PositionSet ret) {
        if (ret == null)
            return null;
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            IceSize.setHome(ret.dark[i], true, i);
            IceSize.setHome(ret.light[i], false, i);
        }
        ret.fireStateChanged();
        return ret;
    }

    public static PositionSet allOut() {
        return allOut(new PositionSet(new RockDouble()));
    }

    public static PositionSet allOut(final PositionSet ret) {
        if (ret == null)
            return null;
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            IceSize.setOut(ret.dark[i], true, i);
            IceSize.setOut(ret.light[i], false, i);
        }
        ret.fireStateChanged();
        return ret;
    }

    /**
     * @param rocks
     * @param pos
     * @return <code>-1</code> if none
     */
    public static int findRockIndexAtPos(final PositionSet rocks,
            final Point2D pos) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--)
            if (rocks.getRock(i).distanceSq(pos) <= RR)
                return i;
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
            final Point2D pos, final int myself) {
        for (int i = ROCKS_PER_SET - 1; i >= 0; i--) {
            if (i == myself)
                continue;
            if (rocks.getRock(i).distanceSq(pos) <= RR4)
                return i;
        }
        return -1;
    }

    /**
     * Get the "out" rocks (as bitmask).
     * 
     * @param a
     * @return bitmask of the out rocks.
     */
    public static int getOutRocks(final PositionSet a) {
        final double xmin = IceSize.SIDE_2_CENTER
                + RockProps.DEFAULT.getRadius();
        final double ymin = -IceSize.BACK_2_TEE - RockProps.DEFAULT.getRadius();
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
     * Get the "shot" rocks (as bitmask).
     * 
     * @param a
     * @return bitmask of the shot rocks.
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
            final double distSq = a.getRock(i).distanceSq(0, 0);
            if (distSq <= scoreDistSq)
                ret |= 1 << i;
        }
        return ret;
    }

    /**
     * Get the "waiting" rocks (as bitmask).
     * 
     * @param a
     * @return bitmask of the waiting rocks.
     */
    public static int getWaitRocks(final PositionSet a) {
        final double xmax = IceSize.SIDE_2_CENTER
                + RockProps.DEFAULT.getRadius();
        final double ymax = IceSize.HOG_2_TEE - RockProps.DEFAULT.getRadius();
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

    public PositionSet(final Rock seed) {
        super(seed);
    }

    public PositionSet(final RockSet b) {
        super(b);
    }

    @Override
    public Object clone() {
        return new PositionSet(this);
    }
}
