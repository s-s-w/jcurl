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
package jcurl.core.io;

import jcurl.core.NotImplementedYetException;
import jcurl.core.PositionSet;
import jcurl.core.SpeedSet;
import jcurl.sim.core.SlideStrategy;

import org.apache.ugli.LoggerFactory;
import org.apache.ugli.ULogger;

/**
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SetupBuilder {

    private static final ULogger log = LoggerFactory
            .getLogger(SetupBuilder.class);

    public static int toIdx(final boolean isDark, final int no) {
        return 2 * no + (isDark ? 1 : 0);
    }

    public void freeze() {
        log.debug("-");
    }

    public PositionSet getPos() {
        throw new NotImplementedYetException();
    }

    public SlideStrategy getSlide() {
        throw new NotImplementedYetException();
    }

    public SpeedSet getSpeed() {
        throw new NotImplementedYetException();
    }

    public void setCollModel(final String v) throws ClassNotFoundException {
        final Class clz = Class.forName(v);
        log.debug(clz);
    }

    public void setCollParam(final String name, final DimVal val) {
        log.debug(name + " " + val);
    }

    public void setDrawCurl(final DimVal v) {
        log.debug(v);
    }

    public void setDrawTime(final DimVal v) {
        log.debug(v);
    }

    public void setEvent(final String v) {
        log.debug(v);
    }

    public void setGame(final String v) {
        log.debug(v);
    }

    public void setIceModel(final String v) throws ClassNotFoundException {
        final Class clz = Class.forName(v);
        log.debug(clz);
    }

    public void setIceParam(final String name, final DimVal val) {
        log.debug(name + " " + val);
    }

    public void setLoss(final DimVal v) {
        log.debug(v);
    }

    public void setPosA(final int idx, final DimVal v) {
        log.debug(v);
    }

    public void setPosNHog(final int idx) {
        log.debug("-");
    }

    public void setPosOut(final int idx) {
        log.debug("-");
    }

    public void setPosRelease(final int idx) {
        log.debug("-");
    }

    public void setPosX(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }

    public void setPosY(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }

    public void setSpeed(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }

    public void setSpeedA(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }

    public void setSpeedX(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }

    public void setSpeedY(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }

    public void setSpin(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }

    public void setToX(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }

    public void setToY(final int idx, final DimVal v) {
        log.debug(idx + " " + v);
    }
}