/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.core.impl;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Map;

import org.jcurl.core.api.Measure;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.helpers.PropModelHelper;
import org.jcurl.math.MathVec;

/**
 * A hitter including spin and loss of energy.
 * <p>
 * This hitter gets the transfer of spin and momentum right. The 'loss of
 * energy'-mechanism bases upon a Hookisch elasticity-model for the rocks. Only
 * stillstanding rocks cause a loss of energy. For details see the paper <a
 * href="http://jcurl.berlios.de/curlsci.pdf">Curling Scientific </a>.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CollissionSpinLoss.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CollissionSpinLoss extends ColliderBase {
    private static final double HIT_MAX_DIST = 1e-6;

    private static final double INERTIA = RockProps.DEFAULT.getInertia();

    private static final double MASS = RockProps.DEFAULT.getMass();

    private static final double RADIUS = RockProps.DEFAULT.getRadius();

    private static final long serialVersionUID = 8282468221793955034L;

    protected static double fabs(final double a) {
        return Math.abs(a);
    }

    private transient double mu;

    private transient double U;

    public CollissionSpinLoss() {
        setLoss(0);
        setFricRockRock(0);
    }

    public boolean compute(final Rock xa, final Rock xb, final Rock va,
            final Rock vb) {
        // vector from a's center to b's:
        final Point2D r = MathVec.sub(xb, xa, null);
        double tmp = MathVec.abs2D(r);
        if (!(va.isNotZero() || vb.isNotZero())
                || tmp > RADIUS + RADIUS + HIT_MAX_DIST)
            return false;
        // boolean f;
        // if (true == (va.nonZero() ^ vb.nonZero()))
        // if (false != (f = vb.nonZero()))
        // tmp = -tmp;
        if (true == (va.isNotZero() ^ vb.isNotZero()))
            if (false != vb.isNotZero())
                tmp = -tmp;

        // get the coordinate-system:
        final Point2D eY = MathVec.mult(1 / tmp, r, null);
        final Point2D eX = new Point2D.Double(eY.getY(), -eY.getX());

        // from here you can reuse 'r' and 'tmp'
        final double[] flat = { eX.getX(), eX.getY(), eY.getX(), eY.getY() };
        final AffineTransform mat = new AffineTransform(flat);

        // do the coordinate-trafo world->new:
        // va[0] = eX[0] * Va[0] + eX[1] * Va[1];
        // va[1] = eY[0] * Va[0] + eY[1] * Va[1];
        final Rock _va = new RockDouble();
        final Rock _vb = new RockDouble();
        mat.transform(va, _va);
        mat.transform(vb, _vb);
        final double[] w = { va.getA(), vb.getA() };

        if (va.isNotZero() ^ vb.isNotZero())
            singleLoss(_va, _vb, w);
        singleNoLoss(_va, _vb, w);

        va.setA(w[0]);
        vb.setA(w[1]);

        try {
            // re-transformation: do the coordinate-trafo new->world:
            // Va[0] = eX[0] * va[0] + eY[0] * va[1];
            // Va[1] = eX[1] * va[0] + eY[1] * va[1];
            mat.inverseTransform(_va, va);
            mat.inverseTransform(_vb, vb);
        } catch (final NoninvertibleTransformException e) {
            throw new RuntimeException("matrix MUST be invertible.", e);
        }
        return true;
    }

    @Override
    public void computeCC(final Rock va, final Rock vb) {
        // TODO Auto-generated method stub

    }

    void init(final double fritionRockRock, final double loss) {
        setFricRockRock(fritionRockRock);
        setLoss(loss);
    }

    public void init(final Map<CharSequence, Measure> params) {
        internalInit(params);
        init(PropModelHelper.getFrictionRockRock(this.params), PropModelHelper
                .getLoss(this.params));
    }

    private double Loss() {
        return U;
    }

    /**
     * The friction rock/rock. Set the parameter for friction rock/rock.
     * 
     * @param v
     *            the value
     */
    public void setFricRockRock(final double v) {
        mu = v;
    }

    /**
     * The loss of energy on raises. Set the parameter for the loss of energy
     * raises suffer.
     * 
     * @param v
     *            [Joule] the value
     */
    public void setLoss(final double v) {
        U = v / MASS;
    }

    void singleLoss(final Point2D va, final Point2D vb, final double[] w) {
        final double I = 2.0 * (1.0 / MASS + MathVec.sqr(RADIUS) / INERTIA);
        final double FHdivOmega = Math.sqrt(2.0 * MASS * Loss());

        // A's real surface-speed:
        final double Veff = va.getX() - RADIUS * w[0];

        // t0 is the time when A's effective surface speed becomes 0 (=B's)
        double cost0;
        cost0 = 1.0 - Math.abs(Veff / (mu * va.getY() * I * MASS));
        if (0.0 > cost0)
            cost0 = 0.0;
        assert cost0 <= 1.0;

        final Point2D dv = new Point2D.Double(Math.signum(Veff) * mu, 1.0);
        // sint1 is the time when the Hook-force equals the friction.
        double sint1;

        sint1 = FHdivOmega / (MASS * va.getY() * MathVec.abs2D(dv));
        if (sint1 > 1.0)
            sint1 = 1.0;
        assert 0.0 <= sint1;

        if (1.0 - MathVec.sqr(cost0) < MathVec.sqr(sint1) || cost0 == 0.0
                && sint1 == 1.0) {
            // surface speed becomes 0 before the friction is killed:
            // => our force points in || direction only, but the loss of
            // momentum
            // does not!
            dv.setLocation(0, 1);

            sint1 = FHdivOmega / (MASS * va.getY() * MathVec.abs2D(dv));
            if (sint1 > 1.0)
                sint1 = 1.0;
            assert 0.0 <= sint1;

            // the loss of momentum in perp. direction:
            dv.setLocation(-Math.signum(Veff) * mu * va.getY()
                    * fabs(cost0 - 1.0), va.getY()
                    * (Math.sqrt(1.0 - MathVec.sqr(sint1)) - 1.0));
        } else
            MathVec.mult(va.getY()
                    * (Math.sqrt(1.0 - MathVec.sqr(sint1)) - 1.0), dv, dv);
        assert Math.signum(dv.getX()) == -Math.signum(Veff);
        assert dv.getY() <= 0.0;

        MathVec.add(va, dv, va);
        w[0] -= dv.getX() * MASS * RADIUS / INERTIA;

        return;
    }

    void singleNoLoss(final Point2D va, final Point2D vb, final double[] w) {
        final double Veff = -vb.getX() - RADIUS * w[1] + va.getX() - RADIUS
                * w[0];
        final double I = 2.0 * (1.0 / MASS + MathVec.sqr(RADIUS) / INERTIA);
        double tmp = vb.getY() - va.getY();
        va.setLocation(va.getX(), va.getY() + tmp);
        vb.setLocation(vb.getX(), vb.getY() - tmp);

        double X = -Veff / I;
        if (fabs(X) > (tmp = mu * MASS * fabs(tmp)))
            X = -Math.signum(Veff) * tmp;

        // apply the x-change:
        tmp = X / MASS;
        va.setLocation(va.getX() + tmp, va.getY());
        vb.setLocation(vb.getX() - tmp, vb.getY());

        // the spin's change:
        tmp = X * RADIUS / INERTIA;
        w[0] -= tmp;
        w[1] -= tmp;

        return;
    }
}