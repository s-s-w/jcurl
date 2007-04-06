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
package org.jcurl.demo.viewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.TrajectorySet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.swing.PositionDisplay;
import org.xml.sax.SAXException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Controller {

    public static class RealTimeRunner extends Thread {
        private final long dtMillis;

        private final TrajectorySet s;

        private final double timeWarp;

        public RealTimeRunner(final TrajectorySet s, long dt) {
            this.s = s;
            this.dtMillis = dt;
            this.timeWarp = 1.0 * 1e-3;
        }

        public void run() {
            final long start = System.currentTimeMillis();
            final long t0sys = start - (long) (s.getCurrentTime() / timeWarp);
            for (int counter = 0; !isInterrupted();) {
                s.setCurrentTime(timeWarp * (System.currentTimeMillis() - t0sys));
                yield();
                try {
                    final long dt = ++counter * dtMillis
                            - (System.currentTimeMillis() - start);
                    if (dt > 0)
                        sleep(dt);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private static final Log log = JCLoggerFactory.getLogger(Controller.class);

    private long dt = 1000 / 25;

    private boolean paused = false;

    private RealTimeRunner runner;

    private ComputedTrajectorySet src = new CurveManager();

    private boolean stopped = true;

    public synchronized void addTarget(PositionDisplay dst) {
        dst.setPos(src.getCurrentPos());
    }

    public synchronized long getDt() {
        return dt;
    }

    public synchronized long getTime() {
        return (long) (src.getCurrentTime() * 1000);
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }

    public synchronized void load(final File f) throws SAXException,
            IOException {
        load(new FileInputStream(f));
    }

    public synchronized void load(final InputStream f) throws SAXException,
            IOException {
        stop();
        throw new NotImplementedException();
        // FIXME
        //src.loadStart(f);
        //src.setCurrentTime(35.0);
        //src.setCurrentTime(0.0);
    }

    public synchronized void pause() {
        if (isPaused() || isStopped())
            return;
        runner.interrupt();
        runner = null;
        paused = true;
    }

    public synchronized void setDt(long dt) {
        this.dt = dt;
    }

    public synchronized void setTime(long time) {
        if (log.isDebugEnabled())
            log.debug(Long.toString(getTime()) + " / " + Long.toString(time));
        src.setCurrentTime(time * 1e-3);
    }

    public synchronized void start() {
        if (isStopped() || isPaused()) {
            runner = new RealTimeRunner(src, getDt());
            // fork a thread that runs the stuff.
            runner.start();
            paused = false;
            stopped = false;
        }
    }

    public synchronized void stop() {
        if (isStopped())
            return;
        if (isPaused()) {
            paused = false;
        }
        if (runner != null)
            runner.interrupt();
        runner = null;
        setTime(0);
        stopped = true;
    }
}
