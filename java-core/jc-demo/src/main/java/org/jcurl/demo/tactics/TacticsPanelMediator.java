/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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

import java.awt.geom.Point2D;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.MathVec;

/**
 * Mediates {@link CurveManager} and {@link BroomPromptModel} under a
 * convenient, stateless, high-level interface.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
class TacticsPanelMediator {
	private static final Log log = JCLoggerFactory
			.getLogger(TacticsPanelMediator.class);

	private static <R extends RockType> void swap(final RockSet<R> r,
			final int a, final int b) {
		final Rock<R> tmp = new RockDouble<R>(r.getRock(a));
		r.getRock(a).setLocation(r.getRock(b));
		r.getRock(b).setLocation(tmp);
	}

	private void recomputeCurves(final CurveManager cm,
			final BroomPromptModel prompt, final Point2D newPos,
			final long splitTimeMillis, final boolean outTurn) {
		if (cm == null)
			return;
		// set to initial pos
		final Rock start = new RockDouble(0, IceSize.FAR_HACK_2_TEE, Math.PI);
		cm.getInitialPos().getRock(prompt.getIdx16()).setLocation(start);

		// Compute direction
		MathVec.sub(newPos, start.p(), start.p());
		MathVec.norm(start.p(), start.p());

		// Compute initial Speed
		final double v0 = cm.getCurler().computeHackSpeed(
				splitTimeMillis * 1e-3, newPos);
		MathVec.mult(v0, start.p(), start.p());

		// Handle
		start.setA((outTurn ? 1 : -1) * 0.25);
		cm.getInitialSpeed().getRock(prompt.getIdx16()).setLocation(start);

		// Call the CurveManager to update it's curves.
		cm.stateChanged(null);
	}

	/** Initialise the broom. */
	public BroomPromptModel setCm(final CurveManager cm, BroomPromptModel prompt) {
		if (cm == null)
			return null;
		if (prompt == null)
			prompt = new BroomPromptModel();
		final int idx16;
		if (true || prompt.getIdx16() < 0) {
			// Try to find the running rock.
			// 1st try: if there's only one rock running, that's it.
			int tmp = -1;
			for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
				if (cm.getInitialSpeed().getRock(i).isNotZero()) {
					if (tmp >= 0)
						log.error("Multiple running rocks: " + tmp + i);
					tmp = i;
					break;
				}
			idx16 = tmp;
		} else
			idx16 = prompt.getIdx16();
		release(cm.getInitialPos().getRock(idx16));
		final Rock<Vel> v = cm.getInitialSpeed().getRock(idx16);

		// Compute Handle
		prompt.setOutTurn(v.getA() >= 0);

		// Compute Broom Location
		final Point2D x = cm.getInitialPos().getRock(idx16).p();
		final Point2D b = prompt.getBroom();
		final Point2D b2 = new Point2D.Double((b.getY() - x.getY()) * v.getX()
				/ v.getY(), b.getY());
		log.info("v=" + v + ", x=" + b2);
		prompt.setBroom(b2);

		// Compute Split Time
		final CurveCombined<CurveRock<Pos>> c1 = (CurveCombined<CurveRock<Pos>>) cm
				.getCurveStore().getCurve(idx16);
		prompt.getSplitTimeMillis().setValue(
				(int) (1000 * cm.getCurler().computeIntervalTime(c1.first())));
		if (prompt.getIdx16() < 0)
			prompt.setIdx16(idx16);
		return prompt;
	}

	public void updateBroom(final Point2D newPos, final CurveManager cm,
			final BroomPromptModel prompt) {
		recomputeCurves(cm, prompt, newPos, prompt.getSplitTimeMillis()
				.getValue(), prompt.getOutTurn());
	}

	public void updateIndex(final int i16, final CurveManager cm,
			final BroomPromptModel prompt) {
		if (i16 == prompt.getIdx16())
			return;
		log.debug("");
		// initial pos
		swap(cm.getInitialPos(), prompt.getIdx16(), i16);
		release(cm.getInitialPos().getRock(i16));
		// initial speed
		swap(cm.getInitialSpeed(), prompt.getIdx16(), i16);
	}

	public static void release(Rock<Pos> r) {
		r.setLocation(0, IceSize.FAR_HACK_2_TEE, Math.PI);
	}

	public void updatePos(final int i16, final Point2D newPos,
			final CurveManager cm) {
		log.debug("");
		cm.getInitialPos().getRock(i16).p().setLocation(newPos);
		cm.stateChanged(null);
	}

	public void updateSplitTimeMillis(final CurveManager cm,
			final BroomPromptModel prompt, final long newSplit) {
		recomputeCurves(cm, prompt, prompt.getBroom(), newSplit, prompt
				.getOutTurn());
	}

	public void updateTurn(final boolean outTurn,
			final BroomPromptModel prompt, final CurveManager cm) {
		recomputeCurves(cm, prompt, prompt.getBroom(), prompt
				.getSplitTimeMillis().getValue(), outTurn);
	}
}
