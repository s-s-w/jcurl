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

import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.PositionSet;
import org.jcurl.core.SpeedSet;
import org.jcurl.core.helpers.NotImplementedYetException;

/**
 * Paths for quick display via java2d.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class AwtPaths extends RecordedPaths {

    private static final long serialVersionUID = 1530923593296287467L;

    private PathIterator[] paths;

    public boolean equals(Object obj) {
        throw new NotImplementedYetException();
    }

    public PathIterator getAwtPath(int i) {
        return paths[i];
    }

    public PositionSet getCurrentPos() throws FunctionEvaluationException {
        throw new NotImplementedYetException();
    }

    public SpeedSet getCurrentSpeed() {
        throw new NotImplementedYetException();
    }

    public double getCurrentT() {
        throw new NotImplementedYetException();
    }

    public double getMaxT() {
        throw new NotImplementedYetException();
    }

    public PositionSet getPosition(double t, PositionSet dst) {
        throw new NotImplementedYetException();
    }

    public SpeedSet getSpeed(double t, SpeedSet dst) {
        throw new NotImplementedYetException();
    }

    public void setCurrentT(double currentT) throws FunctionEvaluationException {
        throw new NotImplementedYetException();
    }
}
