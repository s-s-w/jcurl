/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.impl;

import java.awt.geom.AffineTransform;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.Collider;
import org.jcurl.core.api.CollissionDetector;
import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.Curler;
import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.CurveStore;
import org.jcurl.core.api.MutableObject;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.StopDetector;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.impl.CollissionStore.Tupel;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.MathVec;
import org.jcurl.math.R1RNFunction;

/**
 * Bring it all together and trigger computation.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CurveManager.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class CurveManager extends MutableObject implements ChangeListener,
		ComputedTrajectorySet, Serializable {

	// compute beyond a realistic amount of time
	private static final double _30 = 60.0;
	/** Time leap during a hit. */
	private static final double hitDt = 1e-6;
	private static final Log log = JCLoggerFactory
			.getLogger(CurveManager.class);
	private static final double NoSweep = 0;
	private static final long serialVersionUID = 7198540442889130378L;
	private static final StopDetector stopper = new NewtonStopDetector();
	private final Map<CharSequence, CharSequence> annotations = new HashMap<CharSequence, CharSequence>();
	private Collider collider = null;
	private CollissionDetector collissionDetector = null;
	private transient final CollissionStore collissionStore = new CollissionStore();
	private Curler curler = null;
	private transient final RockSet<Pos> currentPos = RockSetUtils.allHome();
	private transient final RockSet<Vel> currentSpeed = RockSet.allZero(null);
	private transient double currentTime = 0;
	private transient CurveStore curveStore = new CurveStoreImpl(stopper,
			RockSet.ROCKS_PER_SET);
	private final RockSet<Pos> initialPos = RockSetUtils.allHome();
	private final RockSet<Vel> initialSpeed = RockSet.allZero(null);
	private final transient RockSet<Pos> tmpPos = RockSetUtils.allHome();
	private final transient RockSet<Vel> tmpSpeed = RockSet.allZero(null);

	public CurveManager() {
		initialPos.addRockListener(this);
		initialSpeed.addRockListener(this);
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
	CurveRock<Pos> doComputeCurve(final int i16, final double t0,
			final RockSet<Pos> p, final RockSet<Vel> s, final double sweepFactor) {
		final Rock<Pos> x = p.getRock(i16);
		final Rock<Vel> v = s.getRock(i16);
		final CurveRock<Pos> wc;
		if (v.p().distanceSq(0, 0) == 0)
			wc = CurveStill.newInstance(x);
		else
			// Convert the initial angle from WC to RC.
			// TUNE 2x sqrt, 2x atan2 to 1x each?
			wc = new CurveTransformed<Pos>(curler.computeRc(x.getA()
					+ Math.atan2(v.getX(), v.getY()), MathVec.abs2D(v.p()), v
					.getA(), sweepFactor), CurveTransformed.createRc2Wc(x.p(),
					v.p(), null), t0);
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
		return collissionStore.first();
	}

	/**
	 * Internal. Compute initial curves and the first hit of each combination of
	 * 2 rocks if the dirty flag is set.
	 */
	void doInit() {
		final double t0 = 0.0;
		// TUNE Parallel
		// initial curves:
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--) {
			curveStore.reset(i16);
			curveStore.add(i16, t0, doComputeCurve(i16, t0, initialPos,
					initialSpeed, NoSweep), _30);
		}
		// initial collission detection:
		collissionStore.clear();
		// TUNE Parallel
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--)
			for (int j16 = i16 - 1; j16 >= 0; j16--)
				// log.info("collissionDetect " + i + ", " + j);
				collissionStore.add(collissionDetector.compute(t0, _30,
						curveStore.getCurve(i16), curveStore.getCurve(j16)),
						i16, j16);
	}

	/**
	 * Internal. Typically after a hit: Recompute the new curves and upcoming
	 * collission candidates.
	 * 
	 * @param hitMask
	 * @return bitmask of rocks with new curves
	 */
	int doRecomputeCurvesAndCollissionTimes(final int hitMask, double t0,
			final RockSet<Pos> cp, final RockSet<Vel> cv) {
		int computedMask = 0;
		// first compute the new curves:
		// TUNE Parallel
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--) {
			if (!RockSet.isSet(hitMask, i16))
				continue;
			curveStore.add(i16, t0, doComputeCurve(i16, t0, cp, cv, NoSweep),
					_30);
			computedMask |= 1 << i16;
		}
		// then add all combinations of potential collissions
		t0 += hitDt;
		// TUNE Parallel
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
	 * Internal. Does not {@link RockSet#fireStateChanged()}!
	 * 
	 * @param currentTime
	 */
	void doUpdatePosAndSpeed(final double currentTime, final RockSet<Pos> cp,
			final RockSet<Vel> cv) {
		// TUNE Parallel
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
			final R1RNFunction c = curveStore.getCurve(i);
			double x = c.at(currentTime, 0, 0);
			double y = c.at(currentTime, 0, 1);
			double a = c.at(currentTime, 0, 2);
			cp.getRock(i).setLocation(x, y, a);
			x = c.at(currentTime, 1, 0);
			y = c.at(currentTime, 1, 1);
			a = c.at(currentTime, 1, 2);
			cv.getRock(i).setLocation(x, y, a);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		return false;
	}

	public Map<CharSequence, CharSequence> getAnnotations() {
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

	public RockSet<Pos> getCurrentPos() {
		return currentPos;
	}

	public RockSet<Vel> getCurrentSpeed() {
		return currentSpeed;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public CurveStore getCurveStore() {
		return curveStore;
	}

	public RockSet<Pos> getInitialPos() {
		return initialPos;
	}

	public RockSet<Vel> getInitialSpeed() {
		return initialSpeed;
	}

	@Override
	public int hashCode() {
		return 0;
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

	public void recompute() {
		recompute(currentTime, true);
	}

	private void recompute(final double currentTime, final boolean complete) {
		if (complete) {
			doInit();
			final AffineTransform m = new AffineTransform();
			// NaN-safe time range check (are we navigating known ground?):
			while (currentTime > doGetNextHit().t) {
				final Tupel nh = doGetNextHit();
				if (log.isDebugEnabled())
					log.debug(nh.a + " - " + nh.b + " : " + nh.t);
				doUpdatePosAndSpeed(nh.t, tmpPos, tmpSpeed);
				// compute collission(s);
				final int mask = collider.compute(tmpPos, tmpSpeed, m);
				if (mask == 0)
					break;
				doRecomputeCurvesAndCollissionTimes(mask, nh.t, tmpPos,
						tmpSpeed);
			}
		}
		doUpdatePosAndSpeed(currentTime, currentPos, currentSpeed);
	}

	public void setCollider(final Collider collider) {
		final Collider old = this.collider;
		if (old == collider)
			return;
		propChange
				.firePropertyChange("collider", old, this.collider = collider);
	}

	public void setCollissionDetector(CollissionDetector collissionDetector) {
		// FIXME currently use ONLY Bisection.
		collissionDetector = new BisectionCollissionDetector();
		propChange.firePropertyChange("collissionDetector",
				this.collissionDetector, collissionDetector);
		this.collissionDetector = collissionDetector;
	}

	public void setCurler(final Curler curler) {
		final Curler old = this.curler;
		if (old == curler)
			return;
		propChange.firePropertyChange("curler", old, this.curler = curler);
	}

	public void setCurrentTime(final double currentTime) {
		final double old = this.currentTime;
		if (old == currentTime)
			return;
		this.currentTime = currentTime;
		if (this.currentTime > old)
			recompute(currentTime, true);
		propChange.firePropertyChange("currentTime", old, this.currentTime);
	}

	public void setCurveStore(final CurveStore curveStore) {
		this.curveStore = curveStore;
		recompute(currentTime, true);
		propChange
				.firePropertyChange("curveStore", this.curveStore, curveStore);
	}

	public void setInitialPos(final RockSet<Pos> initialPos) {
		this.initialPos.setLocation(initialPos);
	}

	public void setInitialSpeed(final RockSet<Vel> initialSpeed) {
		this.initialSpeed.setLocation(initialSpeed);
	}

	public void stateChanged(final ChangeEvent arg0) {
		final Object src = arg0 == null ? null : arg0.getSource();
		if (src == null || src == initialPos || src == initialSpeed)
			;// recompute(currentTime, true);
		else if (initialPos.findI16(src) >= 0) {
			log.debug("Startpos rock change");
			recompute(currentTime, true);
		} else if (initialSpeed.findI16(src) >= 0) {
			log.debug("Startvel rock change");
			recompute(currentTime, true);
		} else
			log.info(arg0);
	}
}