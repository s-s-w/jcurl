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

import org.apache.commons.logging.Log;
import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.dto.PositionSet;
import org.jcurl.core.dto.RockSet;
import org.jcurl.core.dto.SpeedSet;
import org.jcurl.core.helpers.JCLoggerFactory;

public class ComputedPathsTest extends TestCase {
    
    private static final Log log = JCLoggerFactory
            .getLogger(ComputedPathsTest.class);

    private int ec = -1;

    private int pos = -1;

    private int vel = -1;

    public void setUp() {
        ec = 0;
        // TODO re-activate Events pos = 2;
        // TODO re-activate Events vel = 128;
        pos = vel = 0;
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

    public void testSimple() throws FunctionEvaluationException {
        ComputedPaths model = new ComputedPaths();
        // model.setInitialPos(PositionSet.allHome());
        // model.setInitialSpeed(new SpeedSet());
        // model.setCollider(new CollissionSpinLoss());
        // model.setIce(new DennyCurves());
        // model.recompute();

        final int r = 0;
        model.getInitialPos().getRock(r).setLocation(0, 10, 0);
        model.getInitialSpeed().getRock(r).setLocation(0, -1, 0);
        model.recompute();

        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (i == r) {
                assertEquals("", 0, model.getInitialPos().getRock(i).getX(),
                        1e-9);
                assertEquals("", 10, model.getInitialPos().getRock(i).getY(),
                        1e-9);
                assertEquals("", 0, model.getInitialPos().getRock(i).getZ(),
                        1e-9);
                assertEquals("", 0, model.getInitialSpeed().getRock(i).getX(),
                        1e-9);
                assertEquals("", -1, model.getInitialSpeed().getRock(i).getY(),
                        1e-9);
                assertEquals("", 0, model.getInitialSpeed().getRock(i).getZ(),
                        1e-9);
            } else {
                ;
            }
            assertEquals("x[" + i + "]", model.getInitialPos().getRock(i),
                    model.getCurrentPos().getRock(i));
            // TODO assertEquals("v[" + i + "]",
            // model.getInitialSpeed().getRock(i),
            // model.getCurrentSpeed().getRock(i));
        }

        model.setCurrentT(0);
        model.setCurrentT(1);
        model.setCurrentT(-1);
        model.setCurrentT(10);
        model.setCurrentT(0);
    }
}
