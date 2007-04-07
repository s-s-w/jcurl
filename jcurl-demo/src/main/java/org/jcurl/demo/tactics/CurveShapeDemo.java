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
package org.jcurl.demo.tactics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.Version;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.CurveFkt;
import org.jcurl.math.CurveShape;
import org.jcurl.math.Polynome;
import org.jcurl.math.R1R1Function;
import org.jcurl.math.R1RNFunction;

/**
 * Demonstrate how to draw a {@link org.jcurl.math.R1RNFunctionImpl}converted to a
 * {@link java.awt.Shape}.
 * 
 * @see org.jcurl.math.CurveShape
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CurveShapeDemo.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CurveShapeDemo extends JFrame {

    private static final long serialVersionUID = 1596300831419412317L;

    private static final Log log = JCLoggerFactory
            .getLogger(CurveShapeDemo.class);

    public static void main(String[] args) {
        log.info("Version: " + Version.find());
        final R1RNFunction c;
        {
            final R1R1Function[] f = new R1R1Function[2];
            final double[] fx = { 200, 150 };
            final double[] fy = { 4, 4, 4, 4, 4 };
            f[0] = new Polynome(fx);
            f[1] = new Polynome(fy);
            c = new CurveFkt(f);
        }
        final CurveShapeDemo frame = new CurveShapeDemo(c);
        frame.setSize(500, 400);
        frame.setVisible(true);
        frame.setContentPane(new JPanel() {

            private static final long serialVersionUID = -3582299332757831635L;

            private final double[] sections = new double[10];

            final Stroke st = new BasicStroke(20, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND, 0);

            public void paintComponent(Graphics g) {
                setBackground(new Color(255, 255, 255));
                super.paintComponent(g);
                setBackground(new Color(255, 255, 255));
                final Graphics2D g2 = (Graphics2D) g;
                g2.scale(0.75, 0.75);
                g2.setPaint(new Color(0, 0, 255));
                g2.setStroke(st);
                g2.drawLine(0, 0, 650, 500);
                g2.setPaint(new Color(255, 170, 0, 128));
                // FIXME g2.draw(CurveShape.approximate(frame.curve, CurveShape.sections(-1, 3, sections)));
            }
        });
    }

    private final R1RNFunction curve;

    public CurveShapeDemo(final R1RNFunction c) {
        curve = c;
    }
}