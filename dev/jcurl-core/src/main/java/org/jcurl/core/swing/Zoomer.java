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

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.JCLoggerFactory;
import org.jcurl.core.helpers.MutableObject;

/**
 * Smart handler for creating wc to dc transformations.
 * 
 * @see org.jcurl.core.swing.JCurlDisplay
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Zoomer.java 330 2006-06-05 14:29:14Z mrohrmoser $
 */
public abstract class Zoomer extends MutableObject {

    private static final Log log = JCLoggerFactory.getLogger(Zoomer.class);

    private AffineTransform wc2Dc = new AffineTransform();

    public boolean equals(Object obj) {
        return false;
    }

    public AffineTransform getWc2Dc() {
        return wc2Dc;
    }

    public void setWc2Dc(AffineTransform wc2dc) {
        log.debug("");
        // if (this.wc2dc.equals(wc2dc)) return;
        propChange.firePropertyChange("wc2Dc", this.wc2Dc.equals(wc2dc) ? null
                : this.wc2Dc, wc2dc);
        this.wc2Dc = wc2dc;
    }

    public abstract void setDc(Rectangle2D r);
}