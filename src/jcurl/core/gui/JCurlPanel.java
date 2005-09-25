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
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import jcurl.core.PositionSet;
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

    private static final Map hints = new HashMap();

    /**
     * Scale WC a bit to avoid int rounding errors. This is relevant only for
     * int based drawing operations e.g. fonts. WC objects (rocks etc.) remain
     * unaffected by this.
     */
    public static final int SCALE = 1000;

    private static final Color timeB = new Color(0.9F, 0.9F, 1.0F, 0.75F);

    private static final Color timeC = Color.BLACK;

    private static final Font timeF = new Font("SansSerif", Font.PLAIN, 10);

    static {
        //        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
        //                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //        hints.put(RenderingHints.KEY_COLOR_RENDERING,
        //                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //        hints.put(RenderingHints.KEY_DITHERING,
        //                RenderingHints.VALUE_DITHER_ENABLE);
        //        hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
        //                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        //        hints.put(RenderingHints.KEY_INTERPOLATION,
        //                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        //        hints.put(RenderingHints.KEY_RENDERING,
        //                RenderingHints.VALUE_RENDER_QUALITY);
        //        hints.put(RenderingHints.KEY_STROKE_CONTROL,
        //                RenderingHints.VALUE_STROKE_NORMALIZE);
        //        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
        //                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private final IcePainter iceP;

    private final AffineTransform wc_mat = new AffineTransform();

    private int oldHei = -1;

    private int oldWid = -1;

    private Orientation orient = Orientation.W;

    private final RockPainter rockP;

    private PositionSet rocks;

    private double time = 0;

    private Zoomer zom;

    public JCurlPanel(final PositionSet rocks, final Zoomer zoom,
            final IcePainter iceP, final RockPainter rockP) {
        this.rocks = rocks == null ? PositionSet.allHome() : rocks;
        this.zom = zoom == null ? Zoomer.HOUSE2HACK : zoom;
        this.iceP = iceP == null ? new IcePainter() : iceP;
        this.rockP = rockP == null ? new RockPainter() : rockP;
    }

    public JCurlPanel(Zoomer zoom) {
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
            wc = wc_mat.inverseTransform(dc, wc);
            wc.setLocation(wc.getX() / SCALE, wc.getY() / SCALE);
            return wc;
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException("Why uninvertible?", e);
        }
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        final AffineTransform dc_mat = g2.getTransform();
        g2.setRenderingHints(hints);
        // background
        g2.setPaint(iceP.color.backGround);
        final int w = this.getWidth();
        final int h = this.getHeight();
        g2.fillRect(0, 0, w, h);
        {
            // paint WC stuff
            if (zom.hasChanged() || oldWid != w || oldHei != h) {
                // either the wc viewport, fixpoint or dc viewport has changed:
                // re-compute the transformation
                wc_mat.setToIdentity();
                zom.applyTrafo(this.getBounds(), orient, true, wc_mat);
                oldWid = w;
                oldHei = w;
            }
            g2.transform(wc_mat);
            // Ice
            iceP.paintIce(g2);
            // all rocks
            rockP.paintRocks(g2, rocks, PositionSet.ALL_MASK);
        }
        // additional DC stuff
        g2.setTransform(dc_mat);
        { // paint additional DC stuff
            g2.setColor(timeB);
            g2.fillRect(w - 70, 0, 70, 20);
            //g2.fillRect(0, 0, w, 20);
            g2.setFont(timeF);
            g2.setColor(timeC);
            g2.drawString(Double.toString(time), w - 70 + 10, 3 * 20 / 4);
        }
    }

    /**
     * Triggers a repaint.
     * 
     * @param time
     *            [sec]
     * @param rocks
     *            rocks' locations.
     */
    public void setPos(final double time, final PositionSet rocks) {
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
    public void setPos(final double time, final PositionSet rocks,
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
        return wc_mat.transform(dc, dc);
    }
}