/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.mr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.TestCase;

public class HsqldbTest extends TestCase {

	public HsqldbTest() {
		try {
			Class.forName("org.hsqldb.jdbcDriver", true, this.getClass()
					.getClassLoader());
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	public void testFile() throws SQLException {
		final Connection c = DriverManager.getConnection(
				"jdbc:hsqldb:file:target/jcurl-hsqldb", "sa", "");
		final PreparedStatement stmt = c
				.prepareStatement("Create Table SCHEMA_VERSION (id int primary key)");
		try {
			stmt.execute();
		} catch (final SQLException e) {
			;
		}
	}

	public void testMem() throws SQLException {
		final Connection c = DriverManager.getConnection(
				"jdbc:hsqldb:mem:jcurl", "sa", "");
		PreparedStatement stmt = c
				.prepareStatement("Create Table EVENTS (id int primary key)");
		stmt.execute();
		stmt = c.prepareStatement("Create Table GAMES (id INT PRIMARY KEY)");
		stmt.execute();
		stmt = c.prepareStatement("Create Table ENDS (id int primary key)");
		stmt.execute();
		stmt = c.prepareStatement("Create Table ROCKS (id int primary key)");
		stmt.execute();
	}
}
