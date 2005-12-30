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
package jcurl.sim.model;

import java.awt.geom.PathIterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jcurl.core.NotImplementedYetException;
import jcurl.core.Source;
import jcurl.core.dto.MutableObject;
import jcurl.core.dto.RockSetProps;
import jcurl.core.io.SetupBuilder;
import jcurl.core.io.SetupIO;
import jcurl.math.CurveBase;
import jcurl.model.PositionSet;
import jcurl.model.RockSet;
import jcurl.model.SpeedSet;
import jcurl.sim.core.CollissionStrategy;
import jcurl.sim.core.SlideStrategy;

import org.xml.sax.SAXException;

/**
 * @see jcurl.sim.model.ModelImplTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ComputedSource extends MutableObject implements Source {

    private double maxT;

    private double minT;

    private SlideStrategy slide = new SlideStraight();

    private final PositionSet startPos = PositionSet.allHome();

    private final SpeedSet startSpeed = new SpeedSet();

    private CurveBase curveIterator(int idx) {
        throw new NotImplementedYetException();
    }

    public boolean equals(Object obj) {
        return false;
    }

    public CollissionStrategy getColl() {
        return slide.getColl();
    }

    public double getMaxT() {
        return maxT;
    }

    public double getMinT() {
        return minT;
    }

    public PositionSet getPos() {
        return slide.getPos();
    }

    public SlideStrategy getSlide() {
        return slide;
    }

    public SpeedSet getSpeed() {
        return slide.getSpeed();
    }

    public double getT() {
        return slide.getT();
    }

    private void init(PositionSet startPos, SpeedSet startSpeed,
            RockSetProps props) {
        init(startPos, startSpeed, null, null);
    }

    public void init(final PositionSet x, final SpeedSet v,
            final SlideStrategy ice, final CollissionStrategy coll) {
        minT = 0;
        maxT = 35;
        if (coll != null)
            setColl(coll);
        if (ice != null) {
            propChange.firePropertyChange("slide", this.slide, ice);
            this.slide = ice;
            this.slide.setColl(getColl());
        }
        RockSet.copy(x, startPos);
        RockSet.copy(v, startSpeed);
        this.slide.reset(x, v, RockSetProps.DEFAULT);
        //        propChange.firePropertyChange("pos", slide.getPos(), ice.getPos());
        //        propChange
        //                .firePropertyChange("speed", slide.getSpeed(), ice.getSpeed());
        setT(minT);
    }

    public boolean isDiscrete() {
        return false;
    }

    public boolean isForwardOnly() {
        return false;
    }

    public boolean isWithSpeed() {
        return true;
    }

    public void load(final InputStream in) throws SAXException, IOException {
        final SetupBuilder sb = SetupIO.load(in);
        this.init(sb.getPos(), sb.getSpeed(), sb.getSlide(),
                new CollissionSpin());
    }

    public PathIterator pathIterator(int idx) {
        throw new NotImplementedYetException();
    }

    public void save(final OutputStream out) throws SAXException {
        SetupIO.save(out, this.startPos, this.startSpeed, this.getSlide(), this
                .getColl());
    }

    private void setColl(CollissionStrategy coll) {
        propChange.firePropertyChange("coll", this.slide.getColl(), coll);
        this.slide.setColl(coll);
    }

    public void setT(double current) {
        propChange.firePropertyChange("t", this.slide.getT(), current);
        slide.setT(current);
    }
}