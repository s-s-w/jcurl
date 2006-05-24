/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.model;

import java.awt.geom.PathIterator;

import org.jcurl.core.PositionSet;
import org.jcurl.core.SpeedSet;

public class AwtPaths extends RecordedPaths {

    private PathIterator[] paths;

    public PositionSet getPosition(double t, PositionSet dst) {
        return null;
    }

    public SpeedSet getSpeed(double t, SpeedSet dst) {
        return null;
    }

    public double getMaxT() {
        return 0;
    }
    
    public PathIterator getAwtPath(int i) {
        return paths[i];
    }
}
