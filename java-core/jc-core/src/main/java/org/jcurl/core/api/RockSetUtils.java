/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.core.api;

import java.awt.geom.Point2D;

import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.math.MathVec;

/**
 * Utils for {@link RockSet}s with location semantics {@link Pos}.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PositionSet.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class RockSetUtils {

	private static final double MaxScoreDistSq = MathVec.sqr(RockProps.DEFAULT
			.getRadius()
			+ Unit.f2m(6));

	private static final double RR = MathVec.sqr(RockProps.DEFAULT.getRadius());
	private static final double RR4 = RR * 4;

	public static RockSet<Pos> allHome() {
		return allHome(new RockSet<Pos>(new RockDouble<Pos>()));
	}

	public static RockSet<Pos> allHome(final RockSet<Pos> ret) {
		if (ret == null)
			return null;
		for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
			IceSize.setHome(ret.dark[i], true, i);
			IceSize.setHome(ret.light[i], false, i);
		}
		ret.fireStateChanged();
		return ret;
	}

	public static RockSet<Pos> allOut() {
		return allOut(new RockSet<Pos>(new RockDouble<Pos>()));
	}

	public static RockSet<Pos> allOut(final RockSet<Pos> ret) {
		if (ret == null)
			return null;
		for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
			IceSize.setOut(ret.dark[i], true, i);
			IceSize.setOut(ret.light[i], false, i);
		}
		ret.fireStateChanged();
		return ret;
	}

	/**
	 * @param rocks
	 * @param pos
	 * @return <code>-1</code> if none
	 */
	public static int findRockIndexAtPos(final RockSet<Pos> rocks,
			final Point2D pos) {
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
			if (rocks.getRock(i).p().distanceSq(pos) <= RR)
				return i;
		return -1;
	}

	/**
	 * 
	 * @param rocks
	 * @param pos
	 * @param myself
	 * @return <code>-1</code> if none
	 */
	public static int findRockIndexTouchingRockAtPos(final RockSet<Pos> rocks,
			final Point2D pos, final int myself) {
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
			if (i == myself)
				continue;
			if (rocks.getRock(i).p().distanceSq(pos) <= RR4)
				return i;
		}
		return -1;
	}

	/**
	 * Get the "out" rocks (as bitmask).
	 * 
	 * @param a
	 * @return bitmask of the out rocks.
	 */
	public static int getOutRocks(final RockSet<Pos> a) {
		final double xmin = IceSize.SIDE_2_CENTER
				+ RockProps.DEFAULT.getRadius();
		final double ymin = -IceSize.BACK_2_TEE - RockProps.DEFAULT.getRadius();
		int ret = 0;
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
			final Point2D rock = a.getRock(i).p();
			final double x = Math.abs(rock.getX());
			final double y = rock.getY();
			if (x > xmin || y < ymin)
				ret |= 1 << i;
		}
		return ret;
	}

	/**
	 * Get the "shot" rocks (as bitmask).
	 * 
	 * @param a
	 * @return bitmask of the shot rocks.
	 */
	public static int getShotRocks(final RockSet<Pos> a) {
		final int scorer;
		final double scoreDistSq;
		{
			// first get the best rock's distance square for each dark and
			// light.
			double distDarkSq = MaxScoreDistSq;
			double distLightSq = MaxScoreDistSq;
			for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
				double distSq = a.getDark(i).p().distanceSq(0, 0);
				if (distSq < distDarkSq)
					distDarkSq = distSq;
				distSq = a.getLight(i).p().distanceSq(0, 0);
				if (distSq < distLightSq)
					distLightSq = distSq;
			}
			if (distDarkSq == distLightSq)
				return 0;
			// who scores?
			scorer = distDarkSq < distLightSq ? 0 : 1;
			// limiting distance
			scoreDistSq = distDarkSq > distLightSq ? distDarkSq : distLightSq;
		}
		int ret = 0;
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
			if (i % 2 != scorer)
				continue;
			final double distSq = a.getRock(i).p().distanceSq(0, 0);
			if (distSq <= scoreDistSq)
				ret |= 1 << i;
		}
		return ret;
	}

	/**
	 * Get the "waiting" rocks (as bitmask).
	 * 
	 * @param a
	 * @return bitmask of the waiting rocks.
	 */
	public static int getWaitRocks(final RockSet<Pos> a) {
		final double xmax = IceSize.SIDE_2_CENTER
				+ RockProps.DEFAULT.getRadius();
		final double ymax = IceSize.HOG_2_TEE - RockProps.DEFAULT.getRadius();
		int ret = 0;
		for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
			final Point2D rock = a.getRock(i).p();
			final double x = Math.abs(rock.getX());
			final double y = rock.getY();
			if (x < xmax && y > ymax)
				ret |= 1 << i;
		}
		return ret;
	}

	static <T extends RockType> RockSet<T> zero() {
		return new RockSet<T>(new RockDouble<T>());
	}

	public static RockSet<Vel> zeroSpeed() {
		return zero();
	}

	private RockSetUtils() {}
}
