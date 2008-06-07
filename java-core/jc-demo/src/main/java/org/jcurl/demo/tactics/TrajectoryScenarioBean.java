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
import java.awt.Cursor;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.ChangeManager;
import org.jcurl.core.ui.PosMemento;
import org.jcurl.core.ui.TrajectoryBroomPromptWrapper;
import org.jcurl.demo.tactics.sg.AnimateAffine;
import org.jcurl.demo.tactics.sg.BroomPromptScenario;
import org.jcurl.demo.tactics.sg.SGIceFactory;
import org.jcurl.demo.tactics.sg.SGRockFactory;
import org.jcurl.demo.tactics.sg.SGTrajectoryFactory;
import org.jcurl.math.R1RNFunction;

import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGComposite;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGRenderCache;
import com.sun.scenario.scenegraph.SGTransform;
import com.sun.scenario.scenegraph.SGComposite.OverlapBehavior;
import com.sun.scenario.scenegraph.SGTransform.Affine;
import com.sun.scenario.scenegraph.event.SGMouseAdapter;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class TrajectoryScenarioBean extends TrajectoryBean implements
		ChangeListener {
	private class MoveHandler extends SGMouseAdapter {
		private PosMemento first = null;
		private final Point2D tmp = new Point2D.Double(0, 0);

		@SuppressWarnings("unchecked")
		private PosMemento create(final MouseEvent e, final SGNode node) {
			final RockSet<Pos> rs = (RockSet<Pos>) node
					.getAttribute(ATTR_ROCKSET);
			final Point2D p = dc2wc.globalToLocal(e.getPoint(), tmp);
			if (log.isDebugEnabled())
				log.debug(p);
			return new PosMemento(rs, (Integer) node.getAttribute(ATTR_IDX16),
					p);
		}

		@Override
		public void mouseDragged(final MouseEvent e, final SGNode node) {
			if (log.isDebugEnabled())
				log.debug(node.getAttribute(ATTR_IDX16));
			getChanger().temporary(create(e, node));
		}

		@Override
		public void mousePressed(final MouseEvent e, final SGNode node) {
			if (log.isDebugEnabled())
				log.debug(node.getAttribute(ATTR_IDX16));
			final int i16 = (Integer) node.getAttribute(ATTR_IDX16);
			final RockSet<Pos> ipos = getCurves().getInitialPos();
			first = new PosMemento(ipos, i16, ipos.getRock(i16).p());
		}

		@Override
		public void mouseReleased(final MouseEvent e, final SGNode node) {
			if (log.isDebugEnabled())
				log.debug(node.getAttribute(ATTR_IDX16));
			getChanger().undoable(first, create(e, node));
		}
	}

	private static final String ATTR_IDX16 = "idx16";
	private static final String ATTR_ROCK = "rock";
	private static final String ATTR_ROCKSET = "rockset";
	private static final String ATTR_TRIGGER_CURVE_UPDATE = "trigger_curve_update";
	private static final boolean DoCacheRocks = true;
	private static final Log log = JCLoggerFactory
			.getLogger(TrajectoryScenarioBean.class);
	private static final Cursor moveC = Cursor
			.getPredefinedCursor(Cursor.MOVE_CURSOR);
	private static final long serialVersionUID = 6661957210899967106L;

	private static SGNode createSceneIce() {
		// never cache the background.
		return new SGIceFactory.Fancy().newInstance();
	}

	private static Affine createSceneRock(final int i16, final int opacity) {
		SGNode rock = new SGRockFactory.Fancy().newInstance(i16);
		if (DoCacheRocks) {
			final SGRenderCache rc = new SGRenderCache();
			rc.setChild(rock);
			rc
					.setInterpolationHint(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			rock = rc;
		}
		return SGTransform.createAffine(new AffineTransform(), rock);
	}

	/** update one curve's path */
	private static void syncM2V(final ComputedTrajectorySet src, final int i16,
			final SGGroup dst, final SGTrajectoryFactory tf) {
		syncM2V(src.getCurveStore().iterator(i16), i16, (SGGroup) dst
				.getChildren().get(i16), tf);

	}

	/** update one curves' path */
	private static void syncM2V(
			final Iterator<Entry<Double, R1RNFunction>> src, final int i16,
			final SGGroup dst, final SGTrajectoryFactory tf) {
		if (log.isDebugEnabled())
			log.debug("path " + i16);
		tf.refresh(src, RockSet.isDark(i16), 0, 30, dst);
	}

	/** update one rock */
	private static void syncM2V(final Rock<Pos> src, final Affine dst) {
		if (src == null || dst == null) {
			if (log.isDebugEnabled())
				log.debug(src + " " + dst);
			return;
		}
		if (log.isDebugEnabled())
			log.debug("rock " + dst.getAttribute(ATTR_IDX16));
		dst.setAffine(src.getAffineTransform());
	}

	private final BroomPromptScenario bp = new BroomPromptScenario();
	private final Affine[] current = new Affine[RockSet.ROCKS_PER_SET];
	private ComputedTrajectorySet curves = null;
	private final Affine dc2wc;
	private final Affine[] initial = new Affine[RockSet.ROCKS_PER_SET];
	private final MoveHandler mouse = new MoveHandler();
	/** all rocks, trajectories and broomprompt */
	private final SGComposite opa_r0 = new SGComposite();
	private final SGComposite opa_r1 = new SGComposite();
	private final SGComposite opa_t0 = new SGComposite();
	private final JSGPanel pico;
	/** Rock<Pos> -> SGNode lookup */
	private final Map<Rock<Pos>, Affine> r2n = new IdentityHashMap<Rock<Pos>, Affine>();
	private final SGGroup scene = new SGGroup();
	private final transient SGTrajectoryFactory tf = new SGTrajectoryFactory.Fancy();
	private transient RectangularShape tmpViewPort = null;
	private final SGGroup traj = new SGGroup();
	private final TrajectoryBroomPromptWrapper tt = new TrajectoryBroomPromptWrapper();
	private final Affine zoom;

	public TrajectoryScenarioBean() {
		bp.setModel(tt);
		pico = new JSGPanel();
		setVisible(false);
		setLayout(new BorderLayout());
		add(pico, BorderLayout.CENTER);

		final SGGroup root = new SGGroup();
		root.add(createSceneIce());

		// rocks.setVisible(false);
		final SGGroup r0 = new SGGroup();
		final SGGroup r1 = new SGGroup();
		// rocks.add(traj);
		final RockSet<Pos> home = RockSetUtils.allHome();
		final RockSet<Pos> out = RockSetUtils.allOut();
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--) {
			Affine n = createSceneRock(home, i16, 255);
			n.setMouseBlocker(true);
			n.addMouseListener(mouse);
			n.setCursor(moveC);
			r0.add(initial[i16] = n);
			r1.add(current[i16] = n = createSceneRock(out, i16, 255));
			n.putAttribute(ATTR_TRIGGER_CURVE_UPDATE, true);
			traj.add(new SGGroup());
		}
		if (false) {
			scene.add(traj);
			scene.add(r0);
			scene.add(r1);
		} else {
			opa_r0.setChild(r0);
			opa_r0.setOpacity(64.0F / 255.0F);
			opa_r0.setOverlapBehavior(OverlapBehavior.LAYER);

			opa_r1.setChild(r1);
			opa_r1.setOverlapBehavior(OverlapBehavior.LAYER);

			opa_t0.setChild(traj);
			opa_t0.setMouseBlocker(true);
			opa_t0.setOverlapBehavior(OverlapBehavior.LAYER);
			opa_t0.setOpacity(100.0F / 255.0F);

			scene.add(opa_t0);
			scene.add(opa_r0);
			scene.add(opa_r1);
		}
		scene.add(bp.getScene());
		root.add(scene);

		final AffineTransform rightHand = AffineTransform.getScaleInstance(1,
				-1);
		zoom = SGTransform.createAffine(new AffineTransform(),
				dc2wc = SGTransform.createAffine(rightHand, root));
		bp.setDc2wc(dc2wc);
		pico.setScene(zoom);
		setVisible(true);
		scene.setVisible(false);
	}

	private void addCL(final RockSet<Pos> s) {
		if (s == null)
			return;
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--)
			s.getRock(i16).addChangeListener(this);
	}

	private Affine createSceneRock(final RockSet<Pos> pos, final int i16,
			final int opacity) {
		return syncM2V(pos, i16, createSceneRock(i16, opacity));
	}

	/** keep the recent viewport visible */
	@Override
	public void doLayout() {
		super.doLayout();
		if (tmpViewPort != null)
			zoom.setAffine(AnimateAffine.map(tmpViewPort, this.getBounds(),
					zoom.getAffine()));
	}

	@Override
	public BroomPromptModel getBroom() {
		return bp.getModel();
	}

	@Override
	public ComputedTrajectorySet getCurves() {
		return curves;
	}

	Affine getDc2Wc() {
		return dc2wc;
	}

	public RectangularShape getZoom() {
		if (tmpViewPort == null)
			return null;// pico.getCamera().getViewBounds();
		return tmpViewPort;
	}

	private void removeCL(final RockSet<Pos> s) {
		if (s == null)
			return;
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--)
			s.getRock(i16).removeChangeListener(this);
	}

	@Override
	public void setChanger(final ChangeManager changer) {
		super.setChanger(changer);
		bp.setChanger(changer);
	}

	@Override
	public void setCurves(final ComputedTrajectorySet curves) {
		// rocks.setVisible(model != null);
		if (this.curves != null) {
			removeCL(this.curves.getInitialPos());
			removeCL(this.curves.getCurrentPos());
		}
		this.curves = curves;
		r2n.clear();
		if (this.curves != null) {
			final RockSet<Pos> ip = this.curves.getInitialPos();
			final RockSet<Pos> cp = this.curves.getCurrentPos();
			addCL(ip);
			addCL(cp);
			for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--) {
				syncM2V(ip, i16, initial[i16]);
				syncM2V(cp, i16, current[i16]);
				syncM2V(getCurves(), i16, traj, tf);
			}
		}
		tt.init(this.curves);
		scene.setVisible(this.curves != null);
	}

	public void setZoom(final RectangularShape viewport,
			final int transitionMillis) {
		AnimateAffine.animateToCenterBounds(zoom, this.getBounds(),
				tmpViewPort = viewport, transitionMillis);
	}

	public void stateChanged(final ChangeEvent e) {
		if (e.getSource() instanceof Rock) {
			// update the rock position, be it initial or current
			final Rock<Pos> r = (Rock<Pos>) e.getSource();
			final Affine n = r2n.get(r);
			syncM2V(r, n);

			// update the trajectory path, current only.
			if (!Boolean.TRUE.equals(n.getAttribute(ATTR_TRIGGER_CURVE_UPDATE)))
				return;
			final int i16 = (Integer) n.getAttribute(ATTR_IDX16);
			syncM2V(getCurves(), i16, traj, tf);
		} else if (log.isDebugEnabled())
			log.debug("Unconsumed event from " + e.getSource());
	}

	/**
	 * rather an "init" than sync. Don't use with high frequency.
	 * 
	 * @param src
	 * @param i16
	 * @param dst
	 * @return
	 */
	private Affine syncM2V(final RockSet<Pos> src, final int i16,
			final Affine dst) {
		final Rock<Pos> r = src.getRock(i16);
		syncM2V(r, dst);
		dst.putAttribute(ATTR_ROCKSET, src);
		dst.putAttribute(ATTR_ROCK, r);
		dst.putAttribute(ATTR_IDX16, i16);
		r2n.put(r, dst);
		return dst;
	}
}
