package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.DefaultBroomPromptModel;
import org.jcurl.core.ui.UndoRedoDocumentBase;
import org.jcurl.core.ui.TaskExecutor.ForkableFixed;
import org.jcurl.core.ui.TaskExecutor.SwingEDT;
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
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.util.PPaintContext;

class TrajectoryPiccolo extends JComponent implements Zoomable,
		TrajectoryDisplay {
	/**
	 * Mediate ZUI changes to the underlying {@link TacticsPanelMediator}.
	 * 
	 * @see KeyboardZoom
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	class Controller implements PropertyChangeListener, ChangeListener {
		final PInputEventListener keyZoom;
		final PInputEventListener rockMove = new DragHandler() {
			@Override
			protected void pushChange(final boolean isDrop,
					final PRockNode node, final Point2D currentPos,
					final Point2D startPos) {
				/** A Inner Anonymous Class Comment */
				new ForkableFixed<SwingEDT>() {
					/** A Inner Anonymous Method Comment */
					public void run() {
						mediator.updatePos((Integer) node
								.getAttribute(PRockNode.INDEX16), currentPos,
								cm);
						if (isDrop)
							// FIXME Add Undo.
							;
					}
				}.fork();
			}
		};

		Controller(final PCamera cam) {
			keyZoom = new KeyboardZoom(cam);
		}

		public void propertyChange(final PropertyChangeEvent evt) {
			// log.info(evt.getSource().getClass().getName() + " "
			// + evt.getPropertyName());
			if (evt.getSource() instanceof DefaultBroomPromptModel) {
				final BroomPromptModel bpm = (BroomPromptModel) evt.getSource();
				new ForkableFixed<SwingEDT>() {
					/** A Inner Anonymous Method Comment */
					public void run() {
						if ("broom".equals(evt.getPropertyName()))
							mediator.updateBroom(bpm.getBroom(), cm, bpm);
						else if ("outTurn".equals(evt.getPropertyName()))
							mediator.updateTurn(bpm.getOutTurn(), bpm, cm);
						else if ("idx16".equals(evt.getPropertyName()))
							mediator.updateIndex(bpm.getIdx16(), cm, bpm);
						else if ("valueIsAdjusting".equals(evt
								.getPropertyName()))
							if (bpm.getValueIsAdjusting())
								log.info("Push broom to Undo/Redo manager");
							else
								;
						else
							throw new UnsupportedOperationException(evt
									.getPropertyName());
					}
				}.fork();
			} else
				throw new UnsupportedOperationException(evt.getSource()
						.getClass().getName());
		}

		public void stateChanged(final ChangeEvent evt) {
			// log.info(evt.getSource().getClass().getName());
			if (evt.getSource() instanceof BoundedRangeModel) {
				final BoundedRangeModel brm = (BoundedRangeModel) evt
						.getSource();
				new ForkableFixed<SwingEDT>() {
					/** A Inner Anonymous Method Comment */
					public void run() {
						if (brm.getValueIsAdjusting())
							log.info("Speed change underway");
						else
							log.info("Speed change done");
						mediator.updateSplitTimeMillis(cm, broom.getModel(),
								brm.getValue());
					}
				}.fork();
			}
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(TrajectoryPiccolo.class);

	private static final long serialVersionUID = -4648771240323713217L;
	private static final int TMAX = 30;
	private final BroomPromptSimple broom;
	private CurveManager cm = null;
	private final Controller con;
	private final PPositionSet current;
	private final PNode ice;
	private final PPositionSet initial;
	private final int major = 255;
	private final TacticsPanelMediator mediator = new TacticsPanelMediator();
	private final int minor = 64;
	private final PCanvas pico;
	private transient volatile RectangularShape tmpViewPort = null;
	private final PCurveStore traj;

	private UndoRedoDocumentBase undo;

	public TrajectoryPiccolo() {
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

		traj.setVisible(false);
		current.setVisible(false);
		initial.setVisible(false);
		broom.setVisible(false);

		ice.addChild(traj);
		ice.addChild(current);
		ice.addChild(initial);
		ice.addChild(broom);

		pico.getLayer().addChild(ice);
		setBackground(super.getBackground());
		setVisible(true);
	}

	public BroomPromptModel getBroom() {
		return broom.getModel();
	}

	public CurveManager getCurves() {
		return cm;
	}

	public UndoRedoDocumentBase getUndo() {
		return undo;
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

	public void setBroom(final DefaultBroomPromptModel b) {
		if (broom.getModel() != null) {
			broom.getModel().removePropertyChangeListener(con);
			broom.getModel().getSplitTimeMillis().removeChangeListener(con);
		}
		broom.setModel(b);
		if (broom.getModel() != null) {
			broom.getModel().addPropertyChangeListener(con);
			broom.getModel().getSplitTimeMillis().addChangeListener(con);
		}
	}

	public void setCurves(final CurveManager model) {
		setBroom(mediator.setCm(cm = model, broom.getModel()));
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

	public void setUndo(final UndoRedoDocumentBase undo) {
		this.undo = undo;
	}

	public void setZoom(final RectangularShape viewport) {
		setZoom(viewport, -1);
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
		tmpViewPort = (RectangularShape) r.clone();
		pico.getCamera().animateViewToCenterBounds(r, true, transitionMillis);
	}
}