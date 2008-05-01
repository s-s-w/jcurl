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
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;

import javolution.testing.AssertionException;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
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
import org.jcurl.core.log.JCLoggerFactory;

public class JDKSerializerTest extends TestCase {

	private static final Log log = JCLoggerFactory
			.getLogger(JDKSerializerTest.class);

	static CurveManager initHammy(CurveManager te) {
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

	public void testEmpty() throws IOException {
		final JDKSerializer io = new JDKSerializer();
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		io.write(new IOGroup(), bout);
		final IOGroup d = (IOGroup) io.read(new ByteArrayInputStream(bout
				.toByteArray()));
		assertEquals(0, d.children().size());
		assertNotNull(d.annotations().get(IONode.CreatedByUser));
	}

	public void testHammy() throws IOException {
		final JDKSerializer io = new JDKSerializer();
		IOTrajectories l = new IOTrajectories();
		l.trajectories().add(initHammy(null));
		l.annotations().put(IONode.CreatedByProgram, "value");

		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		io.write(l, bout);
		final IONode d = io.read(new ByteArrayInputStream(bout.toByteArray()));
		l = (IOTrajectories) l;
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

	public void testRead() throws IOException {
		final URL u = JCurlSerializerTest.class
				.getResource("/hammy-scotch-takeout.jcz");
		assertNotNull(u);
		final IONode n = new JCurlSerializer().read(u);
		assertNotNull(n);
	}

	public void testSerialVersionUID() {
		final String[][] ser1 = {
				{ "org.jcurl.math.Distance2DSq", "0" },
				{ "org.jcurl.math.R1R1Function", "0" },
				{ "org.jcurl.math.R1RNFunctionImpl", "0" },
				{ "org.jcurl.math.R1RNFunction", "0" },
				{ "org.jcurl.math.CurveCombined$Part", "0" },
				{ "org.jcurl.math.CurveCombined", "0" },
				{ "org.jcurl.math.BisectionSolver", "0" },
				{ "org.jcurl.math.Polynome", "0" },
				{ "org.jcurl.math.CurveFkt", "0" },
				{ "org.jcurl.math.CSplineInterpolator", "0" },
				{ "org.jcurl.math.MathVec", "0" },
				{ "org.jcurl.math.NewtonSimpleSolver", "0" },
				{ "org.jcurl.math.PolynomeCurve", "0" },
				{ "org.jcurl.math.CurveShape", "0" },
				{ "org.jcurl.core.helpers.FilterIterator", "0" },
				{ "org.jcurl.core.helpers.MergedIterator$1", "0" },
				{ "org.jcurl.core.helpers.MergedIterator$2", "0" },
				{ "org.jcurl.core.helpers.MergedIterator", "0" },
				{ "org.jcurl.core.helpers.PeekIterator", "0" },
				{ "org.jcurl.core.helpers.PropModelHelper", "0" },
				{ "org.jcurl.core.helpers.AnnoHelper", "0" },
				{ "org.jcurl.core.helpers.Version", "0" },
				{ "org.jcurl.core.helpers.NotImplementedYetException", "0" },
				{ "org.jcurl.core.helpers.Service", "0" },
				{ "org.jcurl.core.api.RockType$Acc", "0" },
				{ "org.jcurl.core.api.RockType$Pos", "0" },
				{ "org.jcurl.core.api.RockType$Vel", "0" },
				{ "org.jcurl.core.api.RockType", "0" },
				{ "org.jcurl.core.api.PropModel", "0" },
				{ "org.jcurl.core.api.Measure", "0" },
				{ "org.jcurl.core.api.Collider", "0" },
				{ "org.jcurl.core.api.Strategy", "0" },
				{ "org.jcurl.core.api.Rock$ImmutableRock", "0" },
				{ "org.jcurl.core.api.Rock$RockPoint", "0" },
				{ "org.jcurl.core.api.Rock$1", "0" },
				{ "org.jcurl.core.api.Rock", "0" },
				{ "org.jcurl.core.api.IChangeSupport", "0" },
				{ "org.jcurl.core.api.RockSet$1", "0" },
				{ "org.jcurl.core.api.RockSet", "0" },
				{ "org.jcurl.core.api.ChangeSupport", "0" },
				{ "org.jcurl.core.api.Unit", "0" },
				{ "org.jcurl.core.api.EnumBase$HashCodeComp", "0" },
				{ "org.jcurl.core.api.EnumBase$1", "0" },
				{ "org.jcurl.core.api.EnumBase", "0" },
				{ "org.jcurl.core.api.CollissionDetector", "0" },
				{ "org.jcurl.core.api.MutableObject", "0" },
				{ "org.jcurl.core.api.TransferObject", "0" },
				{ "org.jcurl.core.api.IPropertyChangeSupport", "0" },
				{ "org.jcurl.core.api.PropertyChangeSupport", "0" },
				{ "org.jcurl.core.api.CurveRock", "0" },
				{ "org.jcurl.core.api.RockSetProps", "0" },
				{ "org.jcurl.core.api.RockProps", "0" },
				{ "org.jcurl.core.api.StopDetector", "0" },
				{ "org.jcurl.core.api.ComputedTrajectorySet", "0" },
				{ "org.jcurl.core.api.TrajectorySet", "0" },
				{ "org.jcurl.core.api.Curler", "0" },
				{ "org.jcurl.core.api.Factory", "0" },
				{ "org.jcurl.core.api.CurveStore", "0" },
				{ "org.jcurl.core.api.PositionSet", "0" },
				{ "org.jcurl.core.api.WeakHashSet", "0" },
				{ "org.jcurl.core.api.Physics", "0" },
				{ "org.jcurl.core.api.IceSize", "0" },
				{ "org.jcurl.core.api.RockDouble", "0" },
				{ "org.jcurl.core.impl.CollissionSpinLoss", "0" },
				{ "org.jcurl.core.impl.ColliderBase", "0" },
				{ "org.jcurl.core.impl.PropModelImpl", "0" },
				{ "org.jcurl.core.impl.NewtonCollissionDetector", "0" },
				{ "org.jcurl.core.impl.CollissionDetectorBase", "0" },
				{ "org.jcurl.core.impl.CurveRockAnalytic", "0" },
				{ "org.jcurl.core.impl.NewtonStopDetector", "0" },
				{ "org.jcurl.core.impl.CollissionStore$Tupel", "0" },
				{ "org.jcurl.core.impl.CollissionStore$TupelComp", "0" },
				{ "org.jcurl.core.impl.CollissionStore", "0" },
				{ "org.jcurl.core.impl.CurveManager", "0" },
				{ "org.jcurl.core.impl.CurveTransformed", "0" },
				{ "org.jcurl.core.impl.CoulombCurler", "0" },
				{ "org.jcurl.core.impl.CurlerBase$1", "0" },
				{ "org.jcurl.core.impl.CurlerBase$2", "0" },
				{ "org.jcurl.core.impl.CurlerBase", "0" },
				{ "org.jcurl.core.impl.CollissionSimple", "0" },
				{ "org.jcurl.core.impl.CurlerNoCurl", "0" },
				{ "org.jcurl.core.impl.StoredTrajectorySet", "0" },
				{ "org.jcurl.core.impl.CurveStoreImpl$1", "0" },
				{ "org.jcurl.core.impl.CurveStoreImpl", "0" },
				{ "org.jcurl.core.impl.CollissionSpin", "0" },
				{ "org.jcurl.core.impl.CurlerDenny$1", "0" },
				{ "org.jcurl.core.impl.CurlerDenny", "0" },
				{ "org.jcurl.core.impl.CurveStill", "0" },
				{ "org.jcurl.core.ui.Orientation", "0" },
				{ "org.jcurl.core.ui.Zoomer", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$Current", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$ExecutorDelegate", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$ForkableFixed", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$ForkableFlex", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$Parallel", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$Single", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$SmartQueue", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$SwingEDT", "0" },
				{ "org.jcurl.core.ui.TaskExecutor$Task", "0" },
				{ "org.jcurl.core.ui.TaskExecutor", "0" },
				{ "org.jcurl.core.ui.FixpointZoomer", "0" },
				{ "org.jcurl.core.ui.UndoRedoDocumentBase", "0" },
				{ "org.jcurl.core.ui.UndoRedoDocument", "0" },
				{ "org.jcurl.core.ui.BroomPromptModel", "0" },
				{ "org.jcurl.core.io.JCurlSerializer$Engine", "0" },
				{ "org.jcurl.core.io.JCurlSerializer", "0" },
				{ "org.jcurl.core.io.IONode", "0" },
				{ "org.jcurl.core.io.IOTrajectories", "0" },
				{ "org.jcurl.core.io.IOGroup", "0" },
				{ "org.jcurl.core.io.JDKSerializer", "0" },
				{ "org.jcurl.core.log.JCLoggerFactory", "0" },
				{ "org.jcurl.core.jnlp.FileDialogService$Contents", "0" },
				{ "org.jcurl.core.jnlp.FileDialogService$ContentsBuffer", "0" },
				{ "org.jcurl.core.jnlp.FileDialogService$ContentsFile", "0" },
				{ "org.jcurl.core.jnlp.FileDialogService$OpenService", "0" },
				{ "org.jcurl.core.jnlp.FileDialogService$SaveService", "0" },
				{ "org.jcurl.core.jnlp.FileDialogService", "0" },
				{ "org.jcurl.core.jnlp.FileDialogWebstart$1", "0" },
				{ "org.jcurl.core.jnlp.FileDialogWebstart", "0" },
				{ "org.jcurl.core.jnlp.FileDialogSwing$1", "0" },
				{ "org.jcurl.core.jnlp.FileDialogSwing", "0" } };
		final String[][] ser = {
				{ "org.jcurl.core.api.Collider", null },
				{ "org.jcurl.core.api.CollissionDetector", null },
				{ "org.jcurl.core.api.Curler", null },
				{ "org.jcurl.core.api.Unit", "6779663806431722367" },
				{ "org.jcurl.core.api.EnumBase", null },
				{ "org.jcurl.core.api.EnumBase$HashCodeComp", null },
				{ "org.jcurl.core.api.Measure", "-958212044733309378" },
				{ "org.jcurl.core.api.MutableObject", null },
				{ "org.jcurl.core.api.Rock", null },
				{ "org.jcurl.core.api.Rock$ImmutableRock",
						"3485638632856914198" },
				{ "org.jcurl.core.api.Rock$RockPoint", null },
				{ "org.jcurl.core.api.RockDouble", "2337028316325540776" },
				{ "org.jcurl.core.api.RockSet", "-7154547850436886952" },
				{ "org.jcurl.core.api.TransferObject", null },
				{ "org.jcurl.core.api.Unit", "6779663806431722367" },
				{ "org.jcurl.core.impl.ColliderBase", null },
				{ "org.jcurl.core.impl.CollissionDetectorBase", null },
				{ "org.jcurl.core.impl.CollissionSpin", "8103077481042211458" },
				{ "org.jcurl.core.impl.CoulombCurler", null },
				{ "org.jcurl.core.impl.CurlerBase", "-3873001715024033329" },
				{ "org.jcurl.core.impl.CurlerDenny", "9048729754646886751" },
				{ "org.jcurl.core.impl.CurveManager", "7198540442889130378" },
				{ "org.jcurl.core.impl.NewtonCollissionDetector",
						"-3370270087945653006" },
				{ "org.jcurl.core.impl.PropModelImpl", null },
				{ "org.jcurl.core.io.IONode", "-4734020637823903908" },
				{ "org.jcurl.core.io.IOTrajectories", "-8243459215398281867" } };
		for (final String[] elem : ser)
			try {
				final Class<?> c = Class.forName(elem[0]);
				if (elem[1] == null) {
					if (Serializable.class.isAssignableFrom(c))
						log.warn(elem[0] + " shouldn't be Serializable");
					try {
						if (c.getDeclaredField("serialVersionUID") != null)
							log.error(elem[0]
									+ " shouldn't have a serialVersionUID");
					} catch (NoSuchFieldException e) {}
				} else {
					final Field f = c.getDeclaredField("serialVersionUID");
					f.setAccessible(true);
					assertEquals(elem[0], Long.parseLong(elem[1]), f
							.getLong(null));
				}
			} catch (final ClassNotFoundException e) {
				throw new AssertionException(elem[0], null, e);
			} catch (final SecurityException e) {
				throw new AssertionException(elem[0], null, e);
			} catch (final NoSuchFieldException e) {
				throw new AssertionException(elem[0], null, e);
			} catch (final IllegalArgumentException e) {
				throw new AssertionException(elem[0], null, e);
			} catch (final IllegalAccessException e) {
				throw new AssertionException(elem[0], null, e);
			}
	}
}
