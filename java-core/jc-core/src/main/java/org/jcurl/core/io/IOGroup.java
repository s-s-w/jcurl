/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.io;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IOGroup extends IONode {
    private static final long serialVersionUID = 6594185597261724279L;
    private final List<IONode> children;

    public IOGroup() {
        this(null, null);
    }

    protected IOGroup(final Map<CharSequence, CharSequence> annotations,
            final List<IONode> children) {
        super(annotations);
        this.children = children == null ? new ArrayList<IONode>() : children;
    }

    public List<IONode> children() {
        return children;
    }

    protected Object readResolve() throws ObjectStreamException {
        return new IOGroup(annotations(), children());
    }
}
