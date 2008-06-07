/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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

package javolution.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javolution.xml.stream.XMLStreamException;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.RockSetUtils;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class XmlTest extends TestCase {
	static class JCBinding extends XMLBinding {
		private static final long serialVersionUID = -465814438193508450L;

		private final XMLFormat<Rock> rock;
		private final XMLFormat<RockSet> rockset;

		JCBinding() {
			setAlias(RockDouble.class, "rock");
			rock = new XMLFormat<Rock>(null) {
				@Override
				public void read(final InputElement xml, final Rock foo)
						throws XMLStreamException {
					foo.setLocation(xml.getAttribute("x", Double.NaN), xml
							.getAttribute("y", Double.NaN), xml.getAttribute(
							"a", Double.NaN));
				}

				@Override
				public void write(final Rock foo, final OutputElement xml)
						throws XMLStreamException {
					xml.setAttribute("x", foo.getX());
					xml.setAttribute("y", foo.getY());
					xml.setAttribute("a", foo.getA());
				}
			};
			setAlias(RockSet.class, "rockset");
			rockset = new XMLFormat<RockSet>(null) {
				@Override
				public void read(final InputElement arg0, final RockSet arg1)
						throws XMLStreamException {
					Rock[] tmp = (Rock[]) arg0.get("dark");
					for (int i = 0; i < RockSet.ROCKS_PER_COLOR; i++)
						arg1.getDark(i).setLocation(tmp[i]);
					tmp = (Rock[]) arg0.get("light");
					for (int i = 0; i < RockSet.ROCKS_PER_COLOR; i++)
						arg1.getLight(i).setLocation(tmp[i]);
				}

				@Override
				public void write(final RockSet foo, final OutputElement xml)
						throws XMLStreamException {
					final Rock[] tmp = new Rock[RockSet.ROCKS_PER_COLOR];
					for (int i = 0; i < RockSet.ROCKS_PER_COLOR; i++)
						tmp[i] = foo.getDark(i);
					xml.add(tmp, "dark");
					for (int i = 0; i < RockSet.ROCKS_PER_COLOR; i++)
						tmp[i] = foo.getLight(i);
					xml.add(tmp, "light");
				}
			};
		}

		@Override
		public <T> XMLFormat<T> getFormat(final Class<T> cls) {
			if (Rock.class.isAssignableFrom(cls))
				return (XMLFormat<T>) rock;
			if (RockSet.class.isAssignableFrom(cls))
				return (XMLFormat<T>) rockset;
			return super.getFormat(cls);
		}
	}

	private static final Log log = JCLoggerFactory.getLogger(XmlTest.class);

	public void testRock() throws XMLStreamException {
		final XMLBinding jc = new JCBinding();
		final String xml;
		{
			final XMLObjectWriter wri = new XMLObjectWriter().setBinding(jc);
			final StringWriter bout = new StringWriter();
			wri.setOutput(bout);
			wri.write(new RockDouble(1, 2, 3));
			wri.close();
			xml = bout.getBuffer().toString();
			assertEquals(
					"<?xml version=\"1.0\" ?><rock x=\"1.0\" y=\"2.0\" a=\"3.0\"/>",
					xml);

		}
		{
			final XMLObjectReader rea = new XMLObjectReader().setBinding(jc);
			rea.setInput(new StringReader(xml));
			final RockDouble r = rea.read();
			assertEquals("[1.0, 2.0, 3.0]", r.toString());
		}
	}

	public void testRockSet() throws XMLStreamException {
		final XMLBinding jc = new JCBinding();
		final String xml;
		{
			final XMLObjectWriter wri = new XMLObjectWriter().setBinding(jc);
			final StringWriter bout = new StringWriter();
			wri.setOutput(bout);
			wri.write(RockSetUtils.allHome());
			wri.close();
			xml = bout.getBuffer().toString();
			assertEquals(
					"<?xml version=\"1.0\" ?><rockset><dark class=\"[Lorg.jcurl.core.api.Rock;\" componentType=\"org.jcurl.core.api.Rock\" length=\"8\"><rock x=\"-2.2098000049591064\" y=\"9.448800086975097\" a=\"0.0\"/><rock x=\"-2.2098000049591064\" y=\"9.083040237426757\" a=\"0.0\"/><rock x=\"-2.2098000049591064\" y=\"8.717280387878418\" a=\"0.0\"/><rock x=\"-2.2098000049591064\" y=\"8.351519584655761\" a=\"0.0\"/><rock x=\"-2.2098000049591064\" y=\"7.98576021194458\" a=\"0.0\"/><rock x=\"-2.2098000049591064\" y=\"7.619999885559082\" a=\"0.0\"/><rock x=\"-2.2098000049591064\" y=\"7.254240036010742\" a=\"0.0\"/><rock x=\"-2.2098000049591064\" y=\"6.888480186462402\" a=\"0.0\"/></dark><light class=\"[Lorg.jcurl.core.api.Rock;\" componentType=\"org.jcurl.core.api.Rock\" length=\"8\"><rock x=\"2.2098000049591064\" y=\"9.448800086975097\" a=\"0.0\"/><rock x=\"2.2098000049591064\" y=\"9.083040237426757\" a=\"0.0\"/><rock x=\"2.2098000049591064\" y=\"8.717280387878418\" a=\"0.0\"/><rock x=\"2.2098000049591064\" y=\"8.351519584655761\" a=\"0.0\"/><rock x=\"2.2098000049591064\" y=\"7.98576021194458\" a=\"0.0\"/><rock x=\"2.2098000049591064\" y=\"7.619999885559082\" a=\"0.0\"/><rock x=\"2.2098000049591064\" y=\"7.254240036010742\" a=\"0.0\"/><rock x=\"2.2098000049591064\" y=\"6.888480186462402\" a=\"0.0\"/></light></rockset>",
					xml);

		}
		{
			final XMLObjectReader rea = new XMLObjectReader().setBinding(jc);
			rea.setInput(new StringReader(xml));
			try {
				rea.read();
				fail();
			} catch (final XMLStreamException e) {
				assertEquals(InstantiationException.class, e
						.getNestedException().getClass());
			}
		}
	}
}
