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

import org.jcurl.core.base.PositionSet;

public class PiccoloBasicTest extends TestZuiBase {

    private static final long serialVersionUID = -8485372274509187133L;

    public void testThroughPut() throws InterruptedException {
        if (frame == null)
            return;

        // add some curling stuff:
        final PPositionSet initial = new PPositionSet(new PRockFactory.Simple());
        initial.setPositionSet(PositionSet.allHome());
        ice.addChild(initial);
        initial.addInputEventListener(new PPositionSetDrag());
        final PPositionSet end = new PPositionSet(new PRockFactory.Fancy(10));
        end.setPositionSet(PositionSet.allOut());
        ice.addChild(end);

        frame.setVisible(true);
        while (frame.isVisible())
            Thread.sleep(100);
    }
}
