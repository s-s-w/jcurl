/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is subject
 * to license terms.
 */

package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.EventObject;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.ComputedTrajectorySet;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.TrajectorySet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.io.IONode;
import org.jcurl.core.io.IOTrajectories;
import org.jcurl.core.io.JCurlSerializer;
import org.jcurl.core.io.JDKSerializer;
import org.jcurl.core.log.JCLoggerFactory;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

/**
 * Makes heavy use of the <a
 * href="https://appframework.dev.java.net/intro/index.html">Swing Application
 * Framework</a>.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlShotPlanner extends SingleFrameApplication {
	private static class ChangeManager implements ChangeListener {
		private final JCurlShotPlanner host;

		public ChangeManager(final JCurlShotPlanner host) {
			this.host = host;
		}

		public void deregister(final ComputedTrajectorySet cts) {
			if (cts == null)
				return;
			cts.getInitialPos().removeChangeListener(this);
			cts.getInitialSpeed().removeChangeListener(this);
			cts.getCurrentPos().removeChangeListener(this);
			cts.getCurrentSpeed().removeChangeListener(this);
		}

		public void register(final ComputedTrajectorySet cts) {
			if (cts == null)
				return;
			cts.getInitialPos().addChangeListener(this);
			cts.getInitialSpeed().addChangeListener(this);
			cts.getCurrentPos().addChangeListener(this);
			cts.getCurrentSpeed().addChangeListener(this);
		}

		public void stateChanged(final ChangeEvent e) {
			host.setModified(true);
		}
	}

	private static class ZoomHelper {
		/**
		 * Inter-hog area area plus house area plus 1 rock margin plus "out"
		 * rock space.
		 */
		public static final Rectangle2D ActivePlus;
		/** All from back to back */
		public static final Rectangle2D CompletePlus;
		/** House area plus 1 rock margin plus "out" rock space. */
		public static final Rectangle2D HousePlus;
		/** 12-foot circle plus 1 rock */
		public static final Rectangle2D TwelvePlus;
		static {
			final double r2 = 2 * RockProps.DEFAULT.getRadius();
			final double x = IceSize.SIDE_2_CENTER + r2;
			HousePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2),
					2 * x, IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2
							* r2);
			final double c12 = r2 + Unit.f2m(6.0);
			TwelvePlus = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
			ActivePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
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
			zoom(dst, new Rectangle2D.Double(src.getX() + src.getWidth() * rx,
					src.getY() + src.getHeight() * ry, src.getWidth(), src
							.getHeight()), dt);
		}

		private void zoom(final Zoomable dst, final Point2D center,
				final double ratio, final int dt) {
			if (dst == null)
				return;
			final RectangularShape src = dst.getZoom();
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

	private static final BatikButler batik = new BatikButler();
	private static final double currentTime = 30;
	private static final int FAST = 200;
	private static URL initialScene = null;
	private static final Pattern jcxzPat = Pattern.compile("^.*\\.jc[xz]$");
	private static final Log log = JCLoggerFactory
			.getLogger(JCurlShotPlanner.class);
	private static final Pattern pngPat = Pattern.compile("^.*\\.png$");
	private static final int SLOW = 333;
	private static final Pattern svgPat = Pattern.compile("^.*\\.svgz?$");

	// static void bind(final Object src, final String src_p, final Object dst,
	// final String dst_p) {
	// Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ, src,
	// BeanProperty.create(src_p), dst, BeanProperty.create(dst_p))
	// .bind();
	// }

	private static URL templateScene = null;
	private static final Cursor waitc = Cursor
			.getPredefinedCursor(Cursor.WAIT_CURSOR);
	private static final ZoomHelper zh = new ZoomHelper();

	private static File ensureSuffix(File dst, final Pattern pat,
			final String suffix) {
		final Matcher m = pat.matcher(dst.getName());
		if (!m.matches())
			dst = new File(dst.getAbsoluteFile() + suffix);
		return dst;
	}

	private static JFileChooser jc(final File base, final Pattern pat,
			final String txt) {
		final JFileChooser chooser = new JFileChooser(base);
		// chooser.setName(name);
		chooser.setMultiSelectionEnabled(false);
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(final File f) {
				if (f == null)
					return false;
				return f.isDirectory() || pat.matcher(f.getName()).matches();
			}

			@Override
			public String getDescription() {
				return txt;
			}
		});
		return chooser;
	}

	private static JFileChooser jcx(final File base) {
		return jc(base, jcxzPat, "JCurl Setup Files (.jcx) (.jcz)");
	}

	public static void main(final String[] args) {
		// for debugging reasons only:
		Locale.setDefault(Locale.CANADA);
		launch(JCurlShotPlanner.class, args);
	}

	private static JFileChooser png(final File base) {
		return jc(base, pngPat, "Png Bitmap Images (.png)");
	}

	private static void renderPng(final Container src, final File dst)
			throws IOException {
		final BufferedImage img = new BufferedImage(src.getWidth(), src
				.getHeight(), BufferedImage.TYPE_INT_ARGB);
		final Graphics g = img.getGraphics();
		try {
			src.paintAll(g);
		} finally {
			g.dispose();
		}
		ImageIO.write(img, "png", dst);
	}

	private static JFileChooser svg(final File base) {
		return jc(base, svgPat, "Svg Vector Images (.svg) (.svgz)");
	};

	private final ChangeManager cm = new ChangeManager(this);
	private URL document;
	private File file;
	private boolean modified = false;
	private final TacticsBean tactics = new TacticsBean();
	private final JLabel url = new JLabel();

	private JCurlShotPlanner() {
		// tactics.setName("tactics");
		url.setName("urlLabel");
	};

	private boolean askDiscardUnsaved(final javax.swing.Action action) {
		if (!isModified())
			return true;
		final String title, msg;
		if (true) {
			final ResourceMap r = getContext().getResourceMap();
			title = r.getString("discard" + ".Dialog" + ".title", action
					.getValue(javax.swing.Action.NAME));
			msg = r.getString("discard" + ".Dialog" + ".message");
		} else if (action instanceof ApplicationAction) {
			final ApplicationAction aa = (ApplicationAction) action;
			final ResourceMap r = getContext().getResourceMap();
			title = action == null ? null : r.getString(aa.getName()
					+ ".Dialog" + ".title", aa
					.getValue(javax.swing.Action.NAME));
			msg = r.getString(aa.getName() + ".Dialog" + ".message");
		} else {
			title = null;
			msg = "Discard unsaved changes?";
		}
		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
				getMainFrame(), msg, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
	};

	private boolean askOverwrite(final File f) {
		if (!f.exists())
			return true;
		final String title, msg;
		{
			final ResourceMap r = getContext().getResourceMap();
			title = r.getString("overwrite" + ".Dialog" + ".title");
			msg = r.getString("overwrite" + ".Dialog" + ".message", f
					.toString());
		}
		return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
				getMainFrame(), msg, title, JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);
	};

	private JMenu createMenu(final String menuName, final String[] actionNames) {
		final JMenu menu = new JMenu();
		menu.setName(menuName);
		for (final String actionName : actionNames)
			if (actionName.equals("---"))
				menu.add(new JSeparator());
			else {
				final JMenuItem menuItem = new JMenuItem();
				menuItem.setAction(findAction(actionName));
				menuItem.setIcon(null);
				menu.add(menuItem);
			}
		return menu;
	}

	private JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();

		final String[] fileMenuActionNames = { /*"fileClear",*/"fileNewDoc",
				"fileHammy", "---", "fileOpen", "fileOpenURL", "---",
				"fileReset", "fileSave", "fileSaveAs", "fileSaveCopyAs", "---",
				"fileExportPng", "fileExportSvg", "---", "quit" };
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
			button.setAction(findAction(actionName));
			button.setFocusable(false);
			toolBar.add(button);
		}
		return toolBar;
	}

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editPreferences() {}

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editProperties() {}

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editRedo() {}

	/** Edit Menu Action */
	@Action(enabledProperty = "alwaysFalse")
	public void editUndo() {}

	/** File Menu Action */
	@Action
	private void fileClear() {
		if (!askDiscardUnsaved(findAction("fileClear")))
			return;
		try {
			setDocument(null);
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/** File Menu Action */
	@Action
	public void fileExportPng() {
		final JFileChooser fcPng = png(getFile());
		fcPng.setName("exportPngDialog");
		for (;;) {
			if (JFileChooser.APPROVE_OPTION != fcPng
					.showSaveDialog(getMainFrame()))
				return;
			final File dst = ensureSuffix(fcPng.getSelectedFile(), pngPat,
					".png");
			if (!askOverwrite(dst))
				continue;

			final Cursor cu = switchCursor(waitc);
			try {
				renderPng(tactics, dst);
				return;
			} catch (final Exception e) {
				throw new RuntimeException("Unhandled", e);
			} finally {
				switchCursor(cu);
			}
		}
	}

	/** File Menu Action */
	@Action(enabledProperty = "renderSvgAvailable")
	public void fileExportSvg() {
		final JFileChooser fcSvg = svg(getFile());
		fcSvg.setName("exportSvgDialog");
		for (;;) {
			if (JFileChooser.APPROVE_OPTION != fcSvg
					.showSaveDialog(getMainFrame()))
				return;
			final File dst = ensureSuffix(fcSvg.getSelectedFile(), svgPat,
					".svgz");
			if (!askOverwrite(dst))
				continue;

			final Cursor cu = switchCursor(waitc);
			try {
				batik.renderSvg(tactics, dst);
				return;
			} catch (final Exception e) {
				throw new RuntimeException("Unhandled", e);
			} finally {
				switchCursor(cu);
			}
		}
	}

	/** File Menu Action */
	@Action
	public void fileHammy() {
		if (!askDiscardUnsaved(findAction("fileHammy")))
			return;
		try {
			setDocument(initialScene);
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/** File Menu Action */
	@Action
	public void fileNewDoc() {
		if (!askDiscardUnsaved(findAction("fileNewDoc")))
			return;
		try {
			setDocument(templateScene);
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/** File Menu Action */
	@Action
	public void fileOpen() {
		if (!askDiscardUnsaved(findAction("fileOpen")))
			return;
		final JFileChooser chooser = jcx(getFile());
		chooser.setName("openDialog");
		final int option = chooser.showOpenDialog(getMainFrame());
		if (option == JFileChooser.APPROVE_OPTION) {
			final File file = chooser.getSelectedFile();
			try {
				setDocument(file.toURI().toURL());
			} catch (final MalformedURLException e) {
				// shouldn't happen unless the JRE fails
				log.warn("File.toURI().toURL() failed", e);
			} catch (final IOException e) {
				showErrorDialog("can't open \"" + file + "\"", e);
			}
		}
	}

	/** File Menu Action */
	@Action
	public void fileOpenURL() {
		final String a = "fileOpenURL";
		if (!askDiscardUnsaved(findAction(a)))
			return;
		final ResourceMap r = getContext().getResourceMap();
		final String title = r.getString(a + ".Dialog" + ".title");
		final String msg = r.getString(a + ".Dialog" + ".message");
		for (;;) {
			final String url = JOptionPane.showInputDialog(getMainFrame(), msg,
					title, JOptionPane.QUESTION_MESSAGE);
			if (url == null)
				return;
			try {
				setDocument(new URL(url));
				return;
			} catch (final IOException e) {
				showErrorDialog(r.getString(a + ".Dialog" + ".error", url), e);
			}
		}
	};

	/**
	 * File Menu Action
	 * 
	 * @throws IOException
	 */
	@Action(enabledProperty = "modified")
	public void fileReset() throws IOException {
		if (!askDiscardUnsaved(findAction("fileReset")))
			return;
		final URL tmp = getDocument();
		setDocument(null);
		setDocument(tmp);
	};

	/** File Menu Action */
	@Action(enabledProperty = "modified")
	public void fileSave() {
		if (!isModified())
			return;
		final File f = saveHelper(getFile(), getFile(), "saveDialog", true);
		log.info(f);
		if (f != null) {
			try {
				setDocument(f.toURL(), false);
			} catch (final IOException e) {
				throw new RuntimeException("Unhandled", e);
			}
			setModified(false);
		}
	}

	/** File Menu Action */
	@Action
	public void fileSaveAs() {
		final File f = saveHelper(null, getFile(), "saveAsDialog", false);
		log.info(f);
		if (f != null) {
			try {
				setDocument(f.toURL(), false);
			} catch (final IOException e) {
				throw new RuntimeException("Unhandled", e);
			}
			setModified(false);
		}
	};

	/** File Menu Action */
	@Action
	public void fileSaveCopyAs() {
		log.info(saveHelper(null, getFile(), "saveCopyAsDialog", false));
	};

	private javax.swing.Action findAction(final String actionName) {
		return getContext().getActionMap().get(actionName);
	};

	private URL getDocument() {
		return document;
	}

	private File getFile() {
		return file;
	}

	/** Help Menu Action */
	@Action
	public void helpAbout() {}

	/**
	 * Setting the internal field {@link #document} directly (bypassing
	 * {@link #setDocument(URL)}) is used to deplay the document loading until
	 * {@link #ready()}.
	 */
	@Override
	protected void initialize(final String[] as) {
		final Class<?> mc = this.getClass();
		{
			final String res;
			if (true) {
				final ResourceMap r = Application.getInstance().getContext()
						.getResourceMap();
				res = "/" + r.getResourcesDir()
						+ r.getString("Application.defaultDocument");
			} else
				res = "/" + mc.getPackage().getName().replace('.', '/')
						+ "/resources" + "/" + "default.jcz";
			initialScene = mc.getResource(res);
		}

		// schedule the document to load in #ready()
		document = initialScene;
		for (final String p : as) {
			// ignore javaws parameters
			if ("-open".equals(p) || "-print".equals(p))
				continue;
			try {
				document = new URL(p);
				break;
			} catch (final MalformedURLException e) {
				final File f = new File(p);
				if (f.canRead())
					try {
						document = f.toURL();
						break;
					} catch (final MalformedURLException e2) {
						log.warn("Cannot load '" + p + "'.", e);
					}
				else
					log.warn("Cannot load '" + p + "'.", e);
			}
		}
	}

	public boolean isAlwaysFalse() {
		return false;
	}

	public boolean isModified() {
		return modified;
	}

	public boolean isRenderSvgAvailable() {
		return batik.isBatikAvailable();
	}

	@Override
	protected void ready() {
		final URL tmp = document;
		document = null;
		try {
			setDocument(tmp);
		} catch (final IOException e) {
			log.warn("Couldn't load '" + tmp + "'.", e);
		}
		addExitListener(new Application.ExitListener() {
			public boolean canExit(final EventObject e) {
				return askDiscardUnsaved(findAction("quit"));
			}

			public void willExit(final EventObject e) {
				log.info("Good bye!");
			}
		});
	}

	private final void save(final TrajectorySet cts, final File dst)
			throws IOException {
		final Cursor cu = switchCursor(waitc);
		try {
			final IOTrajectories t = new IOTrajectories();
			// TODO add annotations
			t.trajectories().add(cts);
			new JCurlSerializer().write(t, dst, JDKSerializer.class);
		} finally {
			switchCursor(cu);
		}
	}

	private File saveHelper(File dst, final File base, final String name,
			final boolean forceOverwrite) {
		JFileChooser fcJcx = null;
		for (;;) {
			if (fcJcx == null) {
				fcJcx = jcx(base);
				fcJcx.setName(name);
			}
			if (dst == null) {
				if (JFileChooser.APPROVE_OPTION != fcJcx
						.showSaveDialog(getMainFrame()))
					return null;
				dst = fcJcx.getSelectedFile();
			}
			if (dst == null)
				continue;
			dst = ensureSuffix(dst, jcxzPat, ".jcz");
			if (forceOverwrite || askOverwrite(dst))
				try {
					save(tactics.getCurves(), dst);
					return dst;
				} catch (final Exception e) {
					showErrorDialog("Couldn't save to '" + dst + "'", e);
				}
		}
	}

	private void setDocument(final URL document) throws IOException {
		this.setDocument(document, true);
	}

	private void setDocument(final URL document, boolean load)
			throws IOException {
		final Cursor cu = switchCursor(waitc);
		try {
			log.info(document);
			final URL old = this.document;
			this.firePropertyChange("document", old, this.document = document);
			setFile(this.document);
			url.setText(this.document == null ? "{null}" : this.document
					.toString());
			if (!load)
				return;

			cm.deregister(tactics.getCurves());
			final CurveManager cts;
			if (this.document == null)
				cts = null;
			else {
				final IONode n = new JCurlSerializer().read(this.document);
				final IOTrajectories it = (IOTrajectories) n;
				final TrajectorySet ts = it.trajectories().get(0);
				cts = (CurveManager) ts;
			}
			if (cts != null)
				cts.setCurrentTime(currentTime);
			tactics.setCurves(cts);
			// TODO just push to the Swing Trajectory Bean
			// bpm = tp.getBroom();
			cm.register(tactics.getCurves());
			setModified(false);
		} finally {
			switchCursor(cu);
		}
	}

	private void setFile(final URL url) {
		File file;
		if (url != null && "file".equals(url.getProtocol()))
			try {
				file = new File(url.toURI());
			} catch (final URISyntaxException e) {
				file = null;
			}
		else
			file = null;
		final File old = this.file;
		this.firePropertyChange("file", old, this.file = file);
	};

	public void setModified(final boolean modified) {
		final boolean old = this.modified;
		firePropertyChange("modified", old, this.modified = modified);
	}

	private void showErrorDialog(final String message, final Exception e) {
		JOptionPane.showMessageDialog(getMainFrame(), "Error: " + message,
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	protected void startup() {
		// set the window icon:
		{
			final Image img;
			if (true)
				img = getContext().getResourceMap().getImageIcon(
						"Application.icon").getImage();
			else {
				final ResourceMap r = getContext().getResourceMap();
				if (true)
					try {
						img = ImageIO.read(this.getClass().getResource(
								"/" + r.getResourcesDir() + "/"
										+ r.getString("Application.icon")));
					} catch (final IOException e) {
						throw new RuntimeException("Unhandled", e);
					}
				else
					img = Toolkit.getDefaultToolkit().createImage(
							this.getClass().getResource(
									"/" + r.getResourcesDir() + "/"
											+ r.getString("Application.icon")));
			}
			getMainFrame().setIconImage(img);
			// SystemTray tray = SystemTray.getSystemTray();
		}

		getMainFrame().setJMenuBar(createMenuBar());

		// Either use the binding here or delegate the change event?
		// bind(tactics, "savingSensible", this, "savingSensible");

		final JComponent c = new JPanel();
		c.setLayout(new BorderLayout());
		c.add(tactics, BorderLayout.CENTER);
		c.add(url, BorderLayout.NORTH);
		show(c);

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
		zh.zoom(tactics, ZoomHelper.ActivePlus, SLOW);
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
		zh.pan(tactics, 0.2, 0, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanNorth() {
		zh.pan(tactics, 0, -0.2, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanSouth() {
		zh.pan(tactics, 0, 0.2, FAST);
	}

	/** View Menu Action */
	@Action
	public void viewPanWest() {
		zh.pan(tactics, -0.2, 0, FAST);
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
