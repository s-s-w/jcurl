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

import java.awt.Graphics;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class IcePainter {

    /**
     * Doesn't need a matrix as it only uses (yet transformed) {@link Pt}
     * objects.
     * 
     * @param g
     */
    public void paintIce(final Graphics g) {
        // hog to hog
        Painter.rectDC(g, Pt.fhl.dc, Pt.nhr.dc);
        // hog to tee
        Painter.rectDC(g, Pt.nhl.dc, Pt.tr.dc);
        // tee to back
        Painter.rectDC(g, Pt.tl.dc, Pt.br.dc);
        // button
        Painter.circleDC(g, Pt.zero.dc, Pt.tr1.dc);
        // 4-foot circle
        Painter.circleDC(g, Pt.zero.dc, Pt.tr4.dc);
        // 8-foot circle
        Painter.circleDC(g, Pt.zero.dc, Pt.tr8.dc);
        // 12-foot circle
        Painter.circleDC(g, Pt.zero.dc, Pt.tr12.dc);
        // Tee line
        Painter.lineDC(g, Pt.tl.dc, Pt.tr.dc);
        // Near Hog line
        Painter.lineDC(g, Pt.nhl.dc, Pt.nhr.dc);
        // Far Hog line
        Painter.lineDC(g, Pt.fhl.dc, Pt.fhr.dc);
        // Center
        Painter.lineDC(g, Pt.fHackC.dc, Pt.nHackC.dc);
    }
}