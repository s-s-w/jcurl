package org.jcurl;

import junit.framework.TestCase;
import antlr.RecognitionException;
import antlr.TokenStreamException;

public class ExprParserTest extends TestCase {

	public void test010() throws RecognitionException, TokenStreamException {
		final String s = "1+2";
		//final ExprParser p = new ExprParser(new ExprLexer(new StringReader(s)));
		//assertEquals(3, p.expr());
	}
}
