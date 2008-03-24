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
package org.jcurl.core.xsio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcurl.core.api.Measure;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.impl.CollissionSpin;
import org.jcurl.core.impl.CurlerNoCurl;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.impl.CurveStill;
import org.jcurl.core.impl.CurveStoreImpl;
import org.jcurl.core.impl.CurveTransformed;
import org.jcurl.core.impl.NewtonCollissionDetector;
import org.jcurl.core.impl.NewtonStopDetector;
import org.jcurl.core.impl.StoredTrajectorySet;
import org.jcurl.core.io.IODocument;
import org.jcurl.core.io.IOTrajectories;
import org.jcurl.core.io.JCurlSerializer.Engine;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.PolynomeCurve;
import org.jcurl.math.CurveCombined.Part;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * Uses the great {@link XStream} for serialization.
 * <p>
 * To be extensible the configuration ({@link XStream#registerConverter(Converter)}
 * and {@link XStream#alias(String, Class)}) should be applied by
 * implementations of an interface and the service provider plugin mechanism.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:XStreamSerializer.java 752 2007-12-09 22:49:16Z mrohrmoser $
 */
public class XStreamSerializer implements Engine {

    private static class DoubleArrayConverter implements SingleValueConverter {

        @SuppressWarnings("unchecked")
        public boolean canConvert(final Class arg0) {
            return double[].class.isAssignableFrom(arg0);
        }

        public Object fromString(final String s) {
            final String[] p = s.split(" ");
            final double[] d = new double[p.length];
            for (int i = 0; i < d.length; i++)
                d[i] = Double.parseDouble(p[i]);
            return d;
        }

        public String toString(final Object obj) {
            final double[] d = (double[]) obj;
            final StringBuilder s = new StringBuilder();
            for (final double element : d)
                s.append(element).append(' ');
            return s.toString().trim();
        }
    }

    static class MeasureConverter implements SingleValueConverter {

        @SuppressWarnings("unchecked")
        public boolean canConvert(final Class arg0) {
            return Measure.class.isAssignableFrom(arg0);
        }

        public Object fromString(final String s) {
            return Measure.parse(s);
        }

        public String toString(final Object obj) {
            final Measure d = (Measure) obj;
            final StringBuilder s = new StringBuilder();
            s.append(d.value).append(" ").append(d.unit);
            return s.toString();
        }
    }

    static class RockConverter implements SingleValueConverter {
        static final String num = "-?[0-9]+(?:[.][0-9]+)?(?:e-?[0-9]+)?";

        static final Pattern p = Pattern.compile("(" + num + ") (" + num
                + ") (" + num + ")");

        @SuppressWarnings("unchecked")
        public boolean canConvert(final Class arg0) {
            return Rock.class.isAssignableFrom(arg0);
        }

        public Object fromString(final String s) {
            final Matcher m = p.matcher(s);
            if (!m.matches())
                throw new IllegalStateException();
            return new RockDouble(Double.parseDouble(m.group(1)), Double
                    .parseDouble(m.group(2)), Double.parseDouble(m.group(3)));
        }

        public String toString(final Object obj) {
            final Rock d = (Rock) obj;
            final StringBuilder s = new StringBuilder();
            s.append(d.getX()).append(" ");
            s.append(d.getY()).append(" ");
            s.append(d.getA());
            return s.toString();
        }
    }

    private final XStream xs;

    public XStreamSerializer() {
        xs = new XStream();
        registerConverter(xs);
        registerAliases(xs);
    }

    public IODocument read(final InputStream src) throws IOException {
        return (IODocument) xs.fromXML(src);
    }

    public IODocument read(final InputStream src, final IODocument dst) {
        return (IODocument) xs.fromXML(src, dst);
    };

    public IODocument read(InputStream src, final String name,
            final IODocument dst) throws IOException {
        return read(src, dst);
    }

    public IODocument read(final Reader src, final IODocument dst) {
        return (IODocument) xs.fromXML(src, dst);
    }

    public IODocument read(final String s) {
        return (IODocument) xs.fromXML(s);
    }


    /**
     * Map all basic concepts to be a little bit robust against refactorings.
     * Make the aliases upper- or camelcase to distinguish them from properties.
     */
    protected XStream registerAliases(final XStream xs) {
        xs.alias("measure", Measure.class);
        xs.alias("rock", RockDouble.class);
        // 
        xs.alias("IODocument", IODocument.class);
        xs.alias("IOTrajectories", IOTrajectories.class);
        // 
        xs.alias("StoredTrajectory", StoredTrajectorySet.class);
        xs.alias("CombinedCurve", CurveCombined.class);
        xs.alias("part", Part.class);
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
        xs.registerConverter(new MeasureConverter());
        xs.registerConverter(new DoubleArrayConverter());
        xs.registerConverter(new RockConverter());
        return xs;
    }

    public String write(final IODocument src) {
        return xs.toXML(src);
    }

    public void write(final IODocument src, final OutputStream dst) {
        xs.toXML(src, dst);
    }

    public void write(final IODocument src, final Writer dst) {
        xs.toXML(src, dst);
    }
}
