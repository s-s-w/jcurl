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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.RectangularShape;

import javax.swing.JComponent;

import org.jcurl.core.api.ComputedTrajectorySet;

/**
 * Tactics Panel Bean. Aggregates a graphics display plus swing-widget based
 * stuff.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class TacticsBean extends JComponent implements Zoomable {
	private static final long serialVersionUID = 887263531452742422L;
	private final TrajectoryPiccolo tp;
	private RectangularShape zoom = null;

	public TacticsBean() {		
		setLayout(new BorderLayout());
		this.add(tp = new TrajectoryPiccolo(), BorderLayout.CENTER);
		tp.setPreferredSize(new Dimension(400, 600));
	}

	public RectangularShape getZoom() {
		return zoom;
	}

	public boolean isModified() {
		return false;
	}

	public void setZoom(final RectangularShape viewport) {
		this.setZoom(viewport, 333);
	}

	public void setZoom(final RectangularShape zoom, final int transitionMillis) {
		final RectangularShape old = this.zoom;
		this.firePropertyChange("zoom", old, this.zoom = zoom);
		tp.setZoom(zoom);
	}

	public ComputedTrajectorySet getCurves() {
		return tp.getCurves();
	}

	public void setCurves(ComputedTrajectorySet curves) {
		tp.setCurves(curves);
	}
}
