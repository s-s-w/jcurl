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
package org.jcurl.core.api;

/**
 * Properties of a unique set of rocks (size, mass, etc.)
 * 
 * @see org.jcurl.core.api.RockProps
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:RockSetProps.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class RockSetProps {

	public static final RockSetProps DEFAULT;
	static {
		DEFAULT = new RockSetProps();
		for (int i = DEFAULT.dark.length - 1; i >= 0; i--) {
			DEFAULT.dark[i] = RockProps.DEFAULT;
			DEFAULT.light[i] = RockProps.DEFAULT;
		}
	}

	private final RockProps[] dark = new RockProps[RockSet.ROCKS_PER_COLOR];

	private final RockProps[] light = new RockProps[RockSet.ROCKS_PER_COLOR];

	public RockSetProps() {
		for (int i = dark.length - 1; i >= 0; i--) {
			dark[i] = new RockProps();
			light[i] = new RockProps();
		}
	}

	public RockProps getDark(final int i) {
		return dark[i];
	}

	public RockProps getLight(final int i) {
		return light[i];
	}
}