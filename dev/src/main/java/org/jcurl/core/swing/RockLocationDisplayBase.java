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
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.TargetDiscrete;

/**
 * Base for rock location displays. Does all the coordinate transformation math
 * and setters to delegate to {@link RockPainter} and {@link IcePainter} .
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: RockLocationDisplayBase.java 230 2006-02-19 12:34:18Z
 *          mrohrmoser $
 */
public class RockLocationDisplayBase extends WCComponent implements
        TargetDiscrete, PropertyChangeListener {

    private static final long serialVersionUID = -2680676530327406261L;

    public static final Map hints = new HashMap(); 

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

    private IcePainter icePainter;

    private BufferedImage img = null;

    private PositionSet pos;

    private RockPainter rockPainter;

    /**
     * 
     * @param rocks
     *            {@link PositionSet#allHome()}if <code>null</code>
     * @param zoom
     *            {@link Zoomer#HOG2HACK}if <code>null</code>
     */
    public RockLocationDisplayBase() {
        this.setRockPainter(new RockPainter());
        this.setIcePainter(new IcePainter());
        this.setPos(PositionSet.allHome());
        initialize();
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

    public IcePainter getIcePainter() {
        return icePainter;
    }

    public PositionSet getPos() {
        return pos;
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(453, 129));
    }

    /**
     * Used e.g.&nbsp;by {@link #paintRockRC(Graphics2D, boolean, int)}
     * 
     * @return the current painter
     */
    public RockPainter getRockPainter() {
        return rockPainter;
    }

    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(hints);

        final AffineTransform dc_mat = g2.getTransform();

        // paint WC stuff (ice and rocks)
        if (resized()) {
            // re-build the background image
            img = new BufferedImage(getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
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
    protected void paintIceDC(final Graphics2D g2) {
        if (getIcePainter() == null)
            return;
        // background
        g2.setPaint(getIcePainter().color.backGround);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.transform(wc_mat);
        // Ice
        getIcePainter().paintIceWC(g2);
    }

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
     * @see #getRockPainter()
     * @see #paintRockWC(Graphics2D, Rock, boolean, int)
     */
    protected void paintRockRC(final Graphics2D g, final boolean isDark,
            final int idx) {
        if (getRockPainter() != null)
            getRockPainter().paintRockRC(g, isDark, idx);
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
        paintRocksWC(g2, pos, PositionSet.ALL_MASK);
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
        g.translate(WCComponent.SCALE * rock.getX(), WCComponent.SCALE
                * rock.getY());
        g.rotate(Math.PI + rock.getZ());
        // make the right-handed coordinate system left handed again (for
        // un-flipped text display)
        g.scale(-1, 1);
        paintRockRC(g, isDark, idx);
        g.setTransform(t);
    }

    /**
     * Property (rocks) changed.
     * 
     * @param evt
     * @see #setPos(PositionSet)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        final Object tmp = evt.getNewValue();
        if (tmp == null || PositionSet.class.isAssignableFrom(tmp.getClass())) {
            setPos((PositionSet) tmp);
        }
    }

    /**
     * Updates the painter and triggers a {@link #repaint()}.
     * 
     * @param icePainter
     *            the new painter
     */
    public void setIcePainter(IcePainter icePainter) {
        this.icePainter = icePainter;
        this.repaint();
    }

    /**
     * Triggers a repaint.
     * 
     * @param rocks
     *            rocks' locations.
     * @see #setPos(PositionSet, int)
     */
    public void setPos(final PositionSet rocks) {
        setPos(rocks, RockSet.ALL_MASK);
    }

    /**
     * Triggers a repaint. If the given <code>rocks</code> reference changed
     * the listeners are updated.
     * 
     * @param rocks
     *            the rocks' locations
     * @param discontinuous
     *            bitmask of discontinuous locations
     */
    public void setPos(final PositionSet rocks, final int discontinuous) {
        if (this.pos != rocks) {
            if (this.pos != null) {
                this.pos.removePropertyChangeListener(this);
            }
            rocks.addPropertyChangeListener(this);
        }
        this.pos = rocks;
        this.repaint();
    }

    /**
     * Updates the painter and triggers a {@link #repaint()}.
     * 
     * @param rockPainter
     */
    public void setRockPainter(RockPainter rockPainter) {
        this.rockPainter = rockPainter;
        this.repaint();
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"