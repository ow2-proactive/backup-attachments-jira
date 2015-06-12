import java.io.Serializable;

import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.EndActive;
import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.node.NodeException;
import org.objectweb.proactive.core.util.wrapper.IntWrapper;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;


public class AOCrash1 implements Serializable, EndActive {
    
    private AOCrash2 ao2;


    public AOCrash1() {
        
    }
    
    public AOCrash1(AOCrash2 ao2) {
        this.ao2 = ao2;
    }
    public StringWrapper foo() {
        return ao2.foo2();
    }
    
    public void terminate() {
        PAActiveObject.terminateActiveObject(true);        
    }


    public void endActivity(Body body) {
        System.out.println("Expected end of activity for "+this.getClass().getName());   
    }
    
    public static void main(String[] args) throws ActiveObjectCreationException, NodeException {
        AOCrash2 ao2 = (AOCrash2) PAActiveObject.newActive(AOCrash2.class.getName(), new Object[0]);
        AOCrash1 ao1 = (AOCrash1) PAActiveObject.newActive(AOCrash1.class.getName(), new Object[] {ao2});
        ao1.foo();
        ao1.terminate();
        StringWrapper iw = ao2.foo2();
        System.out.println(iw.stringValue());
    }
}
