/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.zui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.Unit;
import org.jcurl.core.log.JCLoggerFactory;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PInterpolatingActivity;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;

public class BroomPrompt {

    /** All from back to back */
    static final Rectangle2D completeP;
    /** House area plus 1 rock margin plus "out" rock space. */
    static final Rectangle2D houseP;
    private static final Log log = JCLoggerFactory.getLogger(BroomPrompt.class);
    /**
     * Inter-hog area area plus house area plus 1 rock margin plus "out" rock
     * space.
     */
    static final Rectangle2D sheetP;

    /** 12-foot circle plus 1 rock */
    static final Rectangle2D twelveP;

    static {
        final double r2 = 2 * RockProps.DEFAULT.getRadius();
        final double x = IceSize.SIDE_2_CENTER + r2;
        houseP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2), 2 * x,
                IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        final double c12 = r2 + Unit.f2m(6.0);
        twelveP = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
        sheetP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        completeP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE
                + IceSize.HOG_2_HOG + IceSize.HACK_2_HOG + r2), 2 * x,
                IceSize.HOG_2_HOG + 2 * IceSize.HACK_2_HOG);
    }

    static PCamera animateToBounds(final PCamera c, final Rectangle2D r,
            final long duration) {
        final PInterpolatingActivity pi = c.animateViewToCenterBounds(r, true,
                duration);
        return c;
    }

    /**
     * Pointing along the positive X-axis, ending at 0,0
     * 
     * @param width
     * @param length
     * @param tail
     * @return
     */
    private static Shape createArrowHead(float width, float length,
            final float tail) {
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(-length, width);
        gp.lineTo(-length + tail, 0);
        gp.lineTo(-length, -width);
        gp.closePath();
        return gp;
    }

    static PNode createBroomPrompt(final Paint sp) {
        final float ro = RockProps.DEFAULT.getRadius();
        final float ri = 0.5F * ro;
        final Shape outer = new Arc2D.Float(-ro, -ro, 2 * ro, 2 * ro, 0, 270,
                Arc2D.OPEN);
        final Shape inner = new Arc2D.Float(-ri, -ri, 2 * ri, 2 * ri, 0, 270,
                Arc2D.OPEN);
        final Font fo = new Font("SansSerif", Font.BOLD, 1);
        final PNode p = new PComposite();
        p.setTransform(AffineTransform.getScaleInstance(1, -1));
        final Stroke st = new BasicStroke(0.01f);
        p.addChild(node(outer, st, sp));
        p.addChild(node(inner, st, sp));
        p.addChild(node(new Line2D.Float(0, 1.2f * ro, 0, -5 * ro), st, sp));
        p.addChild(node(new Line2D.Float(-1.2f * ro, 0, 1.2f * ro, 0), st, sp));

        final int angle = 50;
        p.addChild(node(new Arc2D.Float(-ro, -ro, 2 * ro, 2 * ro, 270, angle,
                Arc2D.OPEN), st, sp));

        float f = ro / 20;
        PPath s = node(createArrowHead(f, 3 * f, 0.5f * f), st, sp);
        double ar = Math.PI * (angle + 6) / 180;
        s.translate(ro * Math.sin(ar), ro * Math.cos(ar));
        ar = Math.PI * angle / 180;
        s.rotate(-ar);
        p.addChild(s);

        f = 3.5f / 5.0f;
        s = node(createSlider(0.4f * ro), null, null);
        s.setPaint(interpolateRGB(Color.BLUE, Color.RED, f));
        s.translate(0, -5 * f * ro);
        p.addChild(s);
        return p;
    }

    private static Shape createSlider(final float f) {
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(f, f);
        gp.lineTo(4 * f, f);
        gp.lineTo(4 * f, -f);
        gp.lineTo(f, -f);
        gp.lineTo(-f, f);
        gp.lineTo(-4 * f, f);
        gp.lineTo(-4 * f, -f);
        gp.lineTo(-f, -f);
        gp.closePath();
        return gp;
    }

    private static float interpolate(final float a, final float b,
            final double ratio) {
        return (float) (a + ratio * (b - a));
    }

    private static int interpolate(final int a, final int b, final double ratio) {
        return (int) (a + ratio * (b - a));
    }

    private static Color interpolateHSB(final Color a, final Color b,
            final double ratio) {
        final float[] _a = new float[3];
        final float[] _b = new float[3];
        Color.RGBtoHSB(a.getRed(), a.getGreen(), a.getBlue(), _a);
        Color.RGBtoHSB(b.getRed(), b.getGreen(), b.getBlue(), _b);

        return new Color(Color.HSBtoRGB(interpolate(_a[0], _b[0], ratio),
                interpolate(_a[1], _b[1], ratio), interpolate(_a[2], _b[2],
                        ratio)));
    }

    private static Color interpolateRGB(final Color a, final Color b,
            final double ratio) {
        return new Color(interpolate(a.getRed(), b.getRed(), ratio),
                interpolate(a.getGreen(), b.getGreen(), ratio), interpolate(a
                        .getBlue(), b.getBlue(), ratio));
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFrame application = new JFrame();
                application.setTitle("JCurl BroomPrompt");
                application
                        .setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                final PCanvas pc = new PCanvas();
                pc
                        .setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
                pc
                        .setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
                pc.setBackground(new Color(0xE8E8FF));
                pc.getLayer().addChild(createBroomPrompt(Color.BLACK));
                application.getContentPane().add(pc);
                application.setSize(800, 600);
                application.setVisible(true);
                animateToBounds(pc.getCamera(), houseP, 500);
            }
        });
    }

    private static PPath node(final Shape sh, final Stroke st, final Paint sp) {
        final PPath s = new PPath(sh);
        s.setStroke(st);
        s.setStrokePaint(sp);
        return s;
    }
}