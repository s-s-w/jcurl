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
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.TestShowBase;
import org.jcurl.core.base.Zoomer;

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
        CurlerDenny s = new CurlerDenny(24, 1);
        CurveRock p = s.computeRc(Math.PI, 2, 0.2, 0);
        System.out.println(p);
        showTrajectory(p, FixpointZoomer.HOUSE, 5000, 40);
    }

    public void testDenny() throws InterruptedException {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSimple());
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerDenny(23, 1));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.FAR_HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
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