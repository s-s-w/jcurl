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

import java.awt.geom.Point2D;

import jcurl.core.dto.EnumBase;
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockProps;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ZoomArea extends EnumBase {
    private static int _idx = 0;

    private static final float _dia = 2 * RockProps.DEFAULT.getRadius();

    public static final ZoomArea HOUSE = new ZoomArea("House",
            -(Ice.SIDE_2_CENTER + _dia), Ice.HOG_2_TEE + _dia,
            Ice.SIDE_2_CENTER + _dia,
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK + _dia));

    public final Point2D.Float tl;

    public final Point2D.Float br;

    private ZoomArea(final String txt, final Point2D.Float tl,
            final Point2D.Float br) {
        super(_idx++, txt);
        this.tl = tl;
        this.br = br;
    }

    private ZoomArea(final String txt, float x0, float y0, float x1, float y1) {
        this(txt, new Point2D.Float(x0, y0), new Point2D.Float(x1, y1));
    }
}