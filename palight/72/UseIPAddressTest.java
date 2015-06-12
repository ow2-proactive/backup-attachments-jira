package functionalTests.properties;

import java.net.InetAddress;

import org.objectweb.proactive.core.config.PAProperties;

import functionalTests.FunctionalTest;

public class UseIPAddressTest extends FunctionalTest{

    @org.junit.Test
    public void action() throws Exception {
    	if(PAProperties.PA_NET_USE_IP_ADDRESS.isSet() && PAProperties.PA_HOSTNAME.isSet()) {
    		if(PAProperties.PA_NET_USE_IP_ADDRESS.isTrue())
    			InetAddress.getByName(PAProperties.PA_HOSTNAME.getValue());
    	}
    }
    
}
