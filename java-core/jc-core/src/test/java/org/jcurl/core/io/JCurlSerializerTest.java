/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
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
package org.jcurl.core.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.PositionSet;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.api.RockType.Vel;
import org.jcurl.core.helpers.AnnoHelper;
import org.jcurl.core.impl.CollissionSpin;
import org.jcurl.core.impl.CurlerDenny;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.impl.NewtonCollissionDetector;

public class JCurlSerializerTest extends TestCase {

	static ComputedTrajectorySet initHammy(ComputedTrajectorySet te) {
		if (te == null)
			te = new CurveManager();
		te.setCollider(new CollissionSpin(0.5, 0.0));
		te.setCollissionDetector(new NewtonCollissionDetector());
		te.setCurler(new CurlerDenny(24, 1));
		te.setInitialPos(PositionSet.allOut());
		te.setInitialSpeed(new RockSet<Vel>(new RockDouble<Vel>()));
		te.getAnnotations().put(AnnoHelper.HammerK, AnnoHelper.HammerVDark);
		te.getAnnotations().put(AnnoHelper.DarkTeamK, "Scotland");
		te.getAnnotations().put(AnnoHelper.LightTeamK, "Canada");
		te.getAnnotations().put(AnnoHelper.GameK, "Semifinal");
		te.getAnnotations().put(AnnoHelper.EventK,
				"World Curling Championships");
		te.getAnnotations().put(AnnoHelper.DateK, "1992");
		te.getAnnotations().put(AnnoHelper.LocationK, "Garmisch");
		initHammy(te.getInitialPos(), te.getInitialSpeed());
		return te;
	}

	static void initHammy(final RockSet<Pos> p, final RockSet<Vel> s) {
		PositionSet.allOut(p);
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
		s.getDark(8 - 1).setLocation(0, -3, 100 * Math.PI / 180);

		p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
		s.getDark(8 - 1).setLocation(0.188, -3, -100 * Math.PI / 180);

		p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
		s.getDark(8 - 1).setLocation(0.1785, -4, -100 * Math.PI / 180);
		p.fireStateChanged();
		s.fireStateChanged();
	}

	private final JCurlSerializer io = new JCurlSerializer();

	public void testEmpty() throws IOException {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		io.write(new IOGroup(), bout, JDKSerializer.class);
		bout.close();
		assertEquals("<!-- " + JDKSerializer.class.getName() + " -->\n",
				new String(bout.toByteArray(), 0, 5 + JDKSerializer.class
						.getName().length() + 5, "UTF-8"));

		final IOGroup d = (IOGroup) io.read(new ByteArrayInputStream(bout
				.toByteArray()));
		assertEquals(0, d.children().size());
		assertNotNull(d.annotations().get(IONode.CreatedByUser));
	}

	public void testHammy() throws IOException {
		IOTrajectories l = new IOTrajectories();
		l.trajectories().add(initHammy(null));
		l.annotations().put(IONode.CreatedByProgram, "value");

		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		io.write(l, bout, JDKSerializer.class);
		final IONode d = io.read(new ByteArrayInputStream(bout.toByteArray()));
		l = (IOTrajectories) d;
		assertEquals(1, l.trajectories().size());
		final ComputedTrajectorySet c = (ComputedTrajectorySet) l
				.trajectories().get(0);
		assertEquals(7, c.getAnnotations().size());
		assertNotNull(c.getCollider());
		assertNotNull(c.getCollissionDetector());
		assertNotNull(c.getCurler());
		assertNotNull(c.getCurrentPos());
		assertNotNull(c.getCurrentSpeed());
		assertNotNull(c.getCurveStore());
		assertNotNull(c.getInitialPos());
		assertNotNull(c.getInitialSpeed());
		// assertEquals(0, c.getPropertyChangeListeners().length);
	}

	public void testProperties() throws IOException {
		// for (Entry<Object, Object> elem : System.getProperties().entrySet())
		// System.out.println(elem.getKey() + "=" + elem.getValue());
		assertNotNull(System.getProperty("user.name"));
	}
}
