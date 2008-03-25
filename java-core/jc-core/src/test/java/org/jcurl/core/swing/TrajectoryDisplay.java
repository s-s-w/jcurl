/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jcurl.core.api.PositionSet;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.TrajectorySet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.ui.FixpointZoomer;

/**
 * Base for rock location displays. Does all the coordinate transformation math
 * and setters to delegate to {@link RockPainter} and {@link IcePainter} .
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id: RockLocationDisplayBase.java 230 2006-02-19 12:34:18Z
 *          mrohrmoser $
 */
class TrajectoryDisplay extends WCComponent implements ChangeListener {

    private static final Map<Object, Object> hints = new HashMap<Object, Object>();

    private static final long serialVersionUID = -2680676530327406261L;

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

    private CurvePainter curvePainter = null;

    private IcePainter icePainter = null; // @jve:decl-index=0:

    private BufferedImage img = null;

    private TrajectorySet pos = null;

    private RockPainter rockPainter = null;

    public TrajectoryDisplay() {
        initialize();
    }

    public Component add(final Component comp) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public Component add(final Component comp, final int index) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public void add(final Component comp, final Object constraints) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public void add(final Component comp, final Object constraints,
            final int index) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public Component add(final String name, final Component comp) {
        throw new UnsupportedOperationException("Not allowed.");
    }

    public IcePainter getIcePainter() {
        return icePainter;
    }

    public TrajectorySet getPos() {
        return pos;
    }

    /**
     * Used e.g.&nbsp;by {@link #paintRockRC(Graphics2D, boolean, int)}
     * 
     * @return the current painter
     */
    public RockPainter getRockPainter() {
        return rockPainter;
    }

    private void initialize() {
        int opa = 100;
        curvePainter = new CurvePainter(new Color(255, 0, 0, opa), new Color(
                255, 255, 0, opa), new BasicStroke(2 * RockProps.DEFAULT
                .getRadius(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0));
        opa = 150;
        curvePainter = new CurvePainter(new Color(255, 153, 153, opa),
                new Color(255, 255, 153, opa), new BasicStroke(
                        2 * RockProps.DEFAULT.getRadius(),
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0));
        this.setSize(new Dimension(600, 120));
        // setOpaque(true);
        setRockPainter(new RockPainter());
        setIcePainter(new IcePainter());
        this.setPos(null);
        setZoom(FixpointZoomer.HOUSE2HACK);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(hints);
        paint2(g2, true);
    }

    void paint2(final Graphics2D g2, final boolean useBuffer) {
        final AffineTransform backup = g2.getTransform();
        try {
            if (useBuffer) {
                // paint WC stuff (ice and rocks)
                if (resized() || img == null) {
                    // re-build the background image
                    img = new BufferedImage(getWidth(), getHeight(),
                            BufferedImage.TYPE_INT_ARGB);
                    final Graphics2D gi = (Graphics2D) img.getGraphics();
                    gi.setRenderingHints(hints);
                    paintIceDC(gi);
                }
                g2.drawImage(img, null, 0, 0);
            } else {
                paintIceDC(g2);
                g2.setTransform(backup);
            }
            g2.transform(wc_mat);
            curvePainter.doPaint(g2, ((CurveManager) pos).getCurveStore());
            g2.setTransform(backup);
            g2.transform(wc_mat);
            paintRocksWC(g2, pos.getCurrentPos(), RockSet.ALL_MASK);
        } finally {
            g2.setTransform(backup);
        }
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
        g2.setPaint(IcePainter.colors.backGround);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.transform(wc_mat);
        // IceSize
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
    protected void paintRocksWC(final Graphics2D g, final RockSet<Pos> rocks,
            final int mask) {
        if ((mask & RockSet.ALL_MASK) == 0)
            return;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            if ((mask >> i & 1) == 1)
                paintRockWC(g, rocks.getRock(i), i % 2 == 0, i / 2);
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
        if (false) {
            g.translate(rock.getX(), rock.getY());
            g.rotate(Math.PI + rock.getA());
        } else
            g.transform(rock.getAffineTransform());

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
     * @see #setPos(TrajectorySet)
     */
    public void stateChanged(final ChangeEvent evt) {
        final Object tmp = evt.getSource();
        if (tmp == null || PositionSet.class.isAssignableFrom(tmp.getClass()))
            ;// FIXME this.setPos((PositionSet) tmp);
    }

    /**
     * Updates the painter and triggers a {@link #repaint()}.
     * 
     * @param icePainter
     *            the new painter
     */
    public void setIcePainter(final IcePainter icePainter) {
        this.icePainter = icePainter;
        this.repaint();
    }

    /**
     * Triggers a repaint.
     * 
     * @param rocks
     *            rocks' locations.
     * @see #setPos(TrajectorySet, int)
     */
    public void setPos(final TrajectorySet rocks) {
        this.setPos(rocks, RockSet.ALL_MASK);
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
    public void setPos(final TrajectorySet rocks, final int discontinuous) {
        if (pos != rocks) {
            if (pos != null)
                pos.getCurrentPos().removeChangeListener(this);
            rocks.getCurrentPos().addChangeListener(this);
        }
        pos = rocks;
        this.repaint();
    }

    /**
     * Updates the painter and triggers a {@link #repaint()}.
     * 
     * @param rockPainter
     */
    public void setRockPainter(final RockPainter rockPainter) {
        this.rockPainter = rockPainter;
        this.repaint();
    }
}
