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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.helpers.JCLoggerFactory;

/**
 * Base for rock location displays. Does all the coordinate transformation math
 * and provides callbacks for actual drawing.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: RockLocationDisplayBase.java 295 2006-05-14 11:50:09Z
 *          mrohrmoser $
 */
public abstract class RockLocationDisplayBase extends JComponent implements
        PropertyChangeListener {
    public static final Map hints = new HashMap();

    private static final Log log = JCLoggerFactory
            .getLogger(RockLocationDisplayBase.class);

    /**
     * Scale WC a bit to avoid int rounding errors. This is relevant only for
     * int based wc drawing operations e.g. fonts. WC objects (positions etc.)
     * remain unaffected by this.
     */
    public static final int SCALE = 1000;

    static {
        // hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
        // RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // hints.put(RenderingHints.KEY_COLOR_RENDERING,
        // RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        // hints.put(RenderingHints.KEY_DITHERING,
        // RenderingHints.VALUE_DITHER_ENABLE);
        // hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
        // RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // hints.put(RenderingHints.KEY_INTERPOLATION,
        // RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        // hints.put(RenderingHints.KEY_RENDERING,
        // RenderingHints.VALUE_RENDER_QUALITY);
        // hints.put(RenderingHints.KEY_STROKE_CONTROL,
        // RenderingHints.VALUE_STROKE_NORMALIZE);
        // hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
        // RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private BufferedImage img = null;

    private int oldHei = -1;

    private int oldWid = -1;

    private Orientation orient = Orientation.W;

    protected PositionSet positions;

    protected final AffineTransform wc_mat = new AffineTransform();

    private Zoomer zoom;

    public RockLocationDisplayBase() {
        this(null);
    }

    /**
     * 
     * @param positions
     *            {@link PositionSet#allHome()}if <code>null</code>
     * @param zoom
     *            {@link Zoomer#HOG2HACK}if <code>null</code>
     */
    public RockLocationDisplayBase(final PositionSet positions,
            final Zoomer zoom) {
        this.setPositions(positions);
        this.setZoom(zoom);
        this.setOpaque(true);
    }

    public RockLocationDisplayBase(Zoomer zoom) {
        this(null, zoom);
    }

    public Component add(Component comp) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public Component add(Component comp, int index) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public void add(Component comp, Object constraints) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public void add(Component comp, Object constraints, int index) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public Component add(String name, Component comp) {
        throw new UnsupportedOperationException("Not allowed.");
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
        this.paintComponent(g2);
        g2.dispose();
        if (!dst.getName().endsWith(".png"))
            dst = new File(dst.getName() + ".png");
        ImageIO.write(img, "png", dst);
    }

    public Dimension getMaximumSize() {
        return super.getMaximumSize();
    }

    public Dimension getMinimumSize() {
        return super.getMinimumSize();
    }

    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    public PositionSet getPositions() {
        return positions;
    }

    public Zoomer getZoom() {
        return zoom;
    }

    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (log.isDebugEnabled())
            log.debug("[" + this.getX() + ", " + this.getY() + ", "
                    + this.getWidth() + ", " + this.getHeight() + "]");
        final Graphics2D g2 = (Graphics2D) g;
        final AffineTransform dc_mat = g2.getTransform();
        g2.setRenderingHints(hints);
        final int w = this.getWidth();
        final int h = this.getHeight();

        // paint WC stuff (ice and positions)
        if (zoom.hasChanged() || oldWid != w || oldHei != h) {
            // either the wc viewport, fixpoint or dc viewport has changed:
            // re-compute the transformation
            wc_mat.setToIdentity();
            zoom.computeWctoDcTrafo(this.getBounds(), orient, true, wc_mat);
            oldWid = w;
            oldHei = h;
            // re-build the background image
            img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D gi = (Graphics2D) img.getGraphics();
            gi.setRenderingHints(hints);
            paintIceDC(gi);
        }
        g2.drawImage(img, null, 0, 0);
        paintRocksDC(g2);
        g2.setTransform(dc_mat);
    }

    /**
     * Callback to draw the ice (background).
     * 
     * @param g2
     */
    protected abstract void paintIceDC(final Graphics2D g2);

    /**
     * Basic rock drawing method: draw one single rock at (0,0,0). Assumes the
     * correct transformation display- to rock-coodinates is set already.
     * 
     * @param g
     *            transformed graphics context.
     * @param isDark
     *            rock color
     * @param idx
     *            rock index
     * @see #paintRockWC(Graphics2D, Rock, boolean, int)
     */
    protected abstract void paintRockRC(final Graphics2D g,
            final boolean isDark, final int idx);

    /**
     * Callback to draw the positions. Sets the transformation wc to dc and
     * delegates to {@link #paintRocksWC(Graphics2D, PositionSet, int)}.
     * 
     * @param g2
     *            graphics context.
     */
    protected void paintRocksDC(final Graphics2D g2) {
        g2.transform(wc_mat);
        // all positions
        paintRocksWC(g2, positions, PositionSet.ALL_MASK);
    }

    /**
     * Paint all positions given by the mask.
     * 
     * @see #paintRockWC(Graphics2D, Rock, boolean, int)
     * @param g
     *            graphics context
     * @param positions
     *            locations
     * @param mask
     *            bit field which positions to paint.
     *            {@link PositionSet#ALL_MASK}
     */
    protected void paintRocksWC(final Graphics2D g,
            final PositionSet positions, int mask) {
        if ((mask & RockSet.ALL_MASK) == 0)
            return;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (((mask >> i) & 1) == 1)
                paintRockWC(g, positions.getRock(i), (i % 2) == 0, i / 2);
        }
    }

    /**
     * Draw one rock at it's wc position. Builds the coordinate transform and
     * calls {@link #paintRockRC(Graphics2D, boolean, int)}.
     * 
     * @param g
     * @param rock
     * @param isDark
     * @param idx
     * @see RockPainter#paintRockRC(Graphics2D, boolean, int)
     */
    protected void paintRockWC(final Graphics2D g, final Rock rock,
            final boolean isDark, final int idx) {
        final AffineTransform t = g.getTransform();
        g.translate(JCurlDisplay.SCALE * rock.getX(), JCurlDisplay.SCALE
                * rock.getY());
        g.rotate(Math.PI + rock.getZ());
        // make the right-handed coordinate system left handed again (for
        // un-flipped text display)
        g.scale(-1, 1);
        paintRockRC(g, isDark, idx);
        g.setTransform(t);
    }

    /**
     * Rocks or zoom changed.
     * 
     * @param evt
     * @see #setPositions(PositionSet)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        final Object tmp = evt.getNewValue();
        if (tmp == null || PositionSet.class.isAssignableFrom(tmp.getClass())) {
            setPositions((PositionSet) tmp);
        }
    }

    /**
     * Triggers a repaint.
     * 
     * @see #setPos(double, PositionSet, int)
     * @param time
     *            [sec] unused
     * @param positions
     *            positions' locations.
     */
    public void setPos(final double time, final PositionSet positions) {
        setPos(time, positions, RockSet.ALL_MASK);
    }

    /**
     * Triggers a repaint. If the given <code>positions</code> reference
     * changed the listeners are updated.
     * 
     * @param time
     *            [sec] unused
     * @param positions
     *            the positions' locations
     * @param discontinuous
     *            bitmask of discontinuous locations
     */
    public void setPos(final double time, final PositionSet positions,
            final int discontinuous) {
        setPositions(positions);
    }

    /**
     * Triggers a repaint. If the given <code>positions</code> reference
     * changed the listeners are updated.
     * 
     * @param positions
     */
    public void setPositions(PositionSet positions) {
        if (positions == null)
            positions = PositionSet.allHome();
        if (this.positions != null)
            this.positions.removePropertyChangeListener(this);
        this.positions = positions;
        this.positions.addPropertyChangeListener(this);
        this.repaint();
    }

    public void setZoom(Zoomer zoom) {
        if (zoom == null)
            zoom = Zoomer.HOUSE2HACK;
        if (this.zoom != null)
            this.zoom.removePropertyChangeListener(this);
        this.zoom = zoom;
        this.zoom.addPropertyChangeListener(this);
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