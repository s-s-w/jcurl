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
package org.jcurl.core.curved;

import java.util.Collections;
import java.util.LinkedList;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.SpeedSet;
import org.jcurl.core.math.MathException;
import org.jcurl.core.math.NewtonSolver;
import org.jcurl.core.math.R1R1Function;
import org.jcurl.core.math.R1R1Solver;

/**
 * Detect collissions and store the paths.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CollissionStore {
    public class HitTupel implements Comparable {
        final int a;

        final int b;

        private R1R1Solver solver;

        private double time;

        public HitTupel(int a, int b) {
            this.a = a;
            this.b = b;
            reset();
        }

        public int compareTo(Object arg0) {
            final HitTupel a = this;
            final HitTupel b = (HitTupel) arg0;
            if (a.time < b.time)
                return -1;
            if (a.time > b.time)
                return 1;
            return 0;
        }

        double getTime() {
            return time;
        }

        public void reset() {
            solver = null;
            time = Double.NaN;
        }

        public void solve(double min, double max) throws MathException {
            time = solver.solve(min, max, min + solver.getAbsoluteAccuracy());
        }
    }

    private final CurvePiecewise[] curves;

    private final LinkedList hits;

    private double knownTime = Double.NaN;

    private final HitTupel[] store;

    public CollissionStore() {
        this.curves = new CurvePiecewise[RockSet.ROCKS_PER_SET];
        this.store = new HitTupel[RockSet.ROCKS_PER_SET * RockSet.ROCKS_PER_SET];
        this.hits = new LinkedList();
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            curves[i] = new CurvePiecewise();
            for (int j = i - 1; j >= 0; j--)
                hits.add(store[idx(i, j)] = new HitTupel(i, j));
        }
    }

    private HitTupel cell(int ai, int bi) {
        return store[idx(ai, bi)];
    }

    /**
     * compute the two new trajectories and delegate to
     * {@link #change(double, double, int, CurveTransformed, int, CurveTransformed)}.
     * 
     * @see CurlerCurved#computeWC(double, Rock, Rock)
     * @param c
     * @param min
     * @param max
     * @param ia
     * @param xa
     * @param va
     * @param ib
     * @param xb
     * @param vb
     * @throws MathException
     */
    public void change(final CurlerCurved c, final double min,
            final double max, final int ia, final Rock xa, final Rock va,
            final int ib, final Rock xb, final Rock vb) throws MathException {
        final CurveTransformed ca = c.computeWC(min, xa, va);
        final CurveTransformed cb = c.computeWC(min, xb, vb);
        change(min, max, ia, ca, ib, cb);
    }

    void change(final double min, final double max, final int ia,
            final CurveTransformed ac, final int ib, final CurveTransformed bc)
            throws MathException {
        curve(ia).add(ac);
        curve(ib).add(bc);
        createComputeSolver(ia, ib, min, max);
        createComputeSolver(ib, ib, min, max);
        Collections.sort(hits);
        if (max > knownTime)
            knownTime = max;
    }

    public void clear() {
        knownTime = Double.NaN;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--)
                cell(i, j).reset();
            curves[i].clear();
        }
    }

    private HitTupel createSolver(int ia, int ib) {
        R1R1Function distSq = new DistanceSq(curve(ia), curve(ib));
        final HitTupel ht = cell(ia, ib);
        ht.solver = new NewtonSolver(distSq);
        return ht;
    }

    public CurvePiecewise curve(int i) {
        return curves[i];
    }

    /**
     * Re-compute all combinations of ia, except with itself and ib
     */
    private void createComputeSolver(final int ia, final int ib,
            final double min, final double max) throws MathException {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--) {
            if (i == ia || i == ib)
                continue;
            createSolver(ia, i).solve(min, max);
        }
    }

    public HitTupel getNextHit(double t) throws MathException {
        solveUntil(t);
        return (HitTupel) hits.getFirst();
    }

    private int idx(int ia, int ib) {
        if (ia == ib)
            throw new IllegalArgumentException();
        if (ia < ib) {
            final int tmp = ia;
            ia = ib;
            ib = tmp;
        }
        return ia + RockSet.ROCKS_PER_COLOR * ib;
    }

    public void init(final CurlerCurved c, final PositionSet x, final SpeedSet v)
            throws MathException {
        for (int ia = RockSet.ROCKS_PER_SET - 1; ia >= 0; ia--) {
            curve(ia).clear();
            curve(ia).add(c.computeWC(0, x.getRock(ia), v.getRock(ia)));
        }
        for (int ia = RockSet.ROCKS_PER_SET - 1; ia >= 0; ia--)
            for (int ib = ia - 1; ib >= 0; ib--)
                createSolver(ia, ib);
        knownTime = Double.NaN;
    }

    /**
     * @param max
     * @throws MathException
     */
    private void solveUntil(double max) throws MathException {
        if (max <= knownTime)
            return;
        for (int ia = RockSet.ROCKS_PER_SET - 1; ia >= 0; ia--)
            for (int ib = ia - 1; ib >= 0; ib--)
                cell(ia, ib).solve(knownTime, max);
        knownTime = max;
        Collections.sort(hits);
    }

}
