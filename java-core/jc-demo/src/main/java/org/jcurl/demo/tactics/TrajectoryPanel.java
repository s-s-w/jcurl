package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.undo.StateEdit;

import org.apache.commons.logging.Log;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.UndoRedoDocumentBase;
import org.jcurl.core.zui.BroomPromptSimple;
import org.jcurl.core.zui.PCurveStore;
import org.jcurl.core.zui.PIceFactory;
import org.jcurl.core.zui.PPositionSet;
import org.jcurl.core.zui.PPositionSetDrag;
import org.jcurl.core.zui.PRockFactory;
import org.jcurl.core.zui.PTrajectoryFactory;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
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
		initial.addInputEventListener(new PPositionSetDrag() {
			private StateEdit edit = null;

			@Override
			synchronized public void mouseDragged(final PInputEvent event) {
				if (edit == null)
					edit = new StateEdit(curves.getInitialPos());
				super.mouseDragged(event);
			}

			@Override
			synchronized public void mouseReleased(final PInputEvent event) {
				if (edit == null)
					return;
				super.mouseReleased(event);
				edit.end();
				getUndo().addEdit(edit);
				edit = null;
				// FIXME traj.sync(MainApp.tmax);
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