/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.model;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.jcurl.core.base.Ice;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Slider;
import org.jcurl.core.base.TestShowBase;
import org.jcurl.core.base.Zoomer;
import org.jcurl.core.helpers.Dim;
import org.jcurl.math.Polynome;

public class SlideNoCurlTest extends TestShowBase {

    public void testBeta() {
        assertEquals(0.0980844266686885, new SlideNoCurl(17, 0).beta);
        assertEquals(0.0535848758171096, new SlideNoCurl(23, 0).beta);
        assertEquals(0.04193254335392156, new SlideNoCurl(26, 0).beta);
    }

    public void testComputeV0() {
        Slider s = new SlideNoCurl(17, 0);
        for (int i = 1; i <= 6; i++)
            System.out.println(s.computeV0(i));
        assertEquals(8.131515526029066, s.computeV0(1));
        assertEquals(3.9186311230115, s.computeV0(2));
        assertEquals(2.448946704226519, s.computeV0(3));
        assertEquals(1.6650622814996845, s.computeV0(4));
        assertEquals(1.1554978571961083, s.computeV0(5));
        assertEquals(0.7830934321041613, s.computeV0(6));

        s = new SlideNoCurl(23, 0);
        assertEquals(8.176015076880644, s.computeV0(1));
        assertEquals(4.007630224714657, s.computeV0(2));
        assertEquals(2.5824453567812555, s.computeV0(3));
        assertEquals(1.8430604849060002, s.computeV0(4));
        assertEquals(1.3779956114540028, s.computeV0(5));
        assertEquals(1.0500907372136346, s.computeV0(6));

        s = new SlideNoCurl(26, 0);
        assertEquals(8.187667409343833, s.computeV0(1));
        assertEquals(4.030934889641034, s.computeV0(2));
        assertEquals(2.61740235417082, s.computeV0(3));
        assertEquals(1.8896698147587523, s.computeV0(4));
        assertEquals(1.436257273769943, s.computeV0(5));
        assertEquals(1.1200047319927628, s.computeV0(6));
    }

    public void testDrawToTeeV0() {
        assertEquals(3.334870506735409, new SlideNoCurl(17, 0).drawToTeeV0);
        assertEquals(2.4649042875870415, new SlideNoCurl(23, 0).drawToTeeV0);
        assertEquals(2.180492254403921, new SlideNoCurl(26, 0).drawToTeeV0);
    }

    public void testComputeRcPoly() {
        SlideNoCurl s = new SlideNoCurl(17, 0);
        Polynome[] p = s.computeRcPoly(Math.PI, 2, 0.2);
        showTrajectory(p, Zoomer.HOUSE, 5000, 40);
        assertEquals(0, p[0].at(0));
        assertEquals(0, p[1].at(0));
        assertEquals(Math.PI, p[2].at(0));
        assertEquals(0, p[0].at(17));
        assertEquals(5.653600692749025, p[1].at(17));
        assertEquals(6.5415926535897935, p[2].at(17));

        p = s.computeRcPoly(Math.PI, 3, 0.2);
        assertEquals(0, p[0].at(0));
        assertEquals(0, p[1].at(0));
        assertEquals(Math.PI, p[2].at(0));
        assertEquals(0, p[0].at(17));
        assertEquals(22.653600692749027, p[1].at(17));
        assertEquals(6.5415926535897935, p[2].at(17));

        s = new SlideNoCurl(23, 0);
        p = s.computeRcPoly(Math.PI, 2, 0.2);
        assertEquals(0, p[0].at(0));
        assertEquals(0, p[1].at(0));
        assertEquals(Math.PI, p[2].at(0));
        assertEquals(0, p[0].at(23));
        assertEquals(17.653600692749023, p[1].at(23));
        assertEquals(7.741592653589794, p[2].at(23));

        p = s.computeRcPoly(Math.PI, 3, 0.2);
        assertEquals(0, p[0].at(0));
        assertEquals(0, p[1].at(0));
        assertEquals(Math.PI, p[2].at(0));
        assertEquals(0, p[0].at(23));
        assertEquals(40.65360069274902, p[1].at(23));
        assertEquals(7.741592653589794, p[2].at(23));
    }

    void showTrajectory(final Polynome[] p, final Zoomer zoom,
            final int millis, final int dt) {
        final PositionSet pos = PositionSet.allHome();
        showPositionDisplay(pos, zoom, millis, new TimeRunnable() {
            @Override
            public void run(final double t) throws InterruptedException {
                pos.getRock(0).setLocation(p[0].at(t), p[1].at(t), p[2].at(t));
                pos.notifyChange();
                Thread.sleep(dt);
            }
        });
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
        final SlideNoCurl s = new SlideNoCurl(23, 0);
        final AffineTransform m = new AffineTransform();
        final Point2D x = new Point2D.Double();
        s.releaseRc2Wc(m, 0, 0);
        assertEquals(new AffineTransform(new double[] { -1, 0, 0, -1, 0,
                28.346399307250977 }), m);
        assertEquals(AffineTransform.TYPE_TRANSLATION
                | AffineTransform.TYPE_QUADRANT_ROTATION, m.getType());
        x.setLocation(0.5, 1);
        m.transform(x, x);
        assertEquals(-0.5, x.getX());
        assertEquals(-1, x.getY() - Ice.FAR_HOG_2_TEE);

        s.releaseRc2Wc(m, Dim.f2m(14), 0);
        // System.out.println(new AffineTransform(
        // new double[] { 1, 2, 3, 4, 5, 6 })
        // + "\n" + m);
        assertEquals(new AffineTransform(new double[] { -0.993883734824108,
                0.110431524720441, -0.110431524720441, -0.993883734824108,
                -1.117600003247539, 28.346399307250977 }).toString(), m
                .toString());
        x.setLocation(0.5, 1);
        m.transform(x, x);
        assertEquals(-1.2249733953800335, x.getX() + 0.5);
        assertEquals(0.061332027536110445, x.getY() - Ice.FAR_HOG_2_TEE + 1);

        s.releaseRc2Wc(m, Dim.f2m(-7), 0);
        // System.out.println(new AffineTransform(
        // new double[] { 1, 2, 3, 4, 5, 6 })
        // + "\n" + m);
        assertEquals(new AffineTransform(new double[] { -0.998460353243557,
                -0.055470018935921, 0.055470018935921, -0.998460353243557,
                0.558800001623769, 28.346399307250977 }).toString(), m
                .toString());
        x.setLocation(0.5, 1);
        m.transform(x, x);
        assertEquals(-0.3849601560620878, x.getX() - 0.5);
        assertEquals(-0.026195362711519, x.getY() - Ice.FAR_HOG_2_TEE + 1);
    }
}
