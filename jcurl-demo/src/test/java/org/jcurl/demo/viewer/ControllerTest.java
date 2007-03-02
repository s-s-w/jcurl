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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;

import junit.framework.TestCase;

import org.jcurl.core.swing.PositionDisplay;
import org.xml.sax.SAXException;

public class ControllerTest extends TestCase {

    private static class RenderThread extends Thread {
        public int counter = 0;

        private final boolean doPaint;

        private final Graphics g;

        private final Component p;

        public RenderThread(Component p, Graphics g, boolean doPaint) {
            this.g = g;
            this.p = p;
            this.doPaint = doPaint;
        }

        public void run() {
            for (counter = 0;; counter++) {
                if (doPaint)
                    p.paint(g);
                else
                    ;//p.repaint();
                yield();
            }
        };
    }

    static URL find(String name) {
        final String[] pattern = { "/setup/#{name}",
                "./config/jcurl.jar/setup/#{name}" };
        for (int i = 0; i < pattern.length; i++) {
            URL tmp = ViewerApp.class.getResource(pattern[i].replaceFirst(
                    "\\#\\{name\\}", name));
            if (tmp != null)
                return tmp;
        }
        return null;
    }

    public void _testFrame() throws InterruptedException, SAXException,
            IOException {
        final PositionDisplay p = new PositionDisplay();
        //final Counted co = new Counted(p);
        final JFrame frame = new JFrame();
        frame.getContentPane().add(p);
        frame.setBounds(0, 0, 800, 400);
        frame.show();

        final long t = 5000;
        final Controller c = new Controller();
        assertNotNull(find("hammy.jcx"));
        c.load(find("hammy.jcx").openStream());
        c.addTarget(p);
        c.start();
        assertTrue(Long.toString(c.getTime()), 0 <= c.getTime()
                && c.getTime() < 100);
        Thread.sleep(t);
        c.pause();
        assertTrue(Long.toString(c.getTime()), t - 100 < c.getTime()
                && c.getTime() < t + 100);
        c.stop();
        assertEquals(0, c.getTime());
        // assertTrue(Long.toString(painter.counter), fps < painter.counter *
        // 1000 / t);
        //System.out.println(painter.counter);
        frame.hide();
    }

    public void testLoad() throws SAXException, IOException,
            InterruptedException {
        final PositionDisplay p = new PositionDisplay();
        final RenderThread painter = new RenderThread(p, new BufferedImage(
                2048, 1024, BufferedImage.TYPE_INT_ARGB).getGraphics(), true);

        final long t = 2500;
        final Controller c = new Controller();
        assertNotNull(find("hammy.jcx"));
        c.load(find("hammy.jcx").openStream());
        c.addTarget(p);
        c.start();
        assertTrue(Long.toString(c.getTime()), 0 <= c.getTime()
                && c.getTime() < 100);
        Thread.sleep(t);
        c.pause();
        assertTrue(Long.toString(c.getTime()), t - 100 < c.getTime()
                && c.getTime() < t + 100);
        c.stop();
        assertEquals(0, c.getTime());
        painter.interrupt();
        //assertTrue(25 < painter.counter);
    }
}
