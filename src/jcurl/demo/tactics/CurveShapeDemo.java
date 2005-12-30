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
package jcurl.demo.tactics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jcurl.core.JCLoggerFactory;
import jcurl.core.Version;
import jcurl.math.CurveBase;
import jcurl.math.CurveShape;

import org.apache.ugli.ULogger;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: CurveShapeDemo.java 209 2005-12-30 16:06:15Z mrohrmoser $
 */
public class CurveShapeDemo extends JFrame {

    private static class MyCurve extends CurveShape {

        public MyCurve(CurveBase curve) {
            super(curve);
        }

        protected GeneralPath computeGP(final CurveBase c) {
            final GeneralPath ret = new GeneralPath();
            ret.moveTo(100, 100);
            ret.curveTo(130, 220, 160, 220, 200, 200);
            //gp.quadTo(140, 220, 200, 200);
            ret.lineTo(300, 100);
            ret.lineTo(400, 200);
            ret.lineTo(500, 100);
            return ret;
        }
    }

    private static final ULogger log = JCLoggerFactory
            .getLogger(CurveShapeDemo.class);

    public static void main(String[] args) {
        log.info("Version: " + Version.find());
        final CurveShapeDemo frame = new CurveShapeDemo();
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
    public CurveShapeDemo() {
        s = new MyCurve(null);
    }
}