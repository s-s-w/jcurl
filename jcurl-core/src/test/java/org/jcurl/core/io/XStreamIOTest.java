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

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.JCurlIO;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.StoredTrajectorySet;
import org.jcurl.core.base.TestShowBase;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerNoCurl;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.CurveManagerTest;
import org.jcurl.core.model.FixpointZoomer;
import org.jcurl.core.model.NewtonCollissionDetector;

import com.thoughtworks.xstream.XStream;

public class XStreamIOTest extends TestShowBase {

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
        xs.registerConverter(new XStreamIO.DimValConverter());
        xs.registerConverter(new XStreamIO.RockConverter());
        xs.alias("dimval", DimVal.class);
        xs.alias("rock", RockDouble.class);
        String x = xs.toXML(te.getCurveStore());
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
                        + "                  <double>0.0</double>\n"
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
                        + "                  <double>2.541398097787834</double>\n"
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
                        + "            <a>9.208414547766798</a>\n"
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
                        + "                  <double>0.7853981633974483</double>\n"
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
                        + "            <a>-2.021919914274138</a>\n"
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
            final JCurlIO xs = new XStreamIO();
            final String x = xs.write(CurveManagerTest.initHammy(null));
            // System.out.println(x);
            assertEquals(
                    "<CurveManager>\n"
                            + "  <annotations>\n"
                            + "    <entry>\n"
                            + "      <string>org.jcurl.core.Game</string>\n"
                            + "      <string>Semifinal</string>\n"
                            + "    </entry>\n"
                            + "    <entry>\n"
                            + "      <string>org.jcurl.core.Team.Dark</string>\n"
                            + "      <string>Scotland</string>\n"
                            + "    </entry>\n"
                            + "    <entry>\n"
                            + "      <string>org.jcurl.core.Hammer</string>\n"
                            + "      <string>Dark</string>\n"
                            + "    </entry>\n"
                            + "    <entry>\n"
                            + "      <string>org.jcurl.core.Location</string>\n"
                            + "      <string>Garmisch</string>\n"
                            + "    </entry>\n"
                            + "    <entry>\n"
                            + "      <string>org.jcurl.core.Team.Light</string>\n"
                            + "      <string>Canada</string>\n"
                            + "    </entry>\n"
                            + "    <entry>\n"
                            + "      <string>org.jcurl.core.Event</string>\n"
                            + "      <string>World Curling Championships</string>\n"
                            + "    </entry>\n"
                            + "    <entry>\n"
                            + "      <string>org.jcurl.core.Date</string>\n"
                            + "      <string>1992</string>\n"
                            + "    </entry>\n"
                            + "  </annotations>\n"
                            + "  <collider class=\"CollissionSpin\">\n"
                            + "    <params>\n"
                            + "      <entry>\n"
                            + "        <string>loss</string>\n"
                            + "        <DimVal>0.0 J</DimVal>\n"
                            + "      </entry>\n"
                            + "      <entry>\n"
                            + "        <string>frictionRockRock</string>\n"
                            + "        <DimVal>0.5 </DimVal>\n"
                            + "      </entry>\n"
                            + "    </params>\n"
                            + "  </collider>\n"
                            + "  <collissionDetector class=\"NewtonCollissionDetector\"/>\n"
                            + "  <curler class=\"NoCurlCurler\">\n"
                            + "    <params>\n"
                            + "      <entry>\n"
                            + "        <string>drawToTeeTime</string>\n"
                            + "        <DimVal>24.0 s</DimVal>\n"
                            + "      </entry>\n"
                            + "      <entry>\n"
                            + "        <string>drawToTeeCurl</string>\n"
                            + "        <DimVal>0.0 m</DimVal>\n"
                            + "      </entry>\n"
                            + "    </params>\n"
                            + "  </curler>\n"
                            + "  <initialPos>\n"
                            + "    <dark>\n"
                            + "      <Rock>-0.7620000243186951 -2.4384000301361084 0.0</Rock>\n"
                            + "      <Rock>-0.9448800086975098 -2.8041601181030273 0.0</Rock>\n"
                            + "      <Rock>0.26762932538986206 4.371277809143066 0.0</Rock>\n"
                            + "      <Rock>-0.8028876781463623 4.014439105987549 0.0</Rock>\n"
                            + "      <Rock>1.3827511072158813 -0.1338145136833191 0.0</Rock>\n"
                            + "      <Rock>0.22302429378032684 -1.204331874847412 0.0</Rock>\n"
                            + "      <Rock>-0.8474927544593811 -1.3381463289260864 0.0</Rock>\n"
                            + "      <Rock>1.188692569732666 6.4008002281188965 0.0</Rock>\n"
                            + "    </dark>\n"
                            + "    <light>\n"
                            + "      <Rock>0.7620000243186951 -2.4384000301361084 0.0</Rock>\n"
                            + "      <Rock>-0.356839120388031 4.683512210845947 0.0</Rock>\n"
                            + "      <Rock>0.08920978009700775 2.676292657852173 0.0</Rock>\n"
                            + "      <Rock>0.6690731644630432 3.657599925994873 0.0</Rock>\n"
                            + "      <Rock>0.44604888558387756 1.7395901679992676 0.0</Rock>\n"
                            + "      <Rock>0.44604888558387756 -0.8474927544593811 0.0</Rock>\n"
                            + "      <Rock>-0.1338145136833191 -1.6949855089187622 0.0</Rock>\n"
                            + "      <Rock>-0.5352586507797241 -0.4906536340713501 0.0</Rock>\n"
                            + "    </light>\n"
                            + "  </initialPos>\n"
                            + "  <initialSpeed>\n"
                            + "    <dark>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 -3.0 1.7453292519943295</Rock>\n"
                            + "    </dark>\n" + "    <light>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "      <Rock>0.0 0.0 0.0</Rock>\n"
                            + "    </light>\n" + "  </initialSpeed>\n"
                            + "</CurveManager>", x);
            te = (CurveManager) xs.read(x);
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
        xs.registerConverter(new XStreamIO.DimValConverter());
        xs.registerConverter(new XStreamIO.RockConverter());
        xs.alias("dimval", DimVal.class);
        xs.alias("rock", RockDouble.class);
        String x = xs.toXML(te);
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

        CurveManager o = (CurveManager) xs.fromXML(x);
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

    public void testStoredTrajectorySet() {
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

        final TrajectorySet st = new StoredTrajectorySet(te.getCurveStore());
        final JCurlIO xs = new XStreamIO();

        String x = xs.write(st);
        // System.out.println(x);
        assertEquals(
                "<StoredTrajectory>\n"
                        + "  <annotations/>\n"
                        + "  <store class=\"CurveStoreImpl\">\n"
                        + "    <curve>\n"
                        + "      <CombinedCurve>\n"
                        + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"TransformedCurve\">\n"
                        + "              <base class=\"PolynomeCurve\">\n"
                        + "                <params>\n"
                        + "                  <double-array>0.0</double-array>\n"
                        + "                  <double-array>0.0 1.3779956114540028 -0.0535848758171096</double-array>\n"
                        + "                  <double-array>0.0 1.5707963267948966</double-array>\n"
                        + "                </params>\n"
                        + "              </base>\n"
                        + "              <t0>0.0</t0>\n"
                        + "              <trafo>-1.0 -0.0 0.0 -1.0 0.0 6.4008002281188965</trafo>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"TransformedCurve\">\n"
                        + "              <base class=\"PolynomeCurve\">\n"
                        + "                <params>\n"
                        + "                  <double-array>0.0</double-array>\n"
                        + "                  <double-array>0.0 0.3248903202923696 -0.0535848758171096</double-array>\n"
                        + "                  <double-array>2.541398097787834 1.5707963267948966</double-array>\n"
                        + "                </params>\n"
                        + "              </base>\n"
                        + "              <t0>3.617904278509095</t0>\n"
                        + "              <trafo>-0.3280839854465329 0.9446485581916266 -0.9446485581916266 -0.3280839854465329 0.0 2.116728847092748</trafo>\n"
                        + "            </curve>\n"
                        + "            <t0>3.617904278509095</t0>\n"
                        + "          </CurvePart>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>9.208414547766798</a>\n"
                        + "              <x>-0.46520201874501665</x>\n"
                        + "              <y>1.9551604722467486</y>\n"
                        + "            </curve>\n"
                        + "            <t0>6.6494529688363055</t0>\n"
                        + "          </CurvePart>\n"
                        + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n"
                        + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.7853981633974483</a>\n"
                        + "              <x>0.1</x>\n"
                        + "              <y>1.8287999629974365</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"TransformedCurve\">\n"
                        + "              <base class=\"PolynomeCurve\">\n"
                        + "                <params>\n"
                        + "                  <double-array>0.0</double-array>\n"
                        + "                  <double-array>0.0 0.9354530737515032 -0.0535848758171096</double-array>\n"
                        + "                  <double-array>0.7853981633974483 0.0</double-array>\n"
                        + "                </params>\n"
                        + "              </base>\n"
                        + "              <t0>3.617904278509095</t0>\n"
                        + "              <trafo>-0.9446485581916267 -0.32808398544653294 0.32808398544653294 -0.9446485581916267 0.1 1.8287999629974365</trafo>\n"
                        + "            </curve>\n"
                        + "            <t0>3.617904278509095</t0>\n"
                        + "          </CurvePart>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>-2.021919914274138</a>\n"
                        + "              <x>1.4394509813613883</x>\n"
                        + "              <y>-2.0278663006401088</y>\n"
                        + "            </curve>\n"
                        + "            <t0>12.346608594884449</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>-2.2098000049591064</x>\n"
                        + "              <y>9.083040237426758</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>2.2098000049591064</x>\n"
                        + "              <y>9.083040237426758</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>-2.2098000049591064</x>\n"
                        + "              <y>8.717280387878418</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>2.2098000049591064</x>\n"
                        + "              <y>8.717280387878418</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>-2.2098000049591064</x>\n"
                        + "              <y>8.351519584655762</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>2.2098000049591064</x>\n"
                        + "              <y>8.351519584655762</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>-2.2098000049591064</x>\n"
                        + "              <y>7.98576021194458</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>2.2098000049591064</x>\n"
                        + "              <y>7.98576021194458</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>-2.2098000049591064</x>\n"
                        + "              <y>7.619999885559082</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>2.2098000049591064</x>\n"
                        + "              <y>7.619999885559082</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>-2.2098000049591064</x>\n"
                        + "              <y>7.254240036010742</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>2.2098000049591064</x>\n"
                        + "              <y>7.254240036010742</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>-2.2098000049591064</x>\n"
                        + "              <y>6.888480186462402</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n"
                        + "      <CombinedCurve>\n" + "        <parts>\n"
                        + "          <CurvePart>\n"
                        + "            <curve class=\"PointCurve\">\n"
                        + "              <a>0.0</a>\n"
                        + "              <x>2.2098000049591064</x>\n"
                        + "              <y>6.888480186462402</y>\n"
                        + "            </curve>\n"
                        + "            <t0>0.0</t0>\n"
                        + "          </CurvePart>\n" + "        </parts>\n"
                        + "      </CombinedCurve>\n" + "    </curve>\n"
                        + "    <stopper class=\"NewtonStopDetector\"/>\n"
                        + "  </store>\n" + "</StoredTrajectory>", x);
        TrajectorySet b = (TrajectorySet) xs.read(x);
        b.setCurrentTime(11);
        assertEquals(x, xs.write(b));
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

        final JCurlIO xs = new XStreamIO();
        String x = xs.write(te);
        // System.out.println(x);
        assertEquals(
                "<CurveManager>\n"
                        + "  <annotations/>\n"
                        + "  <collider class=\"CollissionSpin\">\n"
                        + "    <params>\n"
                        + "      <entry>\n"
                        + "        <string>loss</string>\n"
                        + "        <DimVal>0.0 J</DimVal>\n"
                        + "      </entry>\n"
                        + "      <entry>\n"
                        + "        <string>frictionRockRock</string>\n"
                        + "        <DimVal>0.5 </DimVal>\n"
                        + "      </entry>\n"
                        + "    </params>\n"
                        + "  </collider>\n"
                        + "  <collissionDetector class=\"NewtonCollissionDetector\"/>\n"
                        + "  <curler class=\"NoCurlCurler\">\n"
                        + "    <params>\n"
                        + "      <entry>\n"
                        + "        <string>drawToTeeTime</string>\n"
                        + "        <DimVal>23.0 s</DimVal>\n"
                        + "      </entry>\n"
                        + "      <entry>\n"
                        + "        <string>drawToTeeCurl</string>\n"
                        + "        <DimVal>0.0 m</DimVal>\n"
                        + "      </entry>\n"
                        + "    </params>\n"
                        + "  </curler>\n"
                        + "  <initialPos>\n"
                        + "    <dark>\n"
                        + "      <Rock>0.0 6.4008002281188965 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064 9.083040237426758 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064 8.717280387878418 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064 8.351519584655762 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064 7.98576021194458 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064 7.619999885559082 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064 7.254240036010742 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064 6.888480186462402 0.0</Rock>\n"
                        + "    </dark>\n"
                        + "    <light>\n"
                        + "      <Rock>0.1 1.8287999629974365 0.7853981633974483</Rock>\n"
                        + "      <Rock>2.2098000049591064 9.083040237426758 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064 8.717280387878418 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064 8.351519584655762 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064 7.98576021194458 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064 7.619999885559082 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064 7.254240036010742 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064 6.888480186462402 0.0</Rock>\n"
                        + "    </light>\n"
                        + "  </initialPos>\n"
                        + "  <initialSpeed>\n"
                        + "    <dark>\n"
                        + "      <Rock>0.0 -1.3779956114540028 1.5707963267948966</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n" + "    </dark>\n"
                        + "    <light>\n" + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n"
                        + "      <Rock>0.0 0.0 0.0</Rock>\n" + "    </light>\n"
                        + "  </initialSpeed>\n" + "</CurveManager>", x);

        CurveManager o = (CurveManager) xs.read(x);
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