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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.TargetDiscrete;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.core.swing.RockPainter.ColorSet;

/**
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:SumDisplayBase.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public abstract class SumDisplayBase extends JComponent implements
        TargetDiscrete, PropertyChangeListener {

    private static final ColorSet colors = new ColorSet();

    private static final Map hints = new HashMap();

    private static final Log log = JCLoggerFactory
            .getLogger(SumDisplayBase.class);
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

    public Paint backGround = new Color(0xF0F0FF);

    private PositionSet model;

    private int recentMask = -1;

    public SumDisplayBase() {
        this(null);
    }

    public SumDisplayBase(final PositionSet model) {
        this.setPos(model);
    }

    protected abstract int computeMask(final PositionSet rocks);

    public Dimension getMaximumSize() {
        return super.getMaximumSize();
    }

    public Dimension getMinimumSize() {
        final int d = 10;
        return new Dimension(d, d);
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(hints);
        // background
        g2.setPaint(backGround);
        g2.fillRect(0, 0, getWidth(), getHeight());
        for (int i = RockSet.ROCKS_PER_COLOR - 1; i >= 0; i--) {
            if (RockSet.isSet(recentMask, i, true))
                paintRock(g2, i, true);
            if (RockSet.isSet(recentMask, i, false))
                paintRock(g2, i, false);
        }
    }

    protected void paintRock(final Graphics2D g2, int idx8, boolean isDark) {
        final float r = 0.35F * getWidth();
        // vertical display:
        float cx = 0.5F * getWidth();
        float cy = getHeight() / RockSet.ROCKS_PER_SET;
        if (isDark)
            cy *= idx8 + 0.5F;
        else
            cy *= RockSet.ROCKS_PER_SET - (idx8 + 0.5F);
        g2.setPaint(isDark ? colors.dark : colors.light);
        g2.fillArc((int) (cx - r), (int) (cy - r), (int) (2 * r),
                (int) (2 * r), 0, 360);
        g2.setColor(Color.BLACK);
        g2.drawArc((int) (cx - r), (int) (cy - r), (int) (2 * r),
                (int) (2 * r), 0, 360);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        final Object tmp = evt.getNewValue();
        if (tmp == null || PositionSet.class.isAssignableFrom(tmp.getClass()))
            this.setPos((PositionSet) tmp);
        else
            log.info(tmp);
    }

    public void setPos(PositionSet rocks) {
        if (model != null && model != rocks)
            model.removePropertyChangeListener(this);
        rocks.addPropertyChangeListener(this);
        model = rocks;
        showRocks(computeMask(model));
    }

    public void setPos(PositionSet rocks, int discontinuous) {
        this.setPos(rocks);
    }

    public void showRocks(int mask) {
        if (mask == recentMask)
            return;
        recentMask = mask;
        if (log.isDebugEnabled())
            log.debug("mask " + recentMask);
        this.repaint();
    }
}