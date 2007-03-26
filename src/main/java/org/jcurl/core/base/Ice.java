/*
 * jcurl curling simulation framework http://www.jcurl.org
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

import org.jcurl.core.helpers.Dim;

/**
 * Ice dimensions.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Ice.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public final class Ice {

    /** Distance from Back-line to Hog. 21+6 feet (converted to meter) */
    public static final float BACK_2_HOG = Dim.f2m(21 + 6);

    /** Distance from Tee to Back-line. 6 feet (converted to meter) */
    public static final float BACK_2_TEE = Dim.f2m(6);

    /** Distance from Tee to Hack. 6+6+21+72+21 feet (converted to meter) */
    public static final float FAR_HACK_2_TEE = Dim.f2m(6 + 6 + 21 + 72 + 21);

    /**
     * Distance from Tee to Hog (on player's end). 21+72 feet (converted to
     * meter)
     */
    public static final float FAR_HOG_2_TEE = Dim.f2m(21 + 72);

    /** Distance from Hack to Back-line. 6 feet (converted to meter) */
    public static final float HACK_2_BACK = Dim.f2m(6);

    /** Distance from Hack to Hog. 6+6+21 feet (converted to meter) */
    public static final float HACK_2_HOG = Dim.f2m(6 + 6 + 21);

    /** Distance from far Hog-line to near Hog-line. 72 feet (converted to meter) */
    public static final float HOG_2_HOG = Dim.f2m(72);

    /** Distance from Tee to Hog (near the house). 21 feet (converted to meter) */
    public static final float HOG_2_TEE = Dim.f2m(21);

    private static final double outX;

    private static final double outY;

    private static final float rad;

    /** Distance from Center-line to edge. 6+1 feet (converted to meter) */
    public static final float SIDE_2_CENTER = Dim.f2m(7);

    static {
        rad = Dim.f2m(0.5);
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
    public static int checkOut(final PositionSet pos, final PositionSet speed) {
        int ret = 0;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            if (speed.getRock(i).nonZero()) {
                final Rock r = pos.getRock(i);
                if (r.getX() > outX || r.getX() < -outX || r.getY() < -outY) {
                    setOut(r, i % 2 == 0, i / 2);
                    speed.getRock(i).setLocation(0, 0, 0);
                    ret |= 1 << i;
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
        return -x.getY() < BACK_2_TEE + rad && x.getY() < FAR_HOG_2_TEE - rad
                && x.getX() < SIDE_2_CENTER - rad
                && -x.getX() < SIDE_2_CENTER - rad;
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
        return -x.getY() < BACK_2_TEE + rad && x.getY() < FAR_HACK_2_TEE + rad
                && x.getX() < SIDE_2_CENTER - rad
                && -x.getX() < SIDE_2_CENTER - rad;
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
        // R.setLocation((isDark ? -1 : 1) * Dim.f2m(5 + (idx % 2) * 1.2), Dim
        // .f2m(120 + 1.2 * (idx / 2)), 0);
        R.setLocation((isDark ? -1 : 1) * Dim.f2m(7.25), Dim
                .f2m(31 - 1.2 * idx), 0);
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
        R.setLocation((isDark ? -1 : 1) * Dim.f2m(2.5 + 1.2 * i / 2), Dim
                .f2m(i % 2 == 0 ? -8 : -9.2), 0);
    }

    private Ice() {
    }
}