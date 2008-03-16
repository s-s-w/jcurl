package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.undo.StateEdit;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.BroomPromptModel;
import org.jcurl.core.model.CurveManager;
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
	private final PNode ice;
	private final PPositionSet initial;
	private final int major = 255;
	private final BroomSpeedMediator mediator = new BroomSpeedMediator();
	private final int minor = 64;
	private CurveManager model;
	final PCanvas pico;

	private final PCurveStore traj;

	public TrajectoryPanel(final UndoRedoDocumentBase undo) {
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
					edit = new StateEdit(model.getInitialPos());
				super.mouseDragged(event);
			}

			@Override
			synchronized public void mouseReleased(final PInputEvent event) {
				if (edit == null)
					return;
				super.mouseReleased(event);
				edit.end();
				undo.addEdit(edit);
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

	public BroomPromptModel getBroomPrompt() {
		return broom.getModel();
	}

	public CurveManager getModel() {
		return model;
	}

	public void setBroomPrompt(final BroomPromptModel broomPrompt) {
		broom.setModel(broomPrompt);
		mediator.setBroom(broomPrompt);
	}

	public void setModel(final CurveManager model) {
		// TODO PropertyChangeEvent
		this.model = model;
		if (this.model == null) {
			traj.setModel(null);
			current.setModel(null);
			initial.setModel(null);
			mediator.setPosition(null);
			mediator.setSpeed(null);
			mediator.setCurler(null);
		} else {
			traj.setModel(this.model.getCurveStore());
			current.setModel(this.model.getCurrentPos());
			initial.setModel(this.model.getInitialPos());
			mediator.setPosition(this.model.getInitialPos());
			mediator.setSpeed(this.model.getInitialSpeed());
			mediator.setCurler(this.model.getCurler());
		}
	}
}