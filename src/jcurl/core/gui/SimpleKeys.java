/*
 * jcurl curling simulation framework 
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
package jcurl.core.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import jcurl.core.RockSet;
import jcurl.core.Source;
import jcurl.core.TargetDiscrete;

/**
 * A first, simple keyboard input class. Uses a
 * {@link jcurl.core.gui.RealTimePlayer}to play.
 * 
 * @see jcurl.core.gui.SimpleMain
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SimpleKeys implements KeyListener {

    private final Source src;

    private final TargetDiscrete dst;

    private final RealTimePlayer player;

    private Thread worker = null;

    public SimpleKeys(final Source src, final TargetDiscrete dst) {
        this.src = src;
        this.dst = dst;
        player = new RealTimePlayer(src.getMinT(), 1.0, src, dst);
        // push the initial state from src to dst
        final long t0 = src.getMinT();
        dst.setPos(t0, src.getPos(t0, new RockSet()));
    }

    public void keyPressed(KeyEvent e) {
        ; // nop
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_SPACE:
            if (worker == null || !worker.isAlive()) {
                worker = new Thread(player, player.getClass().getName());
                worker.start();
            } else {
                worker.interrupt();
                worker = null;
            }
            break;
        case KeyEvent.VK_LEFT:
            player.setTimeScale(-1);
            break;
        case KeyEvent.VK_RIGHT:
            player.setTimeScale(1);
            break;
        }
    }

    public void keyTyped(KeyEvent e) {
        ; // nop
    }
}