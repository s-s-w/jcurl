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

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.TestShowBase;
import org.jcurl.core.swing.CurvePainter;
import org.jcurl.core.swing.IcePainter;
import org.jcurl.core.swing.RockPainter;

public class SGComponentTest extends TestShowBase {

    private final SGComponent sg;

    public SGComponentTest() {
        super();
        if (frame != null) {
            frame.getContentPane().remove(display);
            frame.getContentPane().add(sg = new SGComponent());
        } else
            sg = null;
    }

    public void testThroughPut() throws InterruptedException {
        if (frame == null)
            return;
        final SGPositionSet _ini = new SGPositionSet(PositionSet.allHome(),
                new RockPainter());
        final SGTrajectory _tra = new SGTrajectory(null, new CurvePainter(null, null,
                null));
        final SGIce _ice = new SGIce(new IcePainter());

        _ice.add(_ini);
        _ini.add(_tra);
        _ini.add(new SGBroom(null));
        _tra.add(new SGPositionSet(PositionSet.allOut(), new RockPainter()));

        sg.setRoot(_ice);
        frame.setVisible(true);
        while (frame.isVisible())
            Thread.sleep(100);
    }
}
