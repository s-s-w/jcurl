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
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.JCurlSerializer;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.StoredTrajectorySet;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.base.JCurlSerializer.Payload;
import org.jcurl.core.helpers.AnnoHelp;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerNoCurl;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.FixpointZoomer;
import org.jcurl.core.model.NewtonCollissionDetector;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

public class XStreamIOTest extends TestShowBase {
    public static ComputedTrajectorySet initHammy(ComputedTrajectorySet te) {
        if (te == null)
            te = new CurveManager();
        te.setCollider(new CollissionSpin(0.5, 0.0));
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerNoCurl(24, 0));
        te.setInitialPos(PositionSet.allOut());
        te.setInitialSpeed(new SpeedSet());
        te.getAnnotations().put(AnnoHelp.HammerK, AnnoHelp.HammerVDark);
        te.getAnnotations().put(AnnoHelp.DarkTeamK, "Scotland");
        te.getAnnotations().put(AnnoHelp.LightTeamK, "Canada");
        te.getAnnotations().put(AnnoHelp.GameK, "Semifinal");
        te.getAnnotations().put(AnnoHelp.EventK, "World Curling Championships");
        te.getAnnotations().put(AnnoHelp.DateK, "1992");
        te.getAnnotations().put(AnnoHelp.LocationK, "Garmisch");
        initHammy(te.getInitialPos(), te.getInitialSpeed());
        return te;
    }

    public static void initHammy(final PositionSet p, final SpeedSet s) {
        PositionSet.allOut(p);
        // te.getInitialPos().getLight(1-1).setLocation(
        p.getLight(2 - 1)
                .setLocation(Dim.f2m(-1.170732), Dim.f2m(15.365854), 0);
        p.getLight(3 - 1).setLocation(Dim.f2m(0.292683), Dim.f2m(8.780488), 0);
        p.getLight(4 - 1).setLocation(Dim.f2m(2.195122), Dim.f2m(12), 0);
        p.getLight(5 - 1).setLocation(Dim.f2m(1.463415), Dim.f2m(5.707317), 0);
        p.getLight(6 - 1).setLocation(Dim.f2m(1.463415), Dim.f2m(-2.780488), 0);
        p.getLight(7 - 1)
                .setLocation(Dim.f2m(-0.439024), Dim.f2m(-5.560976), 0);
        p.getLight(8 - 1)
                .setLocation(Dim.f2m(-1.756098), Dim.f2m(-1.609756), 0);
        // p.getDark(1-1).setLocation(
        // p.getDark(2-1).setLocation(
        p.getDark(3 - 1).setLocation(Dim.f2m(0.878049), Dim.f2m(14.341463), 0);
        p.getDark(4 - 1).setLocation(Dim.f2m(-2.634146), Dim.f2m(13.170732), 0);
        p.getDark(5 - 1).setLocation(Dim.f2m(4.536585), Dim.f2m(-0.439024), 0);
        p.getDark(6 - 1).setLocation(Dim.f2m(0.731707), Dim.f2m(-3.95122), 0);
        p.getDark(7 - 1).setLocation(Dim.f2m(-2.780488), Dim.f2m(-4.390244), 0);
        p.getDark(8 - 1).setLocation(Dim.f2m(3.89991), IceSize.HOG_2_TEE, 0);
        RockSet.allZero(s);
        s.getDark(7).setLocation(0, -3, 100 * Math.PI / 180);
        p.notifyChange();
        s.notifyChange();
    }

    public void testDefaultSettings() {
        XStream xs = new XStream();
        System.out.println(xs.getClassLoader());
        assertEquals(Sun14ReflectionProvider.class, xs.getReflectionProvider()
                .getClass());
    }

    public void testComputedTrajectorySet() throws IOException {
        final XStreamSerializer xs = new XStreamSerializer();
        final Payload con = xs.wrap(null, initHammy(null));
        final String x = xs.write(con);
        assertEquals(
                "<org.jcurl.container.2007>\n"
                        + "  <annotations/>\n"
                        + "  <trajectories>\n"
                        + "    <CurveManager>\n"
                        + "      <annotations>\n"
                        + "        <entry>\n"
                        + "          <string>org.jcurl.core.Light.Team</string>\n"
                        + "          <string>Canada</string>\n"
                        + "        </entry>\n"
                        + "        <entry>\n"
                        + "          <string>org.jcurl.core.Game</string>\n"
                        + "          <string>Semifinal</string>\n"
                        + "        </entry>\n"
                        + "        <entry>\n"
                        + "          <string>org.jcurl.core.Hammer</string>\n"
                        + "          <string>Dark</string>\n"
                        + "        </entry>\n"
                        + "        <entry>\n"
                        + "          <string>org.jcurl.core.Location</string>\n"
                        + "          <string>Garmisch</string>\n"
                        + "        </entry>\n"
                        + "        <entry>\n"
                        + "          <string>org.jcurl.core.Dark.Team</string>\n"
                        + "          <string>Scotland</string>\n"
                        + "        </entry>\n"
                        + "        <entry>\n"
                        + "          <string>org.jcurl.core.Event</string>\n"
                        + "          <string>World Curling Championships</string>\n"
                        + "        </entry>\n"
                        + "        <entry>\n"
                        + "          <string>org.jcurl.core.Date</string>\n"
                        + "          <string>1992</string>\n"
                        + "        </entry>\n"
                        + "      </annotations>\n"
                        + "      <collider class=\"CollissionSpin\">\n"
                        + "        <params>\n"
                        + "          <entry>\n"
                        + "            <string>loss</string>\n"
                        + "            <DimVal>0.0 J</DimVal>\n"
                        + "          </entry>\n"
                        + "          <entry>\n"
                        + "            <string>frictionRockRock</string>\n"
                        + "            <DimVal>0.5 </DimVal>\n"
                        + "          </entry>\n"
                        + "        </params>\n"
                        + "      </collider>\n"
                        + "      <collissionDetector class=\"NewtonCollissionDetector\"/>\n"
                        + "      <curler class=\"NoCurlCurler\">\n"
                        + "        <params>\n"
                        + "          <entry>\n"
                        + "            <string>drawToTeeTime</string>\n"
                        + "            <DimVal>24.0 s</DimVal>\n"
                        + "          </entry>\n"
                        + "          <entry>\n"
                        + "            <string>drawToTeeCurl</string>\n"
                        + "            <DimVal>0.0 m</DimVal>\n"
                        + "          </entry>\n"
                        + "        </params>\n"
                        + "      </curler>\n"
                        + "      <initialPos>\n"
                        + "        <dark>\n"
                        + "          <Rock>-0.7620000243186951 -2.4384000301361084 0.0</Rock>\n"
                        + "          <Rock>-0.9448800086975098 -2.8041601181030273 0.0</Rock>\n"
                        + "          <Rock>0.26762932538986206 4.371277809143066 0.0</Rock>\n"
                        + "          <Rock>-0.8028876781463623 4.014439105987549 0.0</Rock>\n"
                        + "          <Rock>1.3827511072158813 -0.1338145136833191 0.0</Rock>\n"
                        + "          <Rock>0.22302429378032684 -1.204331874847412 0.0</Rock>\n"
                        + "          <Rock>-0.8474927544593811 -1.3381463289260864 0.0</Rock>\n"
                        + "          <Rock>1.188692569732666 6.4008002281188965 0.0</Rock>\n"
                        + "        </dark>\n"
                        + "        <light>\n"
                        + "          <Rock>0.7620000243186951 -2.4384000301361084 0.0</Rock>\n"
                        + "          <Rock>-0.356839120388031 4.683512210845947 0.0</Rock>\n"
                        + "          <Rock>0.08920978009700775 2.676292657852173 0.0</Rock>\n"
                        + "          <Rock>0.6690731644630432 3.657599925994873 0.0</Rock>\n"
                        + "          <Rock>0.44604888558387756 1.7395901679992676 0.0</Rock>\n"
                        + "          <Rock>0.44604888558387756 -0.8474927544593811 0.0</Rock>\n"
                        + "          <Rock>-0.1338145136833191 -1.6949855089187622 0.0</Rock>\n"
                        + "          <Rock>-0.5352586507797241 -0.4906536340713501 0.0</Rock>\n"
                        + "        </light>\n"
                        + "      </initialPos>\n"
                        + "      <initialSpeed>\n"
                        + "        <dark>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 -3.0 1.7453292519943295</Rock>\n"
                        + "        </dark>\n" + "        <light>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "        </light>\n" + "      </initialSpeed>\n"
                        + "    </CurveManager>\n" + "  </trajectories>\n"
                        + "</org.jcurl.container.2007>", x);
        final File f = new File("/tmp/hammy.jcz");
        f.deleteOnExit();
        xs.write(con, f);
        assertEquals(x, xs.write(xs.read(f.toURL(), null)));
    }

    public void testCurveStore() {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSpin());
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getCurler().computeV0(5), Math.PI / 2);
        te.setCurrentTime(20);

        final XStream xs = new XStream();
        xs.registerConverter(new XStreamSerializer.DimValConverter());
        xs.registerConverter(new XStreamSerializer.RockConverter());
        xs.alias("dimval", DimVal.class);
        xs.alias("rock", RockDouble.class);
        final String x = xs.toXML(te.getCurveStore());
        // System.out.println(x);
        assertEquals(
                "<org.jcurl.core.model.CurveStoreImpl>\n"
                        + "  <curve>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveTransformed\">\n"
                        + "            <base class=\"org.jcurl.math.PolynomeCurve\">\n"
                        + "              <params>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                  <double>1.3779956114540028</double>\n"
                        + "                  <double>-0.0535848758171096</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>3.141592653589793</double>\n"
                        + "                  <double>1.5707963267948966</double>\n"
                        + "                </double-array>\n"
                        + "              </params>\n"
                        + "            </base>\n"
                        + "            <t0>0.0</t0>\n"
                        + "            <trafo>\n"
                        + "              <double>-1.0</double>\n"
                        + "              <double>-0.0</double>\n"
                        + "              <double>0.0</double>\n"
                        + "              <double>-1.0</double>\n"
                        + "              <double>0.0</double>\n"
                        + "              <double>6.4008002281188965</double>\n"
                        + "            </trafo>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveTransformed\">\n"
                        + "            <base class=\"org.jcurl.math.PolynomeCurve\">\n"
                        + "              <params>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                  <double>0.3248903202923696</double>\n"
                        + "                  <double>-0.0535848758171096</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>3.7779198486645234</double>\n"
                        + "                  <double>1.5707963267948966</double>\n"
                        + "                </double-array>\n"
                        + "              </params>\n"
                        + "            </base>\n"
                        + "            <t0>3.617904278509095</t0>\n"
                        + "            <trafo>\n"
                        + "              <double>-0.3280839854465329</double>\n"
                        + "              <double>0.9446485581916266</double>\n"
                        + "              <double>-0.9446485581916266</double>\n"
                        + "              <double>-0.3280839854465329</double>\n"
                        + "              <double>0.0</double>\n"
                        + "              <double>2.116728847092748</double>\n"
                        + "            </trafo>\n"
                        + "          </curve>\n"
                        + "          <t0>3.617904278509095</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>10.444936298643487</a>\n"
                        + "            <x>-0.46520201874501665</x>\n"
                        + "            <y>1.9551604722467486</y>\n"
                        + "          </curve>\n"
                        + "          <t0>6.6494529688363055</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.7853981633974483</a>\n"
                        + "            <x>0.1</x>\n"
                        + "            <y>1.8287999629974365</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveTransformed\">\n"
                        + "            <base class=\"org.jcurl.math.PolynomeCurve\">\n"
                        + "              <params>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                  <double>0.9354530737515032</double>\n"
                        + "                  <double>-0.0535848758171096</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>3.5927162410690348</double>\n"
                        + "                  <double>0.0</double>\n"
                        + "                </double-array>\n"
                        + "              </params>\n"
                        + "            </base>\n"
                        + "            <t0>3.617904278509095</t0>\n"
                        + "            <trafo>\n"
                        + "              <double>-0.9446485581916267</double>\n"
                        + "              <double>-0.32808398544653294</double>\n"
                        + "              <double>0.32808398544653294</double>\n"
                        + "              <double>-0.9446485581916267</double>\n"
                        + "              <double>0.1</double>\n"
                        + "              <double>1.8287999629974365</double>\n"
                        + "            </trafo>\n"
                        + "          </curve>\n"
                        + "          <t0>3.617904278509095</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.7853981633974483</a>\n"
                        + "            <x>1.4394509813613883</x>\n"
                        + "            <y>-2.0278663006401088</y>\n"
                        + "          </curve>\n"
                        + "          <t0>12.346608594884449</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>-2.2098000049591064</x>\n"
                        + "            <y>9.083040237426758</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>2.2098000049591064</x>\n"
                        + "            <y>9.083040237426758</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>-2.2098000049591064</x>\n"
                        + "            <y>8.717280387878418</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>2.2098000049591064</x>\n"
                        + "            <y>8.717280387878418</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>-2.2098000049591064</x>\n"
                        + "            <y>8.351519584655762</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>2.2098000049591064</x>\n"
                        + "            <y>8.351519584655762</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>-2.2098000049591064</x>\n"
                        + "            <y>7.98576021194458</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>2.2098000049591064</x>\n"
                        + "            <y>7.98576021194458</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>-2.2098000049591064</x>\n"
                        + "            <y>7.619999885559082</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>2.2098000049591064</x>\n"
                        + "            <y>7.619999885559082</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>-2.2098000049591064</x>\n"
                        + "            <y>7.254240036010742</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>2.2098000049591064</x>\n"
                        + "            <y>7.254240036010742</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>-2.2098000049591064</x>\n"
                        + "            <y>6.888480186462402</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveStill\">\n"
                        + "            <a>0.0</a>\n"
                        + "            <x>2.2098000049591064</x>\n"
                        + "            <y>6.888480186462402</y>\n"
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "  </curve>\n"
                        + "  <stopper class=\"org.jcurl.core.model.NewtonStopDetector\"/>\n"
                        + "</org.jcurl.core.model.CurveStoreImpl>", x);
    }

    public void testHammy() {
        final CurveManager te;
        {
            final JCurlSerializer xs = new XStreamSerializer();
            final String x = xs.write(xs.wrap(null, initHammy(null)));
            // System.out.println(x);
            assertEquals(
                    "<org.jcurl.container.2007>\n"
                            + "  <annotations/>\n"
                            + "  <trajectories>\n"
                            + "    <CurveManager>\n"
                            + "      <annotations>\n"
                            + "        <entry>\n"
                            + "          <string>org.jcurl.core.Light.Team</string>\n"
                            + "          <string>Canada</string>\n"
                            + "        </entry>\n"
                            + "        <entry>\n"
                            + "          <string>org.jcurl.core.Game</string>\n"
                            + "          <string>Semifinal</string>\n"
                            + "        </entry>\n"
                            + "        <entry>\n"
                            + "          <string>org.jcurl.core.Hammer</string>\n"
                            + "          <string>Dark</string>\n"
                            + "        </entry>\n"
                            + "        <entry>\n"
                            + "          <string>org.jcurl.core.Location</string>\n"
                            + "          <string>Garmisch</string>\n"
                            + "        </entry>\n"
                            + "        <entry>\n"
                            + "          <string>org.jcurl.core.Dark.Team</string>\n"
                            + "          <string>Scotland</string>\n"
                            + "        </entry>\n"
                            + "        <entry>\n"
                            + "          <string>org.jcurl.core.Event</string>\n"
                            + "          <string>World Curling Championships</string>\n"
                            + "        </entry>\n"
                            + "        <entry>\n"
                            + "          <string>org.jcurl.core.Date</string>\n"
                            + "          <string>1992</string>\n"
                            + "        </entry>\n"
                            + "      </annotations>\n"
                            + "      <collider class=\"CollissionSpin\">\n"
                            + "        <params>\n"
                            + "          <entry>\n"
                            + "            <string>loss</string>\n"
                            + "            <DimVal>0.0 J</DimVal>\n"
                            + "          </entry>\n"
                            + "          <entry>\n"
                            + "            <string>frictionRockRock</string>\n"
                            + "            <DimVal>0.5 </DimVal>\n"
                            + "          </entry>\n"
                            + "        </params>\n"
                            + "      </collider>\n"
                            + "      <collissionDetector class=\"NewtonCollissionDetector\"/>\n"
                            + "      <curler class=\"NoCurlCurler\">\n"
                            + "        <params>\n"
                            + "          <entry>\n"
                            + "            <string>drawToTeeTime</string>\n"
                            + "            <DimVal>24.0 s</DimVal>\n"
                            + "          </entry>\n"
                            + "          <entry>\n"
                            + "            <string>drawToTeeCurl</string>\n"
                            + "            <DimVal>0.0 m</DimVal>\n"
                            + "          </entry>\n"
                            + "        </params>\n"
                            + "      </curler>\n"
                            + "      <initialPos>\n"
                            + "        <dark>\n"
                            + "          <Rock>-0.7620000243186951 -2.4384000301361084 0.0</Rock>\n"
                            + "          <Rock>-0.9448800086975098 -2.8041601181030273 0.0</Rock>\n"
                            + "          <Rock>0.26762932538986206 4.371277809143066 0.0</Rock>\n"
                            + "          <Rock>-0.8028876781463623 4.014439105987549 0.0</Rock>\n"
                            + "          <Rock>1.3827511072158813 -0.1338145136833191 0.0</Rock>\n"
                            + "          <Rock>0.22302429378032684 -1.204331874847412 0.0</Rock>\n"
                            + "          <Rock>-0.8474927544593811 -1.3381463289260864 0.0</Rock>\n"
                            + "          <Rock>1.188692569732666 6.4008002281188965 0.0</Rock>\n"
                            + "        </dark>\n"
                            + "        <light>\n"
                            + "          <Rock>0.7620000243186951 -2.4384000301361084 0.0</Rock>\n"
                            + "          <Rock>-0.356839120388031 4.683512210845947 0.0</Rock>\n"
                            + "          <Rock>0.08920978009700775 2.676292657852173 0.0</Rock>\n"
                            + "          <Rock>0.6690731644630432 3.657599925994873 0.0</Rock>\n"
                            + "          <Rock>0.44604888558387756 1.7395901679992676 0.0</Rock>\n"
                            + "          <Rock>0.44604888558387756 -0.8474927544593811 0.0</Rock>\n"
                            + "          <Rock>-0.1338145136833191 -1.6949855089187622 0.0</Rock>\n"
                            + "          <Rock>-0.5352586507797241 -0.4906536340713501 0.0</Rock>\n"
                            + "        </light>\n"
                            + "      </initialPos>\n"
                            + "      <initialSpeed>\n"
                            + "        <dark>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 -3.0 1.7453292519943295</Rock>\n"
                            + "        </dark>\n" + "        <light>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "          <Rock>0.0 0.0 0.0</Rock>\n"
                            + "        </light>\n" + "      </initialSpeed>\n"
                            + "    </CurveManager>\n" + "  </trajectories>\n"
                            + "</org.jcurl.container.2007>", x);
            te = (CurveManager) xs.read(x).getTrajectories()[0];
        }
        te.setCurrentTime(10);
        // with Display:
        showPositionDisplay(te.getCurrentPos(), FixpointZoomer.HOUSE, 7500,
                new TimeRunnable() {
                    @Override
                    public void run(final double t) throws InterruptedException {
                        te.setCurrentTime(t);
                        Thread.sleep(1000 / 50);
                    }
                });
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

    public void testRaw() {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSpin(0.5, 0));
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getCurler().computeV0(5), Math.PI / 2);

        final XStream xs = new XStream();
        xs.registerConverter(new XStreamSerializer.DimValConverter());
        xs.registerConverter(new XStreamSerializer.RockConverter());
        xs.alias("dimval", DimVal.class);
        xs.alias("rock", RockDouble.class);
        final String x = xs.toXML(te);
        // System.out.println(x);
        assertEquals(
                "<org.jcurl.core.model.CurveManager>\n"
                        + "  <annotations/>\n"
                        + "  <collider class=\"org.jcurl.core.model.CollissionSpin\">\n"
                        + "    <params>\n"
                        + "      <entry>\n"
                        + "        <string>loss</string>\n"
                        + "        <dimval>0.0 J</dimval>\n"
                        + "      </entry>\n"
                        + "      <entry>\n"
                        + "        <string>frictionRockRock</string>\n"
                        + "        <dimval>0.5 </dimval>\n"
                        + "      </entry>\n"
                        + "    </params>\n"
                        + "  </collider>\n"
                        + "  <collissionDetector class=\"org.jcurl.core.model.NewtonCollissionDetector\"/>\n"
                        + "  <curler class=\"org.jcurl.core.model.CurlerNoCurl\">\n"
                        + "    <params>\n"
                        + "      <entry>\n"
                        + "        <string>drawToTeeTime</string>\n"
                        + "        <dimval>23.0 s</dimval>\n"
                        + "      </entry>\n"
                        + "      <entry>\n"
                        + "        <string>drawToTeeCurl</string>\n"
                        + "        <dimval>0.0 m</dimval>\n"
                        + "      </entry>\n"
                        + "    </params>\n"
                        + "  </curler>\n"
                        + "  <initialPos>\n"
                        + "    <dark>\n"
                        + "      <rock>0.0 6.4008002281188965 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064 9.083040237426758 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064 8.717280387878418 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064 8.351519584655762 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064 7.98576021194458 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064 7.619999885559082 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064 7.254240036010742 0.0</rock>\n"
                        + "      <rock>-2.2098000049591064 6.888480186462402 0.0</rock>\n"
                        + "    </dark>\n"
                        + "    <light>\n"
                        + "      <rock>0.1 1.8287999629974365 0.7853981633974483</rock>\n"
                        + "      <rock>2.2098000049591064 9.083040237426758 0.0</rock>\n"
                        + "      <rock>2.2098000049591064 8.717280387878418 0.0</rock>\n"
                        + "      <rock>2.2098000049591064 8.351519584655762 0.0</rock>\n"
                        + "      <rock>2.2098000049591064 7.98576021194458 0.0</rock>\n"
                        + "      <rock>2.2098000049591064 7.619999885559082 0.0</rock>\n"
                        + "      <rock>2.2098000049591064 7.254240036010742 0.0</rock>\n"
                        + "      <rock>2.2098000049591064 6.888480186462402 0.0</rock>\n"
                        + "    </light>\n"
                        + "  </initialPos>\n"
                        + "  <initialSpeed>\n"
                        + "    <dark>\n"
                        + "      <rock>0.0 -1.3779956114540028 1.5707963267948966</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n" + "    </dark>\n"
                        + "    <light>\n" + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n"
                        + "      <rock>0.0 0.0 0.0</rock>\n" + "    </light>\n"
                        + "  </initialSpeed>\n"
                        + "</org.jcurl.core.model.CurveManager>", x);

        final CurveManager o = (CurveManager) xs.fromXML(x);
        assertNotNull(o);
        assertNotNull(o.getCollider());
        assertNotNull(o.getCollissionDetector());
        assertNotNull(o.getCurrentPos());
        assertNotNull(o.getCurrentSpeed());
        assertNotNull(o.getCurveStore());
        assertNotNull(o.getInitialPos());
        assertNotNull(o.getInitialSpeed());
        assertNotNull(o.getCurler());
    }

    public void testStoredTrajectorySet() throws IOException {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSpin());
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getCurler().computeV0(5), Math.PI / 2);
        te.setCurrentTime(20);

        final JCurlSerializer xs = new XStreamSerializer();
        final Payload con = xs.wrap(null, new StoredTrajectorySet(te
                .getCurveStore()));
        final String x = xs.write(con);
        // System.out.println(x);
        assertEquals(
                "<org.jcurl.container.2007>\n"
                        + "  <annotations/>\n"
                        + "  <trajectories>\n"
                        + "    <StoredTrajectory>\n"
                        + "      <annotations/>\n"
                        + "      <store class=\"CurveStore\">\n"
                        + "        <curve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"TransformedCurve\">\n"
                        + "                  <base class=\"PolynomeCurve\">\n"
                        + "                    <params>\n"
                        + "                      <double-array>0.0</double-array>\n"
                        + "                      <double-array>0.0 1.3779956114540028 -0.0535848758171096</double-array>\n"
                        + "                      <double-array>3.141592653589793 1.5707963267948966</double-array>\n"
                        + "                    </params>\n"
                        + "                  </base>\n"
                        + "                  <t0>0.0</t0>\n"
                        + "                  <trafo>-1.0 -0.0 0.0 -1.0 0.0 6.4008002281188965</trafo>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"TransformedCurve\">\n"
                        + "                  <base class=\"PolynomeCurve\">\n"
                        + "                    <params>\n"
                        + "                      <double-array>0.0</double-array>\n"
                        + "                      <double-array>0.0 0.3248903202923696 -0.0535848758171096</double-array>\n"
                        + "                      <double-array>3.7779198486645234 1.5707963267948966</double-array>\n"
                        + "                    </params>\n"
                        + "                  </base>\n"
                        + "                  <t0>3.617904278509095</t0>\n"
                        + "                  <trafo>-0.3280839854465329 0.9446485581916266 -0.9446485581916266 -0.3280839854465329 0.0 2.116728847092748</trafo>\n"
                        + "                </curve>\n"
                        + "                <t0>3.617904278509095</t0>\n"
                        + "              </CurvePart>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>10.444936298643487</a>\n"
                        + "                  <x>-0.46520201874501665</x>\n"
                        + "                  <y>1.9551604722467486</y>\n"
                        + "                </curve>\n"
                        + "                <t0>6.6494529688363055</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.7853981633974483</a>\n"
                        + "                  <x>0.1</x>\n"
                        + "                  <y>1.8287999629974365</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"TransformedCurve\">\n"
                        + "                  <base class=\"PolynomeCurve\">\n"
                        + "                    <params>\n"
                        + "                      <double-array>0.0</double-array>\n"
                        + "                      <double-array>0.0 0.9354530737515032 -0.0535848758171096</double-array>\n"
                        + "                      <double-array>3.5927162410690348 0.0</double-array>\n"
                        + "                    </params>\n"
                        + "                  </base>\n"
                        + "                  <t0>3.617904278509095</t0>\n"
                        + "                  <trafo>-0.9446485581916267 -0.32808398544653294 0.32808398544653294 -0.9446485581916267 0.1 1.8287999629974365</trafo>\n"
                        + "                </curve>\n"
                        + "                <t0>3.617904278509095</t0>\n"
                        + "              </CurvePart>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.7853981633974483</a>\n"
                        + "                  <x>1.4394509813613883</x>\n"
                        + "                  <y>-2.0278663006401088</y>\n"
                        + "                </curve>\n"
                        + "                <t0>12.346608594884449</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>-2.2098000049591064</x>\n"
                        + "                  <y>9.083040237426758</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>2.2098000049591064</x>\n"
                        + "                  <y>9.083040237426758</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>-2.2098000049591064</x>\n"
                        + "                  <y>8.717280387878418</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>2.2098000049591064</x>\n"
                        + "                  <y>8.717280387878418</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>-2.2098000049591064</x>\n"
                        + "                  <y>8.351519584655762</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>2.2098000049591064</x>\n"
                        + "                  <y>8.351519584655762</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>-2.2098000049591064</x>\n"
                        + "                  <y>7.98576021194458</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>2.2098000049591064</x>\n"
                        + "                  <y>7.98576021194458</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>-2.2098000049591064</x>\n"
                        + "                  <y>7.619999885559082</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>2.2098000049591064</x>\n"
                        + "                  <y>7.619999885559082</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>-2.2098000049591064</x>\n"
                        + "                  <y>7.254240036010742</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>2.2098000049591064</x>\n"
                        + "                  <y>7.254240036010742</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>-2.2098000049591064</x>\n"
                        + "                  <y>6.888480186462402</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n"
                        + "          <CombinedCurve>\n"
                        + "            <parts>\n"
                        + "              <CurvePart>\n"
                        + "                <curve class=\"PointCurve\">\n"
                        + "                  <a>0.0</a>\n"
                        + "                  <x>2.2098000049591064</x>\n"
                        + "                  <y>6.888480186462402</y>\n"
                        + "                </curve>\n"
                        + "                <t0>0.0</t0>\n"
                        + "              </CurvePart>\n"
                        + "            </parts>\n"
                        + "          </CombinedCurve>\n" + "        </curve>\n"
                        + "        <stopper class=\"NewtonStopDetector\"/>\n"
                        + "      </store>\n" + "    </StoredTrajectory>\n"
                        + "  </trajectories>\n" + "</org.jcurl.container.2007>",
                x);
        final TrajectorySet b = (TrajectorySet) xs.read(x).getTrajectories()[0];
        b.setCurrentTime(11);
        assertEquals(x, xs.write(xs.wrap(null, b)));
        final File f = new File("/tmp/hammy.curvestore.jcz");
        f.deleteOnExit();
        xs.write(con, f);
        assertEquals(x, xs.write(xs.read(f.toURL(), null)));

        final File f2 = new File("/tmp/hammy.game.jcz");
        f2.deleteOnExit();
        final TrajectorySet[] tmp = new TrajectorySet[160];
        for (int i = tmp.length - 1; i >= 0; i--)
            tmp[i] = con.getTrajectories()[0];
        Payload c2 = xs.wrap(null, tmp);
        xs.write(c2, f2);
    }

    public void testXSIO() {
        final CurveManager te = new CurveManager();
        te.setCollider(new CollissionSpin(0.5, 0));
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerNoCurl(23, 0));
        te.setInitialPos(PositionSet.allHome());
        te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
        te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
                0.25 * Math.PI);
        te.setInitialSpeed(new SpeedSet());
        te.getInitialSpeed().getDark(0).setLocation(0,
                -te.getCurler().computeV0(5), Math.PI / 2);

        final JCurlSerializer xs = new XStreamSerializer();
        final String x = xs.write(xs.wrap(null, te));
        // System.out.println(x);
        assertEquals(
                "<org.jcurl.container.2007>\n"
                        + "  <annotations/>\n"
                        + "  <trajectories>\n"
                        + "    <CurveManager>\n"
                        + "      <annotations/>\n"
                        + "      <collider class=\"CollissionSpin\">\n"
                        + "        <params>\n"
                        + "          <entry>\n"
                        + "            <string>loss</string>\n"
                        + "            <DimVal>0.0 J</DimVal>\n"
                        + "          </entry>\n"
                        + "          <entry>\n"
                        + "            <string>frictionRockRock</string>\n"
                        + "            <DimVal>0.5 </DimVal>\n"
                        + "          </entry>\n"
                        + "        </params>\n"
                        + "      </collider>\n"
                        + "      <collissionDetector class=\"NewtonCollissionDetector\"/>\n"
                        + "      <curler class=\"NoCurlCurler\">\n"
                        + "        <params>\n"
                        + "          <entry>\n"
                        + "            <string>drawToTeeTime</string>\n"
                        + "            <DimVal>23.0 s</DimVal>\n"
                        + "          </entry>\n"
                        + "          <entry>\n"
                        + "            <string>drawToTeeCurl</string>\n"
                        + "            <DimVal>0.0 m</DimVal>\n"
                        + "          </entry>\n"
                        + "        </params>\n"
                        + "      </curler>\n"
                        + "      <initialPos>\n"
                        + "        <dark>\n"
                        + "          <Rock>0.0 6.4008002281188965 0.0</Rock>\n"
                        + "          <Rock>-2.2098000049591064 9.083040237426758 0.0</Rock>\n"
                        + "          <Rock>-2.2098000049591064 8.717280387878418 0.0</Rock>\n"
                        + "          <Rock>-2.2098000049591064 8.351519584655762 0.0</Rock>\n"
                        + "          <Rock>-2.2098000049591064 7.98576021194458 0.0</Rock>\n"
                        + "          <Rock>-2.2098000049591064 7.619999885559082 0.0</Rock>\n"
                        + "          <Rock>-2.2098000049591064 7.254240036010742 0.0</Rock>\n"
                        + "          <Rock>-2.2098000049591064 6.888480186462402 0.0</Rock>\n"
                        + "        </dark>\n"
                        + "        <light>\n"
                        + "          <Rock>0.1 1.8287999629974365 0.7853981633974483</Rock>\n"
                        + "          <Rock>2.2098000049591064 9.083040237426758 0.0</Rock>\n"
                        + "          <Rock>2.2098000049591064 8.717280387878418 0.0</Rock>\n"
                        + "          <Rock>2.2098000049591064 8.351519584655762 0.0</Rock>\n"
                        + "          <Rock>2.2098000049591064 7.98576021194458 0.0</Rock>\n"
                        + "          <Rock>2.2098000049591064 7.619999885559082 0.0</Rock>\n"
                        + "          <Rock>2.2098000049591064 7.254240036010742 0.0</Rock>\n"
                        + "          <Rock>2.2098000049591064 6.888480186462402 0.0</Rock>\n"
                        + "        </light>\n"
                        + "      </initialPos>\n"
                        + "      <initialSpeed>\n"
                        + "        <dark>\n"
                        + "          <Rock>0.0 -1.3779956114540028 1.5707963267948966</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "        </dark>\n" + "        <light>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "          <Rock>0.0 0.0 0.0</Rock>\n"
                        + "        </light>\n" + "      </initialSpeed>\n"
                        + "    </CurveManager>\n" + "  </trajectories>\n"
                        + "</org.jcurl.container.2007>", x);

        final CurveManager o = (CurveManager) xs.read(x).getTrajectories()[0];
        assertNotNull(o);
        assertNotNull(o.getCollider());
        assertNotNull(o.getCollissionDetector());
        assertNotNull(o.getCurrentPos());
        assertNotNull(o.getCurrentSpeed());
        assertNotNull(o.getCurveStore());
        assertNotNull(o.getInitialPos());
        assertNotNull(o.getInitialSpeed());
        assertNotNull(o.getCurler());
    }
}