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

package org.jcurl.core.ui;

import java.awt.geom.Point2D;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.Curler;
import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.CurveStore;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.MathVec;

/**
 * Push changes down from this {@link BroomPromptModel} to a
 * {@link ComputedTrajectorySet}.
 * <p>
 * Does <b>not sync back</b> events from the {@link ComputedTrajectorySet}!
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class TrajectoryBroomPromptWrapper extends DefaultBroomPromptModel
		implements ChangeListener {
	/**
	 * Mediates {@link ComputedTrajectorySet} and {@link BroomPromptModel} under
	 * a convenient, stateless, high-level interface.
	 * 
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	private static class MediatorWorker {
		private static final Log log = JCLoggerFactory
				.getLogger(MediatorWorker.class);

		private static void release(final Rock<Pos> r) {
			r.setLocation(0, IceSize.FAR_HACK_2_TEE, Math.PI);
		}

		private int findRunning(final RockSet<Vel> ivel, final int idx16,
				final boolean forceCheck) {
			if (ivel == null)
				return forceCheck ? -1 : idx16;
			if (forceCheck || idx16 < 0) {
				// Try to find the running rock.
				// 1st try: if there's only one rock running, that's it.
				int tmp = -1;
				for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
					if (ivel.getRock(i).isNotZero()) {
						if (!forceCheck)
							return i;
						if (tmp >= 0)
							throw new RuntimeException(
									"Multiple running rocks: " + tmp + i);
						tmp = i;
					}
				return tmp;
			} else
				return idx16;
		}

		private void startPos(final RockSet<Pos> pos, final int idx16) {
			if (pos == null || idx16 < 0)
				return;
			release(pos.getRock(idx16));
		}

		private <R extends RockType> void swap(final RockSet<R> r, final int a,
				final int b) {
			if (r == null || a == b || a < 0 || b < 0)
				return;
			final Rock<R> tmp = new RockDouble<R>(r.getRock(a));
			r.getRock(a).setLocation(r.getRock(b));
			r.getRock(b).setLocation(tmp);
		}

		/**
		 * Does <b>NOT</b> fire change events of the two {@link RockSet}s.
		 * 
		 * @param newPos
		 * @param splitTimeMillis
		 * @param idx16
		 * @param outTurn
		 * @param curl
		 * @param dstPos
		 * @param dstVel
		 */
		@SuppressWarnings("unchecked")
		private void syncBpm2Cts(final Point2D newPos,
				final long splitTimeMillis, final int idx16,
				final boolean outTurn, final Curler curl,
				final RockSet<Pos> dstPos, final RockSet<Vel> dstVel) {
			if (log.isDebugEnabled())
				log.debug("i=" + idx16);
			// set to initial pos
			final Rock start = new RockDouble(0, IceSize.FAR_HACK_2_TEE,
					Math.PI);
			dstPos.getRock(idx16).setLocation(start);

			// Compute direction
			MathVec.sub(newPos, start.p(), start.p());
			MathVec.norm(start.p(), start.p());

			// Compute initial Speed
			final double v0 = curl.computeHackSpeed(splitTimeMillis * 1e-3,
					newPos);
			MathVec.mult(v0, start.p(), start.p());

			// Handle
			start.setA((outTurn ? 1 : -1) * 0.25);
			dstVel.getRock(idx16).setLocation(start);
		}

		private void syncCts2Bpm(final int idx16, final RockSet<Pos> ipos,
				final RockSet<Vel> ivel, final Curler curl,
				final CurveCombined<CurveRock<Pos>> rc,
				final BroomPromptModel dst) {
			release(ipos.getRock(idx16));
			final Rock<Vel> v = ivel.getRock(idx16);

			// Compute Handle
			dst.setOutTurn(v.getA() >= 0);

			// Compute Broom Location
			final Point2D x = ipos.getRock(idx16).p();
			final double bY = dst.getBroom() == null ? 0 : dst.getBroom()
					.getY();
			final Point2D b2 = new Point2D.Double((bY - x.getY()) * v.getX()
					/ v.getY(), bY);
			if (log.isDebugEnabled())
				log.debug("v=" + v + ", x=" + b2);
			dst.setBroom(b2);

			// Compute Split Time
			dst.getSplitTimeMillis().setValue(
					(int) (1000 * curl.computeIntervalTime(rc.first())));
			if (dst.getIdx16() < 0)
				dst.setIdx16(idx16);
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(TrajectoryBroomPromptWrapper.class);

	private Curler curler;
	private CurveStore curveStore;
	private final MediatorWorker med = new MediatorWorker();
	private RockSet<Pos> positions;
	private RockSet<Vel> velocities;

	public void init(final ComputedTrajectorySet cts) {
		if (cts == null)
			init(null, null, null, null);
		else
			init(cts.getInitialPos(), cts.getInitialSpeed(), cts.getCurler(),
					cts.getCurveStore());
	}

	private void init(final RockSet<Pos> positions,
			final RockSet<Vel> velocities, final Curler curler,
			final CurveStore curveStore) {
		this.curveStore = curveStore;
		this.curler = curler;

		final int idx16;
		if (this.velocities != velocities)
			idx16 = med.findRunning(velocities, getIdx16(), true);
		else
			idx16 = getIdx16();
		// put rock idx16 into start position
		if (this.positions != positions)
			med.swap(positions, getIdx16(), idx16);
		med.startPos(positions, idx16);

		this.positions = positions;
		this.velocities = velocities;

		super.setIdx16(idx16);
		if (this.curveStore == null || this.curler == null
				|| this.positions == null || this.velocities == null) {
			setBroom(null);
			return;
		}
		updateBroom(true);
	}

	@Override
	public void setBroom(final Point2D broom) {
		// do the update no matter if the reference changed at all.
		super.setBroom(broom);
		updateVelocities();
	}

	@Override
	public void setIdx16(final int idx16) {
		log.debug(idx16);
		final int old = getIdx16();
		if (old == idx16)
			return;
		super.setIdx16(idx16);
		med.swap(positions, old, getIdx16());
		med.swap(velocities, old, getIdx16());
		updateVelocities();
	}

	@Override
	public void setOutTurn(final boolean outTurn) {
		if (getOutTurn() == outTurn)
			return;
		super.setOutTurn(outTurn);
		updateVelocities();
	}

	@Override
	public void setSplitTimeMillis(final BoundedRangeModel splitTimeMillis) {
		if (getSplitTimeMillis() == splitTimeMillis)
			return;
		if (getSplitTimeMillis() != null)
			getSplitTimeMillis().removeChangeListener(this);
		super.setSplitTimeMillis(splitTimeMillis);
		if (getSplitTimeMillis() != null)
			getSplitTimeMillis().addChangeListener(this);
		updateVelocities();
	}

	@Override
	public void setValueIsAdjusting(final boolean valueIsAdjusting) {
		super.setValueIsAdjusting(valueIsAdjusting);
	}

	public void stateChanged(final ChangeEvent evt) {
		if (evt.getSource() == getSplitTimeMillis())
			updateVelocities();
		else
			log.warn("Unprocessed event " + evt);
	}

	@SuppressWarnings("unchecked")
	private void updateBroom(final boolean isInit) {
		// syncCts2Bpm calls back setOutTurn which leads to a buggy cyclic
		// computation. To prevent this we use a (dirty) trick:
		final RockSet<Vel> ivel = velocities;
		try {
			if (isInit)
				velocities = null;
			med.syncCts2Bpm(getIdx16(), positions, ivel, curler,
					(CurveCombined<CurveRock<Pos>>) curveStore
							.getCurve(getIdx16()), this);
		} finally {
			velocities = ivel;
		}
	}

	/**
	 * Call
	 * {@link MediatorWorker#syncBpm2Cts(Point2D, long, int, boolean, Curler, RockSet, RockSet)}
	 * and {@link RockSet#fireStateChanged()}. Maybe the latter should be
	 * replaced by {@link Rock#fireStateChanged} (part of
	 * {@link Rock#setLocation(double, double, double)}.
	 */
	private void updateVelocities() {
		if (positions == null || velocities == null || curler == null
				|| getBroom() == null || getSplitTimeMillis() == null)
			return;
		med.syncBpm2Cts(getBroom(), getSplitTimeMillis().getValue(),
				getIdx16(), getOutTurn(), curler, positions, velocities);
		//velocities.fireStateChanged();
	}
}
