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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.Factory;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.JCurlIO;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.base.JCurlIO.Container;
import org.jcurl.core.helpers.AnnoHelp;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.io.XStreamIO;
import org.jcurl.core.model.CollissionSpin;
import org.jcurl.core.model.CurlerNoCurl;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.model.NewtonCollissionDetector;
import org.jcurl.core.swing.JcxFileChooser;
import org.jcurl.core.swing.PngFileChooser;

public class TacticsApplet extends JApplet {

    static class Controller {
        final Model m;

        final DetailPanel p;

        private Thread running = null;

        private URL url;

        public Controller(final Model m, final DetailPanel p) {
            this.m = m;
            this.p = p;
        }

        public URL exportPng(File f) {
            log.info(f);
            if (f.isDirectory())
                throw new IllegalArgumentException(
                        "Is not a file but a directory: " + f);
            if (!f.getName().endsWith(".png"))
                f = new File(f.getAbsoluteFile() + ".png");
            try {
                p.exportPng(f, "created by " + getClass().getName());
                return f.toURL();
            } catch (final MalformedURLException e) {
                throw new RuntimeException("Unhandled", e);
            } catch (IOException e) {
                throw new RuntimeException("Unhandled", e);
            }
        }

        public File getFile() {
            if (url == null || !"file".equals(url.getProtocol()))
                return null;
            try {
                return new File(url.toURI());
            } catch (final URISyntaxException e) {
                throw new RuntimeException("Unhandled", e);
            }
        }

        public URL getUrl() {
            return url;
        }

        public URL open(final File f) {
            log.info(f);
            if (f.isDirectory())
                throw new IllegalArgumentException(
                        "Is not a file but a directory: " + f);
            try {
                final URL u = f.toURL();
                final JCurlIO io = new XStreamIO();
                final Container co = io.read(u, null);
                switch (co.getTrajectories().length) {
                case 0:
                    return u;
                case 1:
                    m.setTrajectory((ComputedTrajectorySet) co
                            .getTrajectories()[0]);
                    return u;
                default:
                    // TODO show a list? store them all?
                    return u;
                }
            } catch (final IOException e) {
                throw new RuntimeException("Unhandled", e);
            }
        }

        public synchronized void pause() {
            if (running != null) {
                running.interrupt();
                running = null;
            } else {
                play((long) (1e3 * m.getTrajectory().getCurrentTime()));
            }
        }

        public synchronized void play(final long offMillis) {
            log.debug("Play!!");
            if (running != null)
                running.interrupt();
            running = new Thread() {
                @Override
                public void run() {
                    log.debug("run");
                    try {
                        final long t0 = System.currentTimeMillis() - offMillis;
                        for (;;) {
                            final long dt = System.currentTimeMillis() - t0;
                            setTime(dt);
                            if (dt > 25000)
                                return;
                            sleep(20);
                        }
                    } catch (InterruptedException e) {
                        log.debug("Interrupt");
                        return;
                    } finally {
                        log.debug("stop");
                        running = null;
                    }
                }
            };
            running.start();
        }

        public URL save(File f) {
            log.info(f);
            if (f.isDirectory())
                throw new IllegalArgumentException(
                        "Is not a file but a directory: " + f);
            if (!(f.getName().endsWith(".jcx") || f.getName().endsWith(".jcz")))
                f = new File(f.getAbsoluteFile() + ".jcz");
            try {
                return f.toURL();
            } catch (final MalformedURLException e) {
                throw new RuntimeException("Unhandled", e);
            }
        }

        private void setTime(long millis) {
            m.getTrajectory().setCurrentTime(1e-3 * millis);
        }

        public synchronized void stop() {
            if (running != null) {
                running.interrupt();
                running = null;
            }
            setTime(0);
        }
    }

    static class MenuFactory implements Factory {
        public JMenu file(final Controller c, final Component parent) {
            final JMenu m = new JMenu("File");
            m.setMnemonic('F');
            JMenuItem i = null;

            i = new JMenuItem(new AbstractAction("Open") {
                private static final long serialVersionUID = -645032579622467308L;

                public void actionPerformed(final ActionEvent arg0) {
                    final JcxFileChooser fc = new JcxFileChooser(c.getFile());
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
                    final PngFileChooser fc = new PngFileChooser(c.getFile());
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
            });
            i.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
            i.setMnemonic('E');
            m.add(i);

            return m;
        }

        public JMenu help(final Controller c, final Component parent) {
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

        public JMenuBar menu(final Controller c, final Component parent) {
            final JMenuBar b = new JMenuBar();
            b.add(file(c, parent));
            b.add(play(c, parent));
            b.add(help(c, parent));
            return b;
        }

        public JMenu play(final Controller c, final Component parent) {
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

    private static final Log log = LogFactory.getLog(TacticsApplet.class);

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

    final Controller c;

    final Model m = new Model();

    final DetailPanel p = new DetailPanel(m);

    public TacticsApplet() {
        c = new Controller(m, p);
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
        TacticsApplet.initHammy(m.getTrajectory());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
