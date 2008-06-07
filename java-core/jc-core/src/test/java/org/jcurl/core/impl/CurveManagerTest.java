/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.core.impl;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.helpers.AnnoHelper;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.swing.TestShowBase;
import org.jcurl.core.ui.FixpointZoomer;
import org.jcurl.math.R1RNFunction;

public class CurveManagerTest extends TestShowBase {

	private static final Log log = JCLoggerFactory
			.getLogger(CurveManagerTest.class);

	private static final Point2D tee = new Point2D.Double(0, 0);

	public static ComputedTrajectorySet initHammy(ComputedTrajectorySet te) {
		if (te == null)
			te = new CurveManager();
		te.setCollider(new CollissionSpin(0.5, 0.0));
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(24, 0));
		te.setInitialPos(RockSetUtils.allOut());
		te.setInitialSpeed(RockSetUtils.zeroSpeed());
		te.getAnnotations().put(AnnoHelper.HammerK, AnnoHelper.HammerVDark);
		te.getAnnotations().put(AnnoHelper.DarkTeamK, "Scotland");
		te.getAnnotations().put(AnnoHelper.LightTeamK, "Canada");
		te.getAnnotations().put(AnnoHelper.GameK, "Semifinal");
		te.getAnnotations().put(AnnoHelper.EventK,
				"World Curling Championships");
		te.getAnnotations().put(AnnoHelper.DateK, "1992");
		te.getAnnotations().put(AnnoHelper.LocationK, "Garmisch");
		initHammy(te.getInitialPos(), te.getInitialSpeed());
		return te;
	}

	public static void initHammy(final RockSet<Pos> p, final RockSet<Vel> s) {
		RockSetUtils.allOut(p);
		// te.getInitialPos().getLight(1-1).setLocation(
		p.getLight(2 - 1).setLocation(Unit.f2m(-1.170732), Unit.f2m(15.365854),
				0);
		p.getLight(3 - 1)
				.setLocation(Unit.f2m(0.292683), Unit.f2m(8.780488), 0);
		p.getLight(4 - 1).setLocation(Unit.f2m(2.195122), Unit.f2m(12), 0);
		p.getLight(5 - 1)
				.setLocation(Unit.f2m(1.463415), Unit.f2m(5.707317), 0);
		p.getLight(6 - 1).setLocation(Unit.f2m(1.463415), Unit.f2m(-2.780488),
				0);
		p.getLight(7 - 1).setLocation(Unit.f2m(-0.439024), Unit.f2m(-5.560976),
				0);
		p.getLight(8 - 1).setLocation(Unit.f2m(-1.756098), Unit.f2m(-1.609756),
				0);
		// p.getDark(1-1).setLocation(
		// p.getDark(2-1).setLocation(
		p.getDark(3 - 1)
				.setLocation(Unit.f2m(0.878049), Unit.f2m(14.341463), 0);
		p.getDark(4 - 1).setLocation(Unit.f2m(-2.634146), Unit.f2m(13.170732),
				0);
		p.getDark(5 - 1)
				.setLocation(Unit.f2m(4.536585), Unit.f2m(-0.439024), 0);
		p.getDark(6 - 1).setLocation(Unit.f2m(0.731707), Unit.f2m(-3.95122), 0);
		p.getDark(7 - 1).setLocation(Unit.f2m(-2.780488), Unit.f2m(-4.390244),
				0);
		p.getDark(8 - 1).setLocation(Unit.f2m(3.89991), IceSize.HOG_2_TEE, 0);
		RockSet.allZero(s);
		s.getDark(7).setLocation(0, -3, 100 * Math.PI / 180);
	}

	public static ComputedTrajectorySet initOneHit(ComputedTrajectorySet te) {
		if (te == null)
			te = new CurveManager();
		te.setCollider(new CollissionSpin(0.5, 0.0));
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(24, 0));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		te.setInitialSpeed(RockSetUtils.zeroSpeed());
		te.getInitialSpeed().getDark(0).setLocation(0,
				-te.getCurler().computeHackSpeed(5, tee), Math.PI / 2);
		return te;
	}

	public static ComputedTrajectorySet initOneHitCurl(ComputedTrajectorySet te) {
		if (te == null)
			te = new CurveManager();
		te.setCollider(new CollissionSpin(0.5, 0.0));
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerDenny(24, 1));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		te.setInitialSpeed(RockSetUtils.zeroSpeed());
		te.getInitialSpeed().getDark(0).setLocation(0,
				-te.getCurler().computeHackSpeed(5, tee), Math.PI / 2);
		return te;
	}

	void showPaths(final Iterator<Iterable<Entry<Double, R1RNFunction>>> it,
			final double tmin, final double tmax) throws InterruptedException {
		if (frame == null)
			return;
		frame.setVisible(true);
		Thread.sleep(3000);
	}

	public void testFastHit() throws InterruptedException {
		final CurveManager te = new CurveManager();
		te.setCollider(new CollissionSimple());
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(23, 0));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		te.setInitialSpeed(new RockSet<Vel>(new RockDouble<Vel>()));
		te.getInitialSpeed().getDark(0).setLocation(0,
				-te.getCurler().computeHackSpeed(5, tee), Math.PI / 2);
		te.setCurrentTime(0);

