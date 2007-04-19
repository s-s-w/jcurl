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
package org.jcurl.mr.gui;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.Collider;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.Curler;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CollissionSimple;
import org.jcurl.core.model.CurlerNoCurl;
import org.jcurl.core.model.CurveManager;

/**
 * Higher level Data Model - more abstract than {@link ComputedTrajectorySet}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Model extends MutableObject {

    private static final Log log = JCLoggerFactory.getLogger(Model.class);

    private static final long serialVersionUID = -8598083673757204804L;

    private int activeRock;

    private DimVal broomX = null;

    private DimVal interval;

    private ComputedTrajectorySet trajectory;

    public Model() {
        trajectory = new CurveManager();
        trajectory.setCollider(new CollissionSimple());
        trajectory.setCurler(new CurlerNoCurl(23, 0));
        // setBroomX(new DimVal(0, Dim.METER));
        // setInterval(new DimVal(2.5, Dim.SECOND));
        // setDrawCurl(new DimVal(3, Dim.FOOT));
        // setDrawTime(new DimVal(25, Dim.SECOND));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        final Model other = (Model) obj;
        if (activeRock != other.activeRock)
            return false;
        if (broomX == null) {
            if (other.broomX != null)
                return false;
        } else if (!broomX.equals(other.broomX))
            return false;
        if (interval == null) {
            if (other.interval != null)
                return false;
        } else if (!interval.equals(other.interval))
            return false;
        if (trajectory == null) {
            if (other.trajectory != null)
                return false;
        } else if (!trajectory.equals(other.trajectory))
            return false;
        return true;
    }

    public int getActiveRock() {
        return activeRock;
    }

    public DimVal getBroomX() {
        return broomX;
    }

    public Collider getCollider() {
        return getTrajectory().getCollider();
    }

    public Curler getCurler() {
        return getTrajectory().getCurler();
    }

    public DimVal getDrawCurl() {
        return new DimVal(getTrajectory().getCurler().getDrawToTeeCurl(),
                Dim.METER);
    }

    public DimVal getDrawTime() {
        return new DimVal(getTrajectory().getCurler().getDrawToTeeTime(),
                Dim.SECOND);
    }

    public PositionSet getInitialPos() {
        return getTrajectory().getInitialPos();
    }

    public DimVal getInterval() {
        return interval;
    }

    public ComputedTrajectorySet getTrajectory() {
        return trajectory;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + activeRock;
        result = PRIME * result + (broomX == null ? 0 : broomX.hashCode());
        result = PRIME * result + (interval == null ? 0 : interval.hashCode());
        result = PRIME * result
                + (trajectory == null ? 0 : trajectory.hashCode());
        return result;
    }

    public void setActiveRock(final int activeRock) {
        propChange
                .firePropertyChange("activeRock", getActiveRock(), activeRock);
        this.activeRock = activeRock & 0xF;
    }

    public void setBroomX(final DimVal broomX) {
        if (broomX == null)
            return;
        if (this.broomX == broomX)
            return;
        propChange.firePropertyChange("broomX", getBroomX(), broomX);
        this.broomX = broomX;
    }

    public void setCollider(final Collider collider) {
        if (collider == null)
            return;
        propChange.firePropertyChange("collider", getCollider(), collider);
        getTrajectory().setCollider(collider);
    }

    public void setCurler(final Curler curler) {
        if (curler == null)
            return;
        propChange.firePropertyChange("curler", getCurler(), curler);
        getTrajectory().setCurler(curler);
    }

    public void setDrawCurl(final DimVal drawCurl) {
        propChange.firePropertyChange("drawCurl", getDrawCurl(), drawCurl);
        getTrajectory().getCurler()
                .setDrawToTeeCurl(drawCurl.to(Dim.METER).val);
    }

    public void setDrawTime(final DimVal drawTime) {
        propChange.firePropertyChange("drawTime", getDrawTime(), drawTime);
        getTrajectory().getCurler().setDrawToTeeTime(
                drawTime.to(Dim.SECOND).val);
    }

    public void setInitialPos(final PositionSet initialPos) {
        if (initialPos == null)
            return;
        propChange
                .firePropertyChange("initialPos", getInitialPos(), initialPos);
        getTrajectory().setInitialPos(initialPos);
    }

    public void setInterval(final DimVal interval) {
        propChange.firePropertyChange("interval", getInterval(), interval);
        this.interval = interval.to(Dim.SECOND);
    }

    public void setTrajectory(final ComputedTrajectorySet trajectory) {
        if (trajectory == null)
            return;
        propChange
                .firePropertyChange("trajectory", getTrajectory(), trajectory);
        this.trajectory = trajectory;
    }
}