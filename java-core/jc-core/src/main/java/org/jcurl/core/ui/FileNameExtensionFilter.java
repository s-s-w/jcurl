/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
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

package org.jcurl.core.ui;

import java.io.File;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileFilter;

/**
 * Backport of the jdk16 class
 * <code>javax.swing.filechooser.FileNameExtensionFilter</code>.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class FileNameExtensionFilter extends FileFilter {
	private final String description;

	private final String[] extensions;

	private final Pattern pattern;

	public FileNameExtensionFilter(final String description,
			final String... extensions) {
		this.extensions = extensions;

		// At first build a nice description
		final StringBuilder p = new StringBuilder();
		if (description != null)
			p.append(description.trim()).append(" (");
		boolean start = true;
		for (final String ext : extensions) {
			if (!start)
				p.append(", ");
			start = false;
			p.append("*.").append(ext);
		}
		if (description != null)
			p.append(")");
		this.description = p.toString();

		// and the the regular expression pattern to match against
		p.setLength(0);
		p.append("^.*\\.(");
		start = true;
		for (final String ext : extensions) {
			if (!start)
				p.append('|');
			start = false;
			p.append(ext);
		}
		p.append(")$");
		pattern = Pattern.compile(p.toString(), Pattern.CASE_INSENSITIVE);
	}

	@Override
	public boolean accept(final File f) {
		if (f == null)
			return false;
		return f.isDirectory() || pattern.matcher(f.getName()).matches();
	}

	@Override
	public String getDescription() {
		return description;
	}

	public String[] getExtensions() {
		return extensions;
	}

	Pattern getPattern() {
		return pattern;
	}
}
