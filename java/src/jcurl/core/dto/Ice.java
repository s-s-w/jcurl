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
package jcurl.core.dto;

import jcurl.core.PositionSet;
import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.io.Dim;

/**
 * Handle ice properties as friction and curl.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Ice {

    /** Distance from Tee to Back-line. 6 feet (converted to meter) */
    public static final float BACK_2_TEE = Dim.f2m(6.0);

    /** Distance from Tee to Hack. 21+72+21+6 feet (converted to meter) */
    public static final float FAR_HACK_2_TEE = Dim
            .f2m(21.0 + 72.0 + 21.0 + 12.0);

    /**
     * Distance from Tee to Hog (on player's end). 21+72 feet (converted to
     * meter)
     */
    public static final float FAR_HOG_2_TEE = Dim.f2m(21.0 + 72.0);

    /** Distance from Hack to Back-line. 6 feet (converted to meter) */
    public static final float HACK_2_BACK = Dim.f2m(6.0);

    /** Distance from far Hog-line to near Hog-line. 72 feet (converted to meter) */
    public static final float HOG_2_HOG = Dim.f2m(72.0);

    /** Distance from Tee to Hog (near the house). 21 feet (converted to meter) */
    public static final float HOG_2_TEE = Dim.f2m(21.0);

    private static final double outX;

    private static final double outY;

    private static final float rad = RockProps.DEFAULT.getRadius();

    /** Distance from Center-line to edge. 6+1 feet (converted to meter) */
    public static final float SIDE_2_CENTER = Dim.f2m(7.0);

    static {
        outX = SIDE_2_CENTER + rad;
        outY = BACK_2_TEE + rad;
    }

    /**
     * Check all moving rocks if they're still in play
     * 
     * @param pos
     * @param speed
     * @return bitmask of modified (removed) rocks
     */
    public static final int checkOut(final PositionSet pos,
            final PositionSet speed) {
        int ret = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (speed.getRock(i).nonzero()) {
                final Rock r = pos.getRock(i);
                if (r.getX() > outX || r.getX() < -outX || r.getY() < -outY) {
                    setOut(r, (i % 2) == 0, i / 2);
                    speed.getRock(i).setLocation(0, 0, 0);
                    ret |= 1 << i;
                }
            }
        }
        return ret;
    }

    /**
     * tests whether a rock is fully between far-hog and back and the arg and
     * right edge.
     * 
     * @param x
     *            the rock to check
     * @return true/false
     */
    public static boolean ingame(final Rock x) {
        return -x.getY() < (BACK_2_TEE + rad)
                && x.getY() < (FAR_HOG_2_TEE - rad)
                && x.getX() < (SIDE_2_CENTER - rad)
                && -x.getX() < (SIDE_2_CENTER - rad);
    }

    /**
     * tests whether a rock is fully between far-hog and back and the arg and
     * right edge.
     * 
     * @param x
     *            rock's location
     * @return <code>true/false</code>
     */
    public static boolean ingamePlus(final Rock x) {
        return -x.getY() < (BACK_2_TEE + rad)
                && x.getY() < (FAR_HACK_2_TEE + rad)
                && x.getX() < (SIDE_2_CENTER - rad)
                && -x.getX() < (SIDE_2_CENTER - rad);
    }

    /**
     * Start-lineup. Two rows at each edge of the rink (parallel to
     * center-line), lead rocks first (at the front), color 0 on the arg, 1
     * right. Note: the distance between the rows (0.5 foot) is a little more
     * than a rock's diameter.
     * 
     * @param R
     *            rock
     * @param isDark
     *            <code>true/false</code>
     * @param idx
     *            index [0-7]
     */
    public static void setHome(final Rock R, final boolean isDark, final int idx) {
        final float D = 1.2F * rad;
        final float homeX[] = { Ice.SIDE_2_CENTER - 1.0F * D,
                Ice.SIDE_2_CENTER - 3.0F * D };
        final float homeY[] = { Ice.FAR_HACK_2_TEE - 1.0F * D,
                Ice.FAR_HACK_2_TEE - 3.0F * D, Ice.FAR_HACK_2_TEE - 5.0F * D,
                Ice.FAR_HACK_2_TEE - 7.0F * D };
        R.setX((isDark ? -1 : 1) * homeX[idx % 2]);
        R.setY(homeY[idx / 2]);
        R.setZ(0);
    }

    /**
     * 'Stop'-lineup. Two rows at each edge of the rink (parallel to back-line),
     * lead rocks outmost, color 0 on the arg, 1 right. Note: the distance
     * between the rows (0.5 foot) is a little more than a rock's diameter.
     * 
     * @param R
     *            rock reference
     * @param isDark
     *            <code>true/false</code>
     * @param i
     *            index
     */
    public static void setOut(final Rock R, final boolean isDark, final int i) {
        final float D = 1.2F * rad;
        final float outY[] = { -Ice.BACK_2_TEE - 5.0F * D,
                -Ice.BACK_2_TEE - 7.0F * D };
        final float outX[] = { Ice.SIDE_2_CENTER - 7.0F * D,
                Ice.SIDE_2_CENTER - 5.0F * D, Ice.SIDE_2_CENTER - 3.0F * D,
                Ice.SIDE_2_CENTER - 1.0F * D };
        R.setX((isDark ? -1 : 1) * outX[i / 2]);
        R.setY(outY[i % 2]);
        R.setZ(0);
    }
}