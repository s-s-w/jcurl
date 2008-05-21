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
package org.jcurl.core.io;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class IONode implements Serializable {
	public static final String CreatedByProgram = "org.jcurl.core.io.CreatedByProgram";
	public static final String CreatedByUser = "org.jcurl.core.io.CreatedByUser";
	private static final long serialVersionUID = -4734020637823903908L;
	private final Map<CharSequence, CharSequence> annotations;

	public IONode() {
		this(null);
	}

	public IONode(final Map<CharSequence, CharSequence> annotations) {
		this.annotations = annotations == null ? new HashMap<CharSequence, CharSequence>()
				: annotations;
	}

	public Map<CharSequence, CharSequence> annotations() {
		return annotations;
	}
}
