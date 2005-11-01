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
package jcurl.model;

import jcurl.core.dto.MutableObject;
import jcurl.math.CurveBase;

/**
 * Atom for rock (set) data. Combines the two canonical approaches: curve-based
 * (one rock in time) and set based (all rocks at a given time).
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class RockModelBase extends MutableObject {

    public abstract CurveBase curve(int rockIdx);

    public abstract void finish();

    public abstract double getC(int rockIdx, int dim, int c, double t);

    public abstract PositionSet getPos();

    public abstract SpeedSet getSpeed();

    public abstract double getTMax();

    public abstract double getTMin();

    public abstract double getTNow();

    /**
     * Call {@link #setTNow(double)}with {@link #getTMin()}and triggers a
     * {@link java.beans.PropertyChangeEvent}.
     */
    public abstract void reset();

    /**
     * Triggers a {@link java.beans.PropertyChangeEvent}.
     */
    public abstract void setTNow(double tnow);
}