/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.mr.gui;

import org.jcurl.core.api.Collider;
import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.Curler;
import org.jcurl.core.api.Measure;
import org.jcurl.core.api.MutableObject;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.impl.CollissionSimple;
import org.jcurl.core.impl.CurlerNoCurl;
import org.jcurl.core.impl.CurveManager;

/**
 * Higher level Data Model - more abstract than {@link ComputedTrajectorySet}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class Model extends MutableObject {

	// private static final Log log = JCLoggerFactory.getLogger(Model.class);

	private static final long serialVersionUID = -8598083673757204804L;

	private int activeRock;

	private Measure broomX = null;

	private Measure interval;

	private ComputedTrajectorySet trajectory;

	public Model() {
		trajectory = new CurveManager();
		trajectory.setCollider(new CollissionSimple());
		trajectory.setCurler(new CurlerNoCurl(23, 0));
		// setBroomX(new DimVal(0, Dim.METER));
		// setInterval(new DimVal(2.5, Dim.SECOND));
		// setDrawCurl(new DimVal(3, Dim.FOOT));
		// setDrawTime(new DimVal(25, Dim.SECOND));
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		final Model other = (Model) obj;
		if (activeRock != other.activeRock)
			return false;
		if (broomX == null) {
			if (other.broomX != null)
				return false;
		} else if (!broomX.equals(other.broomX))
			return false;
		if (interval == null) {
			if (other.interval != null)
				return false;
		} else if (!interval.equals(other.interval))
			return false;
		if (trajectory == null) {
			if (other.trajectory != null)
				return false;
		} else if (!trajectory.equals(other.trajectory))
			return false;
		return true;
	}

	public int getActiveRock() {
		return activeRock;
	}

	public Measure getBroomX() {
		return broomX;
	}

	public Collider getCollider() {
		return getTrajectory().getCollider();
	}

	public Curler getCurler() {
		return getTrajectory().getCurler();
	}

	public Measure getDrawCurl() {
		return new Measure(getTrajectory().getCurler().getDrawToTeeCurl(),
				Unit.METER);
	}

	public Measure getDrawTime() {
		return new Measure(getTrajectory().getCurler().getDrawToTeeTime(),
				Unit.SECOND);
	}

	public RockSet<Pos> getInitialPos() {
		return getTrajectory().getInitialPos();
	}

	public Measure getInterval() {
		return interval;
	}

	public ComputedTrajectorySet getTrajectory() {
		return trajectory;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + activeRock;
		result = PRIME * result + (broomX == null ? 0 : broomX.hashCode());
		result = PRIME * result + (interval == null ? 0 : interval.hashCode());
		result = PRIME * result
				+ (trajectory == null ? 0 : trajectory.hashCode());
		return result;
	}

	public void setActiveRock(final int activeRock) {
		propChange
				.firePropertyChange("activeRock", getActiveRock(), activeRock);
		this.activeRock = activeRock & 0xF;
	}

	public void setBroomX(final Measure broomX) {
		if (broomX == null)
			return;
		if (this.broomX == broomX)
			return;
		propChange.firePropertyChange("broomX", getBroomX(), broomX);
		this.broomX = broomX;
	}

	public void setCollider(final Collider collider) {
		if (collider == null)
			return;
		propChange.firePropertyChange("collider", getCollider(), collider);
		getTrajectory().setCollider(collider);
	}

	public void setCurler(final Curler curler) {
		if (curler == null)
			return;
		propChange.firePropertyChange("curler", getCurler(), curler);
		getTrajectory().setCurler(curler);
	}

	public void setDrawCurl(final Measure drawCurl) {
		propChange.firePropertyChange("drawCurl", getDrawCurl(), drawCurl);
		getTrajectory().getCurler().setDrawToTeeCurl(
				drawCurl.to(Unit.METER).value);
	}

	public void setDrawTime(final Measure drawTime) {
		propChange.firePropertyChange("drawTime", getDrawTime(), drawTime);
		getTrajectory().getCurler().setDrawToTeeTime(
				drawTime.to(Unit.SECOND).value);
	}

	public void setInitialPos(final RockSet<Pos> initialPos) {
		if (initialPos == null)
			return;
		propChange
				.firePropertyChange("initialPos", getInitialPos(), initialPos);
		getTrajectory().setInitialPos(initialPos);
	}

	public void setInterval(final Measure interval) {
		propChange.firePropertyChange("interval", getInterval(), interval);
		this.interval = interval.to(Unit.SECOND);
	}

	public void setTrajectory(final ComputedTrajectorySet trajectory) {
		if (trajectory == null)
			return;
		propChange
				.firePropertyChange("trajectory", getTrajectory(), trajectory);
		this.trajectory = trajectory;
	}
}