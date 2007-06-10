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

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * Simple keyboard zoom to sheet, house and 12-foot.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class KeyBoardZoom extends PBasicInputEventHandler {
    private static final int _500 = 500;

    private final PCamera cam;

    public KeyBoardZoom(final PCamera cam) {
        this.cam = cam;
    }

    @Override
    public void keyPressed(final PInputEvent event) {
        switch (event.getKeyCode()) {
        case KeyEvent.VK_HOME:
        case KeyEvent.VK_H:
            event.setHandled(true);
            if (event.isControlDown())
                cam.animateViewToCenterBounds(PiccoloBasicTest.twelveP, true,
                        _500);
            else
                cam.animateViewToCenterBounds(PiccoloBasicTest.houseP, true,
                        _500);
            break;
        case KeyEvent.VK_END:
            event.setHandled(true);
            cam.animateViewToCenterBounds(PiccoloBasicTest.sheetP, true, _500);
            break;
        default:
            ;
        }
    }
}