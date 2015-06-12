import java.io.Serializable;

import org.objectweb.proactive.Body;
import org.objectweb.proactive.EndActive;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;


public class AOCrash2 implements Serializable, EndActive {
    
    public AOCrash2() {
        
    }
    
    public StringWrapper foo2()  {
        StringBuilder sb = new StringBuilder();
        for (long i=0L; i < 10000000L; i++) {
            sb.append("X");
        }
        StringWrapper sw = new StringWrapper(sb.toString());
        return sw;
    }


    public void endActivity(Body body) {
        throw new RuntimeException("Unexpected end of activity in "+this.getClass().getName());
    }
    
    

}
