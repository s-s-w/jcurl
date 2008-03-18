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

import org.jcurl.core.api.CurveRock;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.PositionSet;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.VelocitySet;
import org.jcurl.core.impl.CollissionSpin;
import org.jcurl.core.impl.CurlerDenny;
import org.jcurl.core.impl.CurlerNoCurl;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.impl.NewtonCollissionDetector;
import org.jcurl.core.swing.TestShowBase;
import org.jcurl.core.ui.FixpointZoomer;
import org.jcurl.core.ui.Zoomer;
import org.jcurl.math.NewtonSimpleSolver;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SlideDennyTest.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CurlerDennyTest extends TestShowBase {

	private static final Point2D tee = new Point2D.Double(0, 0);

	public void _testDennyShow() throws InterruptedException {
		final CurveManager te = new CurveManager();
		te.setCollider(new CollissionSpin());
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerDenny(23, 1));
		te.setInitialPos(PositionSet.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.FAR_HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		te.setInitialSpeed(new VelocitySet(PositionSet.allHome()));
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

	private void showTrajectory(final CurveRock p, final Zoomer zoom,
			final int millis, final int dt) {
		final PositionSet pos = PositionSet.allOut();
		showPositionDisplay(pos, zoom, millis, new TimeRunnable() {
			@Override
			public void run(final double t) throws InterruptedException {
				p.at(0, t, pos.getRock(0));
				pos.fireStateChanged();
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

		final CurveRock rc = cu.computeRc(a0, v0, omega0, 0);
		assertEquals(
				"[p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 + -6.850392794405297E-4*x**3 + 9.042246587224388E-6*x**4, p(x) = 0.0*x**0 + 5.592497947074459*x**1 + -0.04921250210867988*x**2, ]",
				rc.toString());

		double back = NewtonSimpleSolver.computeNewtonValue(rc, 1, 0,
				IceSize.HACK_2_BACK, 0, 10);
		assertEquals(0.32795595786008197, back);
		double hog = NewtonSimpleSolver.computeNewtonValue(rc, 1, 0,
				IceSize.HACK_2_HOG, 0, 10);
		assertEquals(1.5000000000000002, hog - back);

		final CurveTransformed wc = new CurveTransformed(rc, cu.hackRc2Wc(null,
				broom), 0);
		assertEquals(0, wc.at(0, 1, 0));
		assertEquals(-v0, wc.at(1, 1, 0));
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

		final CurveRock cnc = cs.computeRc(0, 2.3, 1, 0);
		assertEquals(0.0, cnc.at(1, 0, 0));
		assertEquals("20.513399696350092", "" + cnc.at(1, 0, 12));
		assertEquals("26.853598785400386", "" + cnc.at(1, 0, 24));

		final CurveRock cdc = cd.computeRc(0, 2.3, 1, 0);
		assertEquals(0.0, cdc.at(1, 0, 0));
		assertEquals("20.513399696350092", "" + cdc.at(1, 0, 12));
		assertEquals("26.853598785400386", "" + cdc.at(1, 0, 24));
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

		final CurveRock p = s.computeRc(0, s.computeHackSpeed(3.124,
				new Point2D.Float(0, 0)), Math.PI / 2, 0);
		final Rock ret = new RockDouble();
		try {
			p.at(3, 0, ret);
			fail("expected Exception");
		} catch (final IllegalArgumentException e) {
			;
		}
		p.at(0, 0, ret);
		assertEquals(0, ret.getX());
		assertEquals(0, ret.getY());
		assertEquals(0, ret.getA());
		p.at(1, 0, ret);
		assertEquals(0, ret.getX());
		if (false) {
			assertEquals(2.480575119409491, ret.getY());
			assertEquals(1.5707963267948966, ret.getA());
			p.at(2, 0, ret);
			assertEquals(0, ret.getX());
			assertEquals(-0.09842499759462145, ret.getY());
			assertEquals(-0.15565385905987525, ret.getA());

			p.at(0, 10, ret);
			assertEquals(-0.21342947331342377, ret.getX());
			assertEquals(19.88450131436384, ret.getY());
			assertEquals(9.387172187683756, ret.getA());
			p.at(1, 10, ret);
			assertEquals(-0.05498659662365676, ret.getX());
			assertEquals(1.4963251434632767, ret.getY());
			assertEquals(0.4445043723378003, ret.getA());
			p.at(2, 10, ret);
			assertEquals(-0.007380421176583205, ret.getX());
			assertEquals(-0.09842499759462145, ret.getY());
			assertEquals(-0.07302010654804114, ret.getA());

			p.at(0, 20, ret);
			assertEquals(-0.9840561568777606, ret.getX());
			assertEquals(29.926502869265533, ret.getY());
			assertEquals(11.273942229300644, ret.getA());
			p.at(1, 20, ret);
			assertEquals(-0.07527046056870113, ret.getX());
			assertEquals(0.5120751675170623, ret.getY());
			assertEquals(0.03053930825433586, ret.getA());
			p.at(2, 20, ret);
			assertEquals(0.00694054653572247, ret.getX());
			assertEquals(-0.09842499759462145, ret.getY());
			assertEquals(-0.014659456820910044, ret.getA());
		}
	}

	public void testComputeWc() {
		final double split = 1.5;
		final double a0 = 0;
		final double omega0 = 0.25;
		final CurlerDenny cu = new CurlerDenny(24, 1);

		final Point2D broom = new Point2D.Float(0, 0);
		CurveRock wc = cu.computeWc(broom, split, a0, omega0, 0);
		assertEquals(
				"[-1.0, -0.0, -0.0, -1.0, 0.0, 38.40480041503906] [p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 + -6.850392794405297E-4*x**3 + 9.042246587224388E-6*x**4, p(x) = 0.0*x**0 + 5.592497947074459*x**1 + -0.04921250210867988*x**2, ]",
				wc.toString());
		assertEquals("6.469982595262923", "" + wc.at(0, 0, 24));
		assertEquals("-67.46874910014833", "" + wc.at(1, 0, 24));
		assertEquals("1.5000001333164878", "" + cu.computeIntervalTime(wc));

		broom.setLocation(-1, 0);
		wc = cu.computeWc(broom, split, a0, omega0, 0);
		assertEquals(
				"[-0.9996611728446887, 0.026029589062861944, -0.026029589062861944, -0.9996611728446887, 0.0, 38.40480041503906] [p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 + -6.852670846175462E-4*x**3 + 9.042246587224388E-6*x**4, p(x) = 0.0*x**0 + 5.594357694424769*x**1 + -0.04921250210867988*x**2, ]",
				wc.toString());
		assertEquals("3.713931711675212", "" + wc.at(0, 0, 24));
		assertEquals("-67.64598803978889", "" + wc.at(1, 0, 24));
		assertEquals("1.4999806032643561", "" + cu.computeIntervalTime(wc));

		broom.setLocation(1, 0);
		wc = cu.computeWc(broom, split, a0, omega0, 0);
		assertEquals(
				"[-0.9996611728446887, -0.026029589062861846, 0.026029589062861846, -0.9996611728446887, 0.0, 38.40480041503906] [p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 + -6.852670846175462E-4*x**3 + 9.042246587224388E-6*x**4, p(x) = 0.0*x**0 + 5.594357694424769*x**1 + -0.04921250210867988*x**2, ]",
				wc.toString());
		assertEquals("9.227945290734878", "" + wc.at(0, 0, 24));
		assertEquals("-67.30900211973339", "" + wc.at(1, 0, 24));
		assertEquals("1.500019664629486", "" + cu.computeIntervalTime(wc));
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
}