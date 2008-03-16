/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.core.base;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

import org.jcurl.core.helpers.MutableObject;

/**
 * Trajectory wrapping a {@link CurveStore}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:StoredTrajectorySet.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public class StoredTrajectorySet extends MutableObject implements TrajectorySet {

    private static final long serialVersionUID = -829911104054850124L;

    private final Map<CharSequence, CharSequence> annotations = new HashMap<CharSequence, CharSequence>();

    private transient final PositionSet currentPos = PositionSet.allHome();

    private transient final SpeedSet currentSpeed = new SpeedSet(PositionSet
            .allHome());

    private transient double currentTime = 0;

    private final CurveStore store;

    public StoredTrajectorySet(final CurveStore c) {
        store = c;
    }

    /**
     * Internal. Does not {@link RockSet#notifyChange()}!
     * 
     * @param currentTime
     * @param tmp
     */
    void doUpdatePosAndSpeed(final double currentTime, final double[] tmp) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            currentPos.getRock(i).setLocation(
                    store.getCurve(i).at(0, currentTime, tmp));
            currentSpeed.getRock(i).setLocation(
                    store.getCurve(i).at(1, currentTime, tmp));
        }
    }

    @Override
    public boolean equals(final Object obj) {
        return false;
    }

    public Map<CharSequence, CharSequence> getAnnotations() {
        return annotations;
    }

    public PositionSet getCurrentPos() {
        return currentPos;
    }

    public SpeedSet getCurrentSpeed() {
        return currentSpeed;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    protected Object readResolve() throws ObjectStreamException {
        return new StoredTrajectorySet(store);
    }

    public void setCurrentTime(final double currentTime) {
        // TUNE thread safety at the cost of two instanciations per call:
        final double[] tmp = { 0, 0, 0 };
        doUpdatePosAndSpeed(currentTime, tmp);
        {
            final double ot = this.currentTime;
            this.currentTime = currentTime;
            currentPos.notifyChange();
            currentSpeed.notifyChange();
            propChange.firePropertyChange("currentTime", ot, currentTime);
            propChange.firePropertyChange("currentPos", currentPos, currentPos);
            propChange.firePropertyChange("currentSpeed", currentSpeed,
                    currentSpeed);
        }
    }

}
