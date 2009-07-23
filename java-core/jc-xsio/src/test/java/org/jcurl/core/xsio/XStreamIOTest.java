/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.xsio;

import java.awt.geom.Point2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Measure;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.TrajectorySet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.helpers.AnnoHelper;
import org.jcurl.core.impl.CollissionSpin;
import org.jcurl.core.impl.CurlerNoCurl;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.impl.NewtonCollissionDetector;
import org.jcurl.core.impl.StoredTrajectorySet;
import org.jcurl.core.io.IONode;
import org.jcurl.core.io.IOTrajectories;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

public class XStreamIOTest extends TestBase {
	private static final Point2D tee = new Point2D.Double(0, 0);

	public static ComputedTrajectorySet initHammy(ComputedTrajectorySet te) {
		if (te == null)
			te = new CurveManager();
		te.setCollider(new CollissionSpin(0.5, 0.0));
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(24, 0));
		te.setInitialPos(RockSetUtils.allOut());
		te.setInitialVel(new RockSet<Vel>(new RockDouble<Vel>()));
		te.getAnnotations().put(AnnoHelper.HammerK, AnnoHelper.HammerVDark);
		te.getAnnotations().put(AnnoHelper.DarkTeamK, "Scotland");
		te.getAnnotations().put(AnnoHelper.LightTeamK, "Canada");
		te.getAnnotations().put(AnnoHelper.GameK, "Semifinal");
		te.getAnnotations().put(AnnoHelper.EventK,
				"World Curling Championships");
		te.getAnnotations().put(AnnoHelper.DateK, "1992");
		te.getAnnotations().put(AnnoHelper.LocationK, "Garmisch");
		initHammy(te.getInitialPos(), te.getInitialVel());
		return te;
	}

	public static void initHammy(final RockSet<Pos> p, final RockSet<Vel> s) {
		RockSetUtils.allOut(p);
		// te.getInitialPos().getLight(1-1).setLocation(
		p.getLight(2 - 1).setLocation(Unit.f2m(-1.170732), Unit.f2m(15.365854),
				0);
		p.getLight(3 - 1)
				.setLocation(Unit.f2m(0.292683), Unit.f2m(8.780488), 0);
		p.getLight(4 - 1).setLocation(Unit.f2m(2.195122), Unit.f2m(12), 0);
		p.getLight(5 - 1)
				.setLocation(Unit.f2m(1.463415), Unit.f2m(5.707317), 0);
		p.getLight(6 - 1).setLocation(Unit.f2m(1.463415), Unit.f2m(-2.780488),
				0);
		p.getLight(7 - 1).setLocation(Unit.f2m(-0.439024), Unit.f2m(-5.560976),
				0);
		p.getLight(8 - 1).setLocation(Unit.f2m(-1.756098), Unit.f2m(-1.609756),
				0);
		// p.getDark(1-1).setLocation(
		// p.getDark(2-1).setLocation(
		p.getDark(3 - 1)
				.setLocation(Unit.f2m(0.878049), Unit.f2m(14.341463), 0);
		p.getDark(4 - 1).setLocation(Unit.f2m(-2.634146), Unit.f2m(13.170732),
				0);
		p.getDark(5 - 1)
				.setLocation(Unit.f2m(4.536585), Unit.f2m(-0.439024), 0);
		p.getDark(6 - 1).setLocation(Unit.f2m(0.731707), Unit.f2m(-3.95122), 0);
		p.getDark(7 - 1).setLocation(Unit.f2m(-2.780488), Unit.f2m(-4.390244),
				0);
		p.getDark(8 - 1).setLocation(Unit.f2m(3.89991), IceSize.HOG_2_TEE, 0);
		RockSet.allZero(s);
		s.getDark(7).setLocation(0, -3, 100 * Math.PI / 180);
	}

	static IONode wrap(final TrajectorySet t) {
		final IOTrajectories l = new IOTrajectories();
		l.trajectories().add(t);
		return l;
	}

	public void testComputedTrajectorySet() throws IOException {
		final XStreamSerializer xs = new XStreamSerializer();
		final IONode con = wrap(initHammy(null));
		final String x = xs.write(con);
		assertEquals(
				"<IOTrajectories>\n"
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
						+ "            <measure>0.0 J</measure>\n"
						+ "          </entry>\n"
						+ "          <entry>\n"
						+ "            <string>frictionRockRock</string>\n"
						+ "            <measure>0.5 </measure>\n"
						+ "          </entry>\n"
						+ "        </params>\n"
						+ "      </collider>\n"
						+ "      <curler class=\"NoCurlCurler\">\n"
						+ "        <params>\n"
						+ "          <entry>\n"
						+ "            <string>drawToTeeTime</string>\n"
						+ "            <measure>24.0 s</measure>\n"
						+ "          </entry>\n"
						+ "          <entry>\n"
						+ "            <string>drawToTeeCurl</string>\n"
						+ "            <measure>0.0 m</measure>\n"
						+ "          </entry>\n"
						+ "        </params>\n"
						+ "      </curler>\n"
						+ "      <initialPos>\n"
						+ "        <dark>\n"
						+ "          <rock>-0.7620000243186951 -2.4384000301361084 0.0</rock>\n"
						+ "          <rock>-0.9448800086975098 -2.8041601181030273 0.0</rock>\n"
						+ "          <rock>0.26762932538986206 4.371277809143066 0.0</rock>\n"
						+ "          <rock>-0.8028876781463623 4.014439105987549 0.0</rock>\n"
						+ "          <rock>1.3827511072158813 -0.1338145136833191 0.0</rock>\n"
						+ "          <rock>0.22302429378032684 -1.204331874847412 0.0</rock>\n"
						+ "          <rock>-0.8474927544593811 -1.3381463289260864 0.0</rock>\n"
						+ "          <rock>1.188692569732666 6.4008002281188965 0.0</rock>\n"
						+ "        </dark>\n"
						+ "        <light>\n"
						+ "          <rock>0.7620000243186951 -2.4384000301361084 0.0</rock>\n"
						+ "          <rock>-0.356839120388031 4.683512210845947 0.0</rock>\n"
						+ "          <rock>0.08920978009700775 2.676292657852173 0.0</rock>\n"
						+ "          <rock>0.6690731644630432 3.657599925994873 0.0</rock>\n"
						+ "          <rock>0.44604888558387756 1.7395901679992676 0.0</rock>\n"
						+ "          <rock>0.44604888558387756 -0.8474927544593811 0.0</rock>\n"
						+ "          <rock>-0.1338145136833191 -1.6949855089187622 0.0</rock>\n"
						+ "          <rock>-0.5352586507797241 -0.4906536340713501 0.0</rock>\n"
						+ "        </light>\n"
						+ "      </initialPos>\n"
						+ "      <initialSpeed>\n"
						+ "        <dark>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 -3.0 1.7453292519943295</rock>\n"
						+ "        </dark>\n" + "        <light>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "        </light>\n" + "      </initialSpeed>\n"
						+ "    </CurveManager>\n" + "  </trajectories>\n"
						+ "  <annotations/>\n" + "</IOTrajectories>", x);
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		xs.write(con, bout);
		bout.close();
		assertEquals(x, xs.write(xs.read(new ByteArrayInputStream(bout
				.toByteArray()), null)));
	}

	public void testCurveStoreRaw() {
		final ComputedTrajectorySet te = new CurveManager();
		te.setCollider(new CollissionSpin());
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(23, 0));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		te.setInitialVel(new RockSet<Vel>(new RockDouble<Vel>()));
		te.getInitialVel().getDark(0).setLocation(0,
				-te.getCurler().computeHackSpeed(5, tee), Math.PI / 2);
		te.setCurrentTime(20);

		final XStream xs = new XStream();
		xs.registerConverter(new XStreamSerializer.MeasureConverter());
		xs.registerConverter(new XStreamSerializer.RockConverter());
		xs.alias("dimval", Measure.class);
		xs.alias("rock", RockDouble.class);
		final String x = xs.toXML(te.getCurveStore());
		// System.out.println(x);
		assertEquals(
				"<org.jcurl.core.impl.CurveStoreImpl>\n"
						+ "  <curve>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveTransformed\">\n"
						+ "            <base class=\"org.jcurl.math.PolynomeCurve\">\n"
						+ "              <params>\n"
						+ "                <double-array>\n"
						+ "                  <double>0.0</double>\n"
						+ "                </double-array>\n"
						+ "                <double-array>\n"
						+ "                  <double>0.0</double>\n"
						+ "                  <double>2.0136495784249293</double>\n"
						+ "                  <double>-0.053584879422683564</double>\n"
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
						+ "              <double>-0.0</double>\n"
						+ "              <double>-1.0</double>\n"
						+ "              <double>0.0</double>\n"
						+ "              <double>6.4008002281188965</double>\n"
						+ "            </trafo>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveTransformed\">\n"
						+ "            <base class=\"org.jcurl.math.PolynomeCurve\">\n"
						+ "              <params>\n"
						+ "                <double-array>\n"
						+ "                  <double>0.0</double>\n"
						+ "                </double-array>\n"
						+ "                <double-array>\n"
						+ "                  <double>0.0</double>\n"
						+ "                  <double>0.5810457999357032</double>\n"
						+ "                  <double>-0.053584879422683564</double>\n"
						+ "                </double-array>\n"
						+ "                <double-array>\n"
						+ "                  <double>1.6510599037291682</double>\n"
						+ "                  <double>1.5707963267948966</double>\n"
						+ "                </double-array>\n"
						+ "              </params>\n"
						+ "            </base>\n"
						+ "            <t0>2.2639031845066224</t0>\n"
						+ "            <trafo>\n"
						+ "              <double>-0.32808398544653306</double>\n"
						+ "              <double>0.9446485581916266</double>\n"
						+ "              <double>-0.9446485581916266</double>\n"
						+ "              <double>-0.32808398544653306</double>\n"
						+ "              <double>0.0</double>\n"
						+ "              <double>2.116728847092748</double>\n"
						+ "            </trafo>\n"
						+ "          </curve>\n"
						+ "          <t0>2.2639031845066224</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>12.072568819565976</a>\n"
						+ "            <x>-1.4879514105338856</x>\n"
						+ "            <y>1.5999514438082143</y>\n"
						+ "          </curve>\n"
						+ "          <t0>7.685636013804053</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.7853981633974483</a>\n"
						+ "            <x>0.1</x>\n"
						+ "            <y>1.8287999629974365</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveTransformed\">\n"
						+ "            <base class=\"org.jcurl.math.PolynomeCurve\">\n"
						+ "              <params>\n"
						+ "                <double-array>\n"
						+ "                  <double>0.0</double>\n"
						+ "                </double-array>\n"
						+ "                <double-array>\n"
						+ "                  <double>0.0</double>\n"
						+ "                  <double>1.6729986878375467</double>\n"
						+ "                  <double>-0.053584879422683564</double>\n"
						+ "                </double-array>\n"
						+ "                <double-array>\n"
						+ "                  <double>3.5927162410690348</double>\n"
						+ "                  <double>0.0</double>\n"
						+ "                </double-array>\n"
						+ "              </params>\n"
						+ "            </base>\n"
						+ "            <t0>2.2639031845066224</t0>\n"
						+ "            <trafo>\n"
						+ "              <double>-0.9446485581916266</double>\n"
						+ "              <double>-0.328083985446533</double>\n"
						+ "              <double>0.328083985446533</double>\n"
						+ "              <double>-0.9446485581916266</double>\n"
						+ "              <double>0.1</double>\n"
						+ "              <double>1.8287999629974365</double>\n"
						+ "            </trafo>\n"
						+ "          </curve>\n"
						+ "          <t0>2.2639031845066224</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.7853981633974483</a>\n"
						+ "            <x>4.3842418922306665</x>\n"
						+ "            <y>-10.50676990924618</y>\n"
						+ "          </curve>\n"
						+ "          <t0>17.874638020587827</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>-2.2098000049591064</x>\n"
						+ "            <y>9.083040237426758</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>2.2098000049591064</x>\n"
						+ "            <y>9.083040237426758</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>-2.2098000049591064</x>\n"
						+ "            <y>8.717280387878418</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>2.2098000049591064</x>\n"
						+ "            <y>8.717280387878418</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>-2.2098000049591064</x>\n"
						+ "            <y>8.351519584655762</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>2.2098000049591064</x>\n"
						+ "            <y>8.351519584655762</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>-2.2098000049591064</x>\n"
						+ "            <y>7.98576021194458</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>2.2098000049591064</x>\n"
						+ "            <y>7.98576021194458</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>-2.2098000049591064</x>\n"
						+ "            <y>7.619999885559082</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>2.2098000049591064</x>\n"
						+ "            <y>7.619999885559082</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>-2.2098000049591064</x>\n"
						+ "            <y>7.254240036010742</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>2.2098000049591064</x>\n"
						+ "            <y>7.254240036010742</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>-2.2098000049591064</x>\n"
						+ "            <y>6.888480186462402</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "    <org.jcurl.math.CurveCombined>\n"
						+ "      <parts>\n"
						+ "        <org.jcurl.math.CurveCombined_-Part>\n"
						+ "          <curve class=\"org.jcurl.core.impl.CurveStill\">\n"
						+ "            <a>0.0</a>\n"
						+ "            <x>2.2098000049591064</x>\n"
						+ "            <y>6.888480186462402</y>\n"
						+ "          </curve>\n"
						+ "          <t0>0.0</t0>\n"
						+ "        </org.jcurl.math.CurveCombined_-Part>\n"
						+ "      </parts>\n"
						+ "    </org.jcurl.math.CurveCombined>\n"
						+ "  </curve>\n"
						+ "  <stopper class=\"org.jcurl.core.impl.NewtonStopDetector\"/>\n"
						+ "</org.jcurl.core.impl.CurveStoreImpl>", x);
	}

	public void testDefaultSettings() {
		final XStream xs = new XStream();
		System.out.println(xs.getClassLoader());
		assertEquals(Sun14ReflectionProvider.class, xs.getReflectionProvider()
				.getClass());
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
		final ComputedTrajectorySet te = new CurveManager();
		te.setCollider(new CollissionSpin(0.5, 0));
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(23, 0));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		te.setInitialVel(new RockSet<Vel>(new RockDouble<Vel>()));
		te.getInitialVel().getDark(0).setLocation(0,
				-te.getCurler().computeHackSpeed(5, tee), Math.PI / 2);

		final XStream xs = new XStream();
		xs.registerConverter(new XStreamSerializer.MeasureConverter());
		xs.registerConverter(new XStreamSerializer.RockConverter());
		xs.alias("dimval", Measure.class);
		xs.alias("rock", RockDouble.class);
		final String x = xs.toXML(te);
		// System.out.println(x);
		assertEquals(
				"<org.jcurl.core.impl.CurveManager>\n"
						+ "  <annotations/>\n"
						+ "  <collider class=\"org.jcurl.core.impl.CollissionSpin\">\n"
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
						+ "  <curler class=\"org.jcurl.core.impl.CurlerNoCurl\">\n"
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
						+ "      <rock>0.0 -2.0136495784249293 1.5707963267948966</rock>\n"
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
						+ "</org.jcurl.core.impl.CurveManager>", x);

		final ComputedTrajectorySet o = (ComputedTrajectorySet) xs.fromXML(x);
		assertNotNull(o);
		assertNotNull(o.getCollider());
		assertNotNull(o.getCollissionDetector());
		assertNotNull(o.getCurrentPos());
		assertNotNull(o.getCurrentVel());
		assertNotNull(o.getCurveStore());
		assertNotNull(o.getInitialPos());
		assertNotNull(o.getInitialVel());
		assertNotNull(o.getCurler());
	}

	public void testStoredTrajectorySet() throws IOException {
		final ComputedTrajectorySet te = new CurveManager();
		te.setCollider(new CollissionSpin());
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(23, 0));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		te.setInitialVel(new RockSet<Vel>(new RockDouble<Vel>()));
		te.getInitialVel().getDark(0).setLocation(0,
				-te.getCurler().computeHackSpeed(5, tee), Math.PI / 2);
		te.setCurrentTime(20);

		final XStreamSerializer xs = new XStreamSerializer();
		final IONode con = wrap(new StoredTrajectorySet(te.getCurveStore()));
		final String x = xs.write(con);
		// System.out.println(x);
		assertEquals(
				"<IOTrajectories>\n"
						+ "  <trajectories>\n"
						+ "    <StoredTrajectory>\n"
						+ "      <annotations/>\n"
						+ "      <store class=\"CurveStore\">\n"
						+ "        <curve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n"
						+ "              <part>\n"
						+ "                <curve class=\"TransformedCurve\">\n"
						+ "                  <base class=\"PolynomeCurve\">\n"
						+ "                    <params>\n"
						+ "                      <double-array>0.0</double-array>\n"
						+ "                      <double-array>0.0 2.0136495784249293 -0.053584879422683564</double-array>\n"
						+ "                      <double-array>3.141592653589793 1.5707963267948966</double-array>\n"
						+ "                    </params>\n"
						+ "                  </base>\n"
						+ "                  <t0>0.0</t0>\n"
						+ "                  <trafo>-1.0 -0.0 -0.0 -1.0 0.0 6.4008002281188965</trafo>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n"
						+ "              <part>\n"
						+ "                <curve class=\"TransformedCurve\">\n"
						+ "                  <base class=\"PolynomeCurve\">\n"
						+ "                    <params>\n"
						+ "                      <double-array>0.0</double-array>\n"
						+ "                      <double-array>0.0 0.5810457999357032 -0.053584879422683564</double-array>\n"
						+ "                      <double-array>1.6510599037291682 1.5707963267948966</double-array>\n"
						+ "                    </params>\n"
						+ "                  </base>\n"
						+ "                  <t0>2.2639031845066224</t0>\n"
						+ "                  <trafo>-0.32808398544653306 0.9446485581916266 -0.9446485581916266 -0.32808398544653306 0.0 2.116728847092748</trafo>\n"
						+ "                </curve>\n"
						+ "                <t0>2.2639031845066224</t0>\n"
						+ "              </part>\n"
						+ "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>12.072568819565976</a>\n"
						+ "                  <x>-1.4879514105338856</x>\n"
						+ "                  <y>1.5999514438082143</y>\n"
						+ "                </curve>\n"
						+ "                <t0>7.685636013804053</t0>\n"
						+ "              </part>\n"
						+ "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n"
						+ "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.7853981633974483</a>\n"
						+ "                  <x>0.1</x>\n"
						+ "                  <y>1.8287999629974365</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n"
						+ "              <part>\n"
						+ "                <curve class=\"TransformedCurve\">\n"
						+ "                  <base class=\"PolynomeCurve\">\n"
						+ "                    <params>\n"
						+ "                      <double-array>0.0</double-array>\n"
						+ "                      <double-array>0.0 1.6729986878375467 -0.053584879422683564</double-array>\n"
						+ "                      <double-array>3.5927162410690348 0.0</double-array>\n"
						+ "                    </params>\n"
						+ "                  </base>\n"
						+ "                  <t0>2.2639031845066224</t0>\n"
						+ "                  <trafo>-0.9446485581916266 -0.328083985446533 0.328083985446533 -0.9446485581916266 0.1 1.8287999629974365</trafo>\n"
						+ "                </curve>\n"
						+ "                <t0>2.2639031845066224</t0>\n"
						+ "              </part>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.7853981633974483</a>\n"
						+ "                  <x>4.3842418922306665</x>\n"
						+ "                  <y>-10.50676990924618</y>\n"
						+ "                </curve>\n"
						+ "                <t0>17.874638020587827</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>-2.2098000049591064</x>\n"
						+ "                  <y>9.083040237426758</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>2.2098000049591064</x>\n"
						+ "                  <y>9.083040237426758</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>-2.2098000049591064</x>\n"
						+ "                  <y>8.717280387878418</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>2.2098000049591064</x>\n"
						+ "                  <y>8.717280387878418</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>-2.2098000049591064</x>\n"
						+ "                  <y>8.351519584655762</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>2.2098000049591064</x>\n"
						+ "                  <y>8.351519584655762</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>-2.2098000049591064</x>\n"
						+ "                  <y>7.98576021194458</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>2.2098000049591064</x>\n"
						+ "                  <y>7.98576021194458</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>-2.2098000049591064</x>\n"
						+ "                  <y>7.619999885559082</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>2.2098000049591064</x>\n"
						+ "                  <y>7.619999885559082</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>-2.2098000049591064</x>\n"
						+ "                  <y>7.254240036010742</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>2.2098000049591064</x>\n"
						+ "                  <y>7.254240036010742</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>-2.2098000049591064</x>\n"
						+ "                  <y>6.888480186462402</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n"
						+ "          <CombinedCurve>\n"
						+ "            <parts>\n" + "              <part>\n"
						+ "                <curve class=\"PointCurve\">\n"
						+ "                  <a>0.0</a>\n"
						+ "                  <x>2.2098000049591064</x>\n"
						+ "                  <y>6.888480186462402</y>\n"
						+ "                </curve>\n"
						+ "                <t0>0.0</t0>\n"
						+ "              </part>\n" + "            </parts>\n"
						+ "          </CombinedCurve>\n" + "        </curve>\n"
						+ "        <stopper class=\"NewtonStopDetector\"/>\n"
						+ "      </store>\n" + "    </StoredTrajectory>\n"
						+ "  </trajectories>\n" + "  <annotations/>\n"
						+ "</IOTrajectories>", x);
		final TrajectorySet b = ((IOTrajectories) xs.read(x)).trajectories()
				.get(0);
		b.setCurrentTime(11);
		assertEquals(x, xs.write(wrap(b)));

		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		xs.write(con, bout);
		bout.close();
		xs.write(con, bout);
		assertEquals(x, xs.write(xs.read(new ByteArrayInputStream(bout
				.toByteArray()), null)));

		// final File f2 = new File("/tmp/hammy.game.jcz");
		// f2.deleteOnExit();
		// IODocument c2 = wrap(con.getRoot());
		// xs.write(c2, f2);
	}

	public void testXSIO() {
		final ComputedTrajectorySet te = new CurveManager();
		te.setCollider(new CollissionSpin(0.5, 0));
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerNoCurl(23, 0));
		te.setInitialPos(RockSetUtils.allHome());
		te.getInitialPos().getDark(0).setLocation(0, IceSize.HOG_2_TEE, 0);
		te.getInitialPos().getLight(0).setLocation(0.1, IceSize.BACK_2_TEE,
				0.25 * Math.PI);
		te.setInitialVel(new RockSet<Vel>(new RockDouble<Vel>()));
		te.getInitialVel().getDark(0).setLocation(0,
				-te.getCurler().computeHackSpeed(5, tee), Math.PI / 2);

		final XStreamSerializer xs = new XStreamSerializer();
		final String x = xs.write(wrap(te));
		// System.out.println(x);
		assertEquals(
				"<IOTrajectories>\n"
						+ "  <trajectories>\n"
						+ "    <CurveManager>\n"
						+ "      <annotations/>\n"
						+ "      <collider class=\"CollissionSpin\">\n"
						+ "        <params>\n"
						+ "          <entry>\n"
						+ "            <string>loss</string>\n"
						+ "            <measure>0.0 J</measure>\n"
						+ "          </entry>\n"
						+ "          <entry>\n"
						+ "            <string>frictionRockRock</string>\n"
						+ "            <measure>0.5 </measure>\n"
						+ "          </entry>\n"
						+ "        </params>\n"
						+ "      </collider>\n"
						+ "      <curler class=\"NoCurlCurler\">\n"
						+ "        <params>\n"
						+ "          <entry>\n"
						+ "            <string>drawToTeeTime</string>\n"
						+ "            <measure>23.0 s</measure>\n"
						+ "          </entry>\n"
						+ "          <entry>\n"
						+ "            <string>drawToTeeCurl</string>\n"
						+ "            <measure>0.0 m</measure>\n"
						+ "          </entry>\n"
						+ "        </params>\n"
						+ "      </curler>\n"
						+ "      <initialPos>\n"
						+ "        <dark>\n"
						+ "          <rock>0.0 6.4008002281188965 0.0</rock>\n"
						+ "          <rock>-2.2098000049591064 9.083040237426758 0.0</rock>\n"
						+ "          <rock>-2.2098000049591064 8.717280387878418 0.0</rock>\n"
						+ "          <rock>-2.2098000049591064 8.351519584655762 0.0</rock>\n"
						+ "          <rock>-2.2098000049591064 7.98576021194458 0.0</rock>\n"
						+ "          <rock>-2.2098000049591064 7.619999885559082 0.0</rock>\n"
						+ "          <rock>-2.2098000049591064 7.254240036010742 0.0</rock>\n"
						+ "          <rock>-2.2098000049591064 6.888480186462402 0.0</rock>\n"
						+ "        </dark>\n"
						+ "        <light>\n"
						+ "          <rock>0.1 1.8287999629974365 0.7853981633974483</rock>\n"
						+ "          <rock>2.2098000049591064 9.083040237426758 0.0</rock>\n"
						+ "          <rock>2.2098000049591064 8.717280387878418 0.0</rock>\n"
						+ "          <rock>2.2098000049591064 8.351519584655762 0.0</rock>\n"
						+ "          <rock>2.2098000049591064 7.98576021194458 0.0</rock>\n"
						+ "          <rock>2.2098000049591064 7.619999885559082 0.0</rock>\n"
						+ "          <rock>2.2098000049591064 7.254240036010742 0.0</rock>\n"
						+ "          <rock>2.2098000049591064 6.888480186462402 0.0</rock>\n"
						+ "        </light>\n"
						+ "      </initialPos>\n"
						+ "      <initialSpeed>\n"
						+ "        <dark>\n"
						+ "          <rock>0.0 -2.0136495784249293 1.5707963267948966</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "        </dark>\n" + "        <light>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "          <rock>0.0 0.0 0.0</rock>\n"
						+ "        </light>\n" + "      </initialSpeed>\n"
						+ "    </CurveManager>\n" + "  </trajectories>\n"
						+ "  <annotations/>\n" + "</IOTrajectories>", x);

		final IOTrajectories l = (IOTrajectories) xs.read(x);
		final ComputedTrajectorySet o = (ComputedTrajectorySet) l
				.trajectories().get(0);
		assertNotNull(o);
		assertNotNull(o.getCollider());
		assertNotNull(o.getCollissionDetector());
		assertNotNull(o.getCurrentPos());
		assertNotNull(o.getCurrentVel());
		assertNotNull(o.getCurveStore());
		assertNotNull(o.getInitialPos());
		assertNotNull(o.getInitialVel());
		assertNotNull(o.getCurler());
	}
}