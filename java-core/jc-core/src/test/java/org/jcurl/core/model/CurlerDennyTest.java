/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005 M. Rohrmoser
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

import org.jcurl.core.base.CurveRock;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.Zoomer;
import org.jcurl.core.swing.TestShowBase;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SlideDennyTest.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CurlerDennyTest extends TestShowBase {

    void showTrajectory(final CurveRock p, final Zoomer zoom, final int millis,
            final int dt) {
        final PositionSet pos = PositionSet.allOut();
        showPositionDisplay(pos, zoom, millis, new TimeRunnable() {
            @Override
            public void run(final double t) throws InterruptedException {
                p.at(0, t, pos.getRock(0));
                pos.notifyChange();
                Thread.sleep(dt);
            }
        });
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

        final CurveRock p = s.computeRc(0, s.computeV0(3.124), Math.PI / 2, 0);
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

    public void testDennyShow() throws InterruptedException {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSpin());
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerDenny(23, 1));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.FAR_HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet(PositionSet.allHome()));
        if (true)
            te.getInitialSpeed().getDark(0).setLocation(-0.096,
                    -te.getCurler().computeV0(3.124), Math.PI / 2);
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
}