package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.event.ChangeEvent;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.ChangeManager;
import org.jcurl.core.ui.PosMemento;
import org.jcurl.math.R1RNFunction;
import org.jcurl.zui.piccolo.BroomPromptSimple;
import org.jcurl.zui.piccolo.PIceFactory;
import org.jcurl.zui.piccolo.PRockFactory;
import org.jcurl.zui.piccolo.PTrajectoryFactory;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPaintContext;

public class TrajectoryPiccoloBean extends TrajectoryBean<PNode, PNode> {

	/**
	 * V -&gt; M Controller. Push rock movements via {@link ChangeManager} to
	 * the model.
	 */
	private class MoveHandler extends PBasicInputEventHandler {
		private PosMemento first = null;

		private PNode climb(PNode node) {
			for (;;) {
				if (node.getPickable() && node.getAttribute(ATTR_IDX16) != null)
					return node;
				node = node.getParent();
				if (node == null)
					return null;
			}
		}

		@SuppressWarnings("unchecked")
		private PosMemento create(final PInputEvent e, final PNode node) {
			final RockSet<Pos> rs = (RockSet<Pos>) node
					.getAttribute(ATTR_ROCKSET);
			final Point2D p = world.globalToLocal(e.getPosition());
			if (log.isDebugEnabled())
				log.debug(p);
			return new PosMemento(rs, (Integer) node.getAttribute(ATTR_IDX16),
					p);
		}

		@Override
		public void mouseDragged(final PInputEvent e) {
			final PNode node = climb(e.getPickedNode());
			if (log.isDebugEnabled())
				log.debug(node.getAttribute(ATTR_IDX16));
			getChanger().temporary(create(e, node));
			e.setHandled(true);
		}

		@Override
		public void mouseEntered(final PInputEvent arg0) {
			super.mouseEntered(arg0);
			arg0.pushCursor(CURSOR);
		}

		@Override
		public void mouseExited(final PInputEvent arg0) {
			super.mouseExited(arg0);
			arg0.popCursor();
		}

		@Override
		public void mousePressed(final PInputEvent e) {
			final PNode node = climb(e.getPickedNode());
			if (log.isDebugEnabled())
				log.debug(node.getAttribute(ATTR_IDX16));
			final int i16 = (Integer) node.getAttribute(ATTR_IDX16);
			final RockSet<Pos> ipos = getCurves().getInitialPos();
			first = new PosMemento(ipos, i16, ipos.getRock(i16).p());
			e.setHandled(true);
		}

