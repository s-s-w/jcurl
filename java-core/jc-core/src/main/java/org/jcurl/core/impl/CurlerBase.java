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
package org.jcurl.core.impl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jcurl.core.api.Curler;
import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.math.BisectionSolver;
import org.jcurl.math.NewtonSimpleSolver;
import org.jcurl.math.R1R1Function;

/**
 * Base implementation for {@link Curler}s.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:CurlerBase.java 780 2008-03-18 11:06:30Z mrohrmoser $
 */
public abstract class CurlerBase extends PropModelImpl implements Curler {

	private static final Point2D teeWc = new Point2D.Double(0, 0);

	public static double computeHackToTee(final Curler cu) {
		if (true) {
			final double v0 = computeV0ToTee(cu);
			final CurveRock<Pos> rc = cu.computeRc(0, v0, 1, 0);
			return new NewtonStopDetector().compute(rc, 15, 60);
		} else {
			final double splitTime = computeSplitToTee(cu);
			final CurveRock<Pos> wc = cu.computeWc(teeWc, splitTime, 0, 1, 0);
			return new NewtonStopDetector().compute(wc, 20, 60);
		}
	}

	public static double computeSplitToTee(final Curler cu) {
		final NewtonStopDetector s = new NewtonStopDetector();
		// running distance (y) 'til stop:
		final R1R1Function f = new R1R1Function() {
			private static final long serialVersionUID = 5450796623114903424L;

			@Override
			public double at(int c, double splitTime) {
				final CurveRock<Pos> wc = cu.computeWc(teeWc, splitTime, 0, 1,
						0);
				return wc.at(1, 0, s.compute(wc, 0, 60));
			}
		};
		return BisectionSolver.findRoot(f, 0, 2, 6);
	}

	public static double computeV0ToTee(final Curler cu) {
		final NewtonStopDetector s = new NewtonStopDetector();
		// running distance (y) 'til stop:
		final R1R1Function f = new R1R1Function() {
			private static final long serialVersionUID = -4702648935820079700L;

			@Override
			public double at(int c, double v0) {
				final CurveRock<Pos> rc = cu.computeRc(0, v0, 1, 0);
				return rc.at(1, 0, s.compute(rc, 0, 60)) - IceSize.FAR_HACK_2_TEE;
			}
		};
		return BisectionSolver.findRoot(f, 0, 1, 7);
	}

	public double computeIntervalTime(final CurveRock<Pos> wc) {
		final double back = NewtonSimpleSolver.computeNewtonValue(wc, 1, 0,
				IceSize.BACK_2_HOG + IceSize.FAR_HOG_2_TEE, 0, 10);
		final double hog = NewtonSimpleSolver.computeNewtonValue(wc, 1, 0,
				IceSize.FAR_HOG_2_TEE, back, 15);
		return hog - back;
	}

	public CurveRock<Pos> computeWc(final Point2D broom, final double split,
			final double a0, final double omega0, final double sweepFactor) {
		return new CurveTransformed<Pos>(computeRc(a0, computeHackSpeed(split,
				broom), omega0, sweepFactor), hackRc2Wc(null, broom), 0);
	}

	/**
	 * Compute the RC-&gt;WC transformation for a rock from the start (hack).
	 * 
	 * @param ret
	 *            <code>null</code> creates a new one.
	 * @param broomX
	 *            (world coordinates)
	 * @param broomY
	 *            (world coordinates)
	 * @return the transformation matrix
	 * @see CurveTransformed#createRc2Wc(AffineTransform, double, double,
	 *      double, double)
	 */
	public AffineTransform hackRc2Wc(final AffineTransform ret,
			final double broomX, final double broomY) {
		return CurveTransformed.createRc2Wc(ret, 0, IceSize.FAR_HACK_2_TEE,
				broomX, broomY - IceSize.FAR_HACK_2_TEE);
	}

	/**
	 * Compute the RC-&gt;WC transformation for a rock from the start (hack).
	 * <p>
	 * Convenience wrapper for
	 * {@link #hackRc2Wc(AffineTransform, double, double)}.
	 * </p>
	 * 
	 * @param ret
	 *            <code>null</code> creates a new one.
	 * @param broom
	 *            (world coordinates)
	 * @return the transformation matrix
	 * @see #hackRc2Wc(AffineTransform, double, double)
	 */
	public AffineTransform hackRc2Wc(final AffineTransform ret,
			final Point2D broom) {
		return hackRc2Wc(ret, broom.getX(), broom.getY());
	}
}
