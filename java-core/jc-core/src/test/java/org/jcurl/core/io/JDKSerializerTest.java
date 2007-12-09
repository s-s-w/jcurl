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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.Annotations;
import org.jcurl.core.helpers.Unit;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerDenny;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;

public class JDKSerializerTest extends TestCase {

    static CurveManager initHammy(CurveManager te) {
        if (te == null)
            te = new CurveManager();
        te.setCollider(new CollissionSpin(0.5, 0.0));
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerDenny(24, 1));
        te.setInitialPos(PositionSet.allOut());
        te.setInitialSpeed(new SpeedSet(new RockDouble()));
        te.getAnnotations().put(Annotations.HammerK, Annotations.HammerVDark);
        te.getAnnotations().put(Annotations.DarkTeamK, "Scotland");
        te.getAnnotations().put(Annotations.LightTeamK, "Canada");
        te.getAnnotations().put(Annotations.GameK, "Semifinal");
        te.getAnnotations().put(Annotations.EventK,
                "World Curling Championships");
        te.getAnnotations().put(Annotations.DateK, "1992");
        te.getAnnotations().put(Annotations.LocationK, "Garmisch");
        initHammy(te.getInitialPos(), te.getInitialSpeed());
        return te;
    }

    static void initHammy(final PositionSet p, final SpeedSet s) {
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
        p.notifyChange();
        s.notifyChange();
    }

    public void testEmpty() throws IOException {
        final JDKSerializer io = new JDKSerializer();
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        io.write(new IODocument(), bout);
        final IODocument d = io.read(new ByteArrayInputStream(bout
                .toByteArray()));
        assertNull(d.getRoot());
        assertNotNull(d.get(IODocument.CreatedByUser));
    }

    public void testHammy() throws IOException {
        final JDKSerializer io = new JDKSerializer();
        final IODocument a = new IODocument();
        IOTrajectories l;
        a.setRoot(l = new IOTrajectories());
        l.trajectories().add(initHammy(null));
        a.put(IODocument.CreatedByProgram, "value");

        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        io.write(a, bout);
        final IODocument d = io.read(new ByteArrayInputStream(bout
                .toByteArray()));
        assertNotNull(d.getRoot());
        l = (IOTrajectories) d.getRoot();
        assertEquals(1, l.trajectories().size());
        final CurveManager c = (CurveManager) l.trajectories().get(0);
        assertEquals(7, c.getAnnotations().size());
        assertNotNull(c.getCollider());
        assertNotNull(c.getCollissionDetector());
        assertNotNull(c.getCurler());
        assertNotNull(c.getCurrentPos());
        assertNotNull(c.getCurrentSpeed());
        assertNotNull(c.getCurveStore());
        assertNotNull(c.getInitialPos());
        assertNotNull(c.getInitialSpeed());
        assertEquals(0, c.getPropertyChangeListeners().length);
    }

    public void testProperties() throws IOException {
        // for (Entry<Object, Object> elem : System.getProperties().entrySet())
        // System.out.println(elem.getKey() + "=" + elem.getValue());
        assertNotNull(System.getProperty("user.name"));
    }
}
