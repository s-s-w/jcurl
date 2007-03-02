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
package org.jcurl.core.swing;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public abstract class WCComponent extends Component {

    /**
     * Scale WC a bit to avoid int rounding errors. This is relevant only for
     * int based wc drawing operations e.g. fonts. WC objects (rocks etc.)
     * remain unaffected by this.
     */
    protected static final int SCALE = 1000;

    protected int oldHei = -1;

    protected int oldWid = -1;

    protected Orientation orient = Orientation.W;

    protected final AffineTransform wc_mat = new AffineTransform();

    private Zoomer zoom = null;

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

    public void exportPng(File dst) throws IOException {
        final BufferedImage img = new BufferedImage(getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = (Graphics2D) img.getGraphics();
        {
            final Map hints = new HashMap();
            hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            hints.put(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_COLOR_RENDERING,
                    RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            hints.put(RenderingHints.KEY_DITHERING,
                    RenderingHints.VALUE_DITHER_ENABLE);
            hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            hints.put(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            hints.put(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            hints.put(RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_NORMALIZE);
            hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.addRenderingHints(hints);
        }
        paint(g2);
        g2.dispose();
        if (!dst.getName().endsWith(".png"))
            dst = new File(dst.getName() + ".png");
        ImageIO.write(img, "png", dst);
    }

    public Zoomer getZoom() {
        return zoom;
    }

    /**
     * Either the wc viewport, fixpoint or dc viewport has changed: re-compute
     * the transformation {@link #wc_mat}.
     * 
     * @return was the transformation changed?
     */
    protected boolean resized() {
        final int w = getWidth();
        final int h = getHeight();
        if (getZoom().hasChanged() || oldWid != w || oldHei != h) {
            getZoom()
                    .computeWctoDcTrafo(this.getBounds(), orient, true, wc_mat);
            oldWid = w;
            oldHei = h;
            return true;
        }
        return false;
    }

    public void setZoom(Zoomer zoom) {
        this.zoom = zoom;
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
