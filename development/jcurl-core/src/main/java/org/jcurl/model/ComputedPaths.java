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
package org.jcurl.model;

import jcurl.sim.core.CollissionStrategy;
import jcurl.sim.core.SlideStrategy;
import jcurl.sim.model.SlideStraight;

import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.PositionSet;
import org.jcurl.core.RockSet;
import org.jcurl.core.SpeedSet;
import org.jcurl.core.helpers.NotImplementedYetException;

public class ComputedPaths extends RockSetPaths {

    private CollissionStrategy collider;

    private SlideStrategy ice = new SlideStraight();

    private PositionSet initialPos = new PositionSet();

    private SpeedSet initialSpeed  = new SpeedSet();

    public ComputedPaths() throws FunctionEvaluationException {
        setInitialPos(new PositionSet());
        setInitialSpeed(new SpeedSet());
        setCurrentPos(new PositionSet());
        setCurrentSpeed(new SpeedSet());
    }

    public RecordedPaths export(final RecordedPaths dst) {
        throw new NotImplementedYetException();
    }

    public CollissionStrategy getCollider() {
        return collider;
    }

    public PositionSet getCurrentPos() throws FunctionEvaluationException {
        return ice.getPos();
    }

    public SpeedSet getCurrentSpeed() {
        return ice.getSpeed();
    }

    public double getCurrentT() {
        return ice.getT();
    }

    public SlideStrategy getIce() {
        return ice;
    }

    public PositionSet getInitialPos() {
        return initialPos;
    }

    public SpeedSet getInitialSpeed() {
        return initialSpeed;
    }

    public double getKnown() {
        return ice.getMaxT();
    }

    public double getMaxT() {
        return ice.getMaxT();
    }

    public void setCollider(CollissionStrategy collider) {
        this.collider = collider;
    }

    public void setCurrentPos(PositionSet currentPos) {
        ;
    }

    public void setCurrentSpeed(SpeedSet currentSpeed) {
        ;
    }

    public void setCurrentT(double currentT) throws FunctionEvaluationException {
        ice.setT(currentT);
    }

    public void setIce(SlideStrategy slide) throws FunctionEvaluationException {
        this.ice = slide;
        RockSet.copy(getInitialPos(), getCurrentPos());
        RockSet.copy(getInitialSpeed(), getCurrentSpeed());
        if (ice != null)
            this.ice.reset(getCurrentPos(), getCurrentSpeed(), null);
    }

    public void setInitialPos(PositionSet initialPos)
            throws FunctionEvaluationException {
        this.initialPos = initialPos;
        setIce(getIce());
    }

    public void setInitialSpeed(SpeedSet initialSpeed)
            throws FunctionEvaluationException {
        this.initialSpeed = initialSpeed;
        setIce(getIce());
    }
}
