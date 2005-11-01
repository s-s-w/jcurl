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

import jcurl.core.JCLoggerFactory;
import jcurl.core.Source;
import jcurl.core.dto.RockDouble;
import jcurl.core.dto.RockSetProps;
import jcurl.math.CurveBase;
import jcurl.math.Polynome;
import jcurl.model.PositionSet;
import jcurl.model.Rock;
import jcurl.model.SpeedSet;
import junit.framework.TestCase;

import org.apache.ugli.ULogger;

/**
 * JUnit test.
 * 
 * @see jcurl.sim.model.SlideStraight
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SlideStraightTest extends TestCase {
    private static final ULogger log = JCLoggerFactory
            .getLogger(SlideStraightTest.class);

    private final SlideStraight s = new SlideStraight();

    private final PositionSet pos = PositionSet.allOut();

    private final SpeedSet speed = new SpeedSet();

    public void setUp() {
        // initial state
        PositionSet.allOut(pos);
        pos.getDark(0).setLocation(0, 5, 0);
        pos.getLight(0).setLocation(0.2, 2.5);
        pos.getLight(1).setLocation(1.0, 1.5);
        PositionSet.allZero(speed);
        speed.getDark(0).setLocation(0, -1.0, 0.75);
        s.reset(0, pos, speed, RockSetProps.DEFAULT);
    }

    public void test010_createCurve() {
        Rock x0 = new RockDouble(0, 0, 0);
        Rock v0 = new RockDouble(0, 0, 0);
        CurveBase c = s.createCurve(0, x0, v0);
        assertEquals("", 0, c.getC(0, 0, 0), 1e-9);
        assertEquals("", 0, c.getC(1, 0, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 0), 1e-9);

        assertEquals("", 0, c.getC(0, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(1, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 0), 1e-9);

        assertEquals("", 0, c.getC(0, 0, 1), 1e-9);
        assertEquals("", 0, c.getC(1, 0, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 1), 1e-9);

        assertEquals("", 0, c.getC(0, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(1, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 1), 1e-9);

        x0 = new RockDouble(1, 2, 0);
        v0 = new RockDouble(0, 0, 0);
        c = s.createCurve(0, x0, v0);
        assertEquals("", 1, c.getC(0, 0, 0), 1e-9);
        assertEquals("", 2, c.getC(1, 0, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 0), 1e-9);

        assertEquals("", 0, c.getC(0, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(1, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 0), 1e-9);

        assertEquals("", 1, c.getC(0, 0, 1), 1e-9);
        assertEquals("", 2, c.getC(1, 0, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 1), 1e-9);

        assertEquals("", 0, c.getC(0, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(1, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 1), 1e-9);

        x0 = new RockDouble(1, 2, 0);
        v0 = new RockDouble(0.1, 0.2, 0);
        c = s.createCurve(0, x0, v0);
        assertEquals("", 1, c.getC(0, 0, 0), 1e-9);
        assertEquals("", 2, c.getC(1, 0, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 0), 1e-9);

        assertEquals("", 0.1, c.getC(0, 1, 0), 1e-9);
        assertEquals("", 0.2, c.getC(1, 1, 0), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 0), 1e-9);

        //assertEquals("", 1.07603611502, c.getC(0, 0, 1), 1e-9);
        //assertEquals("", 2.15207223004, c.getC(1, 0, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 0, 1), 1e-9);

        //assertEquals("", 0.052072230042, c.getC(0, 1, 1), 1e-9);
        //assertEquals("", 0.104144460085, c.getC(1, 1, 1), 1e-9);
        assertEquals("", 0, c.getC(2, 1, 1), 1e-9);
    }

    private static final double sqr(final double a) {
        return a * a;
    }

    public void test020_createCurve() {
        log.debug("start");
        double t0 = 2.894295921183459;
        double dt = 0;
        Rock x0 = new RockDouble(0.8425835967063904, 1.7610043287277222, 0.0);
        Rock v0 = new RockDouble(0.1020171046257019, 0.06152835115790367, 0.0);

        double v = Math.sqrt(sqr(v0.getX()) + sqr(v0.getY()));
        double a = s.getAccel();
        double[] par = Polynome.getPolyParams(t0, 0, v, a);
        log.info("raw : " + Polynome.toString(par));

        assertEquals("", 0, Polynome.poly(0, t0 + dt, par), 1e-9);
        assertEquals("", v, Polynome.poly(1, t0 + dt, par), 1e-9);
        assertEquals("", a, Polynome.poly(2, t0 + dt, par), 1e-9);

        dt = 0.1;
        assertEquals("", 0.011459990937692677, Polynome.poly(0, t0 + dt, par),
                1e-9);
        assertEquals("", 0.11006448548776704, Polynome.poly(1, t0 + dt, par),
                1e-9);
        assertEquals("", a, Polynome.poly(2, t0 + dt, par), 1e-9);

        dt = 0;
        CurveBase c = s.createCurve(t0, x0, v0);
        //untransformed : p(x) = 0.10406485628694145 + 0.11913533326608741*x +
        // -0.0535848758171096*x**2
        //Curve x : p(x) = 0.9316956597747996*x**0 + 0.1020171046257019*x**1 +
        // -0.04588541226791033*x**2
        //Curve y : p(x) = 1.8147494171518157*x**0 + 0.061528351157903664*x**1
        // + -0.027674317649021823*x**2
        //Curve z : p(x) = 0.0*x**0 + 0.0*x**1 + 0.0*x**2
        assertEquals("", x0.getX(), c.getC(0, 0, t0 + dt), 1e-9);
        assertEquals("", x0.getY(), c.getC(1, 0, t0 + dt), 1e-9);
        assertEquals("", x0.getZ(), c.getC(2, 0, t0 + dt), 1e-9);

        assertEquals("", v0.getX(), c.getC(0, 1, t0 + dt), 1e-9);
        assertEquals("", v0.getY(), c.getC(1, 1, t0 + dt), 1e-9);
        assertEquals("", v0.getZ(), c.getC(2, 1, t0 + dt), 1e-9);

        dt = 0.01;
        assertEquals("", 0.10124035636683068, c.getC(0, 1, t0 + dt), 1e-9);
        assertEquals("", 0.06105988030874099, c.getC(1, 1, t0 + dt), 1e-9);
        assertEquals("", v0.getZ(), c.getC(2, 1, t0 + dt), 1e-9);
    }

    public void test100() {
        double t = 0;
        assertEquals("", 2.26999338899, t = s.estimateNextHit(pos, speed), 1e-6);
        s.getPos(t, pos);
        assertEquals("", 0, pos.getDark(0).getX(), 1e-6);
        assertEquals("", 2.9637110233306885, pos.getDark(0).getY(), 1e-6);
        assertEquals("", 0, speed.getDark(0).getX(), 1e-6);
        assertEquals("", -1, speed.getDark(0).getY(), 1e-6);
        assertEquals("", 0.2, pos.getLight(0).getX(), 1e-6);
        assertEquals("", 2.5, pos.getLight(0).getY(), 1e-6);
        assertEquals("", 1.0, pos.getLight(1).getX(), 1e-6);
        assertEquals("", 1.5, pos.getLight(1).getY(), 1e-6);

        s.getSpeed(t, speed);
        assertEquals("", 0.2943038177141753, t = s.estimateNextHit(pos, speed),
                1e-6);
    }

    public void test110() {
        final PositionSet pos = PositionSet.allOut();
        pos.getDark(0).setLocation(0, 5, 0);
        pos.getLight(0).setLocation(0.2, 2.5);
        pos.getLight(1).setLocation(1.0, 1.5);
        final SpeedSet speed = new SpeedSet();
        speed.getDark(0).setLocation(0, -1.5, 0.75);
        // dynamics engines
        final Source src = new SlideStraight();
        src.reset(0, pos, speed, RockSetProps.DEFAULT);

        src.getPos(2.85, pos);
        src.getPos(2.9, pos);
    }
}