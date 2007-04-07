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

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.helpers.XmlSimpleWriter;
import org.jcurl.core.model.CollissionSimple;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.core.model.SlideNoCurl;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class ComputedTrajectorySetXmlTest extends TestCase {

    public void test010() throws IOException, SAXException {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSimple());
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setSlider(new SlideNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getSlider().computeV0(5), Math.PI / 2);

        StringWriter sw = new StringWriter();
        // Writer w = new OutputStreamWriter(System.out);
        XmlSimpleWriter xs = new XmlSimpleWriter(sw);
        ComputedTrajectorySetXml xml = new ComputedTrajectorySetXml();
        xml.write(te, xs);
        sw.close();
        System.out.println(sw.getBuffer().toString());
        assertEquals(
                "<trajectory class=\"org.jcurl.core.model.CurveManager\"><collider class=\"org.jcurl.core.base.Collider\"></collider><collissionDetector class=\"org.jcurl.core.base.CollissionDetector\"></collissionDetector><currentPos class=\"org.jcurl.core.base.PositionSet\"><lastChanged class=\"long\"></lastChanged></currentPos><currentSpeed class=\"org.jcurl.core.base.SpeedSet\"><lastChanged class=\"long\"></lastChanged></currentSpeed><currentTime class=\"double\"><infinite class=\"boolean\"></infinite><naN class=\"boolean\"></naN></currentTime><curveStore class=\"org.jcurl.core.base.CurveStore\"></curveStore><initialPos class=\"org.jcurl.core.base.PositionSet\"><lastChanged class=\"long\"></lastChanged></initialPos><initialSpeed class=\"org.jcurl.core.base.SpeedSet\"><lastChanged class=\"long\"></lastChanged></initialSpeed><slider class=\"org.jcurl.core.base.Slider\"><drawToTeeCurl class=\"double\"><infinite class=\"boolean\"></infinite><naN class=\"boolean\"></naN></drawToTeeCurl><drawToTeeTime class=\"double\"><infinite class=\"boolean\"></infinite><naN class=\"boolean\"></naN></drawToTeeTime></slider></trajectory>",
                sw.getBuffer().toString());
    }

    public void testXStream() {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSimple());
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setSlider(new SlideNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getSlider().computeV0(5), Math.PI / 2);

        XStream xs = new XStream();
        // xs.omitField(CurveManager.class, "collissionStore");
        // xs.omitField(CurveManager.class, "curveStore");
        // xs.omitField(CurveManager.class, "currentPos");
        // xs.omitField(CurveManager.class, "currentSpeed");
        // xs.omitField(CurveManager.class, "currentTime");
        // xs.omitField(CurveManager.class, "dirty");
        xs.alias("rock", RockDouble.class);
        // xs.alias("position", PositionSet.class);
        // xs.alias("speed", SpeedSet.class);
        assertEquals("", xs.toXML(te));
    }

    public class DimValConverter extends AbstractSingleValueConverter {

        public boolean canConvert(Class clazz) {
            return clazz.equals(DimVal.class);
        }

        public Object fromString(String str) {
            return DimVal.parse(str);
        }

    }
}
