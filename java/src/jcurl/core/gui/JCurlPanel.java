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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import jcurl.core.RockSet;
import jcurl.core.TargetDiscrete;

/**
 * "Stupid" display.
 * 
 * @see jcurl.core.gui.IcePainter
 * @see jcurl.core.gui.RockPainter
 * @see jcurl.core.gui.RealTimePlayer
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlPanel extends JPanel implements TargetDiscrete {
    /**
     * Scale WC a bit to avoid int rounding errors. This is relevant for all
     * painters only. WC objects (rocks etc.) remain unaffected by this.
     */
    public static final int SCALE = 1000;

    private static final Color timeB = new Color(0.9F, 0.9F, 1.0F, 0.75F);

    private static final Color timeC = Color.BLACK;

    private static final Font timeF = new Font("SansSerif", Font.PLAIN, 10);

    /**
     * Re-compute the transformation from world-coordinates to display
     * coordinates.
     * 
     * @param wid
     * @param hei
     * @param zoom
     * @param uniform
     * @param mat
     */
    private static void computeTrafo(final int wid, final int hei,
            final ZoomArea zoom, final boolean uniform,
            final AffineTransform mat) {
        float x_sca = wid / (zoom.tl.y - zoom.br.y);
        float y_sca = hei / (-zoom.tl.x + zoom.br.x);
        if (uniform) {
            if (y_sca > x_sca)
                y_sca = x_sca;
            else
                x_sca = y_sca;
        }
        // then build the transformation wc -> dc matrix
        mat.setToIdentity();

        // add the transformations in reverse order!
        mat.translate(wid / 2, hei / 2);
        mat.scale(x_sca / SCALE, -y_sca / SCALE);
        mat.rotate(Math.PI / 2);
        mat
                .translate((zoom.tl.x + zoom.br.x) / 2,
                        -(zoom.tl.y + zoom.br.y) / 2);
    }

    private final IcePainter iceP;

    private final AffineTransform mat = new AffineTransform();

    private int oldHei = -1;

    private int oldWid = -1;

    private final RockPainter rockP;

    private RockSet rocks;

    private double time = 0;

    private ZoomArea zom;

    public JCurlPanel(final RockSet rocks, final ZoomArea zoom,
            final IcePainter iceP, final RockPainter rockP) {
        this.rocks = rocks == null ? RockSet.allHome() : rocks;
        this.zom = zoom == null ? ZoomArea.HOUSE : zoom;
        this.iceP = iceP == null ? new IcePainter() : iceP;
        this.rockP = rockP == null ? new RockPainter() : rockP;
    }

    public JCurlPanel(ZoomArea zoom) {
        this(null, zoom, null, null);
    }

    /**
     * Convert display to world-coordinates.
     * 
     * @param dc
     * @param wc
     * @return world coordinates
     */
    public Point2D dc2wc(final Point2D dc, Point2D wc) {
        try {
            wc = mat.inverseTransform(dc, wc);
            wc.setLocation(wc.getX() / SCALE, wc.getY() / SCALE);
            return wc;
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException("Why uninvertible?", e);
        }
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        final AffineTransform ot = g2.getTransform();

        final int w = this.getWidth();
        final int h = this.getHeight();
        g2.setPaint(iceP.color.backGround);
        g2.fillRect(0, 0, w, h);

        if (oldWid != w || oldHei != h)
            // re-compute the zoomfactor
            computeTrafo(oldWid = w, oldHei = h, zom, true, mat);
        // paint WC stuff
        g2.transform(mat);
        iceP.paintIce(g2);
        rockP.paintRocks(g2, rocks, RockSet.ALL_MASK);
        g2.setTransform(ot);
        // paint additional stuff
        g2.setColor(timeB);
        g2.fillRect(w - 70, 0, 70, 20);
        //g2.fillRect(0, 0, w, 20);
        g2.setFont(timeF);
        g2.setColor(timeC);
        g2.drawString(Double.toString(time), w - 70 + 10, 3 * 20 / 4);
    }

    /**
     * Triggers a repaint.
     * 
     * @param time
     *            [sec]
     * @param rocks
     *            rocks' locations.
     */
    public void setPos(final double time, final RockSet rocks) {
        setPos(time, rocks, 0);
    }

    /**
     * Triggers a repaint.
     * 
     * @param time
     *            [sec]
     * @param rocks
     *            the rocks' locations
     * @param discontinuous
     *            bitmask of discontinouos locations
     */
    public void setPos(final double time, final RockSet rocks,
            final int discontinuous) {
        this.time = time;
        this.rocks = rocks;
        this.repaint();
    }

    /**
     * Convert world- to display coordinates.
     * 
     * @param wc
     * @param dc
     * @return display coordinates
     */
    public Point2D wc2dc(final Point2D wc, Point2D dc) {
        if (dc == null)
            dc = new Point2D.Float();
        dc.setLocation(wc.getX() * SCALE, wc.getY() * SCALE);
        return mat.transform(dc, dc);
    }
}