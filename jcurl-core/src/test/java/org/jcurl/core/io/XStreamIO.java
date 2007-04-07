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
package org.jcurl.core.io;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.helpers.DimVal;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XStreamIO {
    public static class DimValConverter implements Converter {
        public boolean canConvert(final Class arg0) {
            return DimVal.class.isAssignableFrom(arg0);
        }

        public void marshal(final Object arg0,
                final HierarchicalStreamWriter arg1,
                final MarshallingContext arg2) {
            final DimVal d = (DimVal) arg0;
            final StringBuffer s = new StringBuffer();
            s.append(d.val).append(" ").append(d.dim);
            arg1.setValue(s.toString());
        }

        public Object unmarshal(final HierarchicalStreamReader arg0,
                final UnmarshallingContext arg1) {
            return DimVal.parse(arg0.getValue());
        }
    }

    public static class RockConverter implements Converter {
        static final String num = "-?[0-9]+(?:[.][0-9]+)?(?:e-?[0-9]+)?";

        static final Pattern p = Pattern.compile("(" + num + "), (" + num
                + "), (" + num + ")");

        public boolean canConvert(final Class arg0) {
            return Rock.class.isAssignableFrom(arg0);
        }

        public void marshal(final Object arg0,
                final HierarchicalStreamWriter arg1,
                final MarshallingContext arg2) {
            final Rock d = (Rock) arg0;
            final StringBuffer s = new StringBuffer();
            s.append(d.getX()).append(", ");
            s.append(d.getY()).append(", ");
            s.append(d.getZ());
            arg1.setValue(s.toString());
        }

        public Object unmarshal(final HierarchicalStreamReader arg0,
                final UnmarshallingContext arg1) {
            final Matcher m = p.matcher(arg0.getValue());
            if (!m.matches())
                throw new IllegalStateException();
            return new RockDouble(Double.parseDouble(m.group(1)), Double
                    .parseDouble(m.group(2)), Double.parseDouble(m.group(3)));
        }
    }

    private final XStream xs;

    public XStreamIO() {
        xs = new XStream();
        xs.registerConverter(new DimValConverter());
        xs.registerConverter(new RockConverter());
        xs.alias("dimval", DimVal.class);
        xs.alias("rock", RockDouble.class);
    }

    public TrajectorySet read(String s) {
        return (TrajectorySet) xs.fromXML(s);
    }

    public String write(TrajectorySet t) {
        return xs.toXML(t);
    }
}
