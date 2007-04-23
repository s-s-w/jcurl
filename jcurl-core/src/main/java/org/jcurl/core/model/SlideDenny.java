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

import java.util.Map;

import org.jcurl.core.base.CurlerBase;
import org.jcurl.core.base.CurveRock;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PropModelHelper;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.helpers.Physics;
import org.jcurl.math.CurveFkt;
import org.jcurl.math.MathVec;
import org.jcurl.math.Polynome;
import org.jcurl.math.R1RNFunction;

/**
 * Mark Denny's curl-model. Motion of a curling rock acc. to "Curling rock
 * dynamics", Mark Denny, Canadian Journal of Physics, 1988, P. 295-304.
 * <p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SlideDenny.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class SlideDenny extends CurlerBase {

    private static final double _R = RockProps.DEFAULT.getRadius();

    private static final double eps = RockProps.DEFAULT.getInertia()
            / (RockProps.DEFAULT.getMass() * MathVec.sqr(_R));

    private static final double g = Physics.g;

    private double _mu;

    private double b;

    private double draw_curl;

    private double draw_time;

    public SlideDenny() {
        super();
    }

    @Override
    public CurveRock computeRc(double a0, double v0, double omega0,
            double sweepFactor) {
        throw new NotImplementedYetException();
    }

    @Override
    public double computeV0(final double intervalTime) {
        throw new NotImplementedYetException();
    }

    protected R1RNFunction createCurve(final double t0, final Rock pos,
            final Rock speed) {
        throw new NotImplementedYetException();
    }

    protected R1RNFunction createCurve(final Rock speed) {
        final Polynome[] x = new Polynome[3];
        x[0] = new Polynome(new double[] { 0, 1, 2, 3 });
        x[1] = new Polynome(new double[] { 0, 1, 2, 3 });
        x[2] = new Polynome(new double[] { 0, 1, 2, 3 });
        return new CurveFkt(x);
    }

    public double getDrawToTeeCurl() {
        throw new NotImplementedYetException();
    }

    public double getDrawToTeeTime() {
        throw new NotImplementedYetException();
    }

    /**
     * Guess the initial speed.
     * 
     * @param y
     *            Start (distance to tee-line)
     * @param t
     *            from Hog to Hog. see ./doc/eiszeit.tex for details.
     * @return [meter/sec]
     */
    public double getInitialSpeed(final double y, final double t) {
        double tmp;

        final double Y = IceSize.FAR_HOG_2_TEE - y;
        tmp = _mu * g * t + 2.0 * IceSize.HOG_2_HOG / t;
        tmp *= tmp;
        // tmp += 4.0 * _mu * g * IceSize.HOG_2_HOG;

        assert Y <= tmp / (8.0 * g * _mu); // ensure a positive sqrt-arg.

        tmp -= 8.0 * g * _mu * Y;
        tmp = Math.sqrt(tmp) / 2.0;

        return tmp;
    }

    public double getMu() {
        return _mu;
    }

    @Override
    public void init(final Map<CharSequence, DimVal> ice) {
        throw new NotImplementedYetException();
    }

    public void setDraw2Tee(final double T, final double X) {
        // super.setDraw2Tee(T, X);
        draw_time = T;
        _mu = 2.0 * IceSize.FAR_HOG_2_TEE / (MathVec.sqr(draw_time) * g);
        draw_curl = X;
        b = -draw_curl * 12.0 * eps * _R / MathVec.sqr(IceSize.FAR_HOG_2_TEE);
    }

    public void setDrawToTeeCurl(final double drawToTeeCurl) {
        PropModelHelper.setDrawToTeeCurl(params, drawToTeeCurl);
        throw new NotImplementedYetException();
    }

    public void setDrawToTeeTime(final double drawToTeeTime) {
        PropModelHelper.setDrawToTeeTime(params, drawToTeeTime);
        throw new NotImplementedYetException();
    }
}