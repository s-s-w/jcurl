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
package jcurl.sim.model;

import java.awt.Polygon;

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockProps;
import jcurl.core.dto.RockSetProps;

/**
 * Mark Denny's curl-model. Motion of a curling rock acc. to "Curling rock
 * dynamics", Mark Denny, Canadian Journal of Physics, 1988, P. 295-304.
 * <p>
 * Requires heavy debugging.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideDenny extends SlideSimple {

    private static class DennyDat {

        double inispin;

        double reset;

        double tau;

        final double[][] v = new double[2][4];

        final double[][] x = new double[2][5];
    }

    private static final double _R = 6.5e-2;

    private static final double eps = RockProps.DEFAULT.getInertia()
            / (RockProps.DEFAULT.getMass() * sqr(_R));

    private static final double g = 9.81;

    private double _mu;

    private double b;

    private final DennyDat[] dat = new DennyDat[16];

    private double draw_curl;

    private double draw_time;

    public SlideDenny() {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            dat[i] = new DennyDat();
        setDraw2Tee(23, 0.8);
    }

    /**
     * Move a single rock according to the given time.
     * 
     * @return '1' if the rock 'r' is still in motion <b>after </b> the given
     *         period 'dt', '0' otherwise.
     */
    protected int computeDt(final Rock pos, final Rock speed, long tN,
            final long _DT, int i) {
        double tNow = 1e-3 * tN;
        double DT = 1e-3 * _DT;
        double dt = tNow + DT - dat[i].reset;

        pos.setZ(pos.getZ() + DT * speed.getZ());
        final int ret;
        if (dt < dat[i].tau) {
            speed.setX(poly(dt, 3, dat[i].v[0]));
            speed.setY(poly(dt, 3, dat[i].v[1]));
            speed.setZ(dat[i].inispin
                    * Math.pow(1.0 - dt / dat[i].tau, 1.0 / eps));
            ret = 1;
        } else {
            speed.setLocation(0, 0, 0); // set all speeds to 0.0
            dt = dat[i].tau; // put the rocks to their final location
            ret = 0;
        }
        pos.setX(poly(dt, 4, dat[i].x[0]));
        pos.setY(poly(dt, 4, dat[i].x[1]));

        return ret;
    }

    /**
     * Guess the initial speed.
     * 
     * @param y
     *            Start (distance to tee-line)
     * @param t
     *            from Hog to Hog. see ./doc/eiszeit.tex for details.
     */
    public double getInitialSpeed(final double y, final double t) {
        double tmp;

        final double Y = Ice.FAR_HOG_2_TEE - y;
        tmp = _mu * g * t + 2.0 * Ice.HOG_2_HOG / t;
        tmp *= tmp;
        //        tmp += 4.0 * _mu * g * Ice.HOG_2_HOG;

        assert Y <= tmp / (8.0 * g * _mu); // ensure a positive sqrt-arg.

        tmp -= 8.0 * g * _mu * Y;
        tmp = Math.sqrt(tmp) / 2.0;

        return tmp;
    }

    public double getMu() {
        return _mu;
    }

    /**
     * This version recomputes ALL rocks' parameters, no matter if the rock
     * stands still, moves untouched or had a hit recently. Later versions could
     * be smarter here. Changes should only be necessary local to this routine!
     */
    public void reset(long startTime, RockSet pos, RockSet speed,
            RockSetProps props) {
        /*
         * Woran sind Steine mit 'unstetigem' Lauf zu erkennen? - entweder via
         * einem mitgegebenem Flag (Funktions-Parameter oder Member) - eine
         * Abweichung des berechneten Orts/Geschwindigkeit von der gegebenen (S)
         * Das oben erwaehnte Flag koennte gesetzt werden von: - movetoOut/Home
         * (gekapselt, o.k.) - dem hitter ('Benutzer'-Sache ungekapselt!)
         */
        // mark dt_nexthit beeing 'not computed yet'.
        // ??? reset(S);
        final double t0 = 1e-3 * startTime;
        // Now we need to compute the p4-polygones for each rock and axis.
        // x-axis: a[ 0-15 ][ 0 ][ 0-4 ]
        // y-axis: a[ 0-15 ][ 1 ][ 0-4 ]
        final double[][] tmp = new double[2][5];
        final double[] v = new double[2];
        double vabs;
        for (int i = 0; i < RockSet.ROCKS_PER_SET; i++) {

            v[0] = speed.getRock(i).getX();
            v[1] = speed.getRock(i).getY();
            vabs = hypot(v[0], v[1]);

            // Here we could be a little bit smarter and 'break'
            // e.g. if vabs is zero. But how to get the initial lineup then?

            dat[i].reset = t0;
            // Set the parameters in 'tmp' acc. to Denny's coordinates:

            if (0.0 != (dat[i].tau = vabs / (_mu * g))) {
                dat[i].inispin = speed.getRock(i).getZ();
                final double C = sgn(speed.getRock(i).getZ()) * b * sqr(vabs)
                        / (4.0 * eps * _R * dat[i].tau);

                tmp[0][4] = C / (4.0 * dat[i].tau);
                tmp[0][3] = -C / 3.0;
                tmp[1][2] = -vabs / (2.0 * dat[i].tau);
                tmp[1][1] = vabs;

                // reduce v to a unit-vector:
                v[0] /= vabs;
                v[1] /= vabs;
            }

            // now do the trafo to real coordinates:
            for (int j = 1; j < 5; j++) {
                dat[i].x[1][j] = tmp[1][j] * v[1] + tmp[0][j] * v[0];
                dat[i].x[0][j] = tmp[1][j] * v[0] - tmp[0][j] * v[1];
                // last but not least compute the speed-parameters in v.
                // This is just a derivative.
                dat[i].v[0][j - 1] = j * dat[i].x[0][j];
                dat[i].v[1][j - 1] = j * dat[i].x[1][j];
            }
            // and add the shift:
            dat[i].x[0][0] = pos.getRock(i).getX();
            dat[i].x[1][0] = pos.getRock(i).getY();
        }
    }

    public void setDraw2Tee(final double T, final double X) {
        draw_time = T;
        //        FricRockIce(0, 2.0 * SheetSize::FAR_HOG_2_TEE / (sqr(draw_time)*g) );
        _mu = 2.0 * Ice.FAR_HOG_2_TEE / (sqr(draw_time) * g);

        //        Curl(0,X);
        draw_curl = X;
        b = -draw_curl * 12.0 * eps * _R / sqr(Ice.FAR_HOG_2_TEE);
    }
}