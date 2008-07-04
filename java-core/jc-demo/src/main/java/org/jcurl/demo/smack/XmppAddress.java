/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
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

package org.jcurl.demo.smack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmppAddress implements CharSequence {
	private static final Pattern p = Pattern
			.compile("([^@]+)@([^/]+)(?:/(.*))?");

	public static final XmppAddress parse(final CharSequence str) {
		if (str instanceof XmppAddress)
			return (XmppAddress) str;
		final Matcher m = p.matcher(str);
		if (!m.matches())
			return null;
		return new XmppAddress(m.group(1), m.group(2), m.group(3));
	}

	public static String toString(final String account, final String host,
			final String resource) {
		if (resource != null && resource.length() > 0)
			return account + "@" + host + "/" + resource;
		else
			return account + "@" + host;
	}

	private final String account;
	private final String host;
	private final String resource;
	private final transient String str;

	public XmppAddress(final String account, final String host) {
		this(account, host, null);
	}

	public XmppAddress(final String account, final String host,
			final String resource) {
		this.account = account;
		this.host = host;
		this.resource = resource == null || resource.length() <= 0 ? null
				: resource;
		str = toString(this.account, this.host, this.resource);
	}

	public char charAt(final int index) {
		return str.charAt(index);
	}

	public String getAccount() {
		return account;
	}

	public String getHost() {
		return host;
	}

	public String getResource() {
		return resource;
	}

	public int length() {
		return str.length();
	}

	public CharSequence subSequence(final int start, final int end) {
		return str.subSequence(start, end);
	}

	@Override
	public String toString() {
		return str;
	}

	public String toString(final String resource) {
		return toString(account, host, resource);
	}
}