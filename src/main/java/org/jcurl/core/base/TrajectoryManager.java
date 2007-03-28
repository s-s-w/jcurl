/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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

import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class TrajectoryManager extends MutableObject implements
        PropertyChangeListener {

    private static final Log log = JCLoggerFactory
            .getLogger(TrajectoryManager.class);

    private static final long serialVersionUID = 7198540442889130378L;

    private Collider collider = null;

    private CollissionDetector collissionDetector = null;

    private final PositionSet currentPos = new PositionSet();

    private final SpeedSet currentSpeed = new SpeedSet();

    private double currentTime = 0;

    private CurveStore curveStore = new CurveStore(RockSet.ROCKS_PER_SET);

    private boolean init = false;

    private PositionSet initialPos = null;

    private SpeedSet initialSpeed = null;

    private SlideNoCurl slider = null;

    public TrajectoryManager() {
    }

    void doInit() {
        if (init)
            return;
        final double t0 = 0.0;
        // initial curves:
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            curveStore.reset(i);
            final Rock x = initialPos.getRock(i);
            final Rock v = initialSpeed.getRock(i);
            final CurveRock rc = slider.computeRc(x, v);
            final CurveRock wc = new CurveTransformed(rc, CurveTransformed
                    .createRc2Wc(new AffineTransform(), x, v), t0);
            curveStore.add(i, t0, wc);
        }
        // TODO initial collission detection:

        init = true;
    }

    public boolean equals(final Object obj) {
        return false;
    }

    public Collider getCollider() {
        return collider;
    }

    public CollissionDetector getCollissionDetector() {
        return collissionDetector;
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

    public CurveStore getCurveStore() {
        return curveStore;
    }

    public PositionSet getInitialPos() {
        return initialPos;
    }

    public SpeedSet getInitialSpeed() {
        return initialSpeed;
    }

    public SlideNoCurl getSlider() {
        return slider;
    }

    public int hashCode() {
        return 0;
    }

    public void propertyChange(final PropertyChangeEvent arg0) {
        log.info(arg0);
    }

    public void setCollider(final Collider collider) {
        init = false;
        propChange.firePropertyChange("collider", this.collider, collider);
        this.collider = collider;
    }

    public void setCollissionDetector(
            final CollissionDetector collissionDetector) {
        init = false;
        propChange.firePropertyChange("collissionDetector",
                this.collissionDetector, collissionDetector);
        this.collissionDetector = collissionDetector;
    }

    public void setCurrentTime(final double currentTime) {
        log.info(Double.toString(currentTime));
        if (init) {
            if (this.currentTime == currentTime)
                return;
        } else
            doInit();
        // TODO check time range (is known ground?) and compute collissions if
        // necessary

        // thread safety at the cost of one instanciation per call:
        final double[] tmp = { 0, 0, 0 };
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            currentPos.getRock(i).setLocation(
                    curveStore.getCurve(i).at(0, currentTime, tmp));
            currentSpeed.getRock(i).setLocation(
                    curveStore.getCurve(i).at(1, currentTime, tmp));
        }
        final double ot = this.currentTime;
        this.currentTime = currentTime;
        currentPos.notifyChange();
        currentSpeed.notifyChange();
        propChange.firePropertyChange("currentTime", ot, currentTime);
        propChange.firePropertyChange("currentPos", currentPos, currentPos);
        propChange.firePropertyChange("currentSpeed", currentSpeed,
                currentSpeed);
    }

    public void setCurveStore(final CurveStore curveStore) {
        init = false;
        propChange
                .firePropertyChange("curveStore", this.curveStore, curveStore);
        this.curveStore = curveStore;
    }

    public void setInitialPos(final PositionSet initialPos) {
        init = false;
        propChange
                .firePropertyChange("initialPos", this.initialPos, initialPos);
        this.initialPos = initialPos;
    }

    public void setInitialSpeed(final SpeedSet initialSpeed) {
        init = false;
        propChange.firePropertyChange("initialSpeed", this.initialSpeed,
                initialSpeed);
        this.initialSpeed = initialSpeed;
    }

    public void setSlider(final SlideNoCurl slider) {
        init = false;
        propChange.firePropertyChange("slider", this.slider, slider);
        this.slider = slider;
    }

}
