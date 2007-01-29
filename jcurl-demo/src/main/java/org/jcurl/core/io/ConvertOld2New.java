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
package org.jcurl.core.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.xml.sax.SAXException;

/**
 * <code>java -cp build/jcurl-0.1.jar:build/ugli-simple.jar jcurl.core.io.ConvertOld2New $1 |  xmllint -format -</code>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:ConvertOld2New.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class ConvertOld2New {

    private static class IniFilter implements FileFilter {

        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".ini");
        }
    }

    public static void convertOld2New(final Reader in, final Writer out)
            throws IOException, SAXException {
        final OldConfigReader old = OldConfigReader.parse(in);
        final SetupSaxSer ser = new SetupSaxSer(out);
        ser.write(old.setup);
    }

    public static void main(String[] args) throws FileNotFoundException,
            IOException, SAXException {
        for (int i = args.length - 1; i >= 0; i--) {
            final File f = new File(args[i]);
            if (f.exists()) {
                if (f.isDirectory()) {
                    final File[] files = f.listFiles(new IniFilter());
                    for (int j = files.length - 1; j >= 0; j--) {
                        final File src = files[j];
                        final File dst = new File(files[j].toString() + ".jcx");
                        System.out.println(dst);
                        convertOld2New(new FileReader(src), new FileWriter(dst));

                    }
                } else
                    convertOld2New(new FileReader(f), new PrintWriter(
                            System.out));
            } else
                System.err.println(f.toString() + " not found");
        }
    }
}