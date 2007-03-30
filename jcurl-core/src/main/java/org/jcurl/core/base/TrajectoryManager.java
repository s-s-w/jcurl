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
import org.jcurl.core.base.CollissionStore.Tupel;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.R1RNFunction;

/**
 * Bring it all together and trigger computation.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class TrajectoryManager extends MutableObject implements
        PropertyChangeListener {

    private static final double hitDt = 1e-6;

    private static final Log log = JCLoggerFactory
            .getLogger(TrajectoryManager.class);

    private static final long serialVersionUID = 7198540442889130378L;

    private Collider collider = null;

    private CollissionDetector collissionDetector = null;

    private final CollissionStore collissionStore = new CollissionStore();

    private final PositionSet currentPos = new PositionSet();

    private final SpeedSet currentSpeed = new SpeedSet();

    private double currentTime = 0;

    private CurveStore curveStore = new CurveStore(RockSet.ROCKS_PER_SET);

    private boolean dirty = true;

    private PositionSet initialPos = null;

    private SpeedSet initialSpeed = null;

    private SlideNoCurl slider = null;

    public TrajectoryManager() {
    }

    /**
     * Internal. Compute one rock curve segment and don't change internal state.
     * 
     * @param i
     *            which rock
     * @param t0
     *            starttime
     * @return the new Curve in world coordinates.
     */
    R1RNFunction doComputeCurve(final int i, final double t0,
            final PositionSet p, final SpeedSet s) {
        final Rock x = p.getRock(i);
        final Rock v = s.getRock(i);
        final R1RNFunction wc;
        if (v.distanceSq(0, 0) == 0)
            wc = CurveRock.still(x);
        else
            // FIXME add stop detection! Either here or in each slider?
            wc = new CurveTransformed(slider.computeRc(x, v), CurveTransformed
                    .createRc2Wc(new AffineTransform(), x, v), t0);
        if (log.isDebugEnabled())
            log.debug(i + " " + wc);
        return wc;
    }

    /**
     * Internal.
     * 
     * @return when is the next hit, which are the rocks involved.
     */
    Tupel doGetNextHit() {
        doInit();
        return collissionStore.first();
    }

    /**
     * Internal.
     */
    void doInit() {
        if (!dirty)
            return;
        final double t0 = 0.0;
        // initial curves:
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            curveStore.reset(i);
            curveStore.add(i, t0, doComputeCurve(i, t0, initialPos,
                    initialSpeed));
        }
        // initial collission detection:
        collissionStore.clear();
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            for (int j = i - 1; j >= 0; j--)
                // log.info("collissionDetect " + i + ", " + j);
                collissionStore.add(collissionDetector.compute(t0, 30,
                        curveStore.getCurve(i), curveStore.getCurve(j)), i, j);
        dirty = false;
    }

    /**
     * Internal. Typically after a hit: Recompute the new curves and upcoming
     * collission candidates.
     * 
     * @param hitMask
     * @return bitmask of rocks with new curves
     */
    int doRecomputeCurvesAndCollissionTimes(final int hitMask, double t0) {
        int computedMask = 0;
        // first computed the new curves:
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (!RockSet.isSet(hitMask, i))
                continue;
            curveStore.add(i, t0, doComputeCurve(i, t0, currentPos,
                    currentSpeed));
            computedMask |= 1 << i;
        }
        // then and all combinations of potential collissions
        t0 += hitDt;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (!RockSet.isSet(computedMask, i))
                continue;
            for (int j = RockSet.ROCKS_PER_SET - 1; j >= 0; j--) {
                if (i == j || i > j && RockSet.isSet(computedMask, j))
                    continue;
                collissionStore.replace(collissionDetector.compute(t0, 30,
                        curveStore.getCurve(i), curveStore.getCurve(j)), i, j);
            }
        }
        return computedMask;
    }

    /**
     * Internal. Does not {@link RockSet#notifyChange()}!
     * 
     * @param currentTime
     * @param tmp
     */
    void doUpdatePosAndSpeed(final double currentTime, final double[] tmp) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            currentPos.getRock(i).setLocation(
                    curveStore.getCurve(i).at(0, currentTime, tmp));
            currentSpeed.getRock(i).setLocation(
                    curveStore.getCurve(i).at(1, currentTime, tmp));
        }
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
        dirty = true;
        propChange.firePropertyChange("collider", this.collider, collider);
        this.collider = collider;
    }

    public void setCollissionDetector(
            final CollissionDetector collissionDetector) {
        dirty = true;
        propChange.firePropertyChange("collissionDetector",
                this.collissionDetector, collissionDetector);
        this.collissionDetector = collissionDetector;
    }

    public void setCurrentTime(final double currentTime) {
        // log.info(Double.toString(currentTime));
        if (!dirty) {
            if (this.currentTime == currentTime)
                return;
        } else
            doInit();
        {
            // TUNE thread safety at the cost of two instanciations per call:
            final double[] tmp = { 0, 0, 0 };
            final AffineTransform m = new AffineTransform();
            // NaN-safe time range check (are we navigating known ground?):
            while (currentTime > doGetNextHit().t) {
                final Tupel nh = doGetNextHit();
                doUpdatePosAndSpeed(nh.t, tmp);
                // compute collission(s);
                final int mask = collider.compute(currentPos, currentSpeed, m);
                if (mask == 0)
                    break;
                doRecomputeCurvesAndCollissionTimes(mask, nh.t);
            }
            doUpdatePosAndSpeed(currentTime, tmp);
        }
        {
            final double ot = this.currentTime;
            this.currentTime = currentTime;
            currentPos.notifyChange();
            currentSpeed.notifyChange();
            propChange.firePropertyChange("currentTime", ot, currentTime);
            propChange.firePropertyChange("currentPos", currentPos, currentPos);
            propChange.firePropertyChange("currentSpeed", currentSpeed,
                    currentSpeed);
        }
    }

    public void setCurveStore(final CurveStore curveStore) {
        dirty = true;
        propChange
                .firePropertyChange("curveStore", this.curveStore, curveStore);
        this.curveStore = curveStore;
    }

    public void setInitialPos(final PositionSet initialPos) {
        dirty = true;
        propChange
                .firePropertyChange("initialPos", this.initialPos, initialPos);
        this.initialPos = initialPos;
    }

    public void setInitialSpeed(final SpeedSet initialSpeed) {
        dirty = true;
        propChange.firePropertyChange("initialSpeed", this.initialSpeed,
                initialSpeed);
        this.initialSpeed = initialSpeed;
    }

    public void setSlider(final SlideNoCurl slider) {
        dirty = true;
        propChange.firePropertyChange("slider", this.slider, slider);
        this.slider = slider;
    }
}