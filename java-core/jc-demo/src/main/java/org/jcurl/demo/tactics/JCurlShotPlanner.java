/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is subject
 * to license terms.
 */

package org.jcurl.demo.tactics;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.Unit;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.log.JCLoggerFactory;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;

/**
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlShotPlanner extends SingleFrameApplication {
	private static class ZoomHelper {
		/** All from back to back */
		public static final Rectangle2D CompletePlus;
		/** House area plus 1 rock margin plus "out" rock space. */
		public static final Rectangle2D HousePlus;
		private static final Log log = JCLoggerFactory
				.getLogger(ZoomHelper.class);
		/**
		 * Inter-hog area area plus house area plus 1 rock margin plus "out"
		 * rock space.
		 */
		private static final Rectangle2D SheetPlus;
		/** 12-foot circle plus 1 rock */
		private static final Rectangle2D TwelvePlus;
		static {
			final double r2 = 2 * RockProps.DEFAULT.getRadius();
			final double x = IceSize.SIDE_2_CENTER + r2;
			HousePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2),
					2 * x, IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2
							* r2);
			final double c12 = r2 + Unit.f2m(6.0);
			TwelvePlus = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
			SheetPlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
					+ IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
					+ IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
			CompletePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE
					+ IceSize.HOG_2_HOG + IceSize.HACK_2_HOG + r2), 2 * x,
					IceSize.HOG_2_HOG + 2 * IceSize.HACK_2_HOG);
		}

		private void pan(final Zoomable dst, final double rx, final double ry,
				final int dt) {
			if (dst == null)
				return;
			final RectangularShape src = dst.getZoom();
			src.setFrame(src.getX() + src.getWidth() * rx, src.getY()
					+ src.getHeight() * ry, src.getWidth(), src.getHeight());
			zoom(dst, src, dt);
		}

		private void zoom(final Zoomable dst, final Point2D center,
				final double ratio, final int dt) {
			if (dst == null)
				return;
			final RectangularShape src = dst.getZoom();
			if (log.isDebugEnabled())
				log.debug(src);
			final double w = src.getWidth() * ratio;
			final double h = src.getHeight() * ratio;
			final double cx, cy;
			if (center == null) {
				cx = src.getCenterX();
				cy = src.getCenterY();
			} else {
				cx = center.getX();
				cy = center.getY();
			}
			zoom(dst, new Rectangle2D.Double(cx - w / 2, cy - h / 2, Math
					.abs(w), Math.abs(h)), dt);
		}

		private void zoom(final Zoomable dst, final RectangularShape viewport,
				final int dt) {
			if (dst == null)
				return;
			dst.setZoom(viewport, dt);
		}
	}

	private static URL defaultScene = null;
	private static final int FAST = 200;
	private static Log log = JCLoggerFactory.getLogger(JCurlShotPlanner.class);
	private static final int SLOW = 333;

	private static final Cursor waitc = Cursor
			.getPredefinedCursor(Cursor.WAIT_CURSOR);;

	private static final ZoomHelper zh = new ZoomHelper();

	static void bind(final Object src, final String src_p, final Object dst,
			final String dst_p) {
		Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, src,
				BeanProperty.create(src_p), dst, BeanProperty.create(dst_p))
				.bind();
	};

	private static JFileChooser jcx(final File base) {
		final JFileChooser chooser = new JFileChooser(base);
		chooser.setMultiSelectionEnabled(false);
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileFilter(new FileFilter() {
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
		return chooser;
	};

	public static void main(final String[] args) {
		final Class<? extends Application> mc = JCurlShotPlanner.class;
		{
			final String res;
			if (true) {
				final ResourceMap r = Application.getInstance(mc).getContext()
						.getResourceMap();
				res = "/" + r.getResourcesDir()
						+ r.getString("Application.defaultDocument");
			} else
				res = "/" + mc.getPackage().getName().replace('.', '/')
						+ "/resources" + "/" + "default.jcz";
			defaultScene = mc.getResource(res);
		}
		launch(mc, args);
	}

	private static JFileChooser png(final File base) {
		final JFileChooser fcPng = new JFileChooser(base);
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
		return fcPng;
	};

	private final TacticsBean tactics = new TacticsBean();;

	private JCurlShotPlanner() {
		tactics.setName("tactics");
	};

	private JMenu createMenu(final String menuName, final String[] actionNames) {
		final JMenu menu = new JMenu();
		menu.setName(menuName);
		for (final String actionName : actionNames)
			if (actionName.equals("---"))
				menu.add(new JSeparator());
			else {
				final JMenuItem menuItem = new JMenuItem();
				menuItem.setAction(getAction(actionName));
				menuItem.setIcon(null);
				menu.add(menuItem);
			}
		return menu;
	};

	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();

		final String[] fileMenuActionNames = { "fileClear", "fileHammy", "---",
				"fileNewDoc", "fileOpen", "---", "fileSave", "fileSaveAs",
				"---", "fileExportPng", "fileExportSvg", "---", "quit" };
		menuBar.add(createMenu("fileMenu", fileMenuActionNames));

		final String[] editMenuActionNames = { "editUndo", "editRedo", "---",
				"editProperties", "---", "editPreferences" };
		menuBar.add(createMenu("editMenu", editMenuActionNames));

		final String[] viewMenuActionNames = { "viewHouse", "view12Foot",
				"viewComplete", "viewActive", "---", "viewZoomIn",
				"viewZoomOut", "---", "viewPanNorth", "viewPanSouth",
				"viewPanEast", "viewPanWest" };
		menuBar.add(createMenu("viewMenu", viewMenuActionNames));

		final String[] helpMenuActionNames = { "helpAbout" };
		menuBar.add(createMenu("helpMenu", helpMenuActionNames));

		return menuBar;
	};

	private JComponent createToolBar() {
		final String[] toolbarActionNames = { "cut", "copy", "paste" };
		final JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		for (final String actionName : toolbarActionNames) {
			final JButton button = new JButton();
			button.setAction(getAction(actionName));
			button.setFocusable(false);
			toolBar.add(button);
		}
		return toolBar;
	};

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editPreferences() {};

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editProperties() {};

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editRedo() {};

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editUndo() {};

	/** File Menu Action */
	@Action
	public void fileClear() {
		// TODO check is Modified
		try {
			tactics.setDocument(null);
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	};

	/** File Menu Action */
	@Action
	public void fileExportPng() {};

	/** File Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void fileExportSvg() {};

	/** File Menu Action */
	@Action
	public void fileHammy() {
		// TODO check is Modified
		try {
			tactics.setDocument(defaultScene);
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/** File Menu Action */
	@Action
	public void fileNewDoc() {};

	/** File Menu Action */
	@Action
	public void fileOpen() {
		// TODO check is Modified
		final JFileChooser chooser = jcx(tactics.getFile());
		chooser.setName("openDialog");
		final int option = chooser.showOpenDialog(getMainFrame());
		if (option == JFileChooser.APPROVE_OPTION) {
			final File file = chooser.getSelectedFile();
			try {
				tactics.setDocument(file.toURI().toURL());
			} catch (final MalformedURLException e) {
				// shouldn't happen unless the JRE fails
				log.warn("File.toURI().toURL() failed", e);
			} catch (final IOException e) {
				showErrorDialog("can't open \"" + file + "\"", e);
			}
		}
	}

	/** File Menu Action */
	@Action(enabledProperty = "savingSensible")
	public void fileSave() {}

	/** File Menu Action */
	@Action
	public void fileSaveAs() {
		final JFileChooser fcJcx = jcx(tactics.getFile());
		fcJcx.setDialogTitle("Save As");
		if (JFileChooser.APPROVE_OPTION == fcJcx.showSaveDialog(getMainFrame())) {
			final Cursor cu = switchCursor(waitc);
			try {
				// model.saveAs(td, fcJcx.getSelectedFile());
				throw new NotImplementedYetException();
			} catch (final Exception e1) {
				log.error("", e1);
			} finally {
				switchCursor(cu);
			}
		}
	}

	private javax.swing.Action getAction(final String actionName) {
		return getContext().getActionMap().get(actionName);
	}

	public boolean getAlwaysFalse() {
		return false;
	};

	/** Help Menu Action */
	@Action
	public void helpAbout() {};

	@Override
	protected void initialize(final String[] as) {
		try {
			final URL scene;
			if (as.length == 0)
				scene = defaultScene;
			else
				scene = new URL(as[0]);
			tactics.setDocument(scene);
		} catch (final IOException e) {
			log.warn("Couldn't load '" + as[0] + "'.", e);
		}
	};

	public boolean isSavingSensible() {
		return tactics.isSavingSensible();
	}

	private void showErrorDialog(String message, final Exception e) {
		final String title = "Error";
		final int type = JOptionPane.ERROR_MESSAGE;
		message = "Error: " + message;
		JOptionPane.showMessageDialog(getMainFrame(), message, title, type);
	};

	@Override
	protected void startup() { // set the window icon:
		final Image i;
		{
			final ResourceMap r = getContext().getResourceMap();
			if (false)
				i = getContext().getResourceMap().getImageIcon(
						"Application.icon").getImage();
			else if (true)
				try {
					i = ImageIO.read(this.getClass().getResource(
							"/" + r.getResourcesDir() + "/"
									+ r.getString("Application.icon")));
				} catch (final IOException e) {
					throw new RuntimeException("Unhandled", e);
				}
			else
				i = Toolkit.getDefaultToolkit().createImage(
						this.getClass().getResource(
								"/" + r.getResourcesDir() + "/"
										+ r.getString("Application.icon")));
		}
		getMainFrame().setIconImage(i);
		// SystemTray tray = SystemTray.getSystemTray();

		getMainFrame().setJMenuBar(createMenuBar());
		// tactics.setDocument(src);

		// Either use the binding here or delegate the change event?
		bind(tactics, "savingSensible", this, "savingSensible");

		show(tactics);
		viewHouse();
	}

	private Cursor switchCursor(final Cursor neo) {
		final Cursor cu = getMainFrame().getCursor();
		getMainFrame().setCursor(neo);
		Thread.yield();
		return cu;
	}

	/** View Menu Action */
	@Action
	public void view12Foot() {
		zh.zoom(tactics, ZoomHelper.TwelvePlus, SLOW);
	}

	/** View Menu Action */
	@Action
	public void viewActive() {
		zh.zoom(tactics, ZoomHelper.SheetPlus, SLOW);
	}

	/** View Menu Action */
	@Action
	public void viewComplete() {
		zh.zoom(tactics, ZoomHelper.CompletePlus, SLOW);
	}

	/** View Menu Action */
	@Action
	public void viewHouse() {
		zh.zoom(tactics, ZoomHelper.HousePlus, SLOW);
	}

	/** View Menu Action */
	@Action
	public void viewPanEast() {
		zh.pan(tactics, -0.2, 0, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanNorth() {
		zh.pan(tactics, 0, 0.2, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanSouth() {
		zh.pan(tactics, 0, -0.2, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanWest() {
		zh.pan(tactics, 0.2, 0, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewZoomIn() {
		zh.zoom(tactics, null, 0.75, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewZoomOut() {
		zh.zoom(tactics, null, 1.25, FAST);
	}
}
