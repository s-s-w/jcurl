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

import org.jcurl.core.dto.Collider;
import org.jcurl.core.dto.Curler;
import org.jcurl.core.dto.PositionSet;
import org.jcurl.core.dto.RockSet;
import org.jcurl.core.dto.SpeedSet;
import org.jcurl.core.dto.TrajectoryComputed;
import org.jcurl.core.helpers.NotImplementedYetException;

public class CurveTrajectory implements TrajectoryComputed {

    private Collider collider;

    private CurveCurler curler;

    private PositionSet currentPos;

    private SpeedSet currentSpeed;

    private double currentTime;

    private final CurvePiecewise[] curves = new CurvePiecewise[RockSet.ROCKS_PER_SET];

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
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            curves[i].clear();
        setCurrentTime(0);
    }

    public void setCollider(Collider collider) {
        reset();
        this.collider = collider;
    }

    public void setCurler(Curler curler) {
        reset();
        this.curler = (CurveCurler) curler;
    }

    public void setCurrentTime(double t) {
        // TODO detect collissions
        // TODO compute collissions
        // TODO compute new trajectories
        // TODO set currentPos
        // TODO set currentSpeed
        // TODO set initialPos
        // TODO set initialSpeed
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
