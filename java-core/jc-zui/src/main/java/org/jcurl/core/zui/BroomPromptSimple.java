package org.jcurl.core.zui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.StateEdit;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.BroomPromptModel;
import org.jcurl.core.ui.UndoRedoDocument;
import org.jcurl.math.MathVec;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PPickPath;

/** Piccolo View + Controller for {@link BroomPromptModel}s. */
public class BroomPromptSimple extends PNode implements PropertyChangeListener,
		ChangeListener {
	private static final Cursor CURSOR = new Cursor(Cursor.HAND_CURSOR);
	private static final Color dark = Color.RED;
	private static final Color fast = Color.RED;
	private static final Color light = Color.YELLOW;
	private static final Log log = JCLoggerFactory
			.getLogger(BroomPromptSimple.class);
	private static final double scale0 = 0;
	private static final long serialVersionUID = 3115716478135484000L;
	private static final Color slow = Color.BLUE;

	/**
	 * Pointing along the positive X-axis, tip ending at 0,0
	 * 
	 * @param wingWidth
	 * @param length
	 * @param tail
	 */
	static Shape createArrowHead(final float wingWidth, final float length,
			final float tail) {
		final GeneralPath gp = new GeneralPath();
		gp.moveTo(0, 0);
		gp.lineTo(-length, wingWidth);
		gp.lineTo(-length + tail, 0);
		gp.lineTo(-length, -wingWidth);
		gp.closePath();
		return gp;
	}

	static Shape createSlider(final float f, final boolean bothSides) {
		final GeneralPath gp = new GeneralPath();
		gp.moveTo(0, 0);
		gp.lineTo(-f, f);
		gp.lineTo(-4 * f, f);
		gp.lineTo(-4 * f, -f);
		gp.lineTo(-f, -f);
		if (bothSides) {
			gp.lineTo(f, f);
			gp.lineTo(4 * f, f);
			gp.lineTo(4 * f, -f);
			gp.lineTo(f, -f);
		}
		gp.closePath();
		return gp;
	}

	static float interpolate(final float a, final float b, final double ratio) {
		return (float) (a + ratio * (b - a));
	}

	static int interpolate(final int a, final int b, final double ratio) {
		return (int) (a + ratio * (b - a));
	}

	private static Color interpolateHSB(final Color a, final Color b,
			final double ratio) {
		final float[] _a = new float[3];
		final float[] _b = new float[3];
		Color.RGBtoHSB(a.getRed(), a.getGreen(), a.getBlue(), _a);
		Color.RGBtoHSB(b.getRed(), b.getGreen(), b.getBlue(), _b);

		return new Color(Color.HSBtoRGB(interpolate(_a[0], _b[0], ratio),
				interpolate(_a[1], _b[1], ratio), interpolate(_a[2], _b[2],
						ratio)));
	}

	static Color interpolateRGB(final Color a, final Color b, final double ratio) {
		return new Color(interpolate(a.getRed(), b.getRed(), ratio),
				interpolate(a.getGreen(), b.getGreen(), ratio), interpolate(a
						.getBlue(), b.getBlue(), ratio));
	}

	/**
	 * @param sh
	 * @param st
	 * @param sp
	 * @param minScale
	 *            Threshold for <a
	 *            href="http://www.cs.umd.edu/hcil/jazz/learn/patterns.shtml#Semantic%20Zooming">semantic
	 *            zooming</a>
	 */
	static PPath node(final Shape sh, final Stroke st, final Paint sp,
			final double minScale) {
		final PPath s = new PPath(sh, st) {
			private static final long serialVersionUID = 5255259239579383074L;

			@Override
			public void paint(final PPaintContext aPaintContext) {
				if (aPaintContext.getScale() < minScale)
					return;
				super.paint(aPaintContext);
			}
		};
		// s.setStroke(st);
		s.setStrokePaint(sp);
		s.setPickable(false);
		return s;
	}

	private final PNode handle;
	private BroomPromptModel model;
	private final PNode pie;
	private final PPath slider;
	private final float stickLength;
	private UndoRedoDocument undo;

	public BroomPromptSimple() {
		final boolean stickUp = false;
		final boolean bothSides = true;
		final int pieAngle = 150;
		final Color sp = Color.BLACK;
		final Color bgc = new Color(1, 1, 1, 0.5f);
		final Stroke fine = new BasicStroke(0.01f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER);
		final Stroke bold = new BasicStroke(0.03f, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_MITER);
		// final Font fo = new Font("SansSerif", Font.BOLD, 1);
		final float halo = RockProps.DEFAULT.getRadius();
		final float outer = 0.8f * RockProps.DEFAULT.getRadius();
		stickLength = (stickUp ? 1 : -1) * 5 * outer;
		final float inner = 0.5F * outer;
		setPickable(false);
		final BroomPromptSimple self = this;
		handle = new PNode() {
			private static final long serialVersionUID = -7641452321842902940L;

			/**
			 * Return true if this node or any pickable descendends are picked.
			 * If a pick occurs the pickPath is modified so that this node is
			 * always returned as the picked node, event if it was a decendent
			 * node that initialy reported the pick.
			 * 
			 * @see PComposite
			 */
			@Override
			public boolean fullPick(final PPickPath pickPath) {
				if (super.fullPick(pickPath)) {
					PNode picked = pickPath.getPickedNode();
					// this code won't work with internal cameras, because
					// it doesn't pop
					// the cameras view transform.
					while (picked != self) {
						pickPath.popTransform(picked
								.getTransformReference(false));
						pickPath.popNode(picked);
						picked = pickPath.getPickedNode();
					}
					return true;
				}
				return false;
			}
		};
		{ // opaque Background
			final PNode bg = node(new Arc2D.Float(-halo, -halo, 2 * halo,
					2 * halo, 0, 360, Arc2D.OPEN), null, null, scale0);
			bg.setPaint(bgc);
			bg.setPickable(true);
			handle.addChild(bg);
		}
		{ // Cross-hair circles and pie
			final int off = 90;
			final int pieOff = 180;
			// colored pie:
			pie = node(new Arc2D.Float(-outer, -outer, 2 * outer, 2 * outer,
					off - pieOff, pieAngle, Arc2D.PIE), null, null, scale0);
			handle.addChild(pie);
			// inner circle:
			handle.addChild(node(new Arc2D.Float(-inner, -inner, 2 * inner,
					2 * inner, off, pieOff + pieAngle, Arc2D.OPEN), fine, sp,
					50));
			// outer circle:
			handle.addChild(node(new Arc2D.Float(-outer, -outer, 2 * outer,
					2 * outer, off, pieOff + pieAngle - 12, Arc2D.OPEN), fine,
					sp, scale0));
			final double ar = Math.PI * (off + pieAngle) / 180.0;
			// radius
			// if (pieAngle % 90 != 0)
			handle.addChild(node(new Line2D.Double(0, 0, -outer * Math.cos(ar),
					outer * Math.sin(ar)), bold, sp, scale0));
			// arrow:
			final float f = outer / 10;
			final PPath s = node(createArrowHead(f, 3 * f, 0.5f * f), null,
					null, 50);
			s.setPaint(sp);
			s.translate(-outer * Math.cos(ar), outer * Math.sin(ar));
			s.rotate(Math.PI * (90 - off - pieAngle + 8) / 180.0);
			handle.addChild(s);
			this.addChild(handle);
		}
		{ // y-axis:
			handle.addChild(node(new Line2D.Float(0, -Math.signum(stickLength)
					* halo, 0, stickLength), fine, sp, scale0));
			// x-axis:
			handle.addChild(node(new Line2D.Float(-halo, 0, halo, 0), fine, sp,
					scale0));
		}
		{ // slider
			slider = new PPath(createSlider(0.4f * outer, bothSides), fine);
			slider.setStrokePaint(sp);
			slider.setPickable(true);
			this.addChild(slider);
		}
		// Set up Event handling
		addInputEventListener(new PDragEventHandler() {
			private StateEdit edit = null;
			/** double-click: flip handle */
			@Override
			public void mouseClicked(final PInputEvent arg0) {
				super.mouseClicked(arg0);
				if (arg0.getClickCount() > 1) {
					arg0.setHandled(true);
					getModel().setOutTurn(!getModel().getOutTurn());
				}
			}

			/** drag/move */
			@Override
			public void mouseDragged(final PInputEvent arg0) {
				arg0.setHandled(true);
				getModel().setBroom(
						arg0.getPositionRelativeTo(self.getParent()));
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
		});
		slider.addInputEventListener(new PDragEventHandler() {
			/** adjust the slider */
			@Override
			public void mouseDragged(final PInputEvent arg0) {
				arg0.setHandled(true);
				final Point2D p = arg0.getPositionRelativeTo(self);
				final BoundedRangeModel r = self.getModel()
						.getSplitTimeMillis();
				if (r == null)
					return;
				r.setValue(ratio2value(p.getY() / stickLength, r));
			}
		});
	}

	private void adjustSlider(final BoundedRangeModel r) {
		// log.info(r.getValue() + "/" + r.getMaximum());
		slider.setPaint(sliderColor(r));
		slider.getTransformReference(true).setToTranslation(0,
				stickLength * value2ratio(r));
		slider.invalidateFullBounds();
		slider.invalidatePaint();
		// FIXME getModel().firePropertyChange("splitTimeMillis", r, r);
	}

	public BroomPromptModel getModel() {
		return model;
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		log.debug(evt.getPropertyName());
		if ("broom".equals(evt.getPropertyName()))
			setBroom((Point2D) evt.getNewValue());
		else if ("idx16".equals(evt.getPropertyName()))
			setIdx16((Integer) evt.getNewValue());
		else if ("outTurn".equals(evt.getPropertyName()))
			setOutTurn((Boolean) evt.getNewValue());
		else if ("splitTimeMillis".equals(evt.getPropertyName())) {
			final BoundedRangeModel os = (BoundedRangeModel) evt.getOldValue();
			if (os != null)
				os.removeChangeListener(this);
			setSlider((BoundedRangeModel) evt.getNewValue());
		}
	}

	private int ratio2value(final double ra, final BoundedRangeModel r) {
		return r.getMaximum() + (int) ((r.getMinimum() - r.getMaximum()) * ra);
	}

	/** adjust position + rotation */
	private void setBroom(final Point2D b) {
		final AffineTransform t = getTransformReference(true);
		t.setToIdentity();
		t.translate(b.getX(), b.getY());
		MathVec.rotate(t, b.getX(), b.getY() - IceSize.FAR_HACK_2_TEE);
		MathVec.rotate(t, 0, 1);
		invalidateFullBounds();
		invalidatePaint();
	}

	/** adjust Color */
	private void setIdx16(final int i) {
		if (i < 0 || i >= RockSet.ROCKS_PER_SET)
			pie.setPaint(null);
		else if (i % 2 == 0)
			pie.setPaint(dark);
		else
			pie.setPaint(light);
		pie.invalidatePaint();
	}

	public void setModel(final BroomPromptModel model) {
		if (this.model != null) {
			this.model.removePropertyChangeListener(this);
			if (this.model.getSplitTimeMillis() != null)
				this.model.getSplitTimeMillis().removeChangeListener(this);
		}
		this.model = model;
		setVisible(this.model != null);
		if (this.model != null) {
			setBroom(this.model.getBroom());
			setIdx16(this.model.getIdx16());
			setOutTurn(this.model.getOutTurn());
			setSlider(this.model.getSplitTimeMillis());
			this.model.addPropertyChangeListener(this);
		}
	}

	private void setOutTurn(final boolean ot) {
		handle.getTransformReference(true).setToScale(ot ? -1 : 1, 1);
		handle.invalidatePaint();
	}

	private void setSlider(final BoundedRangeModel s) {
		slider.setVisible(s != null);
		if (s == null)
			return;
		s.addChangeListener(this);
		adjustSlider(s);
	}

	protected Color sliderColor(final BoundedRangeModel r) {
		return interpolateRGB(slow, fast, (double) (r.getValue() - r
				.getMaximum())
				/ (r.getMinimum() - r.getMaximum()));
	}

	public void stateChanged(final ChangeEvent e) {
		// log.info(e);
		if (e.getSource() instanceof BoundedRangeModel)
			adjustSlider((BoundedRangeModel) e.getSource());
	}

	private double value2ratio(final BoundedRangeModel r) {
		return (double) (r.getValue() - r.getMaximum())
				/ (r.getMinimum() - r.getMaximum());
	}

	public UndoRedoDocument getUndo() {
		return undo;
	}

	public void setUndo(UndoRedoDocument undo) {
		this.undo = undo;
	}
}