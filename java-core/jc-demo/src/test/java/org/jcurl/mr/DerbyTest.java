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
package org.jcurl.mr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.TestCase;

public class DerbyTest extends TestCase {
    public DerbyTest() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver", true, this
                    .getClass().getClassLoader());
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    public void _test010() throws SQLException {
        final Connection c = DriverManager.getConnection(
                "jdbc:derby:target/jcurl-derby;create=true", "sa", "");
        final PreparedStatement stmt = c
                .prepareStatement("Create Table SCHEMA_VERSION (id int primary key)");
        stmt.execute();
    }

    public void testOk() {
    }
}
