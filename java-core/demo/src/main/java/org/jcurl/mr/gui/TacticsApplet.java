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
package org.jcurl.mr.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.Factory;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.helpers.AnnoHelp;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.io.FileDialogService;
import org.jcurl.core.io.FileDialogService.Contents;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerNoCurl;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.core.swing.JcxFileChooser;
import org.jcurl.core.swing.PngFileChooser;

/**
 * Demonstration.
 * <p>
 * TODO Paint intial Position regardless of current time (maybe as shades)
 * </p>
 * <p>
 * TODO Paint trajectories.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: TacticsApplet.java 576 2007-05-30 21:39:40Z mrohrmoser $
 */
public class TacticsApplet extends JApplet {

    static class MenuFactory implements Factory {
        public JMenu file(final TacticsController c, final Component parent) {
            final JMenu m = new JMenu("File");
            m.setMnemonic('F');
            JMenuItem i = null;

            i = new JMenuItem(new AbstractAction("Open") {
                private static final long serialVersionUID = -645032579622467308L;

                public void actionPerformed(final ActionEvent arg0) {
                    if (true) {
                        final FileDialogService fs = new FileDialogService();
                        final Contents cs = fs.openFileDialog(c.getPathHint(),
                                c.getJcxExt(), parent);
                        if (cs != null) {
                            if (log.isDebugEnabled()) {
                                log.debug(cs.getInputStream());
                                log.debug(cs.getName());
                                log.debug(cs.getOutputStream(false));
                                log.debug(cs.canRead());
                                log.debug(cs.canWrite());
                            }
                            log.info("TODO");// TODO
                            c.open(cs);
                        }
                    } else {
                        final JcxFileChooser fc = new JcxFileChooser(c
                                .getFile());
                        final int ret = fc.showOpenDialog(parent);
                        switch (ret) {
                        case 0:
                            log.debug(c.open(fc.getSelectedFile()));
                            break;
                        case 1:
                            break;
                        default:
                            log.warn("Ignored Dialog Result " + ret);
                        }
                    }
                }
            });
            i.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
            i.setMnemonic('O');
            m.add(i);

            i = new JMenuItem(new AbstractAction("Save") {
                private static final long serialVersionUID = -645032579622467308L;

                public void actionPerformed(final ActionEvent arg0) {
                    final JcxFileChooser fc = new JcxFileChooser(c.getFile());
                    final int ret = fc.showSaveDialog(parent);
                    switch (ret) {
                    case 0:
                        log.debug(c.save(fc.getSelectedFile()));
                        break;
                    case 1:
                        break;
                    default:
                        log.warn("Ignored Dialog Result " + ret);
                    }
                }
            });
            i.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
            i.setMnemonic('S');
            m.add(i);

            i = new JMenuItem(new AbstractAction("Export Png") {
                private static final long serialVersionUID = -645032579622467308L;

                public void actionPerformed(final ActionEvent arg0) {
                    if (true) {
                        final byte[] b;
                        try {
                            final ByteArrayOutputStream bout = new ByteArrayOutputStream();
                            c.exportPng(bout);
                            bout.close();
                            b = bout.toByteArray();
                        } catch (final IOException e) {
                            throw new RuntimeException("Unhandled", e);
                        }
                        final FileDialogService fs = new FileDialogService();
                        final Contents cs = fs.saveFileDialog(c.getPathHint(),
                                c.getPngExt(), new ByteArrayInputStream(b),
                                "jcurl.png", parent);
                        log.debug(cs);
                        if (cs != null)
                            if (log.isDebugEnabled()) {
                                log.debug(cs.getInputStream());
                                log.debug(cs.getName());
                                log.debug(cs.getOutputStream(false));
                                log.debug(cs.canRead());
                                log.debug(cs.canWrite());
                            }
                    } else {
                        final PngFileChooser fc = new PngFileChooser(c
                                .getFile());
                        final int ret = fc.showSaveDialog(parent);
                        switch (ret) {
                        case 0:
                            log.debug(c.exportPng(fc.getSelectedFile()));
                            break;
                        case 1:
                            break;
                        default:
                            log.warn("Ignored Dialog Result " + ret);
                        }
                    }
                }
            });
            i.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
            i.setMnemonic('E');
            m.add(i);

            return m;
        }

        public JMenu help(final TacticsController c, final Component parent) {
            final JMenu m = new JMenu("Help");
            m.setMnemonic('H');
            JMenuItem i = null;

            i = new JMenuItem(new AbstractAction("About") {
                private static final long serialVersionUID = -645032579622467308L;

                public void actionPerformed(final ActionEvent arg0) {
                    log.debug(arg0);
                }
            });
            i.setMnemonic('A');
            m.add(i);

            return m;
        }

        public JMenuBar menu(final TacticsController c, final Component parent) {
            final JMenuBar b = new JMenuBar();
            b.add(file(c, parent));
            b.add(play(c, parent));
            b.add(help(c, parent));
            return b;
        }

        public JMenu play(final TacticsController c, final Component parent) {
            final JMenu m = new JMenu("Play");
            m.setMnemonic('P');
            JMenuItem i = null;

            i = new JMenuItem(new AbstractAction("Start") {
                private static final long serialVersionUID = -645032579622467308L;

                public void actionPerformed(final ActionEvent arg0) {
                    c.play(0);
                }
            });
            i.setMnemonic('S');
            m.add(i);

            i = new JMenuItem(new AbstractAction("Pause/Play") {
                private static final long serialVersionUID = -645032579622467308L;

                public void actionPerformed(final ActionEvent arg0) {
                    c.pause();
                }
            });
            i.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
            i.setMnemonic('P');
            m.add(i);

            i = new JMenuItem(new AbstractAction("Stop") {
                private static final long serialVersionUID = -645032579622467308L;

                public void actionPerformed(final ActionEvent arg0) {
                    c.stop();
                }
            });
            i.setMnemonic('o');
            m.add(i);

            return m;
        }
    }

    private static final Log log = JCLoggerFactory
            .getLogger(TacticsApplet.class);

    private static final long serialVersionUID = -3501742002653592196L;

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

    private final TacticsController c;

    private final Model m;

    private final DetailPanel p;

    public TacticsApplet() {
        m = new Model();
        p = new DetailPanel(m);
        c = new TacticsController(this, p, m);
        getContentPane().add(p);
        final MenuFactory mf = new MenuFactory();
        setJMenuBar(mf.menu(c, this));
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
        if (true)
            TacticsApplet.initHammy(m.getTrajectory());
        else
            try {
                final Enumeration<URL> en = this.getClass().getClassLoader()
                        .getResources("hammy.jcz");
                if (en.hasMoreElements())
                    c.open(en.nextElement());
            } catch (final IOException e) {
                TacticsApplet.initHammy(m.getTrajectory());
            }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
