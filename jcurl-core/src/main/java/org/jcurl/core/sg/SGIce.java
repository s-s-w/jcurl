/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.sg;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import org.jcurl.core.swing.IcePainter;

public class SGIce extends SGNodeBase {

    private final IcePainter p;

    public SGIce(final IcePainter p) {
        this.p = p;
    }

    public double distance(final Point2D p) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void render(final Graphics2D g) {
        p.paintIceWC(g);
    }
}
