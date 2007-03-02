/*
 * jcurl curling simulation framework http://www.jcurl.org
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.jcurl.core.base.TargetDiscrete;

/**
 * A {@link org.jcurl.core.swing.PositionDisplay}with some additional meta data
 * displayed (here: time).
 * 
 * @see org.jcurl.core.swing.PositionDisplay
 * @see org.jcurl.core.swing.RealTimePlayer
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:JCurlDisplay.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class JCurlDisplay extends PositionDisplay implements TargetDiscrete {

    private static final long serialVersionUID = -3587954807831602402L;

    private static final Color timeB = new Color(0.9F, 0.9F, 1.0F, 0.75F);

    private static final Color timeC = Color.BLACK;

    private static final Font timeF = new Font("SansSerif", Font.PLAIN, 10);

    private double time = 0;

    /**
     * This method initializes
     * 
     */
    public JCurlDisplay() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(607, 148));
    }

    public void paint(final Graphics g) {
        super.paint(g);
        final Graphics2D g2 = (Graphics2D) g;
        // paint additional DC stuff
        g2.setColor(timeB);
        g2.fillRect(getWidth() - 70, 0, 70, 20);
        // g2.fillRect(0, 0, w, 20);
        g2.setFont(timeF);
        g2.setColor(timeC);
        g2.drawString(Double.toString(time), getWidth() - 70 + 10, 3 * 20 / 4);
    }
} // @jve:decl-index=0:visual-constraint="10,10"
