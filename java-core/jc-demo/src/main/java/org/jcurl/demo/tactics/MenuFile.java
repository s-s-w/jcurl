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
import java.awt.Cursor;
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
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.io.IONode;
import org.jcurl.core.io.IOTrajectories;
import org.jcurl.core.io.JCurlSerializer;
import org.jcurl.core.io.JDKSerializer;
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

		private final JFileChooser fcJcx;
		private final JFileChooser fcPng;
		private Model model = new Model();
		private final Component parent;
		private final Component shootable;
		private final TrajectoryDisplay td;

		public Controller(final Component parent, final Component shootable,
				final TrajectoryDisplay td) {
			this.parent = parent;
			this.shootable = shootable;
			this.td = td;
			final File base = new File(".");
			fcJcx = new JFileChooser(base);
			fcJcx.setName("Open");
			fcJcx.setMultiSelectionEnabled(false);
			fcJcx.setAcceptAllFileFilterUsed(true);
			fcJcx.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(final File f) {
					if (f == null)
						return false;
					return f.isDirectory() || f.getName().endsWith(".jcx")
							|| f.getName().endsWith(".jcz");
				}

				@Override
				public String getDescription() {
					return "JCurl Setup Files (.jcx) (.jcz)";
				}
			});
			fcPng = new JFileChooser(base);
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

		@JCAction(title = "Clea&r", idx = 0, accelerator = "CTRL-R")
		public void clear() {
			log.info("");
			final Cursor cu = switchCursor(waitc);
			try {
				// This cast just eases code navigation. Remove it later.
				((TrajectoryPiccoloPanel) td).setCurves(null);
			} finally {
				switchCursor(cu);
			}
		}

		@JCAction(title = "E&xit", idx = 60, separated = true)
		public void exitFile() {
			log.info("");
			// if (!discardUnsavedChanges())
			// fileSave.actionPerformed(null);
			final Cursor cu = switchCursor(waitc);
			vanish(parent, 10, 200);
			System.exit(0);
		}

		public Model getModel() {
			return model;
		}

		@JCAction(title = "&New", idx = 10, accelerator = "CTRL-N", separated = true)
		public void newFile() {
			log.info("");
			final Cursor cu = switchCursor(waitc);
			try {
				// ...
			} finally {
				switchCursor(cu);
			}
		}

		@JCAction(title = "&Open", idx = 20, accelerator = "CTRL-O")
		public void openFile() {
			log.info("");
			fcJcx.setDialogTitle("Open");
			if (JFileChooser.APPROVE_OPTION == fcJcx.showOpenDialog(parent)) {
				final Cursor cu = switchCursor(waitc);
				try {
					td.setCurves(model.open(td, fcJcx.getSelectedFile()));
				} catch (final Exception e1) {
					log.error("", e1);
				} finally {
					switchCursor(cu);
				}
			}
		}

		@JCAction(title = "Save &As", idx = 40)
		public void saveAsFile() {
			log.info("");
			fcJcx.setDialogTitle("Save As");
			if (JFileChooser.APPROVE_OPTION == fcJcx.showSaveDialog(parent)) {
				final Cursor cu = switchCursor(waitc);
				try {
					model.saveAs(td, fcJcx.getSelectedFile());
				} catch (final Exception e1) {
					log.error("", e1);
				} finally {
					switchCursor(cu);
				}
			}
		}

		@JCAction(title = "&Save", idx = 30, separated = true, accelerator = "CTRL-S")
		public void saveFile() {
			log.info("");
			final Cursor cu = switchCursor(waitc);
			try {

				// ...
			} finally {
				switchCursor(cu);
			}
		}

		@JCAction(title = "S&creenshot", idx = 50, separated = true, accelerator = "CTRL-P")
		public void screenShot() {
			if (JFileChooser.APPROVE_OPTION == fcPng.showSaveDialog(parent)) {
				final File dst = fcPng.getSelectedFile();
				final Cursor cu = switchCursor(waitc);
				try {
					model.exportPng(shootable, dst);
				} catch (final Exception e1) {
					log.error("", e1);
				} finally {
					switchCursor(cu);
				}
			}
		}

		public void setModel(final Model model) {
			this.model = model;
		}

		@JCAction(title = "Show &Hammy", idx = 1, accelerator = "CTRL-H")
		public void showHammy() {
			log.info("");
			final Cursor cu = switchCursor(waitc);
			try {
				td.setCurves(model.createHammy(td));
			} finally {
				switchCursor(cu);
			}
		}

		private Cursor switchCursor(final Cursor neo) {
			final Cursor cu = parent.getCursor();
			parent.setCursor(neo);
			Thread.yield();
			return cu;
		}
	}

	public static class Model {
		private File file;
		private CurveManager tpm;

		public CurveManager createHammy(final Object context) {
			// This cast just eases code navigation. Remove it later.
			final CurveManager ret = MainMod.initHammy(tpm);
			ret.setCurrentTime(30);
			setTpm(ret);
			return ret;
		}

		private File ensureProperSuffix(File dst) {
			if (!(dst.getName().endsWith(".jcx") || dst.getName().endsWith(
					".jcz")))
				dst = new File(dst.getAbsoluteFile() + ".jcz");
			return dst;
		}

		public void exportPng(final Component src, File dst) throws IOException {
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

		public File getFile() {
			return file;
		}

		public CurveManager getTpm() {
			return tpm;
		}

		CurveManager open(final Object context, final File src)
				throws IOException {
			final IONode n = new JCurlSerializer().read(src);
			final IOTrajectories it = (IOTrajectories) n;
			setTpm((CurveManager) it.trajectories().get(0));
			setFile(src);
			tpm.setCurrentTime(30);
			return tpm;
		}

		void saveAs(final Object context, final File dst) throws IOException {
			final IOTrajectories it = new IOTrajectories();
			// Add some properties about the file.
			it.annotations().put("foo", "bar");
			it.trajectories().add(tpm);
			new JCurlSerializer().write(it, ensureProperSuffix(dst),
					JDKSerializer.class);
		}

		public void setFile(final File file) {
			this.file = file;
		}

		public void setTpm(final CurveManager tpm) {
			this.tpm = tpm;
		}
	}

	private static final Cursor defau = Cursor.getDefaultCursor(); // getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private static final Cursor waitc = Cursor
			.getPredefinedCursor(Cursor.WAIT_CURSOR);
}
