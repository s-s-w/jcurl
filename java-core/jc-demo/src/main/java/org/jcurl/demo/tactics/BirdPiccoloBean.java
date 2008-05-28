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

package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JComponent;

import org.jcurl.demo.tactics.JCurlShotPlanner.ZoomHelper;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BirdPiccoloBean extends JComponent {

	private static final long serialVersionUID = -408035623675258196L;
	private TrajectoryPiccoloBean master;

	private PCanvas pc;

	public BirdPiccoloBean() {
		setLayout(new BorderLayout());
		add(pc = new PCanvas(), BorderLayout.CENTER);
		pc.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		pc.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		// setEnabled(false);
	}

	public TrajectoryPiccoloBean getMaster() {
		return master;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		pc.getCamera().animateViewToCenterBounds(ZoomHelper.HousePlus, true, 1);
		super.paintComponent(g);
	}

	public void setMaster(final TrajectoryBean master) {
		if (master instanceof TrajectoryPiccoloBean) {
			this.master = (TrajectoryPiccoloBean) master;
			pc.getCamera().removeAllChildren();
			pc.getCamera().addLayer(this.master.getIceLayer());
		}
	}
}