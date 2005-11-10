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
package jcurl.core.gui.demo1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jcurl.core.JCLoggerFactory;
import jcurl.core.Version;

import org.apache.ugli.ULogger;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurveDemo extends JFrame {

    private static class MyCurve implements Shape {

        private final Point2D[] cs;

        public MyCurve() {
            cs = new Point2D[5];
            cs[0] = new Point2D.Double(10, 10);
            cs[1] = new Point2D.Double(20, 20);
            cs[2] = new Point2D.Double(30, 10);
            cs[3] = new Point2D.Double(40, 20);
            cs[4] = new Point2D.Double(50, 10);
        }

        public boolean contains(double x, double y) {
            return false;
        }

        public boolean contains(double x, double y, double w, double h) {
            return false;
        }

        public boolean contains(Point2D p) {
            return false;
        }

        public boolean contains(Rectangle2D r) {
            return false;
        }

        public Rectangle getBounds() {
            return new Rectangle(0, 0, 70, 30);
        }

        public Rectangle2D getBounds2D() {
            return getBounds();
        }

        public PathIterator getPathIterator(AffineTransform at, double flatness) {
            return new PathIterator() {
                private int curr = 0;

                public int getWindingRule() {
                    return WIND_EVEN_ODD;
                }

                public void next() {
                    if (curr < cs.length) {
                        curr++;
                        log.debug("curr=" + curr);
                    }
                }

                public boolean isDone() {
                    return curr == cs.length;
                }

                public int currentSegment(double[] coords) {
                    // redirect
                    final float[] tmp = new float[coords.length];
                    final int ret = currentSegment(tmp);
                    for (int i = tmp.length - 1; i >= 0; i--)
                        coords[i] = tmp[i];
                    return ret;
                }

                public int currentSegment(float[] coords) {
                    log.debug("currentSegment curr=" + curr);
                    if (coords.length == 2) {
                        coords[0] = (float) cs[curr].getX();
                        coords[1] = (float) cs[curr].getY();
                        return curr == 0 ? SEG_MOVETO : SEG_CUBICTO;
                    }
                    if (curr < cs.length - 2) {
                        coords[0] = (float) cs[curr].getX();
                        coords[1] = (float) cs[curr].getY();
                        coords[2] = (float) cs[curr + 1].getX();
                        coords[3] = (float) cs[curr + 1].getY();
                        coords[4] = (float) cs[curr + 2].getX();
                        coords[5] = (float) cs[curr + 2].getY();
                        return SEG_CUBICTO;
                    }
                    return SEG_CLOSE;
                }
            };
        }

        public PathIterator getPathIterator(AffineTransform at) {
            return getPathIterator(at, 0.5);
        }

        public boolean intersects(double x, double y, double w, double h) {
            return false;
        }

        public boolean intersects(Rectangle2D r) {
            return false;
        }
    }

    private static final ULogger log = JCLoggerFactory
            .getLogger(CurveDemo.class);

    public static void main(String[] args) {
        log.info("Version: " + Version.find());
        final CurveDemo frame = new CurveDemo();
        frame.setSize(500, 400);
        frame.setVisible(true);
        frame.setContentPane(new JPanel() {
            final Stroke st = new BasicStroke(20, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND, 0);

            public void paintComponent(Graphics g) {
                this.setBackground(new Color(255, 255, 255));
                super.paintComponent(g);
                final Graphics2D g2 = (Graphics2D) g;
                g2.scale(0.75, 0.5);
                g2.setPaint(new Color(0, 0, 255));
                g2.setStroke(st);
                g2.drawLine(0, 0, 500, 200);
                g2.setPaint(new Color(255, 255, 0, 128));
                g2.draw(frame.s);
            }
        });
    }

    private final Shape s;

    /**
     * @throws java.awt.HeadlessException
     */
    public CurveDemo() {
        //s = new MyCurve();
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(100, 100);
        gp.curveTo(130, 220, 160, 220, 200, 200);
        //gp.quadTo(140, 220, 200, 200);
        gp.lineTo(300, 100);
        gp.lineTo(400, 200);
        gp.lineTo(500, 100);
        s = gp;
        //s = new MyCurve();
    }
}