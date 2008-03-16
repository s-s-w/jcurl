/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.model;

import java.util.Map;

import org.jcurl.core.base.CurlerBase;
import org.jcurl.core.base.CurveRock;
import org.jcurl.core.base.CurveRockAnalytic;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PropModelHelper;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.Measure;
import org.jcurl.core.helpers.Physics;
import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;
import org.jcurl.math.R1R1Function;

/**
 * Mark Denny's curl-model. Motion of a curling rock acc. to "Curling rock
 * dynamics", Mark Denny, Canadian Journal of Physics, 1988, P. 295-304.
 * <p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SlideDenny.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CurlerDenny extends CurlerBase {

    private static final double _R = RockProps.DEFAULT.getRadius();

    private static final double eps = RockProps.DEFAULT.getInertia()
            / (RockProps.DEFAULT.getMass() * MathVec.sqr(_R));

    private static final double g = Physics.g;

    private static final long serialVersionUID = 9048729754646886751L;

    private transient double b;

    private transient double mu;

    public CurlerDenny() {
    }

    /**
     * Computes the internal ice friction coefficient as:
     * <p>
     * <code>beta = {@link IceSize#FAR_HOG_2_TEE} / drawToTeeTime^2</code>
     * </p>
     * 
     * @param drawToTeeTime
     *            may be {@link Double#POSITIVE_INFINITY}.
     * @param drawToTeeCurl
     *            should be &gt; 0
     */
    public CurlerDenny(final double drawToTeeTime, final double drawToTeeCurl) {
        final Map<CharSequence, Measure> t = PropModelHelper.create();
        PropModelHelper.setDrawToTeeTime(t, drawToTeeTime);
        PropModelHelper.setDrawToTeeCurl(t, drawToTeeCurl);
        init(t);
    }

    public CurveRock computeRc(final double a0, final double v0,
            final double omega0, final double sweepFactor) {
        final double f = -Math.signum(omega0) * b * g * mu / (48 * eps * _R);
        final Polynome x = new Polynome(new double[] { 0, 0, 0, -4 * v0 * f,
                3 * g * mu * f });
        final Polynome y = new Polynome(new double[] { 0, v0, -g * mu / 2 });
        final R1R1Function a = new R1R1Function() {
            private static final long serialVersionUID = -4138559036279095873L;

            @Override
            public double at(int c, double t) {
                final double cc = Math.pow(1 - g * mu * t / v0, 1 / eps);
                switch (c) {
                case 0:
                    return omega0 * eps / ((eps + 1) * g * mu)
                            * (g * mu * t * cc - v0 * cc + v0);
                case 1:
                    return omega0 * cc;
                case 2:
                    return omega0 * g * mu * cc / (eps * (g * mu * t - v0));
                default:
                    throw new IllegalArgumentException();
                }
            }

        };
        return new CurveRockAnalytic(x, y, a);
    }

    public double getDrawToTeeCurl() {
        return PropModelHelper.getDrawToTeeCurl(params);
    }

    public double getDrawToTeeTime() {
        return PropModelHelper.getDrawToTeeTime(params);
    }

    /**
     * UNUSED, see {@link #computeV0(double)}. Guess the initial speed.
     * 
     * @param y
     *            Start (distance to tee-line)
     * @param t
     *            from Hog to Hog. see ./doc/eiszeit.tex for details.
     * @return [meter/sec]
     */
    double getInitialSpeed(final double y, final double t) {
        double tmp;

        final double Y = IceSize.FAR_HOG_2_TEE - y;
        tmp = mu * g * t + 2.0 * IceSize.HOG_2_HOG / t;
        tmp *= tmp;
        // tmp += 4.0 * _mu * g * IceSize.HOG_2_HOG;

        assert Y <= tmp / (8.0 * g * mu); // ensure a positive sqrt-arg.

        tmp -= 8.0 * g * mu * Y;
        tmp = Math.sqrt(tmp) / 2.0;

        return tmp;
    }

    public void init(final Map<CharSequence, Measure> ice) {
        internalInit(ice);
        setDrawToTeeCurl(PropModelHelper.getDrawToTeeCurl(params));
        setDrawToTeeTime(PropModelHelper.getDrawToTeeTime(params));
    }

    public void setDrawToTeeCurl(final double drawToTeeCurl) {
        b = -drawToTeeCurl * 12.0 * eps * _R
                / MathVec.sqr(IceSize.FAR_HOG_2_TEE);
        PropModelHelper.setDrawToTeeCurl(params, drawToTeeCurl);
    }

    public void setDrawToTeeTime(final double drawToTeeTime) {
        mu = 2.0 * IceSize.FAR_HOG_2_TEE / (MathVec.sqr(drawToTeeTime) * g);
        PropModelHelper.setDrawToTeeTime(params, drawToTeeTime);
    }
}