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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

import jcurl.core.PositionSet;
import jcurl.core.Rock;
import jcurl.core.RockSet;
import jcurl.core.dto.RockProps;

/**
 * Paint a set of rocks.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockPainter {
    /** Rock colors */
    public static class ColorSet {
        public Paint contour = Color.BLACK;

        public Paint dark = new Color(0xFF0000);

        public Paint granite = new Color(0x565755);

        public Paint label = Color.BLACK;

        public Paint light = new Color(0xFFFF00);
    }

    private static final Font fo;

    protected static final Arc2D.Float inner;

    private static final char[] labels = { '1', '2', '3', '4', '5', '6', '7',
            '8' };

    protected static final Arc2D.Float outer;

    static {
        fo = new Font("SansSerif", Font.BOLD, JCurlPanel.SCALE / 5);

        final int f = JCurlPanel.SCALE;
        final float ro = f * RockProps.DEFAULT.getRadius();
        final float ri = f * 0.7F * RockProps.DEFAULT.getRadius();
        outer = new Arc2D.Float(-ro, -ro, 2 * ro, 2 * ro, 0, 360, Arc2D.CHORD);
        inner = new Arc2D.Float(-ri, -ri, 2 * ri, 2 * ri, 0, 360, Arc2D.CHORD);
    }

    private final ColorSet colors = new ColorSet();

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
    protected void paintRock(final Graphics2D g, final boolean isDark,
            final int idx) {
        // body
        g.setPaint(colors.granite);
        g.fill(outer);
        g.setPaint(isDark ? colors.dark : colors.light);
        g.fill(inner);
        // label
        if (fm == null)
            fm = g.getFontMetrics(fo);
        if (txtXoff == null) {
            txtYoff = (int) (0.6F * 0.5F * fm.getHeight());
            txtXoff = new int[PositionSet.ROCKS_PER_COLOR];
            for (int i = PositionSet.ROCKS_PER_COLOR - 1; i >= 0; i--)
                txtXoff[i] = -fm.charWidth(labels[i]) / 2;
        }
        g.setFont(fo);
        g.setPaint(colors.label);
        g.drawChars(labels, idx, 1, txtXoff[idx], txtYoff);
        // contours
        //        g.setPaint(colors.contour);
        //        // handle
        //        //g.fillOval(-p, -p, 2 * p, ri + p);
        //        g.draw(inner);
        //        g.draw(outer);
    }

    /**
     * Draw one rock at it's wc position. Builds the coordinate transform and
     * calls {@link RockPainter#paintRock(Graphics2D, boolean, int)}.
     * 
     * @param g
     * @param rock
     * @param isDark
     * @param idx
     * @see RockPainter#paintRock(Graphics2D, boolean, int)
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
     *            graphics context
     * @param rocks
     *            locations
     * @param mask
     *            bit field which rocks to paint. {@link PositionSet#ALL_MASK}
     */
    public void paintRocks(final Graphics2D g, final PositionSet rocks, int mask) {
        if ((mask & RockSet.ALL_MASK) == 0)
            return;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (((mask >> i) & 1) == 1)
                paintRock(g, rocks.getRock(i), (i % 2) == 0, i / 2);
        }
    }
}