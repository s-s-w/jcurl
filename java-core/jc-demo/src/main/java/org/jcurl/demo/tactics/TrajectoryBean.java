/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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

package org.jcurl.demo.tactics;

import java.awt.Cursor;
import java.awt.geom.RectangularShape;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.ChangeManager;
import org.jcurl.core.ui.TrajectoryBroomPromptWrapper;

/**
 * Common code of the Piccolo and Scenario implementation.
 * 
 * @param <T>
 *            tranformable node type (rocks)
 * @param <G>
 *            grouping node type (trajectories)
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class TrajectoryBean<T, G> extends JComponent implements
		HasChanger, Zoomable, ChangeListener {
	protected static final String ATTR_IDX16 = "idx16";
	protected static final String ATTR_ROCK = "rock";
	protected static final String ATTR_ROCKSET = "rockset";
	protected static final String ATTR_TRIGGER_CURVE_UPDATE = "trigger_curve_update";
	protected static final Cursor CURSOR = new Cursor(Cursor.HAND_CURSOR);
	private ChangeManager changer;
	protected ComputedTrajectorySet curves = null;
	protected transient RectangularShape tmpViewPort = null;
	protected final TrajectoryBroomPromptWrapper tt = new TrajectoryBroomPromptWrapper();

	protected void addCL(final RockSet<Pos> s) {
		if (s == null)
			return;
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--)
			s.getRock(i16).addChangeListener(this);
	}

	/** keep the recent viewport visible */
	@Override
	public void doLayout() {
		super.doLayout();
		if (tmpViewPort != null)
			setZoom(tmpViewPort, 0);
	}

	public abstract BroomPromptModel getBroom();

	public ChangeManager getChanger() {
		return ChangeManager.getTrivial(changer);
	}

	public ComputedTrajectorySet getCurves() {
		return curves;
	}

	public RectangularShape getZoom() {
		if (tmpViewPort == null)
			return null;// pico.getCamera().getViewBounds();
		return tmpViewPort;
	}

	protected void removeCL(final RockSet<Pos> s) {
		if (s == null)
			return;
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--)
			s.getRock(i16).removeChangeListener(this);
	}

	public void setChanger(final ChangeManager changer) {
		final ChangeManager old = this.changer;
		if (old == changer)
			return;
		firePropertyChange("changer", old, this.changer = changer);
	}

	public abstract void setCurves(ComputedTrajectorySet model);

	public void setZoom(final RectangularShape viewport) {
		setZoom(viewport, -1);
	}

}