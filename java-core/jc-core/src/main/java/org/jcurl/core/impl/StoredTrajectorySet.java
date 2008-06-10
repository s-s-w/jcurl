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

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

import org.jcurl.core.api.CurveStore;
import org.jcurl.core.api.MutableObject;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.TrajectorySet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.math.R1RNFunction;

/**
 * Trajectory wrapping a {@link CurveStore}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:StoredTrajectorySet.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class StoredTrajectorySet extends MutableObject implements TrajectorySet {

	private static final long serialVersionUID = -829911104054850124L;
	private final Map<CharSequence, CharSequence> annotations = new HashMap<CharSequence, CharSequence>();
	private transient final RockSet<Pos> currentPos = RockSetUtils.allHome();
	private transient final RockSet<Vel> currentSpeed = RockSetUtils
			.zeroSpeed();
	private transient double currentTime = 0;

	private final CurveStore store;

	public StoredTrajectorySet(final CurveStore c) {
		store = c;
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
			final R1RNFunction c = store.getCurve(i);
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

	public RockSet<Pos> getCurrentPos() {
		return currentPos;
	}

	public RockSet<Vel> getCurrentSpeed() {
		return currentSpeed;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	protected Object readResolve() throws ObjectStreamException {
		return new StoredTrajectorySet(store);
	}

	public void setCurrentTime(final double currentTime) {
		final double old = this.currentTime;
		if (old == currentTime)
			return;
		this.currentTime = currentTime;
		doUpdatePosAndSpeed(currentTime, currentPos, currentSpeed);
		propChange.firePropertyChange("currentTime", old, this.currentTime);
	}
}
