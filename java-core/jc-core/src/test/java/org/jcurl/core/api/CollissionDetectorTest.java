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

package org.jcurl.core.api;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.impl.BisectionCollissionDetector;
import org.jcurl.core.impl.CurveRockAnalytic;
import org.jcurl.core.impl.CurveStill;
import org.jcurl.core.impl.CurveTransformed;
import org.jcurl.core.impl.NewtonCollissionDetector;
import org.jcurl.core.io.JCurlSerializer;
import org.jcurl.core.io.JCurlSerializerTest;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.Polynome;
import org.jcurl.math.R1RNFunction;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class CollissionDetectorTest extends TestCase {

	public void testHngrrr() {
		final CurveCombined<R1RNFunction> fa;
		final CurveCombined<R1RNFunction> fb;
		{
			fa = new CurveCombined<R1RNFunction>(3);
			final AffineTransform at = new AffineTransform(new double[] {
					-0.998789369167724, -0.04919142239801594,
					0.04919142239801594, -0.998789369167724, 0.0,
					38.40480041503906 });
			final Polynome x = new Polynome(new double[] { 0.0, 0.0, 0.0,
					1.9673717768986312E-4, -4.926076166542786E-6 });
			final Polynome y = new Polynome(new double[] { 0.0,
					2.948166586727712, -0.04921250210867988 });
			final Polynome a = new Polynome(new double[] { 0.0 });
			fa.add(0, new CurveTransformed<Pos>(new CurveRockAnalytic<Pos>(x,
					y, a), at, 0), false);
			fa.add(29.953431143708045,
					CurveStill.newInstance(0.8517897065718871,
							-5.760619285641198, -5.233506662186717), false);
			final String s0 = "[x>=0.0 f(x)=[-0.998789369167724, -0.04919142239801594, 0.04919142239801594, -0.998789369167724, 0.0, 38.40480041503906] [p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 + 1.9673717768986312E-4*x**3 + -4.926076166542786E-6*x**4, p(x) = 0.0*x**0 + 2.948166586727712*x**1 + -0.04921250210867988*x**2, ], x>=29.953431143708045 f(x)=[0.8517897065718871, -5.760619285641198, -5.233506662186717]]";
			final String s1 = "[x>=0.0 f(x)=[-0.998789369167724, -0.04919142239801594, 0.04919142239801594, -0.998789369167724, 0.0, 38.40480041503906] [p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 + 1.9673717768986312E-4*x**3 + -4.926076166542786E-6*x**4, p(x) = 0.0*x**0 + 2.948166586727712*x**1 + -0.04921250210867988*x**2, p(x) = 0.0*x**0], x>=29.953431143708045 f(x)=[0.8517897065718871, -5.760619285641198, -5.233506662186717]]";
			assertEquals(s1, fa.toString());

			fb = new CurveCombined<R1RNFunction>(3);
			fb.add(0, CurveStill.newInstance(-0.7620000243186951,
					-2.4384000301361084, 0.0), false);
			assertEquals(
					"[x>=0.0 f(x)=[-0.7620000243186951, -2.4384000301361084, 0.0]]",
					fb.toString());
		}

		// newton loops endless:
		{
			final NewtonCollissionDetector nc = new NewtonCollissionDetector();
			try {
				nc.compute(0, 60, fa, fb);
				fail("failure expected");
			} catch (final IllegalStateException e) {
				assertEquals(
						"Maxsteps overflow. dx=-4.087481512540596 x=25.324877688797812 f=org.jcurl.math.Distance2DSq([x>=0.0 f(x)=[-0.998789369167724, -0.04919142239801594, 0.04919142239801594, -0.998789369167724, 0.0, 38.40480041503906] [p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 + 1.9673717768986312E-4*x**3 + -4.926076166542786E-6*x**4, p(x) = 0.0*x**0 + 2.948166586727712*x**1 + -0.04921250210867988*x**2, p(x) = 0.0*x**0], x>=29.953431143708045 f(x)=[0.8517897065718871, -5.760619285641198, -5.233506662186717]], [x>=0.0 f(x)=[-0.7620000243186951, -2.4384000301361084, 0.0]])",
						e.getMessage());
			}
		}
		// bisection works ok:
		{
			final BisectionCollissionDetector nc = new BisectionCollissionDetector();
			assertEquals(Double.NaN, nc.compute(0, 30, fa, fb));
		}
	}

	public void testHngrrrDeSerialization() throws IOException {
		final URL u = JCurlSerializerTest.class
				.getResource("/setup/hngrrrr.jcz");
		assertNotNull(u);
		// try {
		new JCurlSerializer().read(u);
		// } catch (final IllegalStateException is) {
		// assertEquals(
		// "Maxsteps overflow. dx=-4.087481512540596 x=25.324877688797812
		// f=org.jcurl.math.Distance2DSq([x>=0.0 f(x)=[-0.998789369167724,
		// -0.04919142239801594, 0.04919142239801594, -0.998789369167724, 0.0,
		// 38.40480041503906] [p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2 +
		// 1.9673717768986312E-4*x**3 + -4.926076166542786E-6*x**4, p(x) =
		// 0.0*x**0 + 2.948166586727712*x**1 + -0.04921250210867988*x**2, ],
		// x>=29.953431143708045 f(x)=[0.8517897065718871, -5.760619285641198,
		// -5.233506662186717]], [x>=0.0 f(x)=[-0.7620000243186951,
		// -2.4384000301361084, 0.0]])",
		// is.getMessage());
		// }
	}
}
