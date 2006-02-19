/*
 * jcurl curling simulation framework 
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
package org.jcurl.core.helpers;

import java.util.Map;
import java.util.TreeMap;

import jcurl.core.dto.Ice;


/**
 * Various units of measurement incl. conversion.
 * 
 * @see org.jcurl.core.helpers.DimVal
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Dim extends EnumBase {

    private static final long serialVersionUID = 6779663806431722367L;

    private static int _idx = 0;

    private static final Map _names = new TreeMap();

    public static final Dim DEG_PER_SEC;

    public static final Dim DEGREE;

    public static final Dim FOOT;

    public static final Dim HERTZ;

    public static final Dim HOUR;

    public static final Dim INCH;

    public static final Dim JOULE;

    public static final Dim METER;

    public static final Dim METER_PER_SEC;

    public static final Dim MICROSEC;

    public static final Dim MILLISEC;

    public static final Dim MINUTE;

    public static final Dim NANOSEC;

    public static final Dim NONE;

    public static final Dim RAD_PER_SEC;

    public static final Dim RADIANT;

    public static final Dim rpm;

    public static final Dim SEC_HOG_HOG;

    public static final Dim SEC_HOG_TEE;

    public static final Dim SECOND;

    public static final Dim thh;

    public static final Dim tht;
    static {
        // Base Units
        NONE = new Dim("", null, 0);
        METER = new Dim("m", null, 1.0);
        SECOND = new Dim("s", null, 1.0);
        JOULE = new Dim("J", null, 1.0);
        RADIANT = new Dim("rad", null, 1.0);
        METER_PER_SEC = new Dim("m/s", null, 1.0);
        RAD_PER_SEC = new Dim("rad/s", null, 1.0);

        // Alternatives
        DEG_PER_SEC = new Dim("deg/s", RAD_PER_SEC, Math.PI / 180);
        HERTZ = new Dim("Hz", RAD_PER_SEC, 2 * Math.PI);
        rpm = new Dim("rpm", RAD_PER_SEC, 2 * Math.PI);
        DEGREE = new Dim("deg", RADIANT, Math.PI / 180);
        FOOT = new Dim("ft", METER, 0.3048);
        INCH = new Dim("in", METER, 0.3048 / 12.0);
        HOUR = new Dim("h", SECOND, 3600);
        MICROSEC = new Dim("us", SECOND, 1e-6);
        MILLISEC = new Dim("ms", SECOND, 1e-3);
        MINUTE = new Dim("min", SECOND, 60);
        NANOSEC = new Dim("ns", SECOND, 1e-9);
        SEC_HOG_HOG = new Dim("shh", METER_PER_SEC, 1.0 / Ice.HOG_2_HOG);
        SEC_HOG_TEE = new Dim("sht", METER_PER_SEC, 1.0 / Ice.HOG_2_TEE);
        thh = new Dim("thh", null, 1.0);
        tht = new Dim("tht", null, 1.0);
    }

    public static final float f2m(final double f) {
        return (float) (f * 0.3048);
    }

    public static final Dim find(final String txt) {
        if (txt == null)
            return null;
        final Dim ret = (Dim) _names.get(txt);
        if (ret == null)
            throw new IllegalArgumentException("Unknown dimension [" + txt
                    + "]");
        return ret;
    }

    static boolean isAngle(final Dim dim) {
        return RADIANT.equals(dim.BaseDim);
    }

    static boolean isEnergy(final Dim dim) {
        return JOULE.equals(dim.BaseDim);
    }

    static boolean isLength(final Dim dim) {
        return METER.equals(dim.BaseDim);
    }

    static boolean isRockTime(final Dim dim) {
        if (SEC_HOG_HOG.equals(dim))
            return true;
        if (SEC_HOG_TEE.equals(dim))
            return true;
        return false;
    }

    static boolean isSpeed(final Dim dim) {
        return METER_PER_SEC.equals(dim.BaseDim);
    }

    static boolean isSpin(final Dim dim) {
        return RAD_PER_SEC.equals(dim.BaseDim);
    }

    static boolean isTime(final Dim dim) {
        return SECOND.equals(dim.BaseDim);
    }

    static double toMeter(final DimVal v) {
        return v.to(METER).val;
    }

    static double toRadians(final DimVal v) {
        return v.to(RADIANT).val;
    }

    static double toSecHogHog(final DimVal v) {
        double ret = v.val;
        if (SEC_HOG_HOG.equals(v.dim))
            return ret;
        throw new IllegalArgumentException("Not convertible");
    }

    static double toSecHogTee(final DimVal v) {
        double ret = v.val;
        if (SEC_HOG_TEE.equals(v.dim))
            return ret;
        throw new IllegalArgumentException("Not convertible");
    }

    public final Dim BaseDim;

    public final double Factor;

    protected Dim(final String txt, final Dim base, final double factor) {
        super(_idx++, txt);
        _names.put(txt, this);
        this.BaseDim = base == null ? this : base;
        this.Factor = factor;
    }

    public boolean isAngle() {
        return Dim.isAngle(this);
    }

    public boolean isLength() {
        return Dim.isLength(this);
    }

    public boolean isSpeed() {
        return Dim.isSpeed(this);
    }

    public boolean isSpin() {
        return Dim.isSpin(this);
    }

    public boolean isTime() {
        return Dim.isTime(this);
    }
}