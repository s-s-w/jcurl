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
package org.jcurl.core.zui;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.Unit;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * Simple keyboard zoom to sheet, house and 12-foot.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class KeyboardZoom extends PBasicInputEventHandler {
    private static final int _500 = 500;

    /** House area plus 1 rock margin plus "out" rock space. */
    public static final Rectangle2D houseP;

    /**
     * Inter-hog area area plus house area plus 1 rock margin plus "out" rock
     * space.
     */
    private static final Rectangle2D sheetP;

    /** 12-foot circle plus 1 rock */
    private static final Rectangle2D twelveP;

    static {
        final double r2 = 2 * RockProps.DEFAULT.getRadius();
        final double x = IceSize.SIDE_2_CENTER + r2;
        houseP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2), 2 * x,
                IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        final double c12 = r2 + Unit.f2m(6.0);
        twelveP = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
        sheetP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
    }

    private final PCamera cam;

    public KeyboardZoom(final PCamera cam) {
        this.cam = cam;
    }

    @Override
    public void keyPressed(final PInputEvent event) {
        switch (event.getKeyCode()) {
        case KeyEvent.VK_HOME:
        case KeyEvent.VK_H:
            event.setHandled(true);
            if (event.isControlDown())
                cam.animateViewToCenterBounds(twelveP, true, _500);
            else
                cam.animateViewToCenterBounds(houseP, true, _500);
            break;
        case KeyEvent.VK_END:
            event.setHandled(true);
            cam.animateViewToCenterBounds(sheetP, true, _500);
            break;
        default:
            ;
        }
    }
}