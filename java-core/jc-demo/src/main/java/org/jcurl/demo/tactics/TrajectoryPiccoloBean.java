package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.ChangeManager;
import org.jcurl.core.ui.PosMemento;
import org.jcurl.core.ui.TrajectoryBroomPromptWrapper;
import org.jcurl.zui.piccolo.BroomPromptSimple;
import org.jcurl.zui.piccolo.KeyboardZoom;
import org.jcurl.zui.piccolo.PCurveStore;
import org.jcurl.zui.piccolo.PIceFactory;
import org.jcurl.zui.piccolo.PPositionSet;
import org.jcurl.zui.piccolo.PRockFactory;
import org.jcurl.zui.piccolo.PRockNode;
import org.jcurl.zui.piccolo.PTrajectoryFactory;
import org.jcurl.zui.piccolo.PRockNode.DragHandler;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PLayer;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.util.PPaintContext;

public class TrajectoryPiccoloBean extends TrajectoryBean {
	class Controller {
		final PInputEventListener keyZoom;
		final PInputEventListener rockMove = new DragHandler() {
			private PosMemento start = null;

			@Override
			protected void pushChange(final boolean isDrop,
					final PRockNode node, final Point2D currentPos,
					final Point2D startPos) {
				// mouse moved
				final PosMemento m = new PosMemento(cm.getInitialPos(),
						(Integer) node.getAttribute(PRockNode.INDEX16),
						currentPos);
				if (start == null)
					start = m;
				if (isDrop) {
					getChanger().undoable(start, m);
					start = null;
				} else
					getChanger().temporary(m);
			}
		};

		Controller(final PCamera cam) {
			keyZoom = new KeyboardZoom(cam);
		}
	}

	private static final long serialVersionUID = -4648771240323713217L;
	private static final int TMAX = 30;
	private final BroomPromptSimple broom;
	private ComputedTrajectorySet cm = null;
	private final Controller con;
	private final PPositionSet current;
	private final PNode ice;
	private final PPositionSet initial;
	private final int major = 255;
	private final int minor = 64;
	private final PCanvas pico;
	private transient volatile Rectangle2D tmpViewPort = null;
	private final PCurveStore traj;

	public TrajectoryPiccoloBean() {
		pico = new PCanvas();
		setVisible(false);
		setLayout(new BorderLayout());
		add(pico, BorderLayout.CENTER);
		pico.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		pico.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);

		con = new Controller(pico.getCamera());
		pico.addInputEventListener(con.keyZoom);

		// create the scene
		ice = new PIceFactory.Fancy().newInstance();
		traj = new PCurveStore(new PTrajectoryFactory.Fancy(), TMAX);
		initial = new PPositionSet(new PRockFactory.Fancy(minor));
		initial.addInputEventListener(con.rockMove);
		current = new PPositionSet(new PRockFactory.Fancy(major));
		broom = new BroomPromptSimple();
		broom.setModel(new TrajectoryBroomPromptWrapper());

		traj.setVisible(false);
		current.setVisible(false);
		initial.setVisible(false);
		broom.setVisible(false);

		ice.addChild(traj);
		ice.addChild(current);
		ice.addChild(initial);
		ice.addChild(broom);

		pico.getLayer().addChild(ice);
		pico.setBackground(super.getBackground());
		setVisible(true);
	}

	/** keep the recent viewport visible */
	@Override
	public void doLayout() {
		super.doLayout();
		if (tmpViewPort != null)
			pico.getCamera().setViewBounds(tmpViewPort);
	}

	@Override
	public BroomPromptModel getBroom() {
		return broom.getModel();
	}

	@Override
	public ComputedTrajectorySet getCurves() {
		return cm;
	}

	PLayer getIceLayer() {
		return pico.getLayer();
	}

	public RectangularShape getZoom() {
		if (tmpViewPort == null)
			return pico.getCamera().getViewBounds();
		return tmpViewPort;
	}

	@Override
	public void setBackground(final Color bg) {
		pico.setBackground(bg);
		super.setBackground(bg);
	}

	@Override
	public void setChanger(final ChangeManager changer) {
		super.setChanger(changer);
		broom.setChanger(changer);
	}

	@Override
	public void setCurves(final ComputedTrajectorySet model) {
		// sync curve- and broom data models:
		if (model == null)
			broom.setModel(null);
		else {
			final TrajectoryBroomPromptWrapper bpm;
			if (broom.getModel() == null
					|| !(broom.getModel() instanceof TrajectoryBroomPromptWrapper))
				bpm = new TrajectoryBroomPromptWrapper();
			else
				bpm = (TrajectoryBroomPromptWrapper) broom.getModel();
			bpm.init(model);
			broom.setModel(bpm);
		}

		cm = model;
		if (model == null) {
			traj.setModel(null);
			current.setModel(null);
			initial.setModel(null);
		} else {
			traj.setModel(model.getCurveStore());
			current.setModel(model.getCurrentPos());
			initial.setModel(model.getInitialPos());
		}
		traj.setVisible(model != null);
		current.setVisible(model != null);
		initial.setVisible(model != null);
		broom.setVisible(model != null);
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
		pico.getCamera().animateViewToCenterBounds(r, true, transitionMillis);
	}
}