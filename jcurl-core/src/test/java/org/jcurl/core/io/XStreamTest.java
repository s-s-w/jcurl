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

import junit.framework.TestCase;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.model.CollissionSimple;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.core.model.SlideNoCurl;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XStreamTest extends TestCase {
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

    public void test010() {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSpin());
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setSlider(new SlideNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getSlider().computeV0(5), Math.PI / 2);

        final XStream xs = new XStream();
        xs.registerConverter(new DimValConverter());
        xs.registerConverter(new RockConverter());
        xs.alias("dimval", DimVal.class);
        xs.alias("rock", RockDouble.class);
        String x = xs.toXML(te);
        // System.out.println(x);
        assertEquals(
                "<org.jcurl.core.model.CurveManager>\n"
                        + "  <collider class=\"org.jcurl.core.model.CollissionSpin\">\n"
                        + "    <params>\n"
                        + "      <entry>\n"
                        + "        <string>loss</string>\n"
                        + "        <dimval>0.0 J</dimval>\n"
                        + "      </entry>\n"
                        + "      <entry>\n"
                        + "        <string>frictionRockRock</string>\n"
                        + "        <dimval>0.0 </dimval>\n"
                        + "      </entry>\n"
                        + "    </params>\n"
                        + "  </collider>\n"
                        + "  <collissionDetector class=\"org.jcurl.core.model.NewtonCollissionDetector\"/>\n"
                        + "  <initialPos>\n"
                        + "    <dark>\n"
                        + "      <rock>0.0, 6.4008002281188965, 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064, 9.083040237426758, 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064, 8.717280387878418, 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064, 8.351519584655762, 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064, 7.98576021194458, 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064, 7.619999885559082, 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064, 7.254240036010742, 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064, 6.888480186462402, 0.0</rock>\n"
                        + "    </dark>\n"
                        + "    <light>\n"
                        + "      <rock>0.1, 1.8287999629974365, 0.7853981633974483</rock>\n"
                        + "      <rock>2.2098000049591064, 9.083040237426758, 0.0</rock>\n"
                        + "      <rock>2.2098000049591064, 8.717280387878418, 0.0</rock>\n"
                        + "      <rock>2.2098000049591064, 8.351519584655762, 0.0</rock>\n"
                        + "      <rock>2.2098000049591064, 7.98576021194458, 0.0</rock>\n"
                        + "      <rock>2.2098000049591064, 7.619999885559082, 0.0</rock>\n"
                        + "      <rock>2.2098000049591064, 7.254240036010742, 0.0</rock>\n"
                        + "      <rock>2.2098000049591064, 6.888480186462402, 0.0</rock>\n"
                        + "    </light>\n"
                        + "  </initialPos>\n"
                        + "  <initialSpeed>\n"
                        + "    <dark>\n"
                        + "      <rock>0.0, -1.3779956114540028, 1.5707963267948966</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "    </dark>\n"
                        + "    <light>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "      <rock>0.0, 0.0, 0.0</rock>\n"
                        + "    </light>\n"
                        + "  </initialSpeed>\n"
                        + "  <slider class=\"org.jcurl.core.model.SlideNoCurl\">\n"
                        + "    <params>\n" + "      <entry>\n"
                        + "        <string>drawToTeeTime</string>\n"
                        + "        <dimval>23.0 s</dimval>\n"
                        + "      </entry>\n" + "      <entry>\n"
                        + "        <string>drawToTeeCurl</string>\n"
                        + "        <dimval>0.0 m</dimval>\n"
                        + "      </entry>\n" + "    </params>\n"
                        + "  </slider>\n"
                        + "</org.jcurl.core.model.CurveManager>", x);

        CurveManager o = (CurveManager) xs.fromXML(x);
        assertNotNull(o);
        assertNotNull(o.getCollider());
        assertNotNull(o.getCollissionDetector());
        assertNotNull(o.getCurrentPos());
        assertNotNull(o.getCurrentSpeed());
        assertNotNull(o.getCurveStore());
        assertNotNull(o.getInitialPos());
        assertNotNull(o.getInitialSpeed());
        assertNotNull(o.getSlider());
    }

    public void testPattern() {
        final Pattern p = Pattern
                .compile("-?[0-9]+(?:[.][0-9]+)?(?:e-?[0-9]+)?");
        Matcher m = p.matcher("0");
        assertTrue(m.matches());
        assertEquals(0, m.groupCount());
        m = p.matcher("-0");
        assertTrue(m.matches());
        assertEquals(0, m.groupCount());
        m = p.matcher("-0.4");
        assertTrue(m.matches());
        assertEquals(0, m.groupCount());
        m = p.matcher("-0.4e-3");
        assertTrue(m.matches());
        assertEquals(0, m.groupCount());
    }

}
