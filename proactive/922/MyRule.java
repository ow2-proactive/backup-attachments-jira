import java.util.LinkedList;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;


/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2010 INRIA/University of 
 * 				Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 
 * or a different license than the GPL.
 *
 *  Initial developer(s):               The ActiveEon Team
 *                        http://www.activeeon.com/
 *  Contributor(s):
 *
 * ################################################################
 * $ACTIVEEON_INITIAL_DEV$
 */

/**
 * @author ProActive team
 * @since  ProActive 4.4.0
 */
public class MyRule extends Check {

    /* (non-Javadoc)
     * @see com.puppycrawl.tools.checkstyle.api.Check#getDefaultTokens()
     */
    @Override
    public int[] getDefaultTokens() {
        return new int[] { TokenTypes.METHOD_DEF, TokenTypes.METHOD_CALL, TokenTypes.PACKAGE_DEF };
    }

    String className = null;
    LinkedList<String> methods = new LinkedList<String>();
    
    /* (non-Javadoc)
     * @see com.puppycrawl.tools.checkstyle.api.Check#visitToken(com.puppycrawl.tools.checkstyle.api.DetailAST)
     */
    @Override
    public void visitToken(DetailAST aAST) {
        switch (aAST.getType()) {
            case TokenTypes.METHOD_DEF:
                final DetailAST mid = aAST.findFirstToken(TokenTypes.IDENT);
                final String methodName = mid.getText();

                if (methodName.equals("writeObject")) {
                    className = aAST.getParent().getParent().findFirstToken(TokenTypes.IDENT).getText();
                }
                break;
            case TokenTypes.METHOD_CALL:
                if (className != null) {
                    int type = aAST.getFirstChild().getType();
                    switch (type) {
                        case TokenTypes.DOT:
                            DetailAST clazz = aAST.getFirstChild().getFirstChild();
                            DetailAST methodname = aAST.getFirstChild().getFirstChild().getNextSibling();
                            if (!filterMethod(methodname.getText())) {
                                methods.add(clazz.getText() + "." + methodname.getText());
                            }
                            break;
                        case TokenTypes.IDENT:
                            methods.add("this." + aAST.getFirstChild().getText());
                            break;
                    }
                }
                break;
        }
    }

    private boolean filterMethod(String methodName) {
        if ("writeInt".equals(methodName)) {
            return true;
        }
        
        if ("writeLong".equals(methodName)) {
            return true;
        }
        
        if ("defaultWriteObject".equals(methodName)) {
            return true;
        }
        
        if ("writeObject".equals(methodName)) {
            return true;
        }

        if ("writeBytes".equals(methodName)) {
            return true;
        }

        if ("writeFloat".equals(methodName)) {
            return true;
        }

        if ("writeDouble".equals(methodName)) {
            return true;
        }
        
        if ("writeFields".equals(methodName)) {
            return true;
        }

        if ("writeByte".equals(methodName)) {
            return true;
        }
        
        if ("writeBoolean".equals(methodName)) {
            return true;
        }


        
        return false;
    }
    
    /* (non-Javadoc)
     * @see com.puppycrawl.tools.checkstyle.api.Check#leaveToken(com.puppycrawl.tools.checkstyle.api.DetailAST)
     */
    @Override
    public void leaveToken(DetailAST aAST) {
        switch (aAST.getType()) {
            case TokenTypes.METHOD_DEF:

                final DetailAST mid = aAST.findFirstToken(TokenTypes.IDENT);
                final String methodName = mid.getText();

                if (methodName.equals("writeObject")) {
                    if (!methods.isEmpty()) {
                        System.out.println(className);
                        for (String method : methods) {
                            System.out.println("\t" + method);
                        }
                    }
                    className = null;
                    methods = new LinkedList<String>();
                }
                break;
        }
    }
}
