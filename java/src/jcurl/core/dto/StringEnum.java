/*
 * jcurl curling simulation framework Copyright (C) 2005 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package jcurl.core.dto;

import java.util.Map;
import java.util.TreeMap;

class StringEnum extends EnumBase {

    private static int _idx = 0;

    private static final Map _names = new TreeMap();

    protected static final StringEnum find(String txt) {
        return (StringEnum) _names.get(txt);
    }

    private StringEnum(final String txt) {
        super(_idx++, txt);
        _names.put(txt, this);
    }
}