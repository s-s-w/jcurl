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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.RockProps;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockPainter {

    private static final Color contourC;

    private static final Color darkC;

    private static final Font fo;

    private static final Color labelC;

    private static final char[] labels = { '1', '2', '3', '4', '5', '6', '7',
            '8' };

    private static final Color lightC;

    private static final Color rockC;
    static {
        fo = new Font("SansSerif", Font.BOLD, JCurlPanel.SCALE / 5);
        rockC = Color.LIGHT_GRAY;
        darkC = Color.RED;
        lightC = Color.YELLOW;
        labelC = Color.BLACK;
        contourC = Color.BLACK;
    }

    private FontMetrics fm = null;

    private int[] txtXoff = null;

    private int txtYoff = 0;

    /**
     * Draw one single rock at (0,0,0)
     * 
     * @param g
     * @param isDark
     * @param idx
     */
    protected void paintRock(final Graphics g, final boolean isDark,
            final int idx) {
        final int f = JCurlPanel.SCALE;
        final int ro = (int) (f * RockProps.DEFAULT.getRadius());
        final int ri = (int) (f * 0.8 * RockProps.DEFAULT.getRadius());
        final int p = ro / 8;
        // body
        g.setColor(rockC);
        g.fillArc(-ro, -ro, 2 * ro, 2 * ro, 0, 360);
        g.setColor(isDark ? darkC : lightC);
        g.fillArc(-ri, -ri, 2 * ri, 2 * ri, 0, 360);
        // label
        if (fm == null)
            fm = g.getFontMetrics(fo);
        if (txtXoff == null) {
            txtYoff = (int) (0.6 * 0.5 * fm.getHeight());
            txtXoff = new int[RockSet.ROCKS_PER_SET];
            for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
                txtXoff[i] = -fm.charWidth(labels[idx]) / 2;
        }
        g.setFont(fo);
        g.setColor(labelC);
        g.drawChars(labels, idx, 1, txtXoff[idx], txtYoff);

        g.setColor(contourC);
        // handle
        //g.fillOval(-p, -p, 2 * p, ri + p);
        // contours
        g.drawArc(-ri, -ri, 2 * ri, 2 * ri, 0, 360);
        g.drawArc(-ro, -ro, 2 * ro, 2 * ro, 0, 360);
    }

    /**
     * Draw one rock. Builds the coordinate transform and calls
     * {@link RockPainter#paintRock(Graphics, boolean, int)}.
     * 
     * @param g
     * @param rock
     * @param isDark
     * @param idx
     * @see RockPainter#paintRock(Graphics, boolean, int)
     */
    private void paintRock(final Graphics2D g, final Rock rock,
            final boolean isDark, final int idx) {
        final AffineTransform t = g.getTransform();
        g.translate(JCurlPanel.SCALE * rock.getX(), JCurlPanel.SCALE
                * rock.getY());
        g.rotate(Math.PI + rock.getZ());
        // make the right-handed coordinate system left handed again (for
        // un-flipped text display)
        g.scale(-1, 1);
        paintRock(g, isDark, idx);
        g.setTransform(t);
    }

    /**
     * Paint all rocks given by the mask.
     * 
     * @see RockPainter#paintRock(Graphics2D, Rock, boolean, int)
     * @param g
     * @param rocks
     * @param mask
     *            bit field which rocks to paint. {@link RockSet#ALL_MASK}
     */
    public void paintRocks(final Graphics2D g, final RockSet rocks, int mask) {
        if ((mask & RockSet.ALL_MASK) == 0)
            return;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (((mask >> i) & 1) == 1)
                paintRock(g, rocks.getRock(i), (i % 2) == 0, i / 2);
        }
    }
}