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

import jcurl.core.dto.Ice;

/**
 * A {@link jcurl.core.RockSet}%nbsp;with location semantics.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PositionSet extends RockSet {
    public static PositionSet allHome() {
        return allHome(null);
    }

    public static PositionSet allHome(PositionSet ret) {
        ret = ret != null ? ret : new PositionSet();
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            Ice.setHome(ret.dark[i], true, i);
            Ice.setHome(ret.light[i], false, i);
        }
        return ret;
    }

    public static PositionSet allOut() {
        return allOut(null);
    }

    public static PositionSet allOut(PositionSet ret) {
        ret = ret != null ? ret : new PositionSet();
        for (int i = ROCKS_PER_COLOR - 1; i >= 0; i--) {
            Ice.setOut(ret.dark[i], true, i);
            Ice.setOut(ret.light[i], false, i);
        }
        return ret;
    }

    public PositionSet() {
        super();
    }

    protected PositionSet(boolean fill) {
        super(fill);
    }

    public PositionSet(final RockSet b) {
        super(b);
    }

    public Object clone() {
        return new PositionSet(this);
    }
}