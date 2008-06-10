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
package org.jcurl.core.impl;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.swing.TestShowBase;
import org.jcurl.core.ui.FixpointZoomer;
import org.jcurl.core.ui.Zoomer;
import org.jcurl.math.NewtonSimpleSolver;
import org.jcurl.math.R1RNFunction;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:SlideDennyTest.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CurlerDennyTest extends TestShowBase {

	private static final Point2D tee = new Point2D.Double(0, 0);

	public void _testDennyShow() throws InterruptedException {
		final CurveManager te = new CurveManager();
		te.setCollider(new CollissionSpin());
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerDenny(23, 1));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.FAR_HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		RockSet<Vel> tmp = null;
		te.setInitialSpeed(tmp = RockSet.allZero(null));
		if (false)
			te.getInitialSpeed().getDark(0).setLocation(-0.096,
					-te.getCurler().computeHackSpeed(3.124, tee), Math.PI / 2);
		else
			te.getInitialSpeed().getDark(0).setLocation(0, -2.455, Math.PI / 2);

		// with Display:
		showPositionDisplay(te.getCurrentPos(), FixpointZoomer.HOUSE, 10000,
				new TimeRunnable() {
					@Override
					public void run(final double t) throws InterruptedException {
						te.setCurrentTime(14 + t);
						Thread.sleep(1000 / 50);
					}
				});
		// showPaths(te.getCurveStore().iterator(), 0, 10);
	}

	private void showTrajectory(final CurveRock<Pos> p, final Zoomer zoom,
			final int millis, final int dt) {
		final RockSet<Pos> pos = RockSetUtils.allOut();
		showPositionDisplay(pos, zoom, millis, new TimeRunnable() {
			@Override
			public void run(final double t) throws InterruptedException {
				p.at(t, 0, pos.getRock(0));
				Thread.sleep(dt);
			}
		});
	}

	public void test01() {
		final CurlerDenny cu = new CurlerDenny(24, 1);

		final Point2D broom = new Point2D.Float(0, 0);
		final double split = 1.5;
		final double a0 = 0;
		final double omega0 = 0.25;

		double v0 = cu.computeHackSpeed(split, broom);
		assertEquals("5.592497947074459", "" + v0);

		final CurveRock<Pos> rc = cu.computeRc(a0, v0, omega0, 0);
		assertEquals(
				"[p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 + -3.731988101679819E-4*x**3 + 4.926076166542786E-6*x**4, p(x) = 0.0*x**0 + 5.592497947074459*x**1 + -0.04921250210867988*x**2, ]",
				rc.toString());

		double back = NewtonSimpleSolver.computeNewtonValue(rc, 1, 0,
				IceSize.HACK_2_BACK, 0, 10);
		assertEquals(0.32795595786008197, back);
		double hog = NewtonSimpleSolver.computeNewtonValue(rc, 1, 0,
				IceSize.HACK_2_HOG, 0, 10);
		assertEquals(1.5000000000000002, hog - back);

		final CurveTransformed<Pos> wc = new CurveTransformed<Pos>(rc, cu
				.hackRc2Wc(null, broom), 0);
		assertEquals(0, wc.at(0, 1, 0));
		assertEquals(-v0, wc.at(0, 1, 1));
		assertEquals("1.5000001333164878", "" + cu.computeIntervalTime(wc));

		back = NewtonSimpleSolver.computeNewtonValue(wc, 1, 0,
				IceSize.BACK_2_HOG + IceSize.FAR_HOG_2_TEE, -10, 10);
		hog = NewtonSimpleSolver.computeNewtonValue(wc, 1, 0,
				IceSize.FAR_HOG_2_TEE, back, 15);
		assertEquals("0.3279560003844706", "" + back);
		assertEquals("1.8279561337009578", "" + hog);
		assertEquals("1.5000001333164872", "" + (hog - back));
	}

	public void testComputeRc() {
		// straight ice:
		final CurlerNoCurl cs = new CurlerNoCurl(24, 0);
		final CurlerDenny cd = new CurlerDenny(24, 0);

		final CurveRock<Pos> cnc = cs.computeRc(0, 2.3, 1, 0);
		assertEquals(0.0, cnc.at(0, 0, 1));
		assertEquals("20.513399696350092", "" + cnc.at(12, 0, 1));
		assertEquals("26.853598785400386", "" + cnc.at(24, 0, 1));

		final CurveRock<Pos> cdc = cd.computeRc(0, 2.3, 1, 0);
		assertEquals(0.0, cdc.at(0, 0, 1));
		assertEquals("20.513399696350092", "" + cdc.at(12, 0, 1));
		assertEquals("26.853598785400386", "" + cdc.at(24, 0, 1));
	}

	public void testComputeRcPoly() {
		CurlerDenny s = new CurlerDenny();
		try {
			s.getDrawToTeeTime();
			fail("expected Exception");
		} catch (final NullPointerException e) {
			;
		}
		s = new CurlerDenny(24, 1);
		assertEquals(24, s.getDrawToTeeTime());
		assertEquals(1, s.getDrawToTeeCurl());

		final CurveRock<Pos> p = s.computeRc(0, s.computeHackSpeed(3.124,
				new Point2D.Float(0, 0)), Math.PI / 2, 0);
		final Rock<Pos> ret = new RockDouble<Pos>();
		try {
			p.at(0, 3, ret);
			fail("expected Exception");
		} catch (final IllegalArgumentException e) {
			;
		}
		p.at(0, 0, ret);
		assertEquals(0, ret.getX());
		assertEquals(0, ret.getY());
		assertEquals(0, ret.getA());
		p.at(0, 1, ret);
		assertEquals(0, ret.getX());
		if (false) {
			assertEquals(2.480575119409491, ret.getY());
			assertEquals(1.5707963267948966, ret.getA());
			p.at(0, 2, ret);
			assertEquals(0, ret.getX());
			assertEquals(-0.09842499759462145, ret.getY());
			assertEquals(-0.15565385905987525, ret.getA());

			p.at(10, 0, ret);
			assertEquals(-0.21342947331342377, ret.getX());
			assertEquals(19.88450131436384, ret.getY());
			assertEquals(9.387172187683756, ret.getA());
			p.at(10, 1, ret);
			assertEquals(-0.05498659662365676, ret.getX());
			assertEquals(1.4963251434632767, ret.getY());
			assertEquals(0.4445043723378003, ret.getA());
			p.at(10, 2, ret);
			assertEquals(-0.007380421176583205, ret.getX());
			assertEquals(-0.09842499759462145, ret.getY());
			assertEquals(-0.07302010654804114, ret.getA());

			p.at(20, 0, ret);
			assertEquals(-0.9840561568777606, ret.getX());
			assertEquals(29.926502869265533, ret.getY());
			assertEquals(11.273942229300644, ret.getA());
			p.at(20, 1, ret);
			assertEquals(-0.07527046056870113, ret.getX());
			assertEquals(0.5120751675170623, ret.getY());
			assertEquals(0.03053930825433586, ret.getA());
			p.at(20, 2, ret);
			assertEquals(0.00694054653572247, ret.getX());
			assertEquals(-0.09842499759462145, ret.getY());
			assertEquals(-0.014659456820910044, ret.getA());
		}
	}

	public void testCurveTrafo() {
		final CurlerDenny c = new CurlerDenny(24, 0);
		final double split = 3.75;
		// compute the initial speed:
		final double v0 = c.computeHackSpeed(split, tee);
		final CurveRock<Pos> rc = c.computeRc(Math.PI, v0, 1, 0);
		final CurveRock<Pos> wc = c.computeWc(tee, split, Math.PI, 1, 0);
		// are the curves aequivalent?
		for (int t = 0; t < 20; t += 1)
			assertEquals("t=" + t, rc.at(t, 0, 1), (IceSize.FAR_HACK_2_TEE - wc
					.at(t, 0, 1)), 1e-9);
	}

	public void testDrawToTeeRC() {
		final CurlerDenny c = new CurlerDenny(24, 1);
		final double tTee = CurlerBase.computeHackToTee(c);
		assertEquals(27.935408, tTee, 1e-6);
		final double v0 = CurlerBase.computeV0ToTee(c);
		assertEquals(2.7495427, v0, 1e-6);
		final CurveRock<Pos> rc = c.computeRc(0, v0, 1, 0);
		// start speed
		assertEquals(0.0, rc.at(0, 1, 0), 1e-6);
		assertEquals(v0, rc.at(0, 1, 1), 1e-6);
		// start location
		assertEquals(0.0, rc.at(0, 0, 0), 1e-6);
		assertEquals(0.0, rc.at(0, 0, 1), 1e-6);
		// stop speed
		assertEquals(0.0, rc.at(tTee, 1, 0), 1e-6);
		assertEquals(0.0, rc.at(tTee, 1, 1), 1e-6);
		// stop location
		assertEquals(-1.0, rc.at(tTee, 0, 0), 1e-6);
		assertEquals(IceSize.FAR_HACK_2_TEE, rc.at(tTee, 0, 1), 1e-6);
	}

	public void testDrawToTeeWC() {
		final CurlerDenny c = new CurlerDenny(24, 1);
		final double tTee = CurlerBase.computeHackToTee(c);
		assertEquals(27.9354089, tTee, 1e-6);
		final double tSplit = CurlerBase.computeSplitToTee(c);
		assertEquals(3.2621680, tSplit, 1e-6);
		assertEquals(1.9621307, IceSize.HOG_2_TEE / tSplit, 1e-6);
		final CurveRock<Pos> wc = c.computeWc(tee, tSplit, 0, 1, 0);
		// start speed
		assertEquals(0.0, wc.at(0, 1, 0), 1e-6);
		assertEquals(-2.7495427, wc.at(0, 1, 1), 1e-6);
		// start location
		assertEquals(0.0, wc.at(0, 0, 0), 1e-6);
		assertEquals(IceSize.FAR_HACK_2_TEE, wc.at(0, 0, 1), 1e-6);
		// stop speed
		assertEquals(0.0, wc.at(tTee, 1, 0), 1e-6);
		assertEquals(0.0, wc.at(tTee, 1, 1), 1e-6);
		// stop location
		assertEquals(1.0, wc.at(tTee, 0, 0), 1e-6);
		assertEquals(0.0, wc.at(tTee, 0, 1), 1e-6);
		{
			final CurveTransformed<Pos> ct = (CurveTransformed<Pos>) wc;
			final R1RNFunction rc = ct.getBase();
			// start speed
			assertEquals(0.0, rc.at(0, 1, 0), 1e-6);
			assertEquals(2.7495427, rc.at(0, 1, 1), 1e-6);
			// start location
			assertEquals(0.0, rc.at(0, 0, 0), 1e-6);
			assertEquals(0.0, rc.at(0, 0, 1), 1e-6);
			// stop speed
			assertEquals(0.0, rc.at(tTee, 1, 0), 1e-6);
			assertEquals(0.0, rc.at(tTee, 1, 1), 1e-6);
			// stop location
			assertEquals(-1.0, rc.at(tTee, 0, 0), 1e-6);
			assertEquals(IceSize.FAR_HACK_2_TEE, rc.at(tTee, 0, 1), 1e-6);
		}
	}

	public void testHackRc2Wc() {
		final CurlerBase cu = new CurlerNoCurl(24, 0);
		final Point2D broom = new Point2D.Float(0, 0);
		final AffineTransform tr = new AffineTransform();

		broom.setLocation(0, 0);
		double angle = Math.atan2(-broom.getX(), IceSize.FAR_HACK_2_TEE
				- broom.getY())
				+ Math.PI;
		assertEquals("3.141592653589793", "" + angle);
		cu.hackRc2Wc(tr, broom);
		assertEquals(1.0, tr.getDeterminant());
		assertEquals(
				"AffineTransform[[-1.0, -0.0, 0.0], [-0.0, -1.0, 38.40480041503906]]",
				tr.toString());

		broom.setLocation(1, 0);
		angle = Math
				.atan2(-broom.getX(), IceSize.FAR_HACK_2_TEE - broom.getY())
				+ Math.PI;
		assertEquals("3.115560124284562", "" + angle);
		cu.hackRc2Wc(tr, broom);
		assertEquals("1.0", "" + tr.getDeterminant());
		assertEquals(
				"AffineTransform[[-0.999661172844689, 0.026029589062862, 0.0], [-0.026029589062862, -0.999661172844689, 38.40480041503906]]",
				tr.toString());

		broom.setLocation(-1, 0);
		angle = Math
				.atan2(-broom.getX(), IceSize.FAR_HACK_2_TEE - broom.getY())
				+ Math.PI;
		assertEquals("3.167625182895024", "" + angle);
		cu.hackRc2Wc(tr, broom);
		assertEquals("1.0", "" + tr.getDeterminant());
		assertEquals(
				"AffineTransform[[-0.999661172844689, -0.026029589062862, 0.0], [0.026029589062862, -0.999661172844689, 38.40480041503906]]",
				tr.toString());
	}

	public void testHackSpeedConsistency() {
		final CurlerDenny c = new CurlerDenny(24, 1);
		final double split = 3.75;
		// compute the initial speed:
		final double v0 = c.computeHackSpeed(split, tee);
		assertEquals(2.453599211554087, v0);
		// check hack speed
		final Rock<Pos> r = c.computeWc(tee, split, Math.PI, 1, 0).at(0, 1,
				new RockDouble<Pos>());
		assertEquals(v0, r.p().distance(0, 0), 1e-6);
	}

	public void testHackToTeeRange() {
		assertEquals(17.4596305, CurlerBase.computeHackToTee(new CurlerDenny(
				15, 1)), 1e-6);
		assertEquals(23.2795074, CurlerBase.computeHackToTee(new CurlerDenny(
				20, 1)), 1e-6);
		assertEquals(27.9354089, CurlerBase.computeHackToTee(new CurlerDenny(
				24, 1)), 1e-6);
		assertEquals(32.5913104, CurlerBase.computeHackToTee(new CurlerDenny(
				28, 1)), 1e-6);
		assertEquals(Double.NaN, CurlerBase.computeHackToTee(new CurlerDenny(
				30, 1)), 1e-6);
	}

	public void testSplitTimeCorrectness() {
		final CurlerDenny c = new CurlerDenny(24, 0);
		final double split = 3.75;
		// compute the initial speed:
		final double v0 = c.computeHackSpeed(split, tee);
		assertEquals(2.453599211554087, v0);

		// compute the split time from a curve numerically:
		final CurveRock<Pos> rc = c.computeRc(Math.PI, v0, 1, 0);
		final double back = NewtonSimpleSolver.computeNewtonValue(rc, 1, 0,
				IceSize.HACK_2_BACK, 0, 10);
		final double hog = NewtonSimpleSolver.computeNewtonValue(rc, 1, 0,
				IceSize.HACK_2_HOG, 0, 10);
		assertEquals(split, hog - back);

		final CurveRock<Pos> wc = c.computeWc(tee, split, Math.PI, 1, 0);
		assertEquals("3.750000374248254", "" + c.computeIntervalTime(wc));
	}

	public void testSplitToTeeRange() {
		assertEquals(2.0388550, CurlerBase.computeSplitToTee(new CurlerDenny(
				15, 1)), 1e-6);
		assertEquals(2.7184733, CurlerBase.computeSplitToTee(new CurlerDenny(
				20, 1)), 1e-6);
		assertEquals(3.2621680, CurlerBase.computeSplitToTee(new CurlerDenny(
				24, 1)), 1e-6);
		assertEquals(3.8058627, CurlerBase.computeSplitToTee(new CurlerDenny(
				28, 1)), 1e-6);
		assertEquals(Double.NaN, CurlerBase.computeSplitToTee(new CurlerDenny(
				30, 1)), 1e-6);
	}

	public void testV0ToTeeRange() {
		assertEquals(4.3992683, CurlerBase
				.computeV0ToTee(new CurlerDenny(15, 1)), 1e-6);
		assertEquals(3.2994512, CurlerBase
				.computeV0ToTee(new CurlerDenny(20, 1)), 1e-6);
		assertEquals(2.7495427, CurlerBase
				.computeV0ToTee(new CurlerDenny(24, 1)), 1e-6);
		assertEquals(2.3567509, CurlerBase
				.computeV0ToTee(new CurlerDenny(28, 1)), 1e-6);
		assertEquals(Double.NaN, CurlerBase.computeV0ToTee(new CurlerDenny(30,
				1)), 1e-6);
	}
}