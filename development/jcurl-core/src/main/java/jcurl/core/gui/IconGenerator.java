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
package jcurl.core.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import jcurl.core.io.SetupBuilder;
import jcurl.core.io.SetupSaxDeSer;

import org.jcurl.core.PositionSet;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class IconGenerator {

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {
        // Load an initial setup
        final URL url;
        {
            URL tmp = IconGenerator.class.getResource("/setup/hammy.jcx");
            if (tmp == null)
                tmp = new URL("file", "localhost",
                        "/home/m/eclipse/berlios/jcurl/config/jcurl.jar/setup/hammy.jcx");
            url = tmp;
        }
        final SetupBuilder setup = SetupSaxDeSer.parse(url);

        savePng(setup.getPos(), Zoomer.HOUSE2HACK, new File("/tmp/jcurl.png"));
    }

    /**
     * @param loc
     * @param zoom
     * @param dst
     * @throws IOException
     */
    public static void savePng(final PositionSet loc, final Zoomer zoom,
            final File dst) throws IOException {
        // Create image and graphics.
        final BufferedImage img = new BufferedImage(1024, 768,
                BufferedImage.TYPE_INT_RGB);
        final Graphics g = img.getGraphics();
        final JCurlDisplay jp = new JCurlDisplay(loc, zoom, null, null);
        jp.setSize(img.getWidth(), img.getHeight());
        jp.paintComponent(g);
        g.dispose();
        ImageIO.write(img, "png", dst);
    }
}