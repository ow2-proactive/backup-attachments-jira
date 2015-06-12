package org.objectweb.proactive.examples.multiprotocol;

import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.node.NodeException;
import org.objectweb.proactive.api.*;



public class MPServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		System.out.println("Hello, I am the server!!\n\n");

	// get an object
        ServerObject so = new ServerObject("myActiveServerObject");
        
        
        try {
        	// transform it into an active obj
			so = (ServerObject) PAActiveObject.turnActive(so);
			
			so.addProtocol("pamr");	
			so.addProtocol("pnp");	
			so.addProtocol("rmi");	
			so.addProtocol("http");	

		} catch (ActiveObjectCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		} catch (ProActiveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

	}

}




