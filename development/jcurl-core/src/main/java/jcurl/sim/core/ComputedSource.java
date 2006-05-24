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
package jcurl.sim.core;

import java.awt.geom.PathIterator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jcurl.core.Source;
import jcurl.core.dto.RockSetProps;
import jcurl.core.io.SetupBuilder;
import jcurl.core.io.SetupIO;
import jcurl.sim.model.CollissionSpin;
import jcurl.sim.model.SlideStraight;

import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.PositionSet;
import org.jcurl.core.RockSet;
import org.jcurl.core.SpeedSet;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.math.analysis.CurveGhost;
import org.xml.sax.SAXException;

/**
 * Base for all - both numerical or analytic - computed rock paths.
 * <p>
 * Closely related to {@link jcurl.sim.core.SlideStrategy}.
 * </p>
 * 
 * @see jcurl.sim.core.ComputedSourceTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ComputedSource extends MutableObject implements Source {

    private static final long serialVersionUID = 6736340176255195690L;

    private double maxT;

    private double minT;

    private SlideStrategy slide = new SlideStraight();

    private final PositionSet startPos = PositionSet.allHome();

    private final SpeedSet startSpeed = new SpeedSet();

    private CurveGhost curveIterator(int idx) {
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

    public void init(final PositionSet x, final SpeedSet v,
            final SlideStrategy ice, final CollissionStrategy coll)
            throws FunctionEvaluationException {
        minT = 0;
        maxT = 35;
        if (coll != null) {
            propChange.firePropertyChange("coll", this.slide.getColl(), coll);
            this.slide.setColl(coll);
        }
        if (ice != null) {
            propChange.firePropertyChange("slide", this.slide, ice);
            this.slide = ice;
            this.slide.setColl(getColl());
        }
        RockSet.copy(x, startPos);
        RockSet.copy(v, startSpeed);
        this.slide.reset(x, v, RockSetProps.DEFAULT);
        // propChange.firePropertyChange("pos", slide.getPos(), ice.getPos());
        // propChange
        // .firePropertyChange("speed", slide.getSpeed(), ice.getSpeed());
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

    /**
     * Load a start setup. Rock positions + speeds as well as ice properties.
     * 
     * @param in
     * @throws SAXException
     * @throws IOException
     * @throws FunctionEvaluationException
     */
    public void loadStart(final InputStream in) throws SAXException,
            IOException, FunctionEvaluationException {
        final SetupBuilder sb = SetupIO.load(in);
        this.init(sb.getPos(), sb.getSpeed(), sb.getSlide(),
                new CollissionSpin());
    }

    /**
     * Get a Java2d drawable rock trajectory per rock.
     * 
     * @param idx
     *            rock index
     * @return ???
     */
    public PathIterator pathIterator(int idx) {
        throw new NotImplementedYetException();
    }

    /**
     * Save a start setup. Rock positions + speeds as well as ice properties.
     * 
     * @param out
     * @throws SAXException
     */
    public void saveStart(final OutputStream out) throws SAXException {
        SetupIO.save(out, this.startPos, this.startSpeed, this.getSlide(), this
                .getColl());
    }

    public void setT(double current) throws FunctionEvaluationException {
        propChange.firePropertyChange("t", this.slide.getT(), current);
        slide.setT(current);
    }
}