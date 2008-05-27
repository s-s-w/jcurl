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
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;

import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.PositionSet;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.ChangeManager;

import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGTransform;
import com.sun.scenario.scenegraph.SGTransform.Affine;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class TrajectoryScenarioBean extends TrajectoryBean {
	private static final long serialVersionUID = 6661957210899967106L;

	private static SGGroup createSceneIce() {
		return new SGIceFactory.Fancy().newInstance();
	}

	private static Affine createSceneRock(final int idx16, final int opacity) {
		return new SGRockFactory.Fancy(opacity).newInstance(idx16, null);
	}

	private static Affine createSceneRock(final RockSet<Pos> pos,
			final int idx16, final int opacity) {
		return sync(pos, idx16, createSceneRock(idx16, opacity));
	}

	/** create a transformation that maps wc to dc */
	private static AffineTransform map(final RectangularShape wc,
			final RectangularShape dc, AffineTransform zt) {
		if (zt == null)
			zt = new AffineTransform();
		else
			zt.setToIdentity();
		// 3. move to the center of dc
		zt.translate(dc.getCenterX(), dc.getCenterY());
		// 2. scale to fit
		final double sw = dc.getWidth() / wc.getWidth();
		final double sh = dc.getHeight() / wc.getHeight();
		final double scale = sw > sh ? sh : sw;
		zt.scale(scale, scale);
		// 1. move the center of viewport to (0,0)
		zt.translate(-wc.getCenterX(), -wc.getCenterY());
		return zt;
	}

	private static Affine sync(final RockSet<Pos> src, final int idx16,
			final Affine dst) {
		final AffineTransform rt = dst.getAffine();
		rt.setToIdentity();
		rt.translate(src.getRock(idx16).getX(), src.getRock(idx16).getY());
		rt.rotate(src.getRock(idx16).getA());
		dst.setAffine(rt);
		// FIXME use the correct attribute names
		dst.putAttribute("idx16", idx16);
		dst.putAttribute("rock", src.getRock(idx16));
		dst.putAttribute("rockset", src);
		return dst;
	}

	private final ChangeManager changer = null;
	private final ComputedTrajectorySet cm = null;
	private final Affine[] current = new Affine[RockSet.ROCKS_PER_SET];
	private final Affine[] initial = new Affine[RockSet.ROCKS_PER_SET];
	private final JSGPanel pico;
	private transient RectangularShape tmpViewPort = null;
	private final Affine zoom;

	public TrajectoryScenarioBean() {
		pico = new JSGPanel();
		setVisible(false);
		setLayout(new BorderLayout());
		add(pico, BorderLayout.CENTER);

		final SGGroup root = new SGGroup();
		root.add(createSceneIce());
		final RockSet<Pos> home = PositionSet.allHome();
		final RockSet<Pos> out = PositionSet.allOut();
		for (int idx16 = RockSet.ROCKS_PER_SET - 1; idx16 >= 0; idx16--) {
			root.add(initial[idx16] = createSceneRock(home, idx16, 64));
			root.add(current[idx16] = createSceneRock(out, idx16, 255));
		}

		final AffineTransform rightHand = new AffineTransform();
		rightHand.scale(1, -1);
		zoom = SGTransform.createAffine(new AffineTransform(), SGTransform
				.createAffine(rightHand, root));
		pico.setScene(zoom);
		setVisible(true);
	}

	@Override
	public BroomPromptModel getBroom() {
		return null;// broom.getModel();
	}

	@Override
	public ChangeManager getChanger() {
		return changer;
	}

	@Override
	public ComputedTrajectorySet getCurves() {
		return cm;
	}

	public RectangularShape getZoom() {
		if (tmpViewPort == null)
			return null;// pico.getCamera().getViewBounds();
		return tmpViewPort;
	}

	@Override
	public void setChanger(final ChangeManager changer) {}

	@Override
	public void setCurves(final ComputedTrajectorySet model) {}

	public void setZoom(final RectangularShape viewport) {
		setZoom(viewport, -1);
	}

	public void setZoom(final RectangularShape viewport,
			final int transitionMillis) {
		// TODO animate
		zoom.setAffine(map(tmpViewPort = viewport, this.getBounds(), zoom
				.getAffine()));
	}
}