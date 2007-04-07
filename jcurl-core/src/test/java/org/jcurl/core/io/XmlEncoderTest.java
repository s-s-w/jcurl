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

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.model.CollissionSimple;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.core.model.SlideNoCurl;

public class XmlEncoderTest extends TestCase {

    public void test010() throws UnsupportedEncodingException {
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

        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final XMLEncoder xs = new XMLEncoder(bout);
        xs.writeObject(te);
        xs.close();
        System.out.println(new String(bout.toByteArray(), "UTF-8"));

    }

}
