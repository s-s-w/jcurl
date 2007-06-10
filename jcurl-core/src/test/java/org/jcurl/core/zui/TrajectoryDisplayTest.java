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
package org.jcurl.core.zui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.helpers.AnnoHelp;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerDenny;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.mr.gui.TacticsApplet;

import edu.umd.cs.piccolo.PNode;

public class TrajectoryDisplayTest extends TestZuiBase {
    private static final Log log = JCLoggerFactory
            .getLogger(TrajectoryDisplayTest.class);

    static ComputedTrajectorySet initHammy(ComputedTrajectorySet te) {
        if (te == null)
            te = new CurveManager();
        te.setCollider(new CollissionSpin(0.5, 0.0));
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerDenny(24, 1));
        te.setInitialPos(PositionSet.allOut());
        te.setInitialSpeed(new SpeedSet());
        te.getAnnotations().put(AnnoHelp.HammerK, AnnoHelp.HammerVDark);
        te.getAnnotations().put(AnnoHelp.DarkTeamK, "Scotland");
        te.getAnnotations().put(AnnoHelp.LightTeamK, "Canada");
        te.getAnnotations().put(AnnoHelp.GameK, "Semifinal");
        te.getAnnotations().put(AnnoHelp.EventK, "World Curling Championships");
        te.getAnnotations().put(AnnoHelp.DateK, "1992");
        te.getAnnotations().put(AnnoHelp.LocationK, "Garmisch");
        initHammy(te.getInitialPos(), te.getInitialSpeed());
        return te;
    }

    static void initHammy(final PositionSet p, final SpeedSet s) {
        PositionSet.allOut(p);
        // te.getInitialPos().getLight(1-1).setLocation(
        p.getLight(2 - 1)
                .setLocation(Dim.f2m(-1.170732), Dim.f2m(15.365854), 0);
        p.getLight(3 - 1).setLocation(Dim.f2m(0.292683), Dim.f2m(8.780488), 0);
        p.getLight(4 - 1).setLocation(Dim.f2m(2.195122), Dim.f2m(12), 0);
        p.getLight(5 - 1).setLocation(Dim.f2m(1.463415), Dim.f2m(5.707317), 0);
        p.getLight(6 - 1).setLocation(Dim.f2m(1.463415), Dim.f2m(-2.780488), 0);
        p.getLight(7 - 1)
                .setLocation(Dim.f2m(-0.439024), Dim.f2m(-5.560976), 0);
        p.getLight(8 - 1)
                .setLocation(Dim.f2m(-1.756098), Dim.f2m(-1.609756), 0);
        // p.getDark(1-1).setLocation(
        // p.getDark(2-1).setLocation(
        p.getDark(3 - 1).setLocation(Dim.f2m(0.878049), Dim.f2m(14.341463), 0);
        p.getDark(4 - 1).setLocation(Dim.f2m(-2.634146), Dim.f2m(13.170732), 0);
        p.getDark(5 - 1).setLocation(Dim.f2m(4.536585), Dim.f2m(-0.439024), 0);
        p.getDark(6 - 1).setLocation(Dim.f2m(0.731707), Dim.f2m(-3.95122), 0);
        p.getDark(7 - 1).setLocation(Dim.f2m(-2.780488), Dim.f2m(-4.390244), 0);
        p.getDark(8 - 1).setLocation(Dim.f2m(3.89991), IceSize.HOG_2_TEE, 0);
        RockSet.allZero(s);
        s.getDark(8 - 1).setLocation(0, -3, 100 * Math.PI / 180);

        p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
        s.getDark(8 - 1).setLocation(0.188, -3, -100 * Math.PI / 180);

        p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
        s.getDark(8 - 1).setLocation(0.1785, -4, -100 * Math.PI / 180);
        p.notifyChange();
        s.notifyChange();
    }

    public void _testThroughPut() {
        final int dt = 5000;
        final Graphics g = new BufferedImage(1024 * 2, 768 * 2,
                BufferedImage.TYPE_INT_ARGB).getGraphics();
        final TrajectorySet p = TacticsApplet.initHammy(null);
        final int frames = showTrajectoryDisplay(p, dt, new TimeRunnable() {
            @Override
            public void run(final double t) throws InterruptedException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void run(final double t, final Component jp)
                    throws InterruptedException {
                jp.paint(g);
            }
        });
        if (frame != null) {
            System.out.println(getClass().getName() + " frequency: " + frames
                    * 1000L / (double) dt + " frames per second");
            // System.out.println(frames + " computations took " + dt
            // + " millis, i.e. " + frames * 1000L / dt + " per second.");
        }
    }

    public int showTrajectoryDisplay(final TrajectorySet p, final long millis,
            final TimeRunnable r) {
        if (frame == null)
            return -1;
        frame.setVisible(true);

        final long t0 = System.currentTimeMillis();
        int loop = 0;
        try {
            while (System.currentTimeMillis() - t0 < millis) {
                r.run(1e-3 * (System.currentTimeMillis() - t0), null);
                loop++;
            }
        } catch (final InterruptedException e) {
            log.warn("Oops", e);
        }
        frame.setVisible(false);
        return loop;
    }

    public void testHammy() throws InterruptedException {
        final CurveManager cm = new CurveManager();
        initHammy(cm);

        // Raw throughput:
        final long t0 = System.currentTimeMillis();
        cm.setCurrentTime(29.9);
        log.info("Initial computation took "
                + (System.currentTimeMillis() - t0) + " millis");
        cm.setCurrentTime(0);

        final PNode a = new PPositionSet(cm.getCurrentPos(),
                new PRockFactory.Fancy());
        final PNode b = new PCurveStore(cm.getCurveStore(),
                new PTrajectoryFactory.Fancy());
        try {
            ice.addChild(b);
            ice.addChild(a);

            // with Display:
            showTrajectoryDisplay(cm, 7500, new TimeRunnable() {
                @Override
                public void run(final double t) throws InterruptedException {
                    cm.setCurrentTime(8.5 + t);
                    Thread.sleep(1000 / 50);
                }
            });
        } finally {
            ice.removeChild(a);
            ice.removeChild(b);
        }
    }

    public void testOneHit() throws InterruptedException {
        final CurveManager cm = new CurveManager();
        {
            cm.setCollider(new CollissionSpin(0.5, 0.0));
            cm.setCollissionDetector(new NewtonCollissionDetector());
            cm.setCurler(new CurlerDenny(23, 1));
            cm.setInitialPos(PositionSet.allHome());
            cm.getInitialPos().getDark(0).setLocation(0, IceSize.FAR_HOG_2_TEE,
                    0);
            cm.getInitialPos().getLight(0).setLocation(0, IceSize.BACK_2_TEE,
                    0.25 * Math.PI);
            cm.setInitialSpeed(new SpeedSet());
            if (true)
                cm.getInitialSpeed().getDark(0).setLocation(-0.095,
                        -cm.getCurler().computeV0(3.124), Math.PI / 2);
            else
                cm.getInitialSpeed().getDark(0).setLocation(0, -2.455,
                        Math.PI / 2);
        }

        // Raw throughput:
        final long t0 = System.currentTimeMillis();
        cm.setCurrentTime(25);
        log.info("Initial computation took "
                + (System.currentTimeMillis() - t0) + " millis");
        cm.setCurrentTime(0);

        final PNode a = new PPositionSet(cm.getCurrentPos(),
                new PRockFactory.Fancy());
        final PNode b = new PCurveStore(cm.getCurveStore(),
                new PTrajectoryFactory.Fancy());
        try {
            ice.addChild(b);
            ice.addChild(a);
            // with Display:
            showTrajectoryDisplay(cm, 10000, new TimeRunnable() {
                @Override
                public void run(final double t) throws InterruptedException {
                    cm.setCurrentTime(14 + t);
                    Thread.sleep(1000 / 50);
                }
            });
        } finally {
            ice.removeChild(a);
            ice.removeChild(b);
        }
    }
}
