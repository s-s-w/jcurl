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
package org.jcurl.core.model;

import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ObjectStreamException;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.Collider;
import org.jcurl.core.base.CollissionDetector;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.Curler;
import org.jcurl.core.base.CurveStill;
import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.CurveTransformed;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.StopDetector;
import org.jcurl.core.helpers.Annotations;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CollissionStore.Tupel;
import org.jcurl.math.MathVec;
import org.jcurl.math.R1RNFunction;
import org.jcurl.math.R1RNFunctionImpl;

/**
 * Bring it all together and trigger computation.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CurveManager.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class CurveManager extends MutableObject implements
        PropertyChangeListener, ComputedTrajectorySet {

    private static final double _30 = 30.0;

    /** Time leap during a hit. */
    private static final double hitDt = 1e-6;

    private static final Log log = JCLoggerFactory
            .getLogger(CurveManager.class);

    private static final double NoSweep = 0;

    private static final long serialVersionUID = 7198540442889130378L;

    private static final StopDetector stopper = new NewtonStopDetector();

    private final Annotations annotations = new Annotations();

    private Collider collider = null;

    private CollissionDetector collissionDetector = null;

    private transient final CollissionStore collissionStore = new CollissionStore();

    private Curler curler = null;

    private transient final PositionSet currentPos = PositionSet.allHome();

    private transient final SpeedSet currentSpeed = new SpeedSet(PositionSet
            .allHome());

    private transient double currentTime = 0;

    private transient CurveStore curveStore = new CurveStoreImpl(stopper,
            RockSet.ROCKS_PER_SET);

    private transient boolean dirty = true;

    private final PositionSet initialPos = PositionSet.allHome();

    private final SpeedSet initialSpeed = new SpeedSet(PositionSet.allHome());

    public CurveManager() {
        initialPos.addPropertyChangeListener(this);
    }

    /**
     * Internal. Compute one rock curve segment and don't change internal state.
     * 
     * @param i16
     *            which rock
     * @param t0
     *            starttime
     * @param sweepFactor
     * @return the new Curve in world coordinates.
     */
    R1RNFunctionImpl doComputeCurve(final int i16, final double t0,
            final PositionSet p, final SpeedSet s, final double sweepFactor) {
        final Rock x = p.getRock(i16);
        final Rock v = s.getRock(i16);
        final R1RNFunctionImpl wc;
        if (v.distanceSq(0, 0) == 0)
            wc = CurveStill.newInstance(x);
        else
            // Convert the initial angle from WC to RC.
            // TUNE 2x sqrt, 2x atan2 to 1x each?
            wc = new CurveTransformed(curler.computeRc(x.getA()
                    + Math.atan2(v.getX(), v.getY()), MathVec.abs2D(v), v
                    .getA(), sweepFactor), CurveTransformed.createRc2Wc(x, v,
                    new AffineTransform()), t0);
        if (log.isDebugEnabled())
            log.debug(i16 + " " + wc);
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
     * Internal. Compute initial curves and the first hit of each combination of
     * 2 rocks if the dirty flag is set.
     */
    void doInit() {
        if (!dirty)
            return;
        final double t0 = 0.0;
        // initial curves:
        for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--) {
            curveStore.reset(i16);
            curveStore.add(i16, t0, doComputeCurve(i16, t0, initialPos,
                    initialSpeed, NoSweep), _30);
        }
        // initial collission detection:
        collissionStore.clear();
        for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--)
            for (int j16 = i16 - 1; j16 >= 0; j16--)
                // log.info("collissionDetect " + i + ", " + j);
                collissionStore.add(collissionDetector.compute(t0, _30,
                        curveStore.getCurve(i16), curveStore.getCurve(j16)),
                        i16, j16);
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
        // first compute the new curves:
        for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--) {
            if (!RockSet.isSet(hitMask, i16))
                continue;
            curveStore.add(i16, t0, doComputeCurve(i16, t0, currentPos,
                    currentSpeed, NoSweep), _30);
            computedMask |= 1 << i16;
        }
        // then add all combinations of potential collissions
        t0 += hitDt;
        for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--) {
            if (!RockSet.isSet(computedMask, i16))
                continue;
            for (int j16 = RockSet.ROCKS_PER_SET - 1; j16 >= 0; j16--) {
                if (i16 == j16 || i16 > j16 && RockSet.isSet(computedMask, j16))
                    continue;
                collissionStore.replace(collissionDetector.compute(t0, _30,
                        curveStore.getCurve(i16), curveStore.getCurve(j16)),
                        i16, j16);
            }
        }
        return computedMask;
    }

    /**
     * Internal. Does not {@link RockSet#notifyChange()}!
     * 
     * @param currentTime
     * @param tmp
     *            buffer to reduce instanciations. See
     *            {@link R1RNFunction#at(int, double, double[])}.
     */
    void doUpdatePosAndSpeed(final double currentTime, final double[] tmp) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            currentPos.getRock(i).setLocation(
                    curveStore.getCurve(i).at(0, currentTime, tmp));
            currentSpeed.getRock(i).setLocation(
                    curveStore.getCurve(i).at(1, currentTime, tmp));
        }
    }

    @Override
    public boolean equals(final Object obj) {
        return false;
    }

    public Annotations getAnnotations() {
        return annotations;
    }

    public Collider getCollider() {
        return collider;
    }

    public CollissionDetector getCollissionDetector() {
        return collissionDetector;
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

    public CurveStore getCurveStore() {
        return curveStore;
    }

    public PositionSet getInitialPos() {
        return initialPos;
    }

    public SpeedSet getInitialSpeed() {
        return initialSpeed;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * TODO Fire a "refresh" event, so the eventqueue can remove obsolete
     * (outdated) events.
     * 
     * @param arg0
     */
    public void propertyChange(final PropertyChangeEvent arg0) {
        final Object src = arg0.getSource();
        if (src == initialPos || src == initialSpeed) {
            // force recomputation:
            dirty = true;
            try {
                setCurrentTime(getCurrentTime());
            } catch (final NullPointerException e) {
                log.warn("Oops!", e);
            }
            return;
        }
        log.info(arg0);
    }

    protected Object readResolve() throws ObjectStreamException {
        final CurveManager m = new CurveManager();
        m.annotations.putAll(annotations);
        m.setCollider(getCollider());
        m.setCurler(getCurler());
        m.setCollissionDetector(getCollissionDetector());
        m.setCurveStore(new CurveStoreImpl(stopper, RockSet.ROCKS_PER_SET));
        m.setInitialPos(getInitialPos());
        m.setInitialSpeed(getInitialSpeed());
        // m.setCurrentTime(this.getCurrentTime());
        return m;
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

    public void setCurler(final Curler curler) {
        dirty = true;
        propChange.firePropertyChange("curler", this.curler, curler);
        this.curler = curler;
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
        // propChange
        // .firePropertyChange("initialPos", this.initialPos, initialPos);
        this.initialPos.setLocation(initialPos);
    }

    public void setInitialSpeed(final SpeedSet initialSpeed) {
        dirty = true;
        // propChange.firePropertyChange("initialSpeed", this.initialSpeed,
        // initialSpeed);
        // this.initialSpeed = initialSpeed;
        this.initialSpeed.setLocation(initialSpeed);
    }
}