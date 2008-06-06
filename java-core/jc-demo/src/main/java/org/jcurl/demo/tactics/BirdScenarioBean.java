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
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;

import org.jcurl.demo.tactics.JCurlShotPlanner.ZoomHelper;
import org.jcurl.demo.tactics.sg.AnimateAffine;

import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGTransform;
import com.sun.scenario.scenegraph.SGTransform.Affine;

/**
 * Bird's eye view to {@link TrajectoryPiccoloBean}.
 * <p>
 * Uses {@link TrajectoryPiccoloBean#getIceLayer()}.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BirdScenarioBean extends JComponent {

	private static final long serialVersionUID = -408035623675258196L;
	private TrajectoryScenarioBean master;
	private final JSGPanel pc;
	private final SGGroup scene = new SGGroup();
	private final Affine zoom = SGTransform.createAffine(new AffineTransform(),
			scene);

	public BirdScenarioBean() {
		pc = new JSGPanel();
		setLayout(new BorderLayout());
		add(pc, BorderLayout.CENTER);
		pc.setScene(zoom);
		// setEnabled(false);
	}

	/** keep the recent viewport visible */
	@Override
	public void doLayout() {
	// super.doLayout();
	}

	public TrajectoryScenarioBean getMaster() {
		return master;
	}

	public void setMaster(final TrajectoryBean master) {
		if (master instanceof TrajectoryScenarioBean) {
			this.master = (TrajectoryScenarioBean) master;
			for (int i = scene.getChildren().size() - 1; i >= 0; i--)
				scene.remove(i);
			// scene.add(this.master.getDc2Wc());

			zoom.setAffine(AnimateAffine.map(ZoomHelper.HousePlus, this
					.getBounds(), zoom.getAffine()));
		}
	}
}