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
import org.jcurl.core.dto.SpeedSet;
import org.jcurl.core.dto.TrajectoryComputed;

public class CurvesComputed implements TrajectoryComputed {

    private Curler curler;

    private Collider collider;

    private PositionSet initialPos;

    private SpeedSet initialSpeed;

    public Curler getCurler() {
        return curler;
    }

    public PositionSet getCurrentPos() {
        // TODO Auto-generated method stub
        return null;
    }

    public SpeedSet getCurrentSpeed() {
        // TODO Auto-generated method stub
        return null;
    }

    public double getCurrentTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    public PathIterator[] getCurves() {
        // TODO Auto-generated method stub
        return null;
    }

    public PositionSet getInitialPos() {
        return initialPos;
    }

    public SpeedSet getInitialSpeed() {
        return initialSpeed;
    }

    public void setCurler(Curler curler) {
        this.curler = curler;
    }

    public void setCurrentTime(double t) {
        // TODO Auto-generated method stub

    }

    public void setInitialPos(PositionSet initialPos) {
        this.initialPos = initialPos;
    }

    public void setInitialSpeed(SpeedSet initialSpeed) {
        this.initialSpeed = initialSpeed;
    }

    public Collider getCollider() {
        return collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
    }

}
