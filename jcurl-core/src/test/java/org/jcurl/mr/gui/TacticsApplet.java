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
import org.jcurl.core.base.Factory;
import org.jcurl.core.model.CurveManagerTest;
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
                return f.toURL();
            } catch (final MalformedURLException e) {
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

        public synchronized void stop() {
            if (running != null) {
                running.interrupt();
                running = null;
            }
            setTime(0);
        }

        private void setTime(long millis) {
            m.getTrajectory().setCurrentTime(1e-3 * millis);
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
            i.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
            i.setMnemonic('S');
            m.add(i);

            i = new JMenuItem(new AbstractAction("Pause") {
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
            // i.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
            i.setMnemonic('o');
            m.add(i);

            return m;
        }
    }

    private static final Log log = LogFactory.getLog(TacticsApplet.class);

    private static final long serialVersionUID = -3501742002653592196L;

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
        CurveManagerTest.initHammy(m.getTrajectory());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
