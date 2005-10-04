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
package jcurl.core;

import jcurl.core.dto.RockFloat;
import jcurl.math.CSplineInterpolator;

/**
 * Use cubic splines {@link jcurl.math.CSplineInterpolator}to interpolate one
 * single {@link jcurl.core.Rock}'s trajectory based on discrete data.
 * 
 * @see jcurl.math.CSplineInterpolator
 * @see jcurl.core.RockSetInterpolator
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CSplineRockInterpolator implements IRockInterpolator {

    private final CSplineInterpolator alpha;

    private final CSplineInterpolator x;

    private final CSplineInterpolator y;

    /**
     *  
     */
    public CSplineRockInterpolator() {
        x = new CSplineInterpolator();
        y = new CSplineInterpolator();
        alpha = new CSplineInterpolator();
    }

    public void add(final double t, final Rock rock) {
        add(t, rock, false);
    }

    public void add(final double t, final Rock rock, final boolean discontinuous) {
        x.add(t, rock.getX());
        y.add(t, rock.getY());
        alpha.add(t, rock.getZ());
    }

    public double getMaxT() {
        return x.getMaxX();
    }

    public double getMinT() {
        return x.getMinX();
    }

    public Rock getPos(final double t, final Rock rock) {
        final Rock ret = rock == null ? new RockFloat() : rock;
        ret.setX(x.getC(0, t));
        ret.setY(y.getC(0, t));
        ret.setZ(alpha.getC(0, t));
        return ret;
    }

    public Rock getSpeed(final double t, final Rock rock) {
        final Rock ret = rock == null ? new RockFloat() : rock;
        ret.setX(x.getC(1, t));
        ret.setY(y.getC(1, t));
        ret.setZ(alpha.getC(1, t));
        return ret;
    }

    public void reset() {
        x.reset();
        y.reset();
        alpha.reset();
    }
}