		@Override
		public void mouseReleased(final PInputEvent e) {
			final PNode node = climb(e.getPickedNode());
			if (log.isDebugEnabled())
				log.debug(node.getAttribute(ATTR_IDX16));
			getChanger().undoable(first, create(e, node));
			e.setHandled(true);
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(TrajectoryPiccoloBean.class);
	private static final int major = 255;
	private static final int minor = 64;
	private static final long serialVersionUID = -4648771240323713217L;

	/** update one curve's path */
	private static void syncM2V(final ComputedTrajectorySet src, final int i16,
			final PNode[] dst, final PTrajectoryFactory tf) {
		syncM2V(src.getCurveStore().iterator(i16), i16, dst[i16], tf);
	}

	/** update one curves' path */
	private static void syncM2V(
			final Iterator<Entry<Double, R1RNFunction>> src, final int i16,
			final PNode dst, final PTrajectoryFactory tf) {
		if (log.isDebugEnabled())
			log.debug("path " + i16);
		tf.refresh(src, RockSet.isDark(i16), 0, 30, dst);
	}

	/** update one rock */
	private static void syncM2V(final Rock<Pos> src, final PNode dst,
			final long dt) {
		if (src == null || dst == null) {
			if (log.isDebugEnabled())
				log.debug(src + " " + dst);
			return;
		}
		if (log.isDebugEnabled())
			log.debug("rock " + dst.getAttribute(ATTR_IDX16));
		if (dt >= 0)
			dst.animateToTransform(src.getAffineTransform(), dt);
		else
			dst.setTransform(src.getAffineTransform());
	}

	private final long AnimationMillis = 150;
	private final BroomPromptSimple broom = new BroomPromptSimple();
	private final PNode[] current = new PNode[RockSet.ROCKS_PER_SET];
	private final PNode[] initial = new PNode[RockSet.ROCKS_PER_SET];
	private final MoveHandler mouse = new MoveHandler();
	private final PCanvas panel;
	private final PNode[] path = new PNode[RockSet.ROCKS_PER_SET];
	/** Rock<Pos> -> SGNode lookup */
	private final Map<Rock<Pos>, PNode> r2n = new IdentityHashMap<Rock<Pos>, PNode>();
	private final PNode rocks;
	private final PTrajectoryFactory tf = new PTrajectoryFactory.Fancy();

	private final PNode world;

	public TrajectoryPiccoloBean() {
		setVisible(false);
		broom.setModel(tt);
		panel = new PCanvas();
		panel.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		panel.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);

		// create the WC coordinate system
		world = new PNode();
		// add the ice
		world.addChild(new PIceFactory.Fancy().newInstance());
		// add initial, current rock and paths
		final PNode init = new PNode();
		final PNode curr = new PNode();
		final PNode paths = new PNode();
		rocks = new PNode();
		final RockSet<Pos> home = RockSetUtils.allHome();
		final RockSet<Pos> out = RockSetUtils.allOut();
		final PRockFactory iniRf = new PRockFactory.Fancy(minor);
		final PRockFactory curRf = new PRockFactory.Fancy(major);
		for (int i16 = RockSet.ROCKS_PER_SET - 1; i16 >= 0; i16--) {
			// initial (read/write) rocks
			PNode n;
			init.addChild(n = initial[i16] = iniRf.newInstance(i16));
			n.setPickable(true);
			n.addInputEventListener(mouse);

			// current (read-only) rocks
			curr.addChild(n = current[i16] = curRf.newInstance(i16));
			n.addAttribute(ATTR_TRIGGER_CURVE_UPDATE, true);
			n.setChildrenPickable(false);

			// trajectories (read-only)
			paths.addChild(path[i16] = new PNode());
		}
		rocks.addChild(paths);
		rocks.addChild(init);
		rocks.addChild(curr);
		rocks.addChild(broom);
		rocks.setVisible(false);
		world.addChild(rocks);

		// make world right-handed:
		world.setTransform(AffineTransform.getScaleInstance(1, -1));

		panel.getLayer().addChild(world);
		panel.setBackground(super.getBackground());
		setVisible(true);
	}

	@Override
	public BroomPromptModel getBroom() {
		return broom.getModel();
	}

	PLayer getIceLayer() {
		return panel.getLayer();
	}

	@Override
	public RectangularShape getZoom() {
		if (super.getZoom() == null)
			return panel.getCamera().getViewBounds();
		return super.getZoom();
	}

	@Override
	public void setBackground(final Color bg) {
		panel.setBackground(bg);
		super.setBackground(bg);
	}

	@Override
	public void setChanger(final ChangeManager changer) {
		super.setChanger(changer);
		broom.setChanger(changer);
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
				syncM2V(getCurves(), i16, path, tf);
			}
		}
		tt.init(this.curves);
		rocks.setVisible(this.curves != null);
	}

	public void setZoom(final RectangularShape viewport, int transitionMillis) {
		if (transitionMillis < 0)
			transitionMillis = 333;
		final Rectangle2D r;
		if (viewport instanceof Rectangle2D)
			r = (Rectangle2D) viewport;
		else
			r = new Rectangle2D.Double(viewport.getX(), viewport.getY(),
					viewport.getWidth(), viewport.getHeight());
		tmpViewPort = (Rectangle2D) r.clone();
		panel.getCamera().animateViewToCenterBounds(r, true, transitionMillis);
	}

	/** M -&gt; V Controller, update the view. */
	public void stateChanged(final ChangeEvent e) {
		if (e.getSource() instanceof Rock) {
			// update the rock position, be it initial or current
			final Rock<Pos> r = (Rock<Pos>) e.getSource();
			final PNode n = r2n.get(r);
			syncM2V(r, n, AnimationMillis);

			// update the trajectory path, current only.
			if (!Boolean.TRUE.equals(n.getAttribute(ATTR_TRIGGER_CURVE_UPDATE)))
				return;
			final int i16 = (Integer) n.getAttribute(ATTR_IDX16);
			syncM2V(getCurves(), i16, path, tf);
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
	private PNode syncM2V(final RockSet<Pos> src, final int i16, final PNode dst) {
		final Rock<Pos> r = src.getRock(i16);
		syncM2V(r, dst, 0);
		dst.addAttribute(ATTR_ROCKSET, src);
		dst.addAttribute(ATTR_ROCK, r);
		dst.addAttribute(ATTR_IDX16, i16);
		r2n.put(r, dst);
		return dst;
	}
}