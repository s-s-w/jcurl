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
package org.jcurl.core.base;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CollissionSimple;
import org.jcurl.math.R1RNFunction;

public class TrajectoryManagerTest extends TestShowBase {

    private static final Log log = JCLoggerFactory
            .getLogger(TrajectoryManagerTest.class);

    public TrajectoryManagerTest() {
        super(5000);
    }

    public void test1() throws InterruptedException {
        final TrajectoryManager te = new TrajectoryManager();
        te.setCollider(new CollissionSimple());
        te.setCollissionDetector(new CollissionDetector() {
            public double compute(double t0, double tmax, R1RNFunction fa,
                    double ra, R1RNFunction fb, double rb) throws NoCollission {
                throw new NotImplementedYetException();
            }
        });
        te.setSlider(new SlideNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0)
                .setLocation(0, Ice.FAR_HOG_2_TEE, Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                te.getSlider().computeV0(3), 0.2);

        doShowPositionDisplay(te.getCurrentPos(), new TimeRunnable() {
            public void run(double t) throws InterruptedException {
                te.setCurrentTime(t);
                Thread.sleep(10);
            }
        });
        // FIXME Test is not ok yet!
    }
}
