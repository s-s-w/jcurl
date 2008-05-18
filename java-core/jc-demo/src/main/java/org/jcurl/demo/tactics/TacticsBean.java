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

import java.awt.BorderLayout;
import java.awt.geom.RectangularShape;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.TrajectorySet;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.io.IONode;
import org.jcurl.core.io.IOTrajectories;
import org.jcurl.core.io.JCurlSerializer;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;

/**
 * Tactics Panel Bean. Aggregates a graphics display plus swing-widget based
 * stuff.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class TacticsBean extends JComponent implements Zoomable {
	private static final Log log = JCLoggerFactory.getLogger(TacticsBean.class);
	private static final long serialVersionUID = 887263531452742422L;
	private transient BroomPromptModel bpm = null;
	private transient CurveManager cts = null;
	private URL document = null;
	private transient File file;
	private transient final JLabel l;
	private final TrajectoryPiccolo tp;
	private RectangularShape zoom = null;

	public TacticsBean() {
		setLayout(new BorderLayout());
		this.add(l = new JLabel("{uninitialised}"), BorderLayout.NORTH);
		this.add(tp = new TrajectoryPiccolo(), BorderLayout.CENTER);
		// l.setVisible(true);
		// this.setVisible(true);
	}

	private static final double currentTime = 30;

	public URL getDocument() {
		return document;
	}

	public File getFile() {
		return file;
	}

	public RectangularShape getZoom() {
		return zoom;
	}

	public boolean isModified() {
		return false;
	}

	public boolean isSavingSensible() {
		final File f = getFile();
		if (f == null || !f.isFile() || !f.canWrite())
			return false;
		return isModified();
	}

	public void setDocument(final URL document) throws IOException {
		log.info(document);
		final URL old = this.document;
		this.firePropertyChange("document", old, this.document = document);
		setFile(this.document);
		l.setText(this.document == null ? "{null}" : this.document.toString());

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
		tp.setCurves(cts);
		// rather just push to the Swing Trajectory Bean
		bpm = tp.getBroom();
	}

	private void setFile(final URL uri) {
		File file;
		if (uri != null && "file".equals(uri.getProtocol()))
			try {
				file = new File(uri.toURI());
			} catch (final URISyntaxException e) {
				file = null;
			}
		else
			file = null;
		final File old = this.file;
		this.firePropertyChange("file", old, this.file = file);
	}

	public void setZoom(final RectangularShape viewport) {
		this.setZoom(viewport, 333);
	}

	public void setZoom(final RectangularShape zoom, final int transitionMillis) {
		final RectangularShape old = this.zoom;
		this.firePropertyChange("zoom", old, this.zoom = zoom);
		tp.setZoom(zoom);
	}
}
