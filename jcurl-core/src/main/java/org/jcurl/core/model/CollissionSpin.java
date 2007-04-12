/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.model;

import java.util.Map;

import org.jcurl.core.base.ColliderBase;
import org.jcurl.core.base.PropModelHelper;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.math.MathVec;

/**
 * A hitter including spin.
 * <p>
 * This hitter gets the transfer of spin and momentum right. For details see the
 * paper <a href="http://jcurl.berlios.de/curlsci.pdf">Curling Scientific </a>.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CollissionSpin.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class CollissionSpin extends ColliderBase {

    private static final double J = RockProps.DEFAULT.getInertia();

    private static final double m = RockProps.DEFAULT.getMass();

    private static final double R = RockProps.DEFAULT.getRadius();

    private transient double mu;

    public CollissionSpin() {
    }

    public CollissionSpin(final double f, final double l) {
        final Map<CharSequence, DimVal> t = PropModelHelper.create();
        PropModelHelper.setFrictionRockRock(t, f);
        PropModelHelper.setLoss(t, l);
        init(t);
    }

    @Override
    public void computeRC(final Rock va, final Rock vb) {
        final double Veff = va.getX() + R * va.getA()
                - (vb.getX() + R * vb.getA());
        double X = -Veff / (2 * (1 / m + R * R / J));
        final double dVy = vb.getY() - va.getY();
        final double dPabs = m * abs(dVy);
        if (abs(X) > mu * dPabs)
            X = -MathVec.sgn(Veff) * mu * dPabs;
        final double dVx = X / m;
        final double dW = -X * R / J;
        va.setX(va.getX() + dVx);
        va.setY(va.getY() + dVy);
        va.setA(va.getA() + dW);
        vb.setX(vb.getX() - dVx);
        vb.setY(vb.getY() - dVy);
        vb.setA(vb.getA() + dW);
    }

    void init(final double fritionRockRock, final double loss) {
        mu = fritionRockRock;
    }

    @Override
    public void init(final Map<CharSequence, DimVal> params) {
        internalInit(params);
        init(PropModelHelper.getFrictionRockRock(this.params), PropModelHelper
                .getLoss(this.params));
    }

    /**
     * The friction rock/rock. Set the parameter for friction rock/rock.
     * 
     * @param v
     *            the value
     */
    public void setFricRockRock(final double v) {
        PropModelHelper.setFrictionRockRock(params, mu = v);
    }

    /**
     * The loss of energy on raises. Set the parameter for the loss of energy
     * raises suffer.
     * 
     * @param v
     *            [Joule] the value
     */
    public void setLoss(final double v) {
        PropModelHelper.setLoss(params, v);
    }
}