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
package jcurl.math.dom;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

import jcurl.exp.mr.math.Calc0;
import jcurl.exp.mr.math.Calc1;
import junit.framework.TestCase;

/**
 * JUnit Test.
 * 
 * @see jcurl.exp.mr.math.Calc0
 * @see jcurl.exp.mr.math.Calc1
 * @see jcurl.math.dom.MathDom
 * @see jcurl.math.dom.ParserInfix
 * @see jcurl.math.dom.DomWalkerInfix
 * @see jcurl.math.dom.DomWalkerPostfix
 * @see jcurl.math.dom.DomWalkerEval
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MathDomTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MathDomTest.class);
    }

    public void test010_0() throws IOException {
        Calc0 c = new Calc0(" 2 * 4 +3 ");
        assertEquals("", 11, c.compute(), 1e-9);

        c = new Calc0(" 2 * (4 +3) ");
        assertEquals("", 14, c.compute(), 1e-9);

        c = new Calc0(" 2 / 4 +3 ");
        assertEquals("", 3.5, c.compute(), 1e-9);
    }

    public void test020_1() throws IOException {
        Calc1 c = new Calc1(" 2 * 4 +3 ");
        assertEquals("", 11, c.compute(), 1e-9);

        c = new Calc1(" 2 * (3-4) ");
        assertEquals("", -2, c.compute(), 1e-9);

        c = new Calc1(" 2 * abs(3-4) ");
        assertEquals("", 2, c.compute(), 1e-9);

        c = new Calc1(" 2 / 4 +3 ");
        assertEquals("", 3.5, c.compute(), 1e-9);
    }

    public void test030_Dom() throws ParseException {
        MathDom.Node n = ParserInfix.parse(" 2 * 4 +3 ");
        assertEquals("2.0 * 4.0 + 3.0", DomWalkerInfix.toString(n));
        assertEquals(" +  * 2.0 4.0 3.0", DomWalkerPostfix.toString(n));
        assertEquals("", 11, DomWalkerEval.eval(n), 1e-9);

        n = ParserInfix.parse(" 2 * (3+-4) ");
        assertEquals("2.0 * (3.0 + -4.0)", DomWalkerInfix.toString(n));
        assertEquals(" * 2.0  + 3.0 -4.0", DomWalkerPostfix.toString(n));
        assertEquals("", -2, DomWalkerEval.eval(n), 1e-9);

        n = ParserInfix.parse(" 2 * abs(3-4) ");
        assertEquals("2.0 * abs(3.0 - 4.0)", DomWalkerInfix.toString(n));
        assertEquals(" * 2.0 abs  - 3.0 4.0", DomWalkerPostfix.toString(n));
        assertEquals("", 2, DomWalkerEval.eval(n), 1e-9);

        n = ParserInfix.parse(" 2 / 4 + 3 ");
        assertEquals("2.0 / 4.0 + 3.0", DomWalkerInfix.toString(n));
        assertEquals(" +  / 2.0 4.0 3.0", DomWalkerPostfix.toString(n));
        assertEquals("", 3.5, DomWalkerEval.eval(n), 1e-9);
    }

    public void test040_Param() throws ParseException {
        Map p = new TreeMap();
        MathDom.Node n = ParserInfix.parse("sqrt2=sqrt(2)");
        assertEquals("sqrt2 = sqrt(2.0)", DomWalkerInfix.toString(n));
        assertEquals(" = sqrt2 sqrt 2.0", DomWalkerPostfix.toString(n));
        assertEquals("", Math.sqrt(2), DomWalkerEval.eval(n, p), 1e-9);
        assertEquals("", Math.sqrt(2), ((Number) p.get("sqrt2")).doubleValue(),
                1e-9);

        n = ParserInfix.parse("sqrt2 * sqrt2");
        assertEquals("", 2, DomWalkerEval.eval(n, p), 1e-9);
    }

    public void test050_Loop() throws ParseException {
        // parsing
        int loop = 100000;
        long start = System.currentTimeMillis();
        for (int i = loop - 1; i >= 0; i--) {
            ParserInfix.parse(" 2 * 4 +3 ");
        }
        long dt = System.currentTimeMillis() - start;
        long cps = loop * 1000 / dt;
        assertTrue("MatDom creation was too slow, only " + cps
                + " calls per second", cps > 50000);

        // evaluation
        final MathDom.Node n = ParserInfix.parse(" 2 * 4 +3 ");
        final DomWalkerEval de = new DomWalkerEval();
        start = System.currentTimeMillis();
        for (int i = loop - 1; i >= 0; i--) {
            de.walk(n);
        }
        dt = System.currentTimeMillis() - start;
        cps = loop * 1000 / dt;
        assertTrue("MatDom evaluation was too slow, only " + cps
                + " calls per second", cps > 2000000);

        // hard coded arithmetics
        loop *= 100;
        start = System.currentTimeMillis();
        for (int i = loop - 1; i >= 0; i--) {
            double d = 2.0 * 4.0 + 3.0;
        }
        dt = System.currentTimeMillis() - start;
        cps = loop * 1000 / dt;
        assertTrue("Built-in math evaluation was too slow, only " + cps
                + " calls per second", cps > 18000000);
    }
}