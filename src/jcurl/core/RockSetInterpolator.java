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

import jcurl.core.dto.RockSetProps;

/**
 * Interpolator for whole {@link jcurl.core.RockSet}s.
 * 
 * @see jcurl.core.IRockInterpolator
 * @see jcurl.core.CSplineRockInterpolator
 * @see jcurl.core.RockSetInterpolatorTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockSetInterpolator implements Source, TargetDiscrete {

    private final IRockInterpolator[] ip;

    /**
     * Use a {@link CSplineRockInterpolator}
     * 
     * @see CSplineRockInterpolator
     */
    public RockSetInterpolator() {
        this(CSplineRockInterpolator.class);
    }

    /**
     * 
     * @param clazz
     *            class implementing {@link IRockInterpolator}
     */
    public RockSetInterpolator(final Class clazz) {
        if (!IRockInterpolator.class.isAssignableFrom(clazz))
            throw new IllegalArgumentException("Must be a IRockInterpolator");
        ip = new IRockInterpolator[RockSet.ROCKS_PER_SET];
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            try {
                ip[i] = (IRockInterpolator) clazz.newInstance();
            } catch (InstantiationException e) {
                final IllegalArgumentException ex = new IllegalArgumentException(
                        "Must be a IRockInterpolator");
                ex.initCause(e);
                throw ex;
            } catch (IllegalAccessException e) {
                final IllegalArgumentException ex = new IllegalArgumentException(
                        "Must be a IRockInterpolator");
                ex.initCause(e);
                throw ex;
            }
    }

    public double getMaxT() {
        return ip[0].getMaxT();
    }

    public double getMinT() {
        return ip[0].getMinT();
    }

    public RockSet getPos(final double t, final RockSet rocks) {
        if (t < ip[0].getMinT() || t > ip[0].getMaxT())
            return null;
        final RockSet dat = rocks != null ? rocks : new RockSet();
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            ip[i].getPos(t, dat.getRock(i));
        return dat;
    }

    public RockSet getSpeed(final double t, final RockSet rocks) {
        if (t < ip[0].getMinT() || t > ip[0].getMaxT())
            return null;
        final RockSet dat = rocks != null ? rocks : new RockSet();
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            ip[i].getSpeed(t, dat.getRock(i));
        return dat;
    }

    /**
     * Interpolators are never discrete - that's what they're for.
     */
    public boolean isDiscrete() {
        return false;
    }

    public boolean isForwardOnly() {
        return false;
    }

    public boolean isWithSpeed() {
        return true;
    }

    public void reset(double startTime, RockSet startPos, RockSet startSpeed,
            RockSetProps props) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            this.ip[i].reset();
        setPos(startTime, startPos);
    }

    public void setPos(final double t, final RockSet rocks) {
        setPos(t, rocks, 0);
    }

    public void setPos(final double t, final RockSet rocks,
            final int discontinuous) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            ip[i].add(t, rocks.getRock(i), 0 != ((1 << i) | discontinuous));
        }
    }
}