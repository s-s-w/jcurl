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
package jcurl.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import jcurl.core.JCLoggerFactory;
import jcurl.model.PositionSet;
import jcurl.model.RockSet;
import jcurl.model.SpeedSet;
import jcurl.sim.core.CollissionStrategy;
import jcurl.sim.core.SlideStrategy;

import org.apache.ugli.ULogger;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * IO Abstraction layer.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 * @see jcurl.core.io.SetupSaxSer
 * @see jcurl.core.io.SetupSaxDeSer
 * @see jcurl.core.io.SetupIOTest
 */
public class SetupIO {

    private static final ULogger log = JCLoggerFactory.getLogger(SetupIO.class);

    public static void load(final File src, final PositionSet pos,
            final SpeedSet speed, final SlideStrategy slide,
            final CollissionStrategy coll) throws FileNotFoundException,
            SAXException, IOException {
        if (log.isDebugEnabled())
            log.debug("Loading " + src);
        final SetupBuilder setup = SetupSaxDeSer.parse(src);
        RockSet.copy(setup.getPos(), pos);
        RockSet.copy(setup.getSpeed(), speed);        
    }

    public static void load(final InputStream src, final PositionSet pos,
            final SpeedSet speed, final SlideStrategy slide,
            final CollissionStrategy coll) throws SAXException, IOException {
        final SetupBuilder setup = SetupSaxDeSer.parse(src);
        RockSet.copy(setup.getPos(), pos);
    }

    public static SetupBuilder load(final InputStream src) throws SAXException,
            IOException {
        return SetupSaxDeSer.parse(src);
    }

    public static void load(final URL src, final PositionSet pos,
            final SpeedSet speed, final SlideStrategy slide,
            final CollissionStrategy coll) throws SAXException, IOException {
        if (log.isDebugEnabled())
            log.debug("Loading " + src);
        final SetupBuilder setup = SetupSaxDeSer.parse(src);
        RockSet.copy(setup.getPos(), pos);
    }

    public static void save(final ContentHandler dst, final PositionSet pos,
            final SpeedSet speed, final SlideStrategy slide,
            final CollissionStrategy coll) throws SAXException {
        new SetupSaxSer(dst).write(pos, speed, slide);
    }

    public static void save(final File dst, final PositionSet pos,
            final SpeedSet speed, final SlideStrategy slide,
            final CollissionStrategy coll) throws SAXException, IOException {
        if (log.isDebugEnabled())
            log.debug("Saving " + dst);
        new SetupSaxSer(dst).write(pos, speed, slide);
    }

    public static void save(final OutputStream dst, final PositionSet pos,
            final SpeedSet speed, final SlideStrategy slide,
            final CollissionStrategy coll) throws SAXException {
        new SetupSaxSer(dst).write(pos, speed, slide);
    }
}