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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.ugli.ULogger;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Version {

    private static final DateFormat fmt = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static final ULogger log = JCLoggerFactory.getLogger(Version.class);

    static Version create(final String complete, final String time) {
        if (complete == null)
            return null;
        final String[] tmp = complete.split("\\.");
        final int[] parts = new int[4];
        for (int i = parts.length - 1; i >= 0; i--)
            parts[i] = Integer.parseInt(tmp[i]);
        try {
            return new Version(parts, fmt.parse(time));
        } catch (ParseException e) {
            return new Version(parts, null);
        }
    }

    public static final Version find() {
        return find(Version.class);
    }

    public static final Version find(final Class clz) {
        return find(clz.getClassLoader());
    }

    public static final Version find(final ClassLoader clz) {
        try {
            final Manifest mf = findManifest(clz, "jcurl-");
            final Attributes main = mf.getMainAttributes();
            return create(main.getValue("Bundle-Version"), main
                    .getValue("Built-Time"));
        } catch (Exception e) {
            return null;
        }
    }

    static final Manifest findManifest(final ClassLoader clz,
            final String marker) throws IOException {
        for (Enumeration enum = clz.getResources("META-INF/MANIFEST.MF"); enum
                .hasMoreElements();) {
            final URL url = (URL) enum.nextElement();
            if (url.getPath().indexOf(marker) >= 0)
                return new Manifest(url.openStream());
        }
        log.info("Manifest not found in");
        for (Enumeration enum = clz.getResources("META-INF/MANIFEST.MF"); enum
                .hasMoreElements();) {
            log.info("url=" + enum.nextElement());
        }
        return new Manifest(new File("config/jcurl.jar/"
                + "META-INF/MANIFEST.MF").toURL().openStream());
    }

    private final int[] parts;

    private final long time;

    Version(final int[] parts, final Date build) {
        this.parts = parts;
        this.time = build.getTime();
    }

    public int getBuild() {
        return parts[3];
    }

    public Date getDate() {
        return new Date(getTime());
    }

    public int getMajor() {
        return parts[0];
    }

    public int getMinor() {
        return parts[1];
    }

    public int getRevision() {
        return parts[2];
    }

    public long getTime() {
        return time;
    }

    public String toString() {
        return parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
    }
}