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
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import jcurl.core.JCLoggerFactory;
import jcurl.core.TargetDiscrete;
import jcurl.model.PositionSet;
import jcurl.model.Rock;
import jcurl.model.RockSet;

import org.apache.ugli.ULogger;

/**
 * Base for rock location displays. Does all the coordinate transformation math
 * and provides callbacks for actual drawing.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: RockLocationDisplayBase.java 135 2005-10-03 17:47:35Z
 *          mrohrmoser $
 */
public abstract class RockLocationDisplayBase extends JComponent implements
        TargetDiscrete, PropertyChangeListener {
    public static final Map hints = new HashMap();

    private static final ULogger log = JCLoggerFactory
            .getLogger(RockLocationDisplayBase.class);

    /**
     * Scale WC a bit to avoid int rounding errors. This is relevant only for
     * int based wc drawing operations e.g. fonts. WC objects (rocks etc.)
     * remain unaffected by this.
     */
    protected static final int SCALE = 1000;

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

    private BufferedImage img = null;

    private int oldHei = -1;

    private int oldWid = -1;

    private Orientation orient = Orientation.W;

    protected PositionSet rocks;

    protected final AffineTransform wc_mat = new AffineTransform();

    private Zoomer zom;

    /**
     * 
     * @param rocks
     *            {@link PositionSet#allHome()}if <code>null</code>
     * @param zoom
     *            {@link Zoomer#HOG2HACK}if <code>null</code>
     */
    public RockLocationDisplayBase(final PositionSet rocks, final Zoomer zoom) {
        this.rocks = rocks == null ? PositionSet.allHome() : rocks;
        this.zom = zoom == null ? Zoomer.HOUSE2HACK : zoom;
        this.rocks.addPropertyChangeListener(this);
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

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (log.isDebugEnabled())
            log.debug("[" + this.getX() + ", " + this.getY() + ", "
                    + this.getWidth() + ", " + this.getHeight() + "]");
        final Graphics2D g2 = (Graphics2D) g;
        final AffineTransform dc_mat = g2.getTransform();
        g2.setRenderingHints(hints);
        final int w = this.getWidth();
        final int h = this.getHeight();

        // paint WC stuff (ice and rocks)
        if (zom.hasChanged() || oldWid != w || oldHei != h) {
            // either the wc viewport, fixpoint or dc viewport has changed:
            // re-compute the transformation
            wc_mat.setToIdentity();
            zom.computeWctoDcTrafo(this.getBounds(), orient, true, wc_mat);
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
     * Callback to draw the rocks. Sets the transformation wc to dc and
     * delegates to {@link #paintRocksWC(Graphics2D, PositionSet, int)}.
     * 
     * @param g2
     *            graphics context.
     */
    protected void paintRocksDC(final Graphics2D g2) {
        g2.transform(wc_mat);
        // all rocks
        paintRocksWC(g2, rocks, PositionSet.ALL_MASK);
    }

    /**
     * Paint all rocks given by the mask.
     * 
     * @see #paintRockWC(Graphics2D, Rock, boolean, int)
     * @param g
     *            graphics context
     * @param rocks
     *            locations
     * @param mask
     *            bit field which rocks to paint. {@link PositionSet#ALL_MASK}
     */
    protected void paintRocksWC(final Graphics2D g, final PositionSet rocks,
            int mask) {
        if ((mask & RockSet.ALL_MASK) == 0)
            return;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (((mask >> i) & 1) == 1)
                paintRockWC(g, rocks.getRock(i), (i % 2) == 0, i / 2);
        }
    }

    /**
     * Property (rocks) changed.
     * 
     * @param evt
     * @see #setPos(double, PositionSet)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        final Object tmp = evt.getNewValue();
        if (tmp == null || PositionSet.class.isAssignableFrom(tmp.getClass())) {
            setPos(0, (PositionSet) tmp);
        }
    }

    /**
     * Triggers a repaint.
     * 
     * @see #setPos(double, PositionSet, int)
     * @param time
     *            [sec] unused
     * @param rocks
     *            rocks' locations.
     */
    public void setPos(final double time, final PositionSet rocks) {
        setPos(time, rocks, RockSet.ALL_MASK);
    }

    /**
     * Triggers a repaint. If the given <code>rocks</code> reference changed
     * the listeners are updated.
     * 
     * @param time
     *            [sec] unused
     * @param rocks
     *            the rocks' locations
     * @param discontinuous
     *            bitmask of discontinuous locations
     */
    public void setPos(final double time, final PositionSet rocks,
            final int discontinuous) {
        //this.time = time;
        if (this.rocks != rocks) {
            if (this.rocks != null) {
                this.rocks.removePropertyChangeListener(this);
            }
            rocks.addPropertyChangeListener(this);
        }
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

    public Dimension getMaximumSize() {
        return super.getMaximumSize();
    }

    public Dimension getMinimumSize() {
        return super.getMinimumSize();
    }

    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }
}