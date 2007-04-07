/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.jcurl.core.base.Collider;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockSetProps;
import org.jcurl.core.base.Slider;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Config.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class Config {

    static DimVal getDim(final String token) {
        if ("HACK".equals(token))
            return new DimVal(IceSize.FAR_HACK_2_TEE, Dim.METER);
        if ("FHOG".equals(token))
            return new DimVal(IceSize.HOG_2_HOG + IceSize.HOG_2_TEE, Dim.METER);
        if ("NHOG".equals(token))
            return new DimVal(IceSize.HOG_2_TEE, Dim.METER);
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

    public Collider getHitter() {
        return null;
    }

    public PositionSet getPosition() {
        return null;
    }

    public RockSetProps getProps() {
        return null;
    }

    public Slider getSlider() {
        return null;
    }

    public long getTime() {
        return -1;
    }

    public PositionSet getSpeed() {
        return null;
    }
}