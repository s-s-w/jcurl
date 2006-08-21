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
package org.jcurl.core.curved;

import java.awt.geom.PathIterator;

import org.jcurl.core.curved.CollissionStore.HitTupel;
import org.jcurl.core.dto.Collider;
import org.jcurl.core.dto.Curler;
import org.jcurl.core.dto.PositionSet;
import org.jcurl.core.dto.Rock;
import org.jcurl.core.dto.RockDouble;
import org.jcurl.core.dto.RockSet;
import org.jcurl.core.dto.SpeedSet;
import org.jcurl.core.dto.TrajectoryComputed;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.math.MathException;

public class CurveTrajectory implements TrajectoryComputed {

    private final CollissionStore cs = new CollissionStore();

    private Collider collider;

    private CurveCurler curler;

    private PositionSet currentPos;

    private SpeedSet currentSpeed;

    private double currentTime;

    private double stopTime;

    private PositionSet initialPos;

    private SpeedSet initialSpeed;

    public PathIterator computePath(int idx) {
        throw new NotImplementedYetException();
    }

    public Collider getCollider() {
        return collider;
    }

    public Curler getCurler() {
        return curler;
    }

    public PositionSet getCurrentPos() {
        return currentPos;
    }

    public SpeedSet getCurrentSpeed() {
        return currentSpeed;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public PositionSet getInitialPos() {
        return initialPos;
    }

    public SpeedSet getInitialSpeed() {
        return initialSpeed;
    }

    public PathIterator[] getPaths() {
        throw new NotImplementedYetException();
    }

    private void reset() {
        stopTime = Double.NaN;
        currentTime = Double.NaN;
        cs.clear();
    }

    public void setCollider(Collider collider) {
        reset();
        this.collider = collider;
    }

    public void setCurler(Curler curler) {
        reset();
        this.curler = (CurveCurler) curler;
    }

    Rock newRock() {
        return new RockDouble();
    }

    public void setCurrentTime(double t) throws MathException {
        if (Double.isNaN(currentTime)) {
            cs.init(curler, initialPos, initialSpeed);
            currentTime = 0;
        }
        if (t > stopTime)
            t = stopTime;
        if (t == currentTime)
            return;
        // could aggregate change-bits to minimize currentXXX computations.
        final int changeMask = RockSet.ALL_MASK;
        for (double tNow = currentTime; tNow < t;) {
            final HitTupel n = cs.getNextHit(t);
            final double tHit = n.getTime();
            if (tNow > tHit) {
                final Rock ax = cs.curve(n.a).value(tHit, 0, newRock());
                final Rock av = cs.curve(n.a).value(tHit, 1, newRock());
                final Rock bx = cs.curve(n.b).value(tHit, 0, newRock());
                final Rock bv = cs.curve(n.b).value(tHit, 1, newRock());
                collider.compute(ax, av, bx, bv);
                cs.change(curler, tHit, t, n.a, ax, av, n.b, bx, bv);
                tNow = tHit;
            } else
                break;
        }
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            // could use change-bits to minimize currentXXX computations.
            cs.curve(i).value(t, 0, currentPos.getRock(i));
            cs.curve(i).value(t, 1, currentSpeed.getRock(i));
        }
        currentPos.notifyChange();
        currentSpeed.notifyChange();
        this.currentTime = t;
    }

    public void setInitialPos(PositionSet initialPos) {
        reset();
        this.initialPos = initialPos;
    }

    public void setInitialSpeed(SpeedSet initialSpeed) {
        reset();
        this.initialSpeed = initialSpeed;
    }
}
