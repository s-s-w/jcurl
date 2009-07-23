/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
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

package org.jcurl.demo.tactics.old;

import java.io.File;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class MenuEdit {
	@ActionRegistry.JCMenu("&Edit")
	public static class Controller {
		private static final Log log = JCLoggerFactory
				.getLogger(Controller.class);
		private Model model;

		@ActionRegistry.JCAction(title = "Pr&eferences", idx = 40, separated = true)
		public void editPreferences() {
			log.info("");
		}

		@ActionRegistry.JCAction(title = "&Properties", idx = 30, separated = true)
		public void editProperties() {
			log.info("");
		}

		@ActionRegistry.JCAction(title = "&Redo", idx = 20, accelerator = "CTRL-Y")
		public void editRedo() {
			log.info("");
		}

		@ActionRegistry.JCAction(title = "&Undo", idx = 10, accelerator = "CTRL-Z")
		public void editUndo() {
			log.info("");
		}

		public Model getModel() {
			return model;
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
