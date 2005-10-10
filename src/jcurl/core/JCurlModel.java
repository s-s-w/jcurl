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
package jcurl.core;

import jcurl.sim.core.CollissionStrategy;
import jcurl.sim.core.SlideStrategy;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlModel extends ModelBase {

    protected SlideStrategy slider;

    /**
     *  
     */
    public JCurlModel() {
    }

    public CollissionStrategy getCollider() {
        throw new NotImplementedYetException();
    }

    public int getFocusIdx() {
        // TODO Auto-generated method stub
        return 0;
    }

    public PositionSet getPos() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getSelectedMask() {
        // TODO Auto-generated method stub
        return 0;
    }

    public SlideStrategy getSlider() {
        throw new NotImplementedYetException();
    }

    public PositionSet getSpeed() {
        // TODO Auto-generated method stub
        return null;
    }

    public long getTmax() {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getTmin() {
        // TODO Auto-generated method stub
        return 0;
    }

    public long getTnow() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void init(PositionSet pos) {
        // TODO Auto-generated method stub
    }

    /**
     * Call {@link #init(PositionSet)}and triggers a
     * {@link java.beans.PropertyChangeEvent}.
     */
    public void init(PositionSet pos, SpeedSet speed, long tnow,
            final SlideStrategy slider) {
        // TODO Auto-generated method stub
    }

    /**
     * Call {@link #setTnow(long)}with {@link #getTmin()}and triggers a
     * {@link java.beans.PropertyChangeEvent}.
     */
    public void reset() {
        // TODO Auto-generated method stub
    }

    /**
     * Just triggers a {@link java.beans.PropertyChangeEvent}.
     */
    public void setFocusIdx(final int idx) {
        throw new NotImplementedYetException();
    }

    /**
     * Just triggers a {@link java.beans.PropertyChangeEvent}.
     */
    public void setSelectedMask(final int selected) {
        throw new NotImplementedYetException();
    }

    /**
     * Asks the {@link #slider}and triggers a
     * {@link java.beans.PropertyChangeEvent}.
     */
    public void setTnow(long tnow) {
        // TODO Auto-generated method stub
    }
}