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
import java.awt.Graphics;

import jcurl.core.dto.Ice;
import jcurl.core.io.Dim;

/**
 * @see jcurl.core.gui.RockPainter
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class IcePainter {
    private static final Color outC = Color.BLUE;

    private static final Color frontC = Color.PINK;

    private static final Color backC = new Color(0xC9C9C9);

    private static final Color c1C = Color.RED;

    private static final Color c4C = Color.MAGENTA;

    private static final Color c8C = Color.ORANGE;

    private static final Color c12C = Color.CYAN;

    private static final Color linesC = Color.BLACK;

    /**
     * 
     * 
     * @param g
     */
    public void paintIce(final Graphics g) {
        final int f = JCurlPanel.SCALE;
        final int fhy = (int) (f * Ice.FAR_HOG_2_TEE);
        final int nhy = (int) (f * Ice.HOG_2_TEE);
        final int hy = (int) (f * Ice.FAR_HACK_2_TEE);
        final int dx = (int) (f * Ice.SIDE_2_CENTER);
        final int by = (int) (f * Ice.BACK_2_TEE);
        final int c1 = (int) (f * Dim.f2m(0.5));
        final int c4 = (int) (f * Dim.f2m(2.0));
        final int c8 = (int) (f * Dim.f2m(4.0));
        final int c12 = (int) (f * Dim.f2m(6.0));

        // hog to hog
        g.setColor(outC);
        g.fillRect(-dx, nhy, 2 * dx, fhy - nhy);
        // hog to tee
        g.setColor(frontC);
        g.fillRect(-dx, 0, 2 * dx, nhy);
        // tee to back
        g.setColor(backC);
        g.fillRect(-dx, -by, 2 * dx, by);
        // 12-foot circle
        g.setColor(c12C);
        g.fillArc(-c12, -c12, 2 * c12, 2 * c12, 0, 360);
        // 8-foot circle
        g.setColor(c8C);
        g.fillArc(-c8, -c8, 2 * c8, 2 * c8, 0, 360);
        // 4-foot circle
        g.setColor(c4C);
        g.fillArc(-c4, -c4, 2 * c4, 2 * c4, 0, 360);
        // button
        g.setColor(c1C);
        g.fillArc(-c1, -c1, 2 * c1, 2 * c1, 0, 360);

        // contours
        g.setColor(linesC);
        g.drawArc(-c12, -c12, 2 * c12, 2 * c12, 0, 360);
        g.drawArc(-c8, -c8, 2 * c8, 2 * c8, 0, 360);
        g.drawArc(-c4, -c4, 2 * c4, 2 * c4, 0, 360);
        g.drawArc(-c1, -c1, 2 * c1, 2 * c1, 0, 360);

        // Back line
        g.drawLine(-dx, -by, dx, -by);
        // Tee line
        g.drawLine(-dx, 0, dx, 0);
        // Near Hog line
        g.drawLine(-dx, nhy, dx, nhy);
        // Far Hog line
        g.drawLine(-dx, fhy, dx, fhy);
        // Center
        g.drawLine(0, hy, 0, -by);
        g.drawLine(-dx, fhy, -dx, -by);
        g.drawLine(dx, fhy, dx, -by);
    }
}