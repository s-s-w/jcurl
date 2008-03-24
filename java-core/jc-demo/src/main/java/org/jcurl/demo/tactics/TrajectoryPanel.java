package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;

import org.apache.commons.logging.Log;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.UndoRedoDocumentBase;
import org.jcurl.core.ui.TaskExecutor.ForkableFixed;
import org.jcurl.core.ui.TaskExecutor.Single;
import org.jcurl.zui.piccolo.BroomPromptSimple;
import org.jcurl.zui.piccolo.PCurveStore;
import org.jcurl.zui.piccolo.PIceFactory;
import org.jcurl.zui.piccolo.PPositionSet;
import org.jcurl.zui.piccolo.PRockFactory;
import org.jcurl.zui.piccolo.PRockNode;
import org.jcurl.zui.piccolo.PTrajectoryFactory;
import org.jcurl.zui.piccolo.PRockNode.DragHandler;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PPaintContext;

class TrajectoryPanel extends JComponent {
	private static final Log log = JCLoggerFactory
			.getLogger(TrajectoryPanel.class);
	private static final long serialVersionUID = -4648771240323713217L;
	private final BroomPromptSimple broom;
	private final PPositionSet current;
	private CurveManager curves;
	private final PNode ice;
	private final PPositionSet initial;
	private final int major = 255;
	private final BroomSpeedMediator mediator = new BroomSpeedMediator();
	private final int minor = 64;
	final PCanvas pico;
	private final PCurveStore traj;
	private UndoRedoDocumentBase undo;

	public TrajectoryPanel() {
		setLayout(new BorderLayout());
		this.add(pico = new PCanvas(), BorderLayout.CENTER);
		pico.setBackground(new Color(0xE8E8FF));
		pico.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		pico.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		// pico.getRoot().getDefaultInputManager().setKeyboardFocus(
		// new KeyboardZoom(pico.getCamera()));

		pico.getLayer().addChild(ice = new PIceFactory.Fancy().newInstance());

		traj = new PCurveStore(new PTrajectoryFactory.Fancy(), MainApp.tmax);
		initial = new PPositionSet(new PRockFactory.Fancy(minor));
		initial.addInputEventListener(new DragHandler() {
			@Override
			protected void pushChange(final boolean isDrop,
					final PRockNode node, final Point2D currentPos,
					final Point2D startPos) {
				/** A Inner Anonymous Class Comment */
				new ForkableFixed<Single>() {
					/** A Inner Anonymous Method Comment */
					public void run() {
						node.getRock().p().setLocation(currentPos);
						if (isDrop)
							// FIXME Add Undo.
							;
						// recompute the curves on every move
						getCurves().stateChanged(
								new ChangeEvent(getCurves().getInitialPos()));
					}
				}.fork();
			}
		});
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
	}

	public BroomPromptModel getBroom() {
		return broom.getModel();
	}

	public CurveManager getCurves() {
		return curves;
	}

	public UndoRedoDocumentBase getUndo() {
		return undo;
	}

	public void setBroom(final BroomPromptModel broomPrompt) {
		mediator.setBroom(broomPrompt);
		broom.setModel(broomPrompt);
	}

	public void setCurves(final CurveManager model) {
		// TODO PropertyChangeEvent
		curves = model;
		if (curves == null) {
			traj.setModel(null);
			current.setModel(null);
			initial.setModel(null);
			mediator.setPosition(null);
			mediator.setSpeed(null);
			mediator.setCurler(null);
		} else {
			traj.setModel(curves.getCurveStore());
			current.setModel(curves.getCurrentPos());
			initial.setModel(curves.getInitialPos());
			mediator.setPosition(curves.getInitialPos());
			mediator.setSpeed(curves.getInitialSpeed());
			mediator.setCurler(curves.getCurler());
		}
	}

	public void setUndo(final UndoRedoDocumentBase undo) {
		this.undo = undo;
	}
}