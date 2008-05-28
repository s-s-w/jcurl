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

import java.awt.geom.RectangularShape;

import javax.swing.JComponent;

import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.ChangeManager;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class TrajectoryBean extends JComponent implements Zoomable {

	private ChangeManager changer;

	public abstract BroomPromptModel getBroom();

	public ChangeManager getChanger() {
		return ChangeManager.getTrivial(changer);
	}

	public abstract ComputedTrajectorySet getCurves();

	public void setChanger(final ChangeManager changer) {
		final ChangeManager old = this.changer;
		if (old == changer)
			return;
		firePropertyChange("changer", old, this.changer);
	}

	public abstract void setCurves(ComputedTrajectorySet model);

	public void setZoom(final RectangularShape viewport) {
		setZoom(viewport, -1);
	}
}