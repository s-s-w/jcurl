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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.NewtonSolver;
import org.apache.commons.math.analysis.UnivariateRealSolver;
import org.jcurl.core.dto.PositionSet;
import org.jcurl.core.dto.Rock;
import org.jcurl.core.dto.RockSet;
import org.jcurl.core.dto.SpeedSet;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.math.analysis.DistanceSq;
import org.jcurl.math.analysis.R1RnCurve;

/**
 * Manages the interaction of {@link org.jcurl.model.CurveFactory} and
 * {@link org.jcurl.model.CollissionModel} and provides high-level access to
 * computed rock locations and velocities.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ComputedPaths extends RockSetPaths implements
        PropertyChangeListener {
    private static final Log log = JCLoggerFactory
            .getLogger(ComputedPaths.class);

    private static final double Never = Double.POSITIVE_INFINITY;

    private static final double NoSweep = 0.0;

    private static final long serialVersionUID = -4785102667266867444L;

    private static final double Unknown = Double.NaN;

    private CollissionModel collider;

    private PositionSet currentPos = PositionSet.allHome();

    private SpeedSet currentSpeed = new SpeedSet();

    private double currentT = 0;

    private final PathSet[] curves = new PathSet[RockSet.ROCKS_PER_SET];

    private CurveFactory ice;

    private PositionSet initialPos = PositionSet.allHome();

    private SpeedSet initialSpeed = new SpeedSet();

    private double known;

    private double maxT;

    private final HitTimeMatrix pm = new HitTimeMatrix();

    /**
     * Defaults to {@link DennyCurves} and {@link CollissionSpinLoss} and calls
     * {@link #clearCurves()}.
     */
    public ComputedPaths() {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            curves[i] = new PathSet();
        this.ice = new DennyCurves();
        this.collider = new CollissionSpinLoss();
        this.ice.addPropertyChangeListener(this);
        this.collider.addPropertyChangeListener(this);
        this.initialPos.addPropertyChangeListener(this);
        this.initialSpeed.addPropertyChangeListener(this);
        clearCurves();
    }

    void clearCurves() {
        known = -1;
    }

    /**
     * Ensure the curvestore has no unprocessed hits until t. Does NOT compute
     * {@link #currentPos} and {@link #currentSpeed}. Calls
     * {@link #computeCurve(int, double, Rock, Rock, double)} for every "dirty"
     * rock.
     * 
     * @throws FunctionEvaluationException
     */
    protected double computeUntil(double t) throws FunctionEvaluationException {
        if (t < 0)
            t = 0;
        if (known < 0) {
            // initial setup!
            known = 0;
            // pm.reset(Unknown);
            for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
                curves[i].clear();
                computeCurve(i, known, getInitialPos().getRock(i),
                        getInitialSpeed().getRock(i), NoSweep);
            }
        }
        while (t > known) {
            double hitTime = Never;
            int hitPair = 0;
            for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
                for (int j = i - 1; j >= 0; j--) {
                    // NaN proof comparison:
                    if (Double.isNaN(pm.get(i, j)) || pm.get(i, j) == Unknown)
                        pm.set(i, j, firstHit(curves[i].getCurve(t), curves[j]
                                .getCurve(t), known, t));
                    // find the soonest hit time+pair (could move to
                    // HitTimeMatrix):
                    final double tmp = pm.get(i, j);
                    if (tmp < hitTime) {
                        hitTime = tmp;
                        hitPair = (1 << i) | (1 << j);
                    }
                }
            }
            if (hitTime == Never || hitPair == 0) {
                maxT = Never;
                break;
            }
            maxT = known = hitTime;
            hitPair = collider.compute(getCurrentPos(), getCurrentSpeed());
            // mark all hit positions' combinations dirty and compute the new
            // curves
            for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
                if ((hitPair & (1 << i)) == 0)
                    continue;
                computeCurve(i, known, getCurrentPos().getRock(i),
                        getCurrentSpeed().getRock(i), NoSweep);
            }
        }
        return t;
    }

    static double firstHit(final R1RnCurve a, final R1RnCurve b, double tmin,
            double tmax) throws FunctionEvaluationException {
        final DistanceSq dist = new DistanceSq(a, b);
        final UnivariateRealSolver solver = new NewtonSolver(dist);
        try {
            return solver.solve(tmin, tmax, tmin + 1e-2);
        } catch (ConvergenceException e) {
            return Never;
        }
    }

    private void computeCurve(int i, double t0, Rock x, Rock v, double sweep) {
        pm.dirty(i, Unknown);
        curves[i].append(t0, ice.compute(t0, x, v, sweep));
    }

    public boolean equals(Object obj) {
        throw new NotImplementedYetException();
    }

    public CollissionModel getCollider() {
        return collider;
    }

    public PositionSet getCurrentPos() {
        return currentPos;
    }

    public SpeedSet getCurrentSpeed() {
        return currentSpeed;
    }

    public double getCurrentT() {
        return currentT;
    }

    public CurveFactory getIce() {
        return ice;
    }

    public PositionSet getInitialPos() {
        return initialPos;
    }

    public SpeedSet getInitialSpeed() {
        return initialSpeed;
    }

    public double getKnown() {
        return known;
    }

    public double getMaxT() {
        return maxT;
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        log.info(arg0);
        try {
            recompute();
        } catch (FunctionEvaluationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @throws FunctionEvaluationException
     */
    public void recompute() throws FunctionEvaluationException {
        final double tmp = currentT;
        clearCurves();
        setCurrentT(tmp);
    }

    public void setCollider(CollissionModel collider)
            throws FunctionEvaluationException {
        final CollissionModel old = this.collider;
        this.collider.removePropertyChangeListener(this);
        this.collider = collider;
        this.collider.addPropertyChangeListener(this);
        propChange.firePropertyChange("collider", old, this.collider);
        recompute();
    }

    public void setCurrentPos(PositionSet currentPos) {
        this.currentPos = currentPos;
    }

    public void setCurrentSpeed(SpeedSet currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void setCurrentT(double currentT) throws FunctionEvaluationException {
        final double old = this.currentT;
        this.currentT = computeUntil(currentT);
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            curves[i].value(this.currentT, getCurrentPos().getRock(i));
            if (log.isDebugEnabled()) {
                log.debug("c(i=" + i + ",t=" + currentT + ")="
                        + getCurrentPos().getRock(i));
                final PathSegment p = curves[i].getCurve(currentT);
                log.debug("    c_x(t)=" + p.component(0));
                log.debug("    c_y(t)=" + p.component(1));
                log.debug("    c_a(t)=" + p.component(2));
            }
            // TODO compute rock speeds
        }

        propChange.firePropertyChange("currentT", old, this.currentT);
        getCurrentPos().notifyChange();
        getCurrentSpeed().notifyChange();
    }

    public void setIce(CurveFactory slide) throws FunctionEvaluationException {
        final CurveFactory old = this.ice;
        this.ice.removePropertyChangeListener(this);
        this.ice = slide;
        this.ice.addPropertyChangeListener(this);
        propChange.firePropertyChange("ice", old, this.ice);
        recompute();
    }

    public void setInitialPos(PositionSet initialPos)
            throws FunctionEvaluationException {
        this.initialPos.removePropertyChangeListener(this);
        this.initialPos = initialPos;
        this.initialPos.addPropertyChangeListener(this);
        recompute();
    }

    public void setInitialSpeed(SpeedSet initialSpeed)
            throws FunctionEvaluationException {
        this.initialSpeed.removePropertyChangeListener(this);
        this.initialSpeed = initialSpeed;
        this.initialSpeed.addPropertyChangeListener(this);
        recompute();
    }
}
