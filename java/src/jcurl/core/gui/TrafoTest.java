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
package jcurl.core.gui;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import jcurl.core.dto.Ice;
import junit.framework.TestCase;

/**
 * Simple 2d matrix operations test.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class TrafoTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TrafoTest.class);
    }

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

        //assertEquals("", (float) 0, p.x, (float) 1e-1);
        //assertEquals("", (float) 0, p.y, (float) 1e-1);
    }

    public void _test020() {
        final int width = 200;
        final Point2D.Float zero = new Point2D.Float(0, 0);
        final Point2D.Float ch = new Point2D.Float(0, Ice.HOG_2_TEE);
        final Point2D.Float cb = new Point2D.Float(0, -Ice.BACK_2_TEE);
        final float h = ch.y - cb.y;
        final float sca = width / h;

        AffineTransform mat = new AffineTransform();
        mat.translate(0, (cb.y + ch.y) / 2);
        mat.scale(sca, sca);
        //        mat.rotate(Math.PI / 2);
        //        mat.translate(width / 2, height / 2);

        final Point2D.Float cht = new Point2D.Float(0, 0);
        final Point2D.Float cbt = new Point2D.Float(0, 0);
        final Point2D.Float zerot = new Point2D.Float(0, 0);
        mat.transform(ch, cht);
        mat.transform(cb, cbt);
        mat.transform(zero, zerot);
        print("Hog :", cht);
        print("Back:", cbt);
        print("0,0 :", zerot);

        //assertEquals("", (float) 0, p.x, (float) 1e-1);
        //assertEquals("", (float) 0, p.y, (float) 1e-1);
    }
}