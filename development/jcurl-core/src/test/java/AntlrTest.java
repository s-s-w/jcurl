
import java.io.StringReader;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;

import junit.framework.TestCase;

/**
 * http://www.cs.usfca.edu/~parrt/course/652/lectures/antlr.html
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class AntlrTest extends TestCase {

    public void test010() throws RecognitionException, TokenStreamException {
        final StringReader s = new StringReader("3+(4*5)");
        final ExprParser parser = new ExprParser(new ExprLexer(s));
        parser.expr();
        final AST t = parser.getAST();
        assertEquals(" 3", t.toStringTree());
    }
}
