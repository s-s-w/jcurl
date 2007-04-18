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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.jcurl.core.base.CurveStill;
import org.jcurl.core.base.CurveTransformed;
import org.jcurl.core.base.JCurlSerializer;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.StoredTrajectorySet;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerNoCurl;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.CurveStoreImpl;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.core.model.NewtonStopDetector;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.CurvePart;
import org.jcurl.math.PolynomeCurve;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Uses the great {@link XStream} for serialization.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class XStreamSerializer implements JCurlSerializer {

    static class DimValConverter implements Converter {

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

    static class DoubleArrayConverter implements Converter {

        public boolean canConvert(final Class arg0) {
            return double[].class.isAssignableFrom(arg0);
        }

        public void marshal(final Object arg0,
                final HierarchicalStreamWriter arg1,
                final MarshallingContext arg2) {
            final double[] d = (double[]) arg0;
            final StringBuffer s = new StringBuffer();
            for (final double element : d)
                s.append(element).append(' ');
            arg1.setValue(s.toString().trim());
        }

        public Object unmarshal(final HierarchicalStreamReader arg0,
                final UnmarshallingContext arg1) {
            final String[] p = arg0.getValue().split(" ");
            final double[] d = new double[p.length];
            for (int i = 0; i < d.length; i++)
                d[i] = Double.parseDouble(p[i]);
            return d;
        }
    }

    private static class Payload2007 implements Payload {
        private final Map<String, Object> annotations = new HashMap<String, Object>();

        private TrajectorySet[] trajectories;

        private Payload2007(final Map<String, Object> annotations,
                final TrajectorySet[] trajectories) {
            this.annotations.clear();
            if (annotations != null)
                this.annotations.putAll(annotations);
            this.trajectories = trajectories;
        }

        public Map<String, Object> getAnnotations() {
            return annotations;
        }

        public TrajectorySet[] getTrajectories() {
            return trajectories;
        }
    }

    public static class RockConverter implements Converter {
        static final String num = "-?[0-9]+(?:[.][0-9]+)?(?:e-?[0-9]+)?";

        static final Pattern p = Pattern.compile("(" + num + ") (" + num
                + ") (" + num + ")");

        public boolean canConvert(final Class arg0) {
            return Rock.class.isAssignableFrom(arg0);
        }

        public void marshal(final Object arg0,
                final HierarchicalStreamWriter arg1,
                final MarshallingContext arg2) {
            final Rock d = (Rock) arg0;
            final StringBuffer s = new StringBuffer();
            s.append(d.getX()).append(" ");
            s.append(d.getY()).append(" ");
            s.append(d.getA());
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

    public XStreamSerializer() {
        xs = new XStream();
        registerConverter(xs);
        registerAliases(xs);
    }

    public Payload read(final InputStream src, final Payload dst) {
        return (Payload) xs.fromXML(src, dst);
    }

    public Payload read(final Reader src, final Payload dst) {
        return (Payload) xs.fromXML(src, dst);
    };

    public Payload read(final String s) {
        return (Payload) xs.fromXML(s);
    }

    public Payload read(final URL src, final Payload dst) throws IOException {
        InputStream s = src.openStream();
        try {
            if (src.getFile().endsWith(".jcz"))
                s = new GZIPInputStream(s);
            return read(s, dst);
        } finally {
            s.close();
        }
    }

    /**
     * Map all basic concepts to be a little bit robust against refactorings.
     * Make the aliases upper- or camelcase to distinguish them from properties.
     */
    protected XStream registerAliases(final XStream xs) {
        xs.alias("DimVal", DimVal.class);
        xs.alias("Rock", RockDouble.class);
        // 
        xs.alias("org.jcurl.container.2007", Payload2007.class);
        // 
        xs.alias("StoredTrajectory", StoredTrajectorySet.class);
        xs.alias("CombinedCurve", CurveCombined.class);
        xs.alias("CurvePart", CurvePart.class);
        xs.alias("TransformedCurve", CurveTransformed.class);
        xs.alias("PolynomeCurve", PolynomeCurve.class);
        xs.alias("PointCurve", CurveStill.class);
        // 
        xs.alias("CurveManager", CurveManager.class);
        xs.alias("CurveStore", CurveStoreImpl.class);
        xs.alias("NewtonStopDetector", NewtonStopDetector.class);
        xs.alias("CollissionSpin", CollissionSpin.class);
        xs.alias("NewtonCollissionDetector", NewtonCollissionDetector.class);
        xs.alias("NoCurlCurler", CurlerNoCurl.class);
        return xs;
    }

    protected XStream registerConverter(final XStream xs) {
        xs.registerConverter(new DimValConverter());
        xs.registerConverter(new DoubleArrayConverter());
        xs.registerConverter(new RockConverter());
        return xs;
    }

    public Payload wrap(final Map<String, Object> annotations,
            final TrajectorySet[] trajectories) {
        return new Payload2007(annotations, trajectories);
    }

    public Payload wrap(Map<String, Object> annotations, final TrajectorySet t) {
        return wrap(null, new TrajectorySet[] { t });
    }

    public String write(final Payload src) {
        return xs.toXML(src);
    }

    public void write(final Payload src, final File dst) throws IOException {
        OutputStream d = new FileOutputStream(dst);
        try {
            if (dst.getName().endsWith(".jcz"))
                d = new GZIPOutputStream(d);
            write(src, d);
        } finally {
            d.close();
        }
    }

    public void write(final Payload src, final OutputStream dst) {
        xs.toXML(src, dst);
    }

    public void write(final Payload src, final Writer dst) {
        xs.toXML(src, dst);
    }
}
