package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.undo.StateEdit;

import org.jcurl.core.api.CurveStore;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.UndoRedoDocumentBase;
import org.jcurl.zui.piccolo.BroomPromptSimple;
import org.jcurl.zui.piccolo.PCurveStore;
import org.jcurl.zui.piccolo.PIceFactory;
import org.jcurl.zui.piccolo.PPositionSet;
import org.jcurl.zui.piccolo.PRockFactory;
import org.jcurl.zui.piccolo.PTrajectoryFactory;
import org.jcurl.zui.piccolo.PRockNode.DragHandler;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPaintContext;

class ZuiPanel extends JComponent {
	private static final long serialVersionUID = -4648771240323713217L;

	private final BroomPromptSimple broom;
	private final PPositionSet current;
	private final PNode ice;
	private final PPositionSet initial;
	private final int major = 255;
	private final int minor = 64;
	final PCanvas pico;

	private final PCurveStore traj;

	public ZuiPanel(final UndoRedoDocumentBase undo) {
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
			private StateEdit edit = null;

			@Override
			public void mouseDragged(final PInputEvent event) {
				if (edit == null)
					edit = new StateEdit(getInitialPos());
				super.mouseDragged(event);
			}

			@Override
			public void mouseReleased(final PInputEvent event) {
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

	public RockSet<Pos> getCurrentPos() {
		return current.getModel();
	}

	public RockSet<Pos> getInitialPos() {
		return initial.getModel();
	}

	public void setBroomPrompt(final BroomPromptModel broomPrompt) {
		broom.setModel(broomPrompt);
	}

	public void setCurrentPos(final RockSet<Pos> current) {
		this.current.setModel(current);
	}

	public void setCurveStore(final CurveStore cs) {
		traj.setModel(cs);
	}

	public void setInitialPos(final RockSet<Pos> initial) {
		this.initial.setModel(initial);
	}
}