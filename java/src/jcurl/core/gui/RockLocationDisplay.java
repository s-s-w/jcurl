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

import java.awt.Graphics2D;

import jcurl.core.JCLoggerFactory;
import jcurl.core.PositionSet;

import org.apache.ugli.ULogger;

/**
 * A simple display for rock locations. Delegates rock- and ice painting.
 * 
 * @see jcurl.core.gui.RockPainter
 * @see jcurl.core.gui.IcePainter
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: JCurlPanel.java 126 2005-10-01 19:26:12Z mrohrmoser $
 */
public class RockLocationDisplay extends RockLocationDisplayBase {
    private static final ULogger log = JCLoggerFactory
            .getLogger(RockLocationDisplay.class);

    private final IcePainter iceP;

    private final RockPainter rockP;

    public RockLocationDisplay(final PositionSet rocks, final Zoomer zoom,
            final IcePainter iceP, final RockPainter rockP) {
        super(rocks, zoom);
        this.iceP = iceP == null ? new IcePainter() : iceP;
        this.rockP = rockP == null ? new RockPainter() : rockP;
    }

    public RockLocationDisplay(final Zoomer zoom) {
        this(null, zoom, null, null);
    }

    protected void paintIce(final Graphics2D g2) {
        // background
        g2.setPaint(iceP.color.backGround);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.transform(wc_mat);
        // Ice
        iceP.paintIce(g2);
    }

    protected void paintRock(Graphics2D g, boolean isDark, int idx) {
        rockP.paintRock(g, isDark, idx);
    }
}