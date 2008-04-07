/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
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

package org.jcurl.demo.tactics;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.demo.tactics.ActionRegistry.JCAction;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class MenuFile {
	@ActionRegistry.JCMenu("&File")
	public static class Controller {
		private static final Log log = JCLoggerFactory
				.getLogger(Controller.class);

		/**
		 * tighten to a given point.
		 * 
		 * TODO play a shy slurping sound.
		 * 
		 * @param frame
		 * @param steps
		 * @param millis
		 */
		private static void vanish(final Component frame, final int steps,
				final int millis) {
			if (false)
				// TODO on a wicked Mac do nothing more but exit...
				return;

			final Rectangle siz0 = frame.getBounds();
			// final BufferedImage img = new
			// BufferedImage(frame.getContentPane()
			// .getWidth(), frame.getContentPane().getHeight(),
			// BufferedImage.TYPE_INT_RGB);
			// frame.getContentPane().paint(img.getGraphics());
			// img.getGraphics().dispose();

			// why does this have no effect?
			// frame.getContentPane().removeAll();
			frame.repaint();

			final Point2D dst = new Point2D.Double(siz0.x + siz0.width * 1 / 3,
					siz0.y + siz0.height * 1 / 3);
			for (int i = steps - 1; i >= 0; i--) {
				final Rectangle siz = frame.getBounds();
				siz.x = (int) (dst.getX() + (siz0.x - dst.getX()) * i / steps);
				siz.y = (int) (dst.getX() + (siz0.y - dst.getY()) * i / steps);
				siz.height = (int) ((float) siz0.height * i / steps);
				siz.width = (int) ((float) siz0.width * i / steps);
				frame.setBounds(siz);
				try {
					Thread.sleep(millis / steps);
				} catch (final InterruptedException e) {
					return;
				}
			}
		}

		private final JFileChooser fcPng;
		private Model model;
		private final Component parent;
		private final Component shootable;

		public Controller(final Component parent, final Component shootable) {
			this.parent = parent;
			this.shootable = shootable;
			fcPng = new JFileChooser(new File("."));
			fcPng.setName("Save Screenshot");
			fcPng.setDialogTitle(fcPng.getName());
			fcPng.setMultiSelectionEnabled(false);
			fcPng.setAcceptAllFileFilterUsed(true);
			fcPng.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(final File f) {
					if (f == null)
						return false;
					return f.isDirectory() || f.getName().endsWith(".png");
				}

				@Override
				public String getDescription() {
					return "Png Bitmap Images (.png)";
				}
			});
		}

		@JCAction(title = "E&xit", idx = 60, separated = true)
		public void exitFile() {
			log.info("");
			// if (!discardUnsavedChanges())
			// fileSave.actionPerformed(null);
			vanish(parent, 10, 200);
			System.exit(0);
		}

		private void exportPng(final Component src, File dst)
				throws IOException {
			final BufferedImage img = new BufferedImage(src.getWidth(), src
					.getHeight(), BufferedImage.TYPE_INT_ARGB);
			final Graphics g = img.getGraphics();
			try {
				src.paintAll(g);
			} finally {
				g.dispose();
			}
			if (!dst.getName().endsWith(".png"))
				dst = new File(dst.getAbsoluteFile() + ".png");
			ImageIO.write(img, "png", dst);
		}

		public Model getModel() {
			return model;
		}

		@JCAction(title = "&New", idx = 10, accelerator = "CTRL-N")
		public void newFile() {
			log.info("");
		}

		@JCAction(title = "&Open", idx = 20, accelerator = "CTRL-O")
		public void openFile() {
			log.info("");
		}

		@JCAction(title = "Save &As", idx = 40)
		public void saveAsFile() {
			log.info("");
		}

		@JCAction(title = "&Save", idx = 30, separated = true, accelerator = "CTRL-S")
		public void saveFile() {
			log.info("");
		}

		@JCAction(title = "S&creenshot", idx = 50, separated = true, accelerator = "CTRL-P")
		public void screenShot() {
			if (JFileChooser.APPROVE_OPTION == fcPng.showSaveDialog(parent)) {
				final File dst = fcPng.getSelectedFile();
				try {
					// this.setCursor(Cwait);
					exportPng(shootable, dst);
				} catch (final Exception e1) {
					log.error("", e1);
				} finally {
					// this.setCursor(Cdefault);
				}
			}
		}

		public void setModel(final Model model) {
			this.model = model;
		}
	}

	public static class Model {
		private File file;

		public File getFile() {
			return file;
		}

		public void setFile(final File file) {
			this.file = file;
		}
	}
}
