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
package org.jcurl.core.swing;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;

import org.jcurl.core.base.Ice;

/**
 * JUnit test
 * 
 * @see org.jcurl.core.swing.Zoomer
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:ZoomerTest.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class ZoomerTest extends TestCase {

    private static void print(String msg, Point2D p) {
        System.out.println(msg + p.toString());
    }

    public void test010() {
        final float width = 3;
        final float height = 0;
        final Point2D.Float zero = new Point2D.Float(0, 0);
        final Point2D.Float ch = new Point2D.Float(0, 2);
        final Point2D.Float cb = new Point2D.Float(0, -1);
        final float h = ch.y - cb.y;
        final float sca = width / h;

        AffineTransform mat = new AffineTransform();
        mat.translate(width / 2, height / 2);
        mat.scale(sca, sca);
        mat.rotate(Math.PI / 2);
        mat.translate(0, -(ch.y + cb.y) / 2);

        final Point2D.Float cht = new Point2D.Float(0, 0);
        final Point2D.Float cbt = new Point2D.Float(0, 0);
        final Point2D.Float zerot = new Point2D.Float(0, 0);
        mat.transform(ch, cht);
        mat.transform(cb, cbt);
        mat.transform(zero, zerot);
        print("Hog :", cht);
        print("Back:", cbt);
        print("0,0 :", zerot);

        // assertEquals("", (float) 0, p.x, (float) 1e-1);
        // assertEquals("", (float) 0, p.y, (float) 1e-1);
    }

    public void test020() {
        final int width = 200;
        final Point2D.Float zero = new Point2D.Float(0, 0);
        final Point2D.Float ch = new Point2D.Float(0, Ice.HOG_2_TEE);
        final Point2D.Float cb = new Point2D.Float(0, -Ice.BACK_2_TEE);
        final float h = ch.y - cb.y;
        final float sca = width / h;

        AffineTransform mat = new AffineTransform();
        mat.translate(0, (cb.y + ch.y) / 2);
        mat.scale(sca, sca);
        // mat.rotate(Math.PI / 2);
        // mat.translate(width / 2, height / 2);

        final Point2D.Float cht = new Point2D.Float(0, 0);
        final Point2D.Float cbt = new Point2D.Float(0, 0);
        final Point2D.Float zerot = new Point2D.Float(0, 0);
        mat.transform(ch, cht);
        mat.transform(cb, cbt);
        mat.transform(zero, zerot);
        print("Hog :", cht);
        print("Back:", cbt);
        print("0,0 :", zerot);

        // assertEquals("", (float) 0, p.x, (float) 1e-1);
        // assertEquals("", (float) 0, p.y, (float) 1e-1);
    }

    public void test090_FixP() {
        double w0 = 1;
        double w = 3;
        double wf = 2;
        double d0 = 10;
        double d = 30;
        double df = d0 + (wf - w0) * d / w;
        assertEquals("", 20, df, 1e-9);
        d0 = 5;
        df = d0 + (wf - w0) * d / w;
        assertEquals("", 15, df, 1e-9);
        d0 = 10;
        d = 60;
        df = d0 + (wf - w0) * d / w;
        assertEquals("", 30, df, 1e-9);
    }

    public void test100_Zoom1by1() {
        final int S = WCComponent.SCALE;
        final double[] flat = new double[6];
        final Point2D dst = new Point2D.Double();
        final Point2D src = new Point2D.Double();
        final AffineTransform mat = new AffineTransform();
        final Rectangle2D wc = new Rectangle2D.Double(1, 2, 3, 4);
        Rectangle dc = new Rectangle(1, 2, 3, 4);
        final Point2D fix = new Point2D.Double(1, 2);
        Zoomer zom = new Zoomer("1:1", wc, fix);

        mat.setToIdentity();
        zom.computeWctoDcTrafo(dc, Orientation.N, false, mat);
        mat.getMatrix(flat);
        assertEquals("", -1.0 / S, flat[0], 1e-9);
        assertEquals("", 0.0 / S, flat[1], 1e-9);
        assertEquals("", 0.0 / S, flat[2], 1e-9);
        assertEquals("", -1.0 / S, flat[3], 1e-9);
        assertEquals("", 1.0, flat[4], 1e-9);
        assertEquals("", 6.0, flat[5], 1e-9);
        src.setLocation(fix);
        mat.transform(src, dst);
        assertEquals(new Point2D.Double(0.999, 5.998), dst);
        src.setLocation(3, 4);
        mat.transform(src, dst);
        assertEquals(new Point2D.Double(0.997, 5.996), dst);
    }

    public void test110_ZoomShift() {
        final double[] flat = new double[6];
        final Point2D dst = new Point2D.Double();
        final Point2D src = new Point2D.Double();
        final AffineTransform mat = new AffineTransform();
        final Rectangle2D wc = new Rectangle2D.Double(1, 2, 3, 4);
        Rectangle dc = new Rectangle(-1, -2, 3, 4);
        final Point2D fix = new Point2D.Double(1, 2);
        Zoomer zom = new Zoomer("1:1", wc, fix);

        mat.setToIdentity();
        zom.computeWctoDcTrafo(dc, Orientation.N, false, mat);
        mat.getMatrix(flat);
        assertEquals("", -0.0010, flat[0], 1e-9);
        assertEquals("", 0, flat[1], 1e-9);
        assertEquals("", 0, flat[2], 1e-9);
        assertEquals("", -0.0010, flat[3], 1e-9);
        assertEquals("", 1.0, flat[4], 1e-9);
        assertEquals("", 6, flat[5], 1e-9);
        src.setLocation(fix);
        mat.transform(src, dst);
        assertEquals(new Point2D.Double(0.999, 5.998), dst);
        src.setLocation(3, 4);
        mat.transform(src, dst);
        assertEquals(new Point2D.Double(0.997, 5.996), dst);
    }
}