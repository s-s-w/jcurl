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
package jcurl.sim.model;

import jcurl.core.Rock;
import jcurl.core.dto.RockProps;
import jcurl.sim.core.CollissionStrategy;

/**
 * A hitter including spin and loss of energy.
 * <p>
 * This hitter gets the transfer of spin and momentum right. The 'loss of
 * energy'-mechanism bases upon a Hookisch elasticity-model for the rocks. Only
 * stillstanding rocks cause a loss of energy. For details see the paper <a
 * href="http://jcurl.berlios.de/curlsci.pdf">Curling Scientific </a>.
 * 
 * @see jcurl.sim.model.CollissionSpinLossTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id: CollissionSpinLoss.java 76 2005-03-28 20:26:21Z mrohrmoser $
 */
public class CollissionSpin extends CollissionStrategy {

    private static final double J = RockProps.DEFAULT.getInertia();

    private static final double m = RockProps.DEFAULT.getMass();

    private static final double R = RockProps.DEFAULT.getRadius();

    protected static double fabs(final double a) {
        return Math.abs(a);
    }

    protected static byte sgn(final double a) {
        if (a < 0)
            return -1;
        if (a > 0)
            return 1;
        return 0;
    }

    protected static double sqrt(final double a) {
        return Math.sqrt(a);
    }

    private double mu;

    private double U;

    public CollissionSpin() {
        setLoss(0);
        setFricRockRock(0.5);
    }

    public void compute(final Rock va, final Rock vb) {
        final double Veff = va.getX() + R * va.getZ()
                - (vb.getX() + R * vb.getZ());
        double X = -Veff / (2 * ((1 / m) + R * R / J));
        final double dVy = vb.getY() - va.getY();
        final double dPabs = m * fabs(dVy);
        if (fabs(X) > mu * dPabs)
            X = -sgn(Veff) * mu * dPabs;
        final double dVx = X / m;
        final double dW = -X * R / J;
        va.setX(va.getX() + dVx);
        va.setY(va.getY() + dVy);
        va.setZ(va.getZ() + dW);
        vb.setX(vb.getX() - dVx);
        vb.setY(vb.getY() - dVy);
        vb.setZ(vb.getZ() + dW);
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
        U = v / m;
    }
}