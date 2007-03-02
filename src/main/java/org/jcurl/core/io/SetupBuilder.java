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
package org.jcurl.core.io;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.CollissionStrategy;
import org.jcurl.core.base.Ice;
import org.jcurl.core.base.ModelBase;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SlideStrategy;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.math.MathVec;

/**
 * Accumulate setup data.
 * 
 * @see org.jcurl.core.io.SetupSaxDeSer
 * @see org.jcurl.core.io.OldConfigReader
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SetupBuilder.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class SetupBuilder {

    /** Internal class to accumulate rock data */
    private static class RockData {

        public int positionFlag = Coords;

        public int speedFlag = Coords;

        public DimVal to_x, to_y, speed;

        public DimVal vx, vy, spin;

        public DimVal x0, y0, a0;
    }

    private static final int Coords = 0;

    private static final Log log = JCLoggerFactory
            .getLogger(SetupBuilder.class);

    private static final int Out = 1;

    private static final int Release = 2;

    private static final int SpeedTo = 4;

    private CollissionStrategy collStrat = null;

    private boolean isFrozen = false;

    private final PositionSet pos = PositionSet.allHome();

    private final RockData[] rocks;

    private SlideStrategy slideStrat = null;

    private final SpeedSet speed = new SpeedSet();

    SetupBuilder() {
        rocks = new RockData[RockSet.ROCKS_PER_SET];
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            rocks[i] = new RockData();
    }

    void addModel(final Class clz, final Map params)
            throws InstantiationException, IllegalAccessException {
        final ModelBase mb = (ModelBase) clz.newInstance();
        mb.init(params);
        if (mb instanceof CollissionStrategy)
            collStrat = (CollissionStrategy) mb;
        else if (mb instanceof SlideStrategy)
            slideStrat = (SlideStrategy) mb;
        else
            throw new IllegalArgumentException("Unknown model type "
                    + clz.getName());
    }

    private void digest() {
        try {
            log.debug("-");
            // set up the slider's collission engine - if possible
            if (slideStrat != null)
                slideStrat.setColl(collStrat);

            // set up positions and speeds
            for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
                final Rock x = pos.getRock(i);
                final Rock v = speed.getRock(i);
                // Position stuff
                switch (rocks[i].positionFlag) {
                case Out:
                    Ice.setOut(x, i % 2 == 0, i / 2);
                    break;
                case Coords:
                    if (rocks[i].x0 != null)
                        x.setX(rocks[i].x0.to(Dim.METER).val);
                    if (rocks[i].y0 != null)
                        x.setY(rocks[i].y0.to(Dim.METER).val);
                    if (rocks[i].a0 != null)
                        x.setZ(rocks[i].a0.to(Dim.RADIANT).val);
                    break;
                case Release:
                    log.info("TODO"); // TODO compute projection
                    break;
                default:
                    throw new IllegalStateException("Illegal positionFlag "
                            + rocks[i].positionFlag);
                }
                // Speed stuff
                if (rocks[i].spin != null)
                    v.setZ(rocks[i].spin.to(Dim.RAD_PER_SEC).val);
                switch (rocks[i].speedFlag) {
                case SpeedTo:
                    // check dimension
                    if (!Dim.SEC_HOG_HOG.equals(rocks[i].speed.dim))
                        throw new IllegalArgumentException(
                                "Expected something like " + Dim.SEC_HOG_HOG
                                        + ", not " + rocks[i].speed.dim);
                    // v = v0 * (to - x) / |to - x|
                    v.setLocation(rocks[i].to_x.to(Dim.METER).val,
                            rocks[i].to_y.to(Dim.METER).val);
                    MathVec.sub(v, x, v);
                    MathVec.mult(slideStrat.getInitialSpeed(x.getY(),
                            rocks[i].speed.val)
                            / MathVec.abs(v), v, v);
                    break;
                case Coords:
                    if (rocks[i].vx != null)
                        v.setX(rocks[i].vx.to(Dim.METER_PER_SEC).val);
                    if (rocks[i].vy != null)
                        v.setY(rocks[i].vy.to(Dim.METER_PER_SEC).val);
                    break;
                default:
                    throw new IllegalStateException("Illegal speedFlag "
                            + rocks[i].speedFlag);
                }
            }
        } catch (RuntimeException e) {
            log.error("", e);
            throw e;
        }
    }

    void freeze() {
        log.debug("-");
        if (!isFrozen) {
            isFrozen = true;
            digest();
        }
    }

    private void freezeCheck() {
        if (isFrozen)
            throw new IllegalStateException("Frozen");
    }

    public PositionSet getPos() {
        return pos;
    }

    public SlideStrategy getSlide() {
        return slideStrat;
    }

    public SpeedSet getSpeed() {
        return speed;
    }

    void setAngle(final int idx, final DimVal val) {
        log.debug(val);
        freezeCheck();
        if (!val.dim.isAngle())
            throw new IllegalArgumentException("Expected something like "
                    + Dim.RADIANT + ", not " + val.dim);
        rocks[idx].a0 = val;
    }

    void setPosNHog(final int idx) {
        log.debug("-");
        freezeCheck();
        setPosY(idx, new DimVal(Ice.HOG_2_TEE, Dim.METER));
    }

    void setPosOut(final int idx) {
        log.debug("-");
        freezeCheck();
        rocks[idx].positionFlag = Out;
    }

    void setPosRelease(final int idx) {
        log.debug("-");
        freezeCheck();
        rocks[idx].positionFlag = Release;
    }

    void setPosX(final int idx, final DimVal val) {
        log.debug(idx + " " + val);
        freezeCheck();
        if (!val.dim.isLength())
            throw new IllegalArgumentException("Expected something like "
                    + Dim.METER + ", not " + val.dim);
        rocks[idx].x0 = val;
        rocks[idx].positionFlag = Coords;
    }

    void setPosY(final int idx, final DimVal val) {
        log.debug(idx + " " + val);
        freezeCheck();
        if (!val.dim.isLength())
            throw new IllegalArgumentException("Expected something like "
                    + Dim.METER + ", not " + val.dim);
        rocks[idx].y0 = val;
        rocks[idx].positionFlag = Coords;
    }

    void setSpeed(final int idx, final DimVal val) {
        log.debug(idx + " " + val);
        freezeCheck();
        rocks[idx].speed = val;
        rocks[idx].speedFlag = SpeedTo;
    }

    void setSpeedX(final int idx, final DimVal val) {
        log.debug(idx + " " + val);
        freezeCheck();
        if (!val.dim.isSpeed())
            throw new IllegalArgumentException("Expected something like "
                    + Dim.METER_PER_SEC + ", not " + val.dim);
        rocks[idx].vx = val;
        rocks[idx].speedFlag = Coords;
    }

    void setSpeedY(final int idx, final DimVal val) {
        log.debug(idx + " " + val);
        freezeCheck();
        if (!val.dim.isSpeed())
            throw new IllegalArgumentException("Expected something like "
                    + Dim.METER_PER_SEC + ", not " + val.dim);
        rocks[idx].vy = val;
        rocks[idx].speedFlag = Coords;
    }

    void setSpin(final int idx, final DimVal val) {
        log.debug(idx + " " + val);
        freezeCheck();
        if (!val.dim.isSpin())
            throw new IllegalArgumentException("Expected something like "
                    + Dim.RAD_PER_SEC + ", not " + val.dim);
        rocks[idx].spin = val;
    }

    void setToX(final int idx, final DimVal val) {
        log.debug(idx + " " + val);
        freezeCheck();
        if (!val.dim.isLength())
            throw new IllegalArgumentException("Expected something like "
                    + Dim.METER + ", not " + val.dim);
        rocks[idx].to_x = val;
        rocks[idx].speedFlag = SpeedTo;
    }

    void setToY(final int idx, final DimVal val) {
        log.debug(idx + " " + val);
        freezeCheck();
        if (!val.dim.isLength())
            throw new IllegalArgumentException("Expected something like "
                    + Dim.METER + ", not " + val.dim);
        rocks[idx].to_y = val;
        rocks[idx].speedFlag = SpeedTo;
    }
}