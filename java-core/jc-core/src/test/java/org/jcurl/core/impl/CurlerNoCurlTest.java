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
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.swing.TestShowBase;
import org.jcurl.core.ui.FixpointZoomer;
import org.jcurl.core.ui.Zoomer;
import org.jcurl.math.NewtonSimpleSolver;
import org.jcurl.math.Polynome;

public class CurlerNoCurlTest extends TestShowBase {

	void showTrajectory(final Polynome[] p, final Zoomer zoom,
			final int millis, final int dt) {
		final RockSet<Pos> pos = PositionSet.allHome();
		showPositionDisplay(pos, zoom, millis, new TimeRunnable() {
			@Override
			public void run(final double t) throws InterruptedException {
				pos.getRock(0).setLocation(p[0].at(t), p[1].at(t), p[2].at(t));
				pos.fireStateChanged();
				Thread.sleep(dt);
			}
		});
	}

	public void testSplitTime() {
		CurlerNoCurl c = new CurlerNoCurl(24, 0);
		double split = 3.75;
		// compute the initial speed:
		assertEquals(2.453599211554087, c.computeHackSpeed(split, tee));

		// compute the split time from a curve numerically:
		CurveRock rc = c.computeRc(Math.PI, 2.453599211554087, 1, 0);
		double back = NewtonSimpleSolver.computeNewtonValue(rc, 1, 0,
				IceSize.HACK_2_BACK, 0, 10);
		double hog = NewtonSimpleSolver.computeNewtonValue(rc, 1, 0,
				IceSize.HACK_2_HOG, 0, 10);
		assertEquals(split, hog - back);

		CurveRock wc = c.computeWc(tee, split, Math.PI, 1, 0);
		assertEquals("3.750000374248254", "" + c.computeIntervalTime(wc));

		for (int t = 0; t < 20; t += 1)
			assertEquals("t=" + t, rc.at(1, 0, t), (IceSize.FAR_HACK_2_TEE - wc
					.at(1, 0, t)), 1e-9);

	}

	// public void testBeta() {
	// assertEquals(0.0980844266686885, new CurlerNoCurl(17, 0).beta);
	// assertEquals(0.0535848758171096, new CurlerNoCurl(23, 0).beta);
	// assertEquals(0.04193254335392156, new CurlerNoCurl(26, 0).beta);
	// }

	public void testComputeRcPoly() {
		CurlerNoCurl s = new CurlerNoCurl(17, 0);
		Polynome[] p = s.computeRcPoly(Math.PI, 2, 0.2, 0);
		showTrajectory(p, FixpointZoomer.HOUSE, 5000, 40);
		assertEquals("p(x) = 0.0*x**0", p[0] + "");
		assertEquals("p(x) = 0.0*x**0 + 2.0*x**1 + -0.09808443326851077*x**2",
				p[1] + "");
		assertEquals("p(x) = 3.141592653589793*x**0 + 0.2*x**1", p[2] + "");
		assertEquals(0, p[0].at(0));
		assertEquals(0, p[1].at(0));
		assertEquals(Math.PI, p[2].at(0));
		assertEquals(0, p[0].at(17));
		assertEquals("5.65359878540039", "" + p[1].at(17));
		assertEquals("6.5415926535897935", "" + p[2].at(17));

		p = s.computeRcPoly(Math.PI, 3, 0.2, 0);
		assertEquals(0, p[0].at(0));
		assertEquals(0, p[1].at(0));
		assertEquals(Math.PI, p[2].at(0));
		assertEquals(0, p[0].at(17));
		assertEquals("22.65359878540039", "" + p[1].at(17));
		assertEquals("6.5415926535897935", "" + p[2].at(17));

		s = new CurlerNoCurl(23, 0);
		p = s.computeRcPoly(Math.PI, 2, 0.2, 0);
		assertEquals(0, p[0].at(0));
		assertEquals(0, p[1].at(0));
		assertEquals(Math.PI, p[2].at(0));
		assertEquals(0, p[0].at(23));
		assertEquals("17.653598785400394", ""+p[1].at(23));
		assertEquals("7.741592653589794",""+ p[2].at(23));

		p = s.computeRcPoly(Math.PI, 3, 0.2, 0);
		assertEquals(0, p[0].at(0));
		assertEquals(0, p[1].at(0));
		assertEquals(Math.PI, p[2].at(0));
		assertEquals(0, p[0].at(23));
		assertEquals("40.6535987854004","" +p[1].at(23));
		assertEquals("7.741592653589794",""+ p[2].at(23));
	}

	private static final Point2D tee = new Point2D.Double(0, 0);

	public void testComputeV0() {
		CurlerNoCurl s = new CurlerNoCurl(17, 0);
		// for (int i = 1; i <= 6; i++)
		// System.out.println(s.computeHackSpeed(i, tee));
		assertEquals("8.370653405496135", "" + s.computeHackSpeed(1, tee));
		assertEquals("4.393399665848348", "" + s.computeHackSpeed(2, tee));
		assertEquals("3.153352211097335", "" + s.computeHackSpeed(3, tee));
		assertEquals("2.592049850343837", "" + s.computeHackSpeed(4, tee));
		assertEquals("2.2981438698953243", "" + s.computeHackSpeed(5, tee));
		assertEquals("2.1353044837766393", "" + s.computeHackSpeed(6, tee));

		s = new CurlerNoCurl(23, 0);
		// for (int i = 1; i <= 6; i++)
		// System.out.println(s.computeHackSpeed(i, tee));
		assertEquals("8.306812807257796", "" + s.computeHackSpeed(1, tee));
		assertEquals("4.26813935497822", "" + s.computeHackSpeed(2, tee));
		assertEquals("2.970679554097963", "" + s.computeHackSpeed(3, tee));
		assertEquals("2.3564347551635496", "" + s.computeHackSpeed(4, tee));
		assertEquals("2.0136495784249293", "" + s.computeHackSpeed(5, tee));
		assertEquals("1.8051601680112996", "" + s.computeHackSpeed(6, tee));

		s = new CurlerNoCurl(26, 0);
		// for (int i = 1; i <= 6; i++)
		// System.out.println(s.computeHackSpeed(i, tee));
		assertEquals("8.290054208487767", "" + s.computeHackSpeed(1, tee));
		assertEquals("4.2350365188195855", "" + s.computeHackSpeed(2, tee));
		assertEquals("2.9219672775353516", "" + s.computeHackSpeed(3, tee));
		assertEquals("2.293021915960784", "" + s.computeHackSpeed(4, tee));
		assertEquals("1.9364742106619368", "" + s.computeHackSpeed(5, tee));
		assertEquals("1.7150824955169333", "" + s.computeHackSpeed(6, tee));
	}

	public void testDrawToTeeV0() {
	// assertEquals(3.334870506735409, new CurlerNoCurl(17, 0).drawToTeeV0);
	// assertEquals(2.4649042875870415, new CurlerNoCurl(23, 0).drawToTeeV0);
	// assertEquals(2.180492254403921, new CurlerNoCurl(26, 0).drawToTeeV0);
	}

	public void testReleaseRc2Wc() {
		assertEquals(0, AffineTransform.TYPE_IDENTITY);
		assertEquals(1, AffineTransform.TYPE_TRANSLATION);
		assertEquals(2, AffineTransform.TYPE_UNIFORM_SCALE);
		assertEquals(4, AffineTransform.TYPE_GENERAL_SCALE);
		assertEquals(6, AffineTransform.TYPE_MASK_SCALE);
		assertEquals(8, AffineTransform.TYPE_QUADRANT_ROTATION);
		assertEquals(16, AffineTransform.TYPE_GENERAL_ROTATION);
		assertEquals(24, AffineTransform.TYPE_MASK_ROTATION);
		assertEquals(32, AffineTransform.TYPE_GENERAL_TRANSFORM);
		assertEquals(64, AffineTransform.TYPE_FLIP);
		final CurlerNoCurl s = new CurlerNoCurl(23, 0);
		final AffineTransform m = new AffineTransform();
		final Point2D x = new Point2D.Double();
		s.hackRc2Wc(m, 0, 0);
		assertEquals(
				"AffineTransform[[-1.0, -0.0, 0.0], [-0.0, -1.0, 38.40480041503906]]",
				m.toString());
		assertEquals(AffineTransform.TYPE_TRANSLATION
				| AffineTransform.TYPE_QUADRANT_ROTATION, m.getType());
		x.setLocation(0.5, 1);
		m.transform(x, x);
		assertEquals(-0.5, x.getX());
		assertEquals("9.058401107788086", ""
				+ (x.getY() - IceSize.FAR_HOG_2_TEE));

		s.hackRc2Wc(m, Unit.f2m(14), 0);
		// System.out.println(new AffineTransform(
		// new double[] { 1, 2, 3, 4, 5, 6 })
		// + "\n" + m);
		assertEquals(
				"AffineTransform[[-0.993883734824108, 0.110431524720441, 0.0], [-0.110431524720441, -0.993883734824108, 38.40480041503906]]",
				m.toString());
		x.setLocation(0.5, 1);
		m.transform(x, x);
		assertEquals("0.11348965730838645", "" + (x.getX() + 0.5));
		assertEquals("10.009301610603757", ""
				+ (x.getY() - IceSize.FAR_HOG_2_TEE + 1));

		s.hackRc2Wc(m, Unit.f2m(-7), 0);
		// System.out.println(new AffineTransform(
		// new double[] { 1, 2, 3, 4, 5, 6 })
		// + "\n" + m);
		assertEquals(
				"AffineTransform[[-0.998460353243557, -0.055470018935922, 0.0], [0.055470018935922, -0.998460353243557, 38.40480041503906]]",
				m.toString());
		x.setLocation(0.5, 1);
		m.transform(x, x);
		assertEquals("-1.0547001955577002", "" + (x.getX() - 0.5));
		assertEquals("10.087675764012488", ""
				+ (x.getY() - IceSize.FAR_HOG_2_TEE + 1));
	}
}
