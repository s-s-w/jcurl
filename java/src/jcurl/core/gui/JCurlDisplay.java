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

import jcurl.model.PositionSet;

/**
 * A {@link jcurl.core.gui.RockLocationDisplayBase}with some additional meta
 * data displayed (here: time).
 * 
 * @see jcurl.core.gui.RockLocationDisplayBase
 * @see jcurl.core.gui.RealTimePlayer
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlDisplay extends RockLocationDisplay {

    private static final Color timeB = new Color(0.9F, 0.9F, 1.0F, 0.75F);

    private static final Color timeC = Color.BLACK;

    private static final Font timeF = new Font("SansSerif", Font.PLAIN, 10);

    private double time = 0;

    public JCurlDisplay(final PositionSet rocks, final Zoomer zoom,
            final IcePainter iceP, final RockPainter rockP) {
        super(rocks, zoom, iceP, rockP);
    }

    public JCurlDisplay(Zoomer zoom) {
        this(null, zoom, null, null);
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        // paint additional DC stuff
        g2.setColor(timeB);
        g2.fillRect(this.getWidth() - 70, 0, 70, 20);
        //g2.fillRect(0, 0, w, 20);
        g2.setFont(timeF);
        g2.setColor(timeC);
        g2.drawString(Double.toString(time), this.getWidth() - 70 + 10,
                3 * 20 / 4);

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
        super.setPos(this.time = time, rocks, discontinuous);
    }
}