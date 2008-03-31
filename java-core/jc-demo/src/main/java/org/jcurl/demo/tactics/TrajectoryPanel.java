package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;
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
import org.jcurl.core.ui.UndoRedoDocumentBase;
import org.jcurl.core.ui.TaskExecutor.ForkableFixed;
import org.jcurl.core.ui.TaskExecutor.SmartQueue;
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
import edu.umd.cs.piccolo.event.PInputEventListener;
import edu.umd.cs.piccolo.util.PPaintContext;

class TrajectoryPanel extends JComponent {
	/**
	 * Mediate ZUI changes to the underlying {@link TacticsPanelModel}.
	 * 
	 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
	 * @version $Id$
	 */
	static class Controller implements PropertyChangeListener, ChangeListener {
		final PInputEventListener rockMove = new DragHandler() {
			@Override
			protected void pushChange(final boolean isDrop,
					final PRockNode node, final Point2D currentPos,
					final Point2D startPos) {
				/** A Inner Anonymous Class Comment */
				new ForkableFixed<SmartQueue>() {
					/** A Inner Anonymous Method Comment */
					public void run() {
						tpm.updatePos((Integer) node
								.getAttribute(PRockNode.INDEX16), currentPos);
						if (isDrop)
							// FIXME Add Undo.
							;
					}
				}.fork();
			}
		};

		private final TacticsPanelModel tpm;

		Controller(final TacticsPanelModel tpm) {
			this.tpm = tpm;
		}

		public void propertyChange(final PropertyChangeEvent evt) {
			// log.info(evt.getSource().getClass().getName() + " "
			//					+ evt.getPropertyName());
			if (evt.getSource() instanceof BroomPromptModel) {
				final BroomPromptModel bpm = (BroomPromptModel) evt.getSource();
				new ForkableFixed<SmartQueue>() {
					/** A Inner Anonymous Method Comment */
					public void run() {
						if ("broom".equals(evt.getPropertyName()))
							tpm.updateBroom(bpm.getBroom());
						else if ("outTurn".equals(evt.getPropertyName()))
							tpm.updateTurn(bpm.getOutTurn());
						else if ("idx16".equals(evt.getPropertyName()))
							tpm.updateIndex(bpm.getIdx16());
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
			//log.info(evt.getSource().getClass().getName());
			if (evt.getSource() instanceof BoundedRangeModel) {
				final BoundedRangeModel brm = (BoundedRangeModel) evt
						.getSource();
				new ForkableFixed<SmartQueue>() {
					/** A Inner Anonymous Method Comment */
					public void run() {
						tpm.updateSplitTimeMillis(brm.getValue());
					}
				}.fork();
			}
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(TrajectoryPanel.class);
	private static final long serialVersionUID = -4648771240323713217L;
	private final BroomPromptSimple broom;
	private final Controller con;
	private final PPositionSet current;
	private final PNode ice;
	private final PPositionSet initial;
	private final int major = 255;
	private final int minor = 64;
	final PCanvas pico;
	private final TacticsPanelModel tpm = new TacticsPanelModel();
	private final PCurveStore traj;
	private UndoRedoDocumentBase undo;

	public TrajectoryPanel() {
		con = new Controller(tpm);
		setLayout(new BorderLayout());
		this.add(pico = new PCanvas(), BorderLayout.CENTER);
		pico.setBackground(new Color(0xE8E8FF));
		pico.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		pico.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
		// pico.getRoot().getDefaultInputManager().setKeyboardFocus(
		// new KeyboardZoom(pico.getCamera()));

		// create the scene
		ice = new PIceFactory.Fancy().newInstance();
		traj = new PCurveStore(new PTrajectoryFactory.Fancy(), MainApp.tmax);
		initial = new PPositionSet(new PRockFactory.Fancy(minor));
		initial.addInputEventListener(con.rockMove);
		current = new PPositionSet(new PRockFactory.Fancy(major));
		broom = new BroomPromptSimple();
		broom.setModel(con.tpm.getPrompt());
		broom.getModel().addPropertyChangeListener(con);
		broom.getModel().getSplitTimeMillis().addChangeListener(con);

		traj.setVisible(false);
		current.setVisible(false);
		initial.setVisible(false);
		broom.setVisible(false);

		ice.addChild(traj);
		ice.addChild(current);
		ice.addChild(initial);
		ice.addChild(broom);

		pico.getLayer().addChild(ice);
	}

	public BroomPromptModel getBroom() {
		return broom.getModel();
	}

	public CurveManager getCurves() {
		return tpm.getCm();
	}

	public UndoRedoDocumentBase getUndo() {
		return undo;
	}

	public void setCurves(final CurveManager model) {
		tpm.setCm(model);
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
}