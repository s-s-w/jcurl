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
package org.jcurl.math;

import java.awt.geom.AffineTransform;

import junit.framework.TestCase;

import org.jcurl.core.impl.CurveRockAnalytic;
import org.jcurl.core.impl.CurveTransformed;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveShapeTest extends TestCase {

	public void testAequidistantSections() {
		double[] d = new double[0];
		CurveShape.linearSections(1, 2, d);

		d = new double[1];
		CurveShape.linearSections(1, 2, d);
		assertEquals(1, d[0], 1e-9);
		CurveShape.linearSections(2, 1, d);
		assertEquals(1, d[0], 1e-9);

		d = new double[2];
		CurveShape.linearSections(1, 2, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(2, d[1], 1e-9);
		CurveShape.linearSections(2, 1, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(2, d[1], 1e-9);

		d = new double[3];
		CurveShape.linearSections(1, 3, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(2, d[1], 1e-9);
		assertEquals(3, d[2], 1e-9);
		CurveShape.linearSections(3, 1, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(2, d[1], 1e-9);
		assertEquals(3, d[2], 1e-9);
	}

	public void testComputeControlPoint() {
		final double[] pa = { 0, 0 };
		final double[] pb = { 1, 0 };
		final double[] va = { 1, 1 };
		final double[] vb = { 1, -1 };
		final double[][] tmp_a = { { 0, 0 }, { 0, 0 } };
		final double[] tmp_b = { 0, 0 };
		final double[] pc = { 0, 0 };

		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(0.5, pc[0], 1e-9);
		assertEquals(0.5, pc[1], 1e-9);

		// FIXME the following Control Points are wrong:
		pa[0] = 0.1;
		pa[1] = 1.8287999629974365;
		va[0] = 0.0;
		va[1] = 0.0;
		pb[0] = 0.1;
		pb[1] = 1.8287999629974365;
		vb[0] = 0.0;
		vb[1] = 0.0;
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(pa[0], pc[0], 1e-9);
		assertEquals(pa[1], pc[1], 1e-9);

		pa[0] = 0.1;
		pa[1] = 1.8287999629974365;
		va[0] = 0.0;
		va[1] = 0.0;
		pb[0] = 0.1;
		pb[1] = 1.8287999629974365;
		vb[0] = 0.0;
		vb[1] = 0.0;
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(pa[0], pc[0], 1e-9);
		assertEquals(pa[1], pc[1], 1e-9);

		pa[0] = 0.1;
		pa[1] = 1.8287999629974365;
		va[0] = 0.0;
		va[1] = 0.0;
		pb[0] = 0.1;
		pb[1] = 1.8287999629974365;
		vb[0] = 0.0;
		vb[1] = 0.0;
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(pa[0], pc[0], 1e-9);
		assertEquals(pa[1], pc[1], 1e-9);

		pa[0] = 0.1;
		pa[1] = 1.8287999629974365;
		va[0] = 0.24830512483441886;
		va[1] = -0.9703039234061694;
		pb[0] = 0.8018748099036024;
		pb[1] = -0.9139218484978673;
		vb[0] = 0.16553674989608172;
		vb[1] = -0.6468692822960592;
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(0.1, pc[0], 1e-9);
		assertEquals(1.8287999629974365, pc[1], 1e-9);

		pa[0] = 0.8018748099036024;
		pa[1] = -0.9139218484978673;
		va[0] = 0.16553674989608172;
		va[1] = -0.6468692822960592;
		pb[0] = 1.2229996958720961;
		pb[1] = -2.559554935497948;
		vb[0] = 0.08276837495774463;
		vb[1] = -0.32343464118594906;
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(Double.POSITIVE_INFINITY, pc[0], 1e-9);
		assertEquals(Double.NEGATIVE_INFINITY, pc[1], 1e-9);

		pa[0] = 1.2229996958720961;
		pa[1] = -2.559554935497948;
		va[0] = 0.08276837495774463;
		va[1] = -0.32343464118594906;
		pb[0] = 1.3633746579054806;
		pb[1] = -3.108099298002804;
		vb[0] = 1.940750329761976E-11;
		vb[1] = -7.583885594691656E-11;
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(Double.POSITIVE_INFINITY, pc[0], 1e-9);
		assertEquals(Double.NEGATIVE_INFINITY, pc[1], 1e-9);
	}

	public void testComputeControlPoint2() {
		// FIXME all those Control Points are wrong:
		final double[] pa = { 0, 0, -1 };
		final double[] pb = { 1, 0, -1 };
		final double[] va = { 1, 1, -1 };
		final double[] vb = { 1, -1, -1 };
		final double[][] tmp_a = { { 0, 0 }, { 0, 0 } };
		final double[] tmp_b = { 0, 0 };
		final double[] pc = { 0, 0 };

		final PolynomeCurve p = new PolynomeCurve(new double[][] { { 0 },
				{ 0.0, 1.0015713348516129, -0.049212498797310725 }, { 0 } });
		final double[] sections = { 3.4880897073627515, 6.880084828014439,
				10.272079948666127, 13.664075069317814 };
		{
			final CurveTransformed ct = new CurveTransformed(
					new CurveRockAnalytic(p), new AffineTransform(new double[] {
							-0.9687816430468471, -0.24791556646457572,
							0.24791556646457572, -0.9687816430468471, 0.1,
							1.8287999629974365 }), sections[0]);
			// transformed:
			ct.at(0, sections[0], pa);
			ct.at(1, sections[0], va);
			ct.at(0, sections[1], pb);
			ct.at(1, sections[1], vb);
			CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
			assertEquals(0.1, pc[0], 1e-9);
			assertEquals(1.8287999629974365, pc[1], 1e-9);

			ct.at(0, sections[1], pa);
			ct.at(1, sections[1], va);
			ct.at(0, sections[2], pb);
			ct.at(1, sections[2], vb);
			CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
			assertEquals(Double.POSITIVE_INFINITY, pc[0], 1e-9);
			assertEquals(Double.NEGATIVE_INFINITY, pc[1], 1e-9);

			ct.at(0, sections[2], pa);
			ct.at(1, sections[2], va);
			ct.at(0, sections[3], pb);
			ct.at(1, sections[3], vb);
			CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
			assertEquals(Double.POSITIVE_INFINITY, pc[0], 1e-9);
			assertEquals(Double.NEGATIVE_INFINITY, pc[1], 1e-9);
		}

		// Untransformed:
		p.at(0, sections[0] - sections[0], pa);
		p.at(1, sections[0] - sections[0], va);
		p.at(0, sections[1] - sections[0], pb);
		p.at(1, sections[1] - sections[0], vb);
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(Double.NaN, pc[0], 1e-9);
		assertEquals(Double.NaN, pc[1], 1e-9);

		p.at(0, sections[1] - sections[0], pa);
		p.at(1, sections[1] - sections[0], va);
		p.at(0, sections[2] - sections[0], pb);
		p.at(1, sections[2] - sections[0], vb);
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(Double.NaN, pc[0], 1e-9);
		assertEquals(Double.NaN, pc[1], 1e-9);

		p.at(0, sections[2] - sections[0], pa);
		p.at(1, sections[2] - sections[0], va);
		p.at(0, sections[3] - sections[0], pb);
		p.at(1, sections[3] - sections[0], vb);
		CurveShape.computeControlPoint(pa, va, pb, vb, tmp_a, tmp_b, pc);
		assertEquals(Double.NaN, pc[0], 1e-9);
		assertEquals(Double.NaN, pc[1], 1e-9);
	}

	public void testExponentialSections() {
		double[] d = new double[0];
		CurveShape.exponentialSections(1, 2, d);

		d = new double[1];
		CurveShape.exponentialSections(1, 2, d);
		assertEquals(1, d[0], 1e-9);
		CurveShape.exponentialSections(2, 1, d);
		assertEquals(1, d[0], 1e-9);

		d = new double[2];
		CurveShape.exponentialSections(1, 2, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(2, d[1], 1e-9);
		CurveShape.exponentialSections(2, 1, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(2, d[1], 1e-9);

		d = new double[3];
		CurveShape.exponentialSections(1, 4, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(3, d[1], 1e-9);
		assertEquals(4, d[2], 1e-9);
		CurveShape.exponentialSections(4, 1, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(3, d[1], 1e-9);
		assertEquals(4, d[2], 1e-9);

		d = new double[4];
		CurveShape.exponentialSections(1, 7, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(4, d[1], 1e-9);
		assertEquals(6, d[2], 1e-9);
		assertEquals(7, d[3], 1e-9);
		CurveShape.exponentialSections(7, 1, d);
		assertEquals(1, d[0], 1e-9);
		assertEquals(4, d[1], 1e-9);
		assertEquals(6, d[2], 1e-9);
		assertEquals(7, d[3], 1e-9);
	}

	public void testInterpolate() {
		double[] s = CurveShape.interpolate(1, 3, new double[3], Interpolators
				.getLinearInstance());
		assertEquals(1.0, s[0], 1e-9);
		assertEquals(2.0, s[1], 1e-9);
		assertEquals(3.0, s[2], 1e-9);

		s = CurveShape.interpolate(1, 4, new double[4], Interpolators
				.getLinearInstance());
		assertEquals(1.0, s[0], 1e-9);
		assertEquals(2.0, s[1], 1e-9);
		assertEquals(3.0, s[2], 1e-9);
		assertEquals(4.0, s[3], 1e-9);
	}
}
