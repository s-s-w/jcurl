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

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class ModelBase {

    public abstract int getFocusIdx();

    public abstract PositionSet getPos();

    public abstract int getSelectedMask();

    public abstract PositionSet getSpeed();

    public abstract long getTmax();

    public abstract long getTmin();

    public abstract long getTnow();

    public abstract void init(final PositionSet pos);

    public abstract void reset();

    public abstract void setTnow(final long tnow);

}