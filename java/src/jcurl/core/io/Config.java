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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import jcurl.core.RockSet;
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockSetProps;
import jcurl.sim.core.CollissionStrategy;
import jcurl.sim.core.SlideStrategy;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Config {

    static DimVal getDim(String token) {
        if ("HACK".equals(token))
            return new DimVal(Ice.FAR_HACK_2_TEE, Dim.METER);
        if ("FHOG".equals(token))
            return new DimVal(Ice.HOG_2_HOG + Ice.HOG_2_TEE, Dim.METER);
        if ("NHOG".equals(token))
            return new DimVal(Ice.HOG_2_TEE, Dim.METER);
        return DimVal.parse(token);
    }

    public static Config load(final File in) throws FileNotFoundException {
        return load(new FileReader(in));
    }

    public static Config load(final InputStream in, final String encoding)
            throws UnsupportedEncodingException {
        return load(new InputStreamReader(in, encoding));
    }

    public static Config load(final Reader in) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    private Config() {

    }

    public CollissionStrategy getHitter() {
        return null;
    }

    public RockSet getPosition() {
        return null;
    }

    public RockSetProps getProps() {
        return null;
    }

    public SlideStrategy getSlider() {
        return null;
    }

    public long getTime() {
        return -1;
    }

    public RockSet getSpeed() {
        return null;
    }
}