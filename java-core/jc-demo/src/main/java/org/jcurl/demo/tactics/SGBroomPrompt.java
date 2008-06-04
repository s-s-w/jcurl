/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.ChangeManager;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.math.MathVec;

import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGTransform;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;
import com.sun.scenario.scenegraph.SGTransform.Affine;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class SGBroomPrompt implements PropertyChangeListener, ChangeListener {

	private class MoveHandler extends MouseAdapter {}

	private class SpeedHandler extends MouseAdapter {}

	private static final Log log = JCLoggerFactory
			.getLogger(SGBroomPrompt.class);
	private static final double scale0 = 0;
	private static final int scale50 = 50;

	static SGShape node(final Shape sh, final Stroke st, final Paint sp,
			final double minScale) {
		final SGShape s = new SGShape();
		s.setShape(sh);
		if (sp != null)
			s.setDrawPaint(sp);
		if (st != null)
			s.setDrawStroke(st);
		s.setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
		if (st != null && sp != null)
			s.setMode(Mode.STROKE);
		else if (sp != null)
			s.setMode(Mode.FILL);
		return s;
	}

	/** adjust position + rotation */
	private static void syncBroomM2V(final Point2D b, final Affine scene) {
		if (b == null)
			return;
		final AffineTransform t = scene.getAffine();
		t.setToIdentity();
		t.translate(b.getX(), b.getY());
		MathVec.rotate(t, b.getX(), b.getY() - IceSize.FAR_HACK_2_TEE);
		MathVec.rotate(t, 0, 1);
		scene.setAffine(t);
	}

	private static void syncHandleM2V(final boolean outTurn, final Affine handle) {
		final AffineTransform t = handle.getAffine();
		t.setToIdentity();
		if (outTurn)
			t.scale(-1, 1);
		handle.setAffine(t);
	}

	private static void syncIndexM2V(final int i16, final SGShape pie) {
		if (pie != null)
			pie.setFillPaint(RockSet.isDark(i16) ? Color.RED : Color.YELLOW);
	}

	private ChangeManager changer;
	private final Affine handle;
	private BroomPromptModel model;
	private final SGShape pie;
	private final Affine scene;
	private final SGShape sli;
	private final Affine slider;
	private final float stickLength;

	public SGBroomPrompt() {
		// create the scene
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

		final SGGroup me = new SGGroup();
		// the transparent background
		{
			final SGShape bg = node(new Arc2D.Float(-halo, -halo, 2 * halo,
					2 * halo, 0, 360, Arc2D.OPEN), null, null, scale0);
			bg.setFillPaint(bgc);
			me.add(bg);
		}
		// the cross-hair and stick
		{
			final int off = 90;
			final int pieOff = 180;
			final int arrowLengthDegrees = 7;
			// colored pie:
			pie = node(new Arc2D.Float(-outer, -outer, 2 * outer, 2 * outer,
					off - pieOff, pieAngle, Arc2D.PIE), null, null, scale0);
			me.add(pie);
			// inner circle:
			me.add(node(new Arc2D.Float(-inner, -inner, 2 * inner, 2 * inner,
					off, pieOff + pieAngle, Arc2D.OPEN), fine, sp, scale50));
			// outer circle:
			me.add(node(new Arc2D.Float(-outer, -outer, 2 * outer, 2 * outer,
					off, pieOff + pieAngle - (14 + arrowLengthDegrees),
					Arc2D.OPEN), fine, sp, scale50));
			// Semantic zooming: me.add(node(new Arc2D.Float(-outer, -outer, 2 *
			// outer, 2 * outer,
			// off, pieOff + pieAngle, Arc2D.OPEN), fine, sp, -scale50));
			final double ar = Math.PI * (off + pieAngle) / 180.0;
			// radius
			// if (pieAngle % 90 != 0)
			me.add(node(new Line2D.Double(0, 0, -outer * Math.cos(ar), outer
					* Math.sin(ar)), bold, sp, scale0));

			// arrow:
			final float f = outer / 10;
			final SGShape s = node(IceShapes.createArrowHead(f, 3 * f,
					0.5f * f), null, null, scale50);
			s.setFillPaint(sp);
			final double a = Math.PI * (off + pieAngle - arrowLengthDegrees)
					/ 180.0;
			final AffineTransform a_ = new AffineTransform();
			a_.translate(-outer * Math.cos(a), outer * Math.sin(a));
			a_.rotate(Math.PI
					* (90 - (off + pieAngle) + 8 + arrowLengthDegrees) / 180.0);
			final Affine s_ = SGTransform.createAffine(a_, s);
			me.add(s_);
		}
		{ // y-axis:
			me.add(node(new Line2D.Float(0, -Math.signum(stickLength) * halo,
					0, stickLength), fine, sp, scale0));
			// x-axis:
			me.add(node(new Line2D.Float(-halo, 0, halo, 0), fine, sp, scale0));
		}
		{ // slider
			sli = new SGShape();
			sli.setShape(IceShapes
					.createSlider(0.4f * outer, bothSides));
			// sli.setFillPaint(sp);
			sli.setDrawStroke(fine);
			sli.setDrawPaint(sp);
			sli.setMode(Mode.STROKE_FILL);
			sli.setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
			me.add(slider = SGTransform
					.createAffine(new AffineTransform(), sli));
		}
		handle = SGTransform.createAffine(new AffineTransform(), me);
		scene = SGTransform.createAffine(new AffineTransform(), handle);
		scene.setVisible(false);
	}

	public ChangeManager getChanger() {
		return changer;
	}

	public BroomPromptModel getModel() {
		return model;
	}

	public SGNode getScene() {
		return scene;
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getSource() == model) {
			if ("idx16".equals(evt.getPropertyName()))
				syncIndexM2V((Integer) evt.getNewValue(), pie);
			else if ("outTurn".equals(evt.getPropertyName()))
				syncHandleM2V((Boolean) evt.getNewValue(), handle);
			else if ("broom".equals(evt.getPropertyName()))
				syncBroomM2V((Point2D) evt.getNewValue(), scene);
			else if ("splitTimeMillis".equals(evt.getPropertyName())) {
				final BoundedRangeModel os = (BoundedRangeModel) evt
						.getOldValue();
				if (os != null)
					os.removeChangeListener(this);
				final BoundedRangeModel ns = (BoundedRangeModel) evt
						.getNewValue();
				if (ns != null)
					ns.addChangeListener(this);
				syncSpeedM2V(ns);
			} else
				log.info(evt.getPropertyName() + " " + evt.getSource());
		} else
			log.warn("Unconsumed event from " + evt.getSource());
	}

	public void setChanger(final ChangeManager changer) {
		final ChangeManager old = this.changer;
		if (old == changer)
			return;
		this.changer = changer;
		// firePropertyChange("changer", old, this.changer);
	}

	public void setModel(final BroomPromptModel model) {
		if (this.model == model)
			return;
		if (this.model != null) {
			this.model.removePropertyChangeListener(this);
			if (this.model.getSplitTimeMillis() != null)
				this.model.getSplitTimeMillis().removeChangeListener(this);
		}
		this.model = model;
		scene
				.setVisible(true || this.model != null
						&& model.getBroom() != null);
		if (this.model != null) {
			// setBroom(this.model.getBroom());
			// setIdx16(this.model.getIdx16());
			// setOutTurn(this.model.getOutTurn());
			// setSlider(this.model.getSplitTimeMillis());
			this.model.addPropertyChangeListener(this);
			if (this.model.getSplitTimeMillis() != null)
				this.model.getSplitTimeMillis().addChangeListener(this);
		}
	}

	public void stateChanged(final ChangeEvent e) {
		if (e.getSource() instanceof BoundedRangeModel)
			syncSpeedM2V((BoundedRangeModel) e.getSource());
		else
			log.warn("Unconsumed event from " + e.getSource());
	}

	private void syncSpeedM2V(final BoundedRangeModel r) {
		// log.info(r.getValue() + "/" + r.getMaximum());
		sli.setFillPaint(IceShapes.sliderColor(r));
		final AffineTransform t = slider.getAffine();
		t.setToTranslation(0, stickLength * IceShapes.value2ratio(r));
		slider.setAffine(t);
	}
}