//		assertTrue(Double.isNaN(te.doGetNextHit().t));
//		assertFalse((1 > te.doGetNextHit().t));

		// Raw throughput:
		long t0 = System.currentTimeMillis();
		te.setCurrentTime(25);
		log.info("Initial computation took "
				+ (System.currentTimeMillis() - t0) + " millis");
		final int loops = 10000;
		t0 = System.currentTimeMillis();
		for (int i = loops; i > 0; i--)
			te.setCurrentTime(1e-3 * i);
		log.info(loops + " computations took "
				+ (System.currentTimeMillis() - t0) + " millis, i.e. " + loops
				* 1000 / (System.currentTimeMillis() - t0) + " per second.");

		// with Display:
		showPositionDisplay(te.getCurrentPos(), FixpointZoomer.HOUSE, 5000,
				new TimeRunnable() {
					@Override
					public void run(final double t) throws InterruptedException {
						te.setCurrentTime(t);
						Thread.sleep(1000 / 50);
					}
				});
		showPaths(te.getCurveStore().iterator(), 0, 10);
	}

	public void testHammy() throws InterruptedException {
		final ComputedTrajectorySet te = initHammy(new CurveManager());

		// Raw throughput:
		final long t0 = System.currentTimeMillis();
		te.setCurrentTime(25);
		log.info("Initial computation took "
				+ (System.currentTimeMillis() - t0) + " millis");

		// with Display:
		showPositionDisplay(te.getCurrentPos(), FixpointZoomer.HOUSE, 7500,
				new TimeRunnable() {
					@Override
					public void run(final double t) throws InterruptedException {
						te.setCurrentTime(t);
						Thread.sleep(1000 / 50);
					}
				});
	}

	public void testSlowNoHit() throws InterruptedException {
		final CurveManager te = new CurveManager();
		te.setCollider(new CollissionSimple());
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(23, 0));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0)
				.setLocation(0, IceSize.HOG_2_TEE, Math.PI);
		te.setInitialSpeed(RockSetUtils.zeroSpeed());
		te.getInitialSpeed().getDark(0).setLocation(0,
				-te.getCurler().computeHackSpeed(9, tee), Math.PI / 2);

		assertEquals(Double.NaN, te.doGetNextHit().t);
		assertFalse((1 > te.doGetNextHit().t));

		// Raw throughput:
		final int loops = 10000;
		final long t0 = System.currentTimeMillis();
		for (int i = loops; i > 0; i--)
			te.setCurrentTime(1e-3 * i);
		log.info(loops + " computations took "
				+ (System.currentTimeMillis() - t0) + " millis, i.e. " + loops
				* 1000 / (System.currentTimeMillis() - t0) + " per second.");

		// with Display:
		showPositionDisplay(te.getCurrentPos(), FixpointZoomer.HOUSE, 5000,
				new TimeRunnable() {
					@Override
					public void run(final double t) throws InterruptedException {
						te.setCurrentTime(t);
						Thread.sleep(1000 / 50);
					}
				});
	}
}
