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
package org.jcurl.core.api;

import java.util.Map;
import java.util.TreeMap;

/**
 * Various units of measurement plus conversion.
 * 
 * Similar to <code>javax.measure.unit.Unit</code> from <a
 * href="http://www.jcp.org/en/jsr/detail?id=275">JSR-275</a> but faaaar
 * simpler.
 * 
 * @see org.jcurl.core.api.Measure
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:Unit.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class Unit extends EnumBase {

	private static int _idx = 0;

	private static final Map<String, Unit> _names = new TreeMap<String, Unit>();

	public static final Unit DEG_PER_SEC;

	public static final Unit DEGREE;

	public static final Unit FOOT;

	public static final Unit HERTZ;

	public static final Unit HOUR;

	public static final Unit INCH;

	public static final Unit JOULE;

	public static final Unit METER;

	public static final Unit METER_PER_SEC;

	public static final Unit MICROSEC;

	public static final Unit MILLISEC;

	public static final Unit MINUTE;

	public static final Unit NANOSEC;

	public static final Unit NONE;

	public static final Unit RAD_PER_SEC;

	public static final Unit RADIANT;

	public static final Unit RPM;

	public static final Unit SECOND;

	private static final long serialVersionUID = 6779663806431722367L;

	static {
		// Base Units
		NONE = new Unit("", null, 0);
		METER = new Unit("m", null, 1.0);
		SECOND = new Unit("s", null, 1.0);
		JOULE = new Unit("J", null, 1.0);
		RADIANT = new Unit("rad", null, 1.0);
		METER_PER_SEC = new Unit("m/s", null, 1.0);
		RAD_PER_SEC = new Unit("rad/s", null, 1.0);

		// Alternatives
		DEG_PER_SEC = new Unit("deg/s", RAD_PER_SEC, Math.PI / 180);
		HERTZ = new Unit("Hz", RAD_PER_SEC, 2 * Math.PI);
		RPM = new Unit("rpm", RAD_PER_SEC, 2 * Math.PI);
		DEGREE = new Unit("deg", RADIANT, Math.PI / 180);
		FOOT = new Unit("ft", METER, 0.3048);
		INCH = new Unit("in", METER, 0.3048 / 12.0);
		HOUR = new Unit("h", SECOND, 3600);
		MICROSEC = new Unit("us", SECOND, 1e-6);
		MILLISEC = new Unit("ms", SECOND, 1e-3);
		MINUTE = new Unit("min", SECOND, 60);
		NANOSEC = new Unit("ns", SECOND, 1e-9);
	}

	public static final float f2m(final double f) {
		return (float) (f * 0.3048);
	}

	public static final Unit getInstance(final String txt) {
		if (txt == null)
			return null;
		final Unit ret = _names.get(txt);
		if (ret == null)
			throw new IllegalArgumentException("Unknown dimension [" + txt
					+ "]");
		return ret;
	}

	static boolean isAngle(final Unit dim) {
		return RADIANT.equals(dim.BaseUnit);
	}

	static boolean isEnergy(final Unit dim) {
		return JOULE.equals(dim.BaseUnit);
	}

	static boolean isLength(final Unit dim) {
		return METER.equals(dim.BaseUnit);
	}

	static boolean isSpeed(final Unit dim) {
		return METER_PER_SEC.equals(dim.BaseUnit);
	}

	static boolean isSpin(final Unit dim) {
		return RAD_PER_SEC.equals(dim.BaseUnit);
	}

	static boolean isTime(final Unit dim) {
		return SECOND.equals(dim.BaseUnit);
	}

	static double toMeter(final Measure v) {
		return v.to(METER).value;
	}

	static double toRadians(final Measure v) {
		return v.to(RADIANT).value;
	}

	public final Unit BaseUnit;

	final double Factor;

	protected Unit(final String txt, final Unit base, final double factor) {
		super(_idx++, txt);
		_names.put(txt, this);
		BaseUnit = base == null ? this : base;
		Factor = factor;
	}

	boolean isAngle() {
		return Unit.isAngle(this);
	}

	boolean isLength() {
		return Unit.isLength(this);
	}

	boolean isSpeed() {
		return Unit.isSpeed(this);
	}

	boolean isSpin() {
		return Unit.isSpin(this);
	}

	boolean isTime() {
		return Unit.isTime(this);
	}
}