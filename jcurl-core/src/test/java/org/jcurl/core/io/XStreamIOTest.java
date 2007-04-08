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

import org.jcurl.core.base.CurveStill;
import org.jcurl.core.base.CurveTransformed;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.JCurlIO;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.StoredTrajectorySet;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.core.model.CurlerNoCurl;
import org.jcurl.math.CurveCombined;
import org.jcurl.math.CurvePart;
import org.jcurl.math.PolynomeCurve;

import com.thoughtworks.xstream.XStream;

public class XStreamIOTest extends TestCase {

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
        assertEquals("<StoredTrajectory>\n" + "  <store>\n" + "    <curve>\n"
                + "      <CombinedCurve>\n" + "        <parts>\n"
                + "          <CurvePart>\n"
                + "            <curve class=\"TransformedCurve\">\n"
                + "              <trafo>\n"
                + "                <double>-1.0</double>\n"
                + "                <double>-0.0</double>\n"
                + "                <double>0.0</double>\n"
                + "                <double>-1.0</double>\n"
                + "                <double>0.0</double>\n"
                + "                <double>6.4008002281188965</double>\n"
                + "              </trafo>\n"
                + "              <base class=\"PolynomeCurve\">\n"
                + "                <params>\n"
                + "                  <double-array>\n"
                + "                    <double>0.0</double>\n"
                + "                  </double-array>\n"
                + "                  <double-array>\n"
                + "                    <double>0.0</double>\n"
                + "                    <double>1.3779956114540028</double>\n"
                + "                    <double>-0.0535848758171096</double>\n"
                + "                  </double-array>\n"
                + "                  <double-array>\n"
                + "                    <double>0.0</double>\n"
                + "                    <double>1.5707963267948966</double>\n"
                + "                  </double-array>\n"
                + "                </params>\n" + "              </base>\n"
                + "              <t0>0.0</t0>\n" + "            </curve>\n"
                + "            <t0>0.0</t0>\n" + "          </CurvePart>\n"
                + "          <CurvePart>\n"
                + "            <curve class=\"TransformedCurve\">\n"
                + "              <trafo>\n"
                + "                <double>-0.3280839854465329</double>\n"
                + "                <double>0.9446485581916266</double>\n"
                + "                <double>-0.9446485581916266</double>\n"
                + "                <double>-0.3280839854465329</double>\n"
                + "                <double>0.0</double>\n"
                + "                <double>2.116728847092748</double>\n"
                + "              </trafo>\n"
                + "              <base class=\"PolynomeCurve\">\n"
                + "                <params>\n"
                + "                  <double-array>\n"
                + "                    <double>0.0</double>\n"
                + "                  </double-array>\n"
                + "                  <double-array>\n"
                + "                    <double>0.0</double>\n"
                + "                    <double>0.2442188609265664</double>\n"
                + "                    <double>-0.0535848758171096</double>\n"
                + "                  </double-array>\n"
                + "                  <double-array>\n"
                + "                    <double>2.541398097787834</double>\n"
                + "                    <double>2.8927673233271416</double>\n"
                + "                  </double-array>\n"
                + "                </params>\n" + "              </base>\n"
                + "              <t0>3.617904278509095</t0>\n"
                + "            </curve>\n"
                + "            <t0>3.617904278509095</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.7853981633974483</a>\n"
                + "              <x>0.1</x>\n"
                + "              <y>1.8287999629974365</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "          <CurvePart>\n"
                + "            <curve class=\"TransformedCurve\">\n"
                + "              <trafo>\n"
                + "                <double>-0.9693440023087092</double>\n"
                + "                <double>-0.2457075602990949</double>\n"
                + "                <double>0.2457075602990949</double>\n"
                + "                <double>-0.9693440023087092</double>\n"
                + "                <double>0.1</double>\n"
                + "                <double>1.8287999629974365</double>\n"
                + "              </trafo>\n"
                + "              <base class=\"PolynomeCurve\">\n"
                + "                <params>\n"
                + "                  <double-array>\n"
                + "                    <double>0.0</double>\n"
                + "                  </double-array>\n"
                + "                  <double-array>\n"
                + "                    <double>0.0</double>\n"
                + "                    <double>0.9389250968779904</double>\n"
                + "                    <double>-0.0535848758171096</double>\n"
                + "                  </double-array>\n"
                + "                  <double-array>\n"
                + "                    <double>0.7853981633974483</double>\n"
                + "                    <double>1.321970996532245</double>\n"
                + "                  </double-array>\n"
                + "                </params>\n" + "              </base>\n"
                + "              <t0>3.617904278509095</t0>\n"
                + "            </curve>\n"
                + "            <t0>3.617904278509095</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>-2.2098000049591064</x>\n"
                + "              <y>9.083040237426758</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>2.2098000049591064</x>\n"
                + "              <y>9.083040237426758</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>-2.2098000049591064</x>\n"
                + "              <y>8.717280387878418</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>2.2098000049591064</x>\n"
                + "              <y>8.717280387878418</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>-2.2098000049591064</x>\n"
                + "              <y>8.351519584655762</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>2.2098000049591064</x>\n"
                + "              <y>8.351519584655762</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>-2.2098000049591064</x>\n"
                + "              <y>7.98576021194458</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>2.2098000049591064</x>\n"
                + "              <y>7.98576021194458</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>-2.2098000049591064</x>\n"
                + "              <y>7.619999885559082</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>2.2098000049591064</x>\n"
                + "              <y>7.619999885559082</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>-2.2098000049591064</x>\n"
                + "              <y>7.254240036010742</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>2.2098000049591064</x>\n"
                + "              <y>7.254240036010742</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>-2.2098000049591064</x>\n"
                + "              <y>6.888480186462402</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "      <CombinedCurve>\n"
                + "        <parts>\n" + "          <CurvePart>\n"
                + "            <curve class=\"PointCurve\">\n"
                + "              <a>0.0</a>\n"
                + "              <x>2.2098000049591064</x>\n"
                + "              <y>6.888480186462402</y>\n"
                + "            </curve>\n" + "            <t0>0.0</t0>\n"
                + "          </CurvePart>\n" + "        </parts>\n"
                + "      </CombinedCurve>\n" + "    </curve>\n"
                + "  </store>\n" + "</StoredTrajectory>", x);
        TrajectorySet b = (TrajectorySet) xs.read(x);
        b.setCurrentTime(11);
        assertEquals(x, xs.write(b));
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
        xs.registerConverter(new XStreamIO.DimValConverter());
        xs.registerConverter(new XStreamIO.RockConverter());
        xs.alias("dimval", DimVal.class);
        xs.alias("rock", RockDouble.class);
        String x = xs.toXML(te.getCurveStore());
        // System.out.println(x);
        assertEquals(
                "<org.jcurl.core.base.CurveStore>\n"
                        + "  <curve>\n"
                        + "    <org.jcurl.math.CurveCombined>\n"
                        + "      <parts>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveTransformed\">\n"
                        + "            <trafo>\n"
                        + "              <double>-1.0</double>\n"
                        + "              <double>-0.0</double>\n"
                        + "              <double>0.0</double>\n"
                        + "              <double>-1.0</double>\n"
                        + "              <double>0.0</double>\n"
                        + "              <double>6.4008002281188965</double>\n"
                        + "            </trafo>\n"
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
                        + "          </curve>\n"
                        + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "        <org.jcurl.math.CurvePart>\n"
                        + "          <curve class=\"org.jcurl.core.base.CurveTransformed\">\n"
                        + "            <trafo>\n"
                        + "              <double>-0.3280839854465329</double>\n"
                        + "              <double>0.9446485581916266</double>\n"
                        + "              <double>-0.9446485581916266</double>\n"
                        + "              <double>-0.3280839854465329</double>\n"
                        + "              <double>0.0</double>\n"
                        + "              <double>2.116728847092748</double>\n"
                        + "            </trafo>\n"
                        + "            <base class=\"org.jcurl.math.PolynomeCurve\">\n"
                        + "              <params>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                  <double>0.2442188609265664</double>\n"
                        + "                  <double>-0.0535848758171096</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>2.541398097787834</double>\n"
                        + "                  <double>2.8927673233271416</double>\n"
                        + "                </double-array>\n"
                        + "              </params>\n"
                        + "            </base>\n"
                        + "            <t0>3.617904278509095</t0>\n"
                        + "          </curve>\n"
                        + "          <t0>3.617904278509095</t0>\n"
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
                        + "            <trafo>\n"
                        + "              <double>-0.9693440023087092</double>\n"
                        + "              <double>-0.2457075602990949</double>\n"
                        + "              <double>0.2457075602990949</double>\n"
                        + "              <double>-0.9693440023087092</double>\n"
                        + "              <double>0.1</double>\n"
                        + "              <double>1.8287999629974365</double>\n"
                        + "            </trafo>\n"
                        + "            <base class=\"org.jcurl.math.PolynomeCurve\">\n"
                        + "              <params>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.0</double>\n"
                        + "                  <double>0.9389250968779904</double>\n"
                        + "                  <double>-0.0535848758171096</double>\n"
                        + "                </double-array>\n"
                        + "                <double-array>\n"
                        + "                  <double>0.7853981633974483</double>\n"
                        + "                  <double>1.321970996532245</double>\n"
                        + "                </double-array>\n"
                        + "              </params>\n"
                        + "            </base>\n"
                        + "            <t0>3.617904278509095</t0>\n"
                        + "          </curve>\n"
                        + "          <t0>3.617904278509095</t0>\n"
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
                        + "          </curve>\n" + "          <t0>0.0</t0>\n"
                        + "        </org.jcurl.math.CurvePart>\n"
                        + "      </parts>\n"
                        + "    </org.jcurl.math.CurveCombined>\n"
                        + "  </curve>\n" + "</org.jcurl.core.base.CurveStore>",
                x);
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

        final XStream xs = new XStream();
        xs.registerConverter(new XStreamIO.DimValConverter());
        xs.registerConverter(new XStreamIO.RockConverter());
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
                        + "  <curler class=\"org.jcurl.core.model.CurlerNoCurl\">\n"
                        + "    <params>\n" + "      <entry>\n"
                        + "        <string>drawToTeeTime</string>\n"
                        + "        <dimval>23.0 s</dimval>\n"
                        + "      </entry>\n" + "      <entry>\n"
                        + "        <string>drawToTeeCurl</string>\n"
                        + "        <dimval>0.0 m</dimval>\n"
                        + "      </entry>\n" + "    </params>\n"
                        + "  </curler>\n"
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

    public void testXSIO() {
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

        final JCurlIO xs = new XStreamIO();
        String x = xs.write(te);
        // System.out.println(x);
        assertEquals(
                "<CurveManager>\n"
                        + "  <collider class=\"CollissionSpin\">\n"
                        + "    <params>\n"
                        + "      <entry>\n"
                        + "        <string>loss</string>\n"
                        + "        <DimVal>0.0 J</DimVal>\n"
                        + "      </entry>\n"
                        + "      <entry>\n"
                        + "        <string>frictionRockRock</string>\n"
                        + "        <DimVal>0.0 </DimVal>\n"
                        + "      </entry>\n"
                        + "    </params>\n"
                        + "  </collider>\n"
                        + "  <collissionDetector class=\"NewtonCollissionDetector\"/>\n"
                        + "  <initialPos>\n"
                        + "    <dark>\n"
                        + "      <Rock>0.0, 6.4008002281188965, 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064, 9.083040237426758, 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064, 8.717280387878418, 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064, 8.351519584655762, 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064, 7.98576021194458, 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064, 7.619999885559082, 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064, 7.254240036010742, 0.0</Rock>\n"
                        + "      <Rock>-2.2098000049591064, 6.888480186462402, 0.0</Rock>\n"
                        + "    </dark>\n"
                        + "    <light>\n"
                        + "      <Rock>0.1, 1.8287999629974365, 0.7853981633974483</Rock>\n"
                        + "      <Rock>2.2098000049591064, 9.083040237426758, 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064, 8.717280387878418, 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064, 8.351519584655762, 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064, 7.98576021194458, 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064, 7.619999885559082, 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064, 7.254240036010742, 0.0</Rock>\n"
                        + "      <Rock>2.2098000049591064, 6.888480186462402, 0.0</Rock>\n"
                        + "    </light>\n"
                        + "  </initialPos>\n"
                        + "  <initialSpeed>\n"
                        + "    <dark>\n"
                        + "      <Rock>0.0, -1.3779956114540028, 1.5707963267948966</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "    </dark>\n" + "    <light>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "      <Rock>0.0, 0.0, 0.0</Rock>\n"
                        + "    </light>\n" + "  </initialSpeed>\n"
                        + "  <curler class=\"NoCurlCurler\">\n"
                        + "    <params>\n" + "      <entry>\n"
                        + "        <string>drawToTeeTime</string>\n"
                        + "        <DimVal>23.0 s</DimVal>\n"
                        + "      </entry>\n" + "      <entry>\n"
                        + "        <string>drawToTeeCurl</string>\n"
                        + "        <DimVal>0.0 m</DimVal>\n"
                        + "      </entry>\n" + "    </params>\n"
                        + "  </curler>\n" + "</CurveManager>", x);

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
