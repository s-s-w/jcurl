/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.TestCase;

import org.apache.commons.math.FunctionEvaluationException;

public class ComputedPathsTest extends TestCase {

    private int ec = -1;

    private int pos = -1;

    private int vel = -1;

    public void setUp() {
        ec = 0;
        pos = 2;
        vel = 128;
    }

    public void testNoneMoving() throws FunctionEvaluationException,
            InterruptedException {
        final ComputedPaths cp = new ComputedPaths();
        assertEquals(0, ec);
        cp.getCurrentPos().addPropertyChangeListener(
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        assertEquals("rock", e.getPropertyName());
                        assertTrue(e.getSource() instanceof PositionSet);
                        ec += pos;
                    }
                });
        cp.getCurrentSpeed().addPropertyChangeListener(
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        assertEquals("rock", e.getPropertyName());
                        assertTrue(e.getSource() instanceof SpeedSet);
                        ec += vel;
                    }
                });
        assertEquals(0, ec);
        assertEquals("", 0.0, cp.getCurrentT(), 1e-9);
        cp.setCurrentT(1);
        // Strange! Eclipse fails here, but maven does it right!
        assertEquals((vel + pos), ec);
        assertEquals("", 1.0, cp.getCurrentT(), 1e-9);
        cp.setCurrentT(Double.MAX_VALUE);
        assertEquals((vel + pos) * 2, ec);
        assertEquals("", Double.MAX_VALUE, cp.getCurrentT(), 1e-9);
        cp.setCurrentT(Double.POSITIVE_INFINITY);
        assertEquals((vel + pos) * 3, ec);
        assertEquals("", Double.POSITIVE_INFINITY, cp.getCurrentT(), 1e-9);
    }

    public void testOneMovingEvents() throws FunctionEvaluationException {
        final ComputedPaths cp = new ComputedPaths();
        assertEquals(0, ec);
        cp.getCurrentPos().addPropertyChangeListener(
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        assertEquals("rock", e.getPropertyName());
                        assertTrue(e.getSource() instanceof PositionSet);
                        ec += pos;
                    }
                });
        cp.getCurrentSpeed().addPropertyChangeListener(
                new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent e) {
                        assertEquals("rock", e.getPropertyName());
                        assertTrue(e.getSource() instanceof SpeedSet);
                        ec += vel;
                    }
                });
        assertEquals(0, ec);
        cp.getInitialSpeed().getLight(0).setY(-1);
        cp.getInitialSpeed().notifyChange();
        assertEquals((vel + pos) * 1, ec);
        cp.setCurrentT(1);
        assertEquals((vel + pos) * 2, ec);
        assertEquals("", 1.0, cp.getCurrentT(), 1e-9);
        cp.setCurrentT(Double.MAX_VALUE);
        assertEquals((vel + pos) * 3, ec);
        assertEquals("", Double.MAX_VALUE, cp.getCurrentT(), 1e-9);
        cp.setCurrentT(Double.POSITIVE_INFINITY);
        assertEquals((vel + pos) * 4, ec);
        assertEquals("", Double.POSITIVE_INFINITY, cp.getCurrentT(), 1e-9);
    }
}
