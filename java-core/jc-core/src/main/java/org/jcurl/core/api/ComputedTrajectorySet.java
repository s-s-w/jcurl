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
package org.jcurl.core.api;

import javax.swing.event.ChangeEvent;

import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.math.R1RNFunction;

/**
 * {@link R1RNFunction} based set of {@link RockSet#ROCKS_PER_SET} trajectories.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:ComputedTrajectorySet.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public interface ComputedTrajectorySet extends TrajectorySet {

	Collider getCollider();

	CollissionDetector getCollissionDetector();

	Curler getCurler();

	CurveStore getCurveStore();

	RockSet<Pos> getInitialPos();

	RockSet<Vel> getInitialVel();

	/** process collected recompute-requests and listen again. */
	void resume();

	void setCollider(final Collider collider);

	void setCollissionDetector(final CollissionDetector collissionDetector);

	void setCurler(final Curler curler);

	void setCurveStore(final CurveStore curveStore);

	/**
	 * Currently the internal reference MUST be final, so this updates via
	 * {@link RockSet#setLocation(RockSet)}. This is the case to simplify event
	 * controllers.
	 * 
	 * @param initialPos
	 */
	void setInitialPos(final RockSet<Pos> initialPos);

	/**
	 * Currently the internal reference MUST be final, so this updates via
	 * {@link RockSet#setLocation(RockSet)}. This is the case to simplify event
	 * controllers.
	 * 
	 * @param initialVel
	 */
	void setInitialVel(final RockSet<Vel> initialVel);

	/**
	 * Don't immediately recompute on following {@link ChangeEvent}s, but
	 * collect triggered recomputations until {@link #resume()}.
	 * <p>
	 * This should be the initial state after object creation (ctor)!
	 * </p>
	 */
	void suspend();
}