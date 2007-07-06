/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005 M. Rohrmoser
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
package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import org.jcurl.core.base.Collider;
import org.jcurl.core.base.CollissionDetector;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.Curler;
import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockDouble;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.AnnoHelp;
import org.jcurl.core.helpers.Unit;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerDenny;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.core.zui.KeyboardZoom;
import org.jcurl.core.zui.PCurveStore;
import org.jcurl.core.zui.PIceFactory;
import org.jcurl.core.zui.PPositionSet;
import org.jcurl.core.zui.PPositionSetDrag;
import org.jcurl.core.zui.PRockFactory;
import org.jcurl.core.zui.PTrajectoryFactory;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MainApp extends JFrame {
    /**
     * Set draw-to-tee time and curl via {@link JSlider}s.
     * 
     * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
     * @version $Id$
     */
    static class IceControl extends JComponent {
        private static final long serialVersionUID = -3381158569292004932L;

        public IceControl(final Curler model) {
            this.setLayout(new BorderLayout());
            JLabel l = new JLabel("Ice");
            this.add(l, BorderLayout.NORTH);
            JSlider s = new JSlider();
            s.setOrientation(JSlider.VERTICAL);
            this.add(s, BorderLayout.WEST);
            s = new JSlider();
            s.setOrientation(JSlider.VERTICAL);
            this.add(s, BorderLayout.CENTER);
        }
    }

    /**
     * Data model for {@link MainPanel}
     */
    static class MainMod implements ZuiMod, RockMod {
        static ComputedTrajectorySet initHammy(ComputedTrajectorySet te) {
            if (te == null)
                te = new CurveManager();
            te.setCollider(new CollissionSpin(0.5, 0.0));
            te.setCollissionDetector(new NewtonCollissionDetector());
            te.setCurler(new CurlerDenny(24, 1));
            te.setInitialPos(PositionSet.allOut());
            te.setInitialSpeed(new SpeedSet(new RockDouble()));
            te.getAnnotations().put(AnnoHelp.HammerK, AnnoHelp.HammerVDark);
            te.getAnnotations().put(AnnoHelp.DarkTeamK, "Scotland");
            te.getAnnotations().put(AnnoHelp.LightTeamK, "Canada");
            te.getAnnotations().put(AnnoHelp.GameK, "Semifinal");
            te.getAnnotations().put(AnnoHelp.EventK,
                    "World Curling Championships");
            te.getAnnotations().put(AnnoHelp.DateK, "1992");
            te.getAnnotations().put(AnnoHelp.LocationK, "Garmisch");
            initHammy(te.getInitialPos(), te.getInitialSpeed());
            return te;
        }

        static void initHammy(final PositionSet p, final SpeedSet s) {
            PositionSet.allOut(p);
            // te.getInitialPos().getLight(1-1).setLocation(
            p.getLight(2 - 1).setLocation(Unit.f2m(-1.170732),
                    Unit.f2m(15.365854), 0);
            p.getLight(3 - 1).setLocation(Unit.f2m(0.292683),
                    Unit.f2m(8.780488), 0);
            p.getLight(4 - 1).setLocation(Unit.f2m(2.195122), Unit.f2m(12), 0);
            p.getLight(5 - 1).setLocation(Unit.f2m(1.463415),
                    Unit.f2m(5.707317), 0);
            p.getLight(6 - 1).setLocation(Unit.f2m(1.463415),
                    Unit.f2m(-2.780488), 0);
            p.getLight(7 - 1).setLocation(Unit.f2m(-0.439024),
                    Unit.f2m(-5.560976), 0);
            p.getLight(8 - 1).setLocation(Unit.f2m(-1.756098),
                    Unit.f2m(-1.609756), 0);
            // p.getDark(1-1).setLocation(
            // p.getDark(2-1).setLocation(
            p.getDark(3 - 1).setLocation(Unit.f2m(0.878049),
                    Unit.f2m(14.341463), 0);
            p.getDark(4 - 1).setLocation(Unit.f2m(-2.634146),
                    Unit.f2m(13.170732), 0);
            p.getDark(5 - 1).setLocation(Unit.f2m(4.536585),
                    Unit.f2m(-0.439024), 0);
            p.getDark(6 - 1).setLocation(Unit.f2m(0.731707),
                    Unit.f2m(-3.95122), 0);
            p.getDark(7 - 1).setLocation(Unit.f2m(-2.780488),
                    Unit.f2m(-4.390244), 0);
            p.getDark(8 - 1).setLocation(Unit.f2m(3.89991), IceSize.HOG_2_TEE,
                    0);
            RockSet.allZero(s);
            s.getDark(8 - 1).setLocation(0, -3, 100 * Math.PI / 180);

            p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
            s.getDark(8 - 1).setLocation(0.188, -3, -100 * Math.PI / 180);

            p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
            s.getDark(8 - 1).setLocation(0.1785, -4, -100 * Math.PI / 180);
            p.notifyChange();
            s.notifyChange();
        }

        private final CurveManager ts = new CurveManager();

        public MainMod() {
            initHammy(ts);
        }

        @Override
        public boolean equals(final Object obj) {
            return ts.equals(obj);
        }

        public Map<String, Object> getAnnotations() {
            return ts.getAnnotations();
        }

        public Rock getBroom() {
            throw new NotImplementedException();
        }

        public Collider getCollider() {
            return ts.getCollider();
        }

        public CollissionDetector getCollissionDetector() {
            return ts.getCollissionDetector();
        }

        public Curler getCurler() {
            return ts.getCurler();
        }

        public PositionSet getCurrentPos() {
            return ts.getCurrentPos();
        }

        public SpeedSet getCurrentSpeed() {
            return ts.getCurrentSpeed();
        }

        public double getCurrentTime() {
            return ts.getCurrentTime();
        }

        public CurveStore getCurveStore() {
            return ts.getCurveStore();
        }

        public PositionSet getInitialPos() {
            return ts.getInitialPos();
        }

        public SpeedSet getInitialSpeed() {
            return ts.getInitialSpeed();
        }

        public int getPlayed() {
            throw new NotImplementedException();
        }

        public double getSplitTime() {
            throw new NotImplementedException();
        }

        @Override
        public int hashCode() {
            return ts.hashCode();
        }

        public void setBroom(final Rock b) {
            throw new NotImplementedException();
        }

        public void setCollider(final Collider collider) {
            ts.setCollider(collider);
        }

        public void setCollissionDetector(
                final CollissionDetector collissionDetector) {
            ts.setCollissionDetector(collissionDetector);
        }

        public void setCurler(final Curler curler) {
            ts.setCurler(curler);
        }

        public void setCurrentTime(final double currentTime) {
            ts.setCurrentTime(currentTime);
        }

        public void setCurveStore(final CurveStore curveStore) {
            ts.setCurveStore(curveStore);
        }

        public void setInitialPos(final PositionSet initialPos) {
            ts.setInitialPos(initialPos);
        }

        public void setInitialSpeed(final SpeedSet initialSpeed) {
            ts.setInitialSpeed(initialSpeed);
        }

        public void setPlayed(final int p) {
            throw new NotImplementedException();
        }

        public void setSplitTime(final double s) {
            throw new NotImplementedException();
        }
    }

    /**
     * Aggregate {@link IceControl}, {@link ZuiPanel}, {@link RockControl}.
     * 
     * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
     * @version $Id$
     */
    static class MainPanel extends JComponent {
        private static final long serialVersionUID = -3700593329890044115L;

        private final IceControl i;

        private final ZuiPanel p;

        private final RockControl r;

        public MainPanel(final MainMod model) {
            setLayout(new BorderLayout());
            this.add(p = new ZuiPanel(model), BorderLayout.CENTER);
            this.add(r = new RockControl(model), BorderLayout.EAST);
            this.add(i = new IceControl(model.getCurler()), BorderLayout.WEST);
        }

        public void center() {
            p.center();
        }
    }

    /**
     * Set played rock, broom position, split time and handle via
     * {@link JSlider}s. etc.
     * 
     * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
     * @version $Id$
     */
    static class RockControl extends JComponent {

        private static final long serialVersionUID = 1286753237795097988L;

        public RockControl(final RockMod model) {
            this.setLayout(new BorderLayout());
            JLabel l = new JLabel("Rock");
            this.add(l, BorderLayout.NORTH);
            JSlider s = new JSlider();
            s.setOrientation(JSlider.VERTICAL);
            this.add(s, BorderLayout.WEST);
            s = new JSlider();
            s.setOrientation(JSlider.VERTICAL);
            this.add(s, BorderLayout.CENTER);
            s = new JSlider();
            s.setOrientation(JSlider.VERTICAL);
            this.add(s, BorderLayout.EAST);
        }
    }

    /**
     * Data model for {@link RockControl}.
     */
    static interface RockMod {
        Rock getBroom();

        int getPlayed();

        double getSplitTime();

        void setBroom(Rock b);

        void setPlayed(int p);

        void setSplitTime(double s);
    }

    /**
     * Data model for {@link ZuiPanel}.
     */
    static interface ZuiMod {

        public abstract PositionSet getCurrentPos();

        public abstract CurveStore getCurveStore();

        public abstract PositionSet getInitialPos();
    }

    /**
     * Graphical display for {@link ZuiMod}.
     */
    static class ZuiPanel extends JComponent {

        private static final long serialVersionUID = -4648771240323713217L;

        private final PNode ice;

        private final PCanvas pico;

        public ZuiPanel(final ZuiMod model) {
            setLayout(new BorderLayout());
            this.add(pico = new PCanvas(), BorderLayout.CENTER);
            pico.setBackground(new Color(0xE8E8FF));
            pico
                    .setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
            pico
                    .setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
            pico.getRoot().getDefaultInputManager().setKeyboardFocus(
                    new KeyboardZoom(pico.getCamera()));

            pico.getLayer().addChild(
                    ice = new PIceFactory.Fancy().newInstance());

            final PCurveStore traj = new PCurveStore(model.getCurveStore(),
                    new PTrajectoryFactory.Fancy(), tmax);
            final PNode initial = new PPositionSet(model.getInitialPos(),
                    new PRockFactory.Fancy());
            initial.addInputEventListener(new PPositionSetDrag() {
                @Override
                public void mouseDragged(final PInputEvent event) {
                    // TODO add undo/redo!
                    super.mouseDragged(event);
                }

                @Override
                public void mouseReleased(final PInputEvent event) {
                    super.mouseReleased(event);
                    traj.sync(tmax);
                }
            });
            final PNode current = new PPositionSet(model.getCurrentPos(),
                    new PRockFactory.Simple());

            ice.addChild(traj);
            ice.addChild(current);
            ice.addChild(initial);
        }

        public void center() {
            pico.getCamera().animateViewToCenterBounds(KeyboardZoom.houseP,
                    true, 1);
        }
    }

    private static final long serialVersionUID = 3398372625156897223L;

    // FIXME What goes wrong if I put here 30?
    private static final double tmax = 15;

    public static void main(final String[] args) {
//        PDebug.debugBounds = true;
//        PDebug.debugPrintUsedMemory = true;
//        PDebug.debugPrintFrameRate = true;
//        PDebug.debugPaintCalls = true;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainApp application = new MainApp();
                application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                application.setSize(800, 600);
                application.setVisible(true);
                application.center();
            }
        });
    }

    private final MainMod m;

    private final MainPanel p;

    public MainApp() {
        m = new MainMod();
        m.setCurrentTime(tmax);
        p = new MainPanel(m);
        getContentPane().add(p);
        // this.setJMenuBar(new MenuFactory().menu(c, this));
    }

    public void center() {
        p.center();
    }
}