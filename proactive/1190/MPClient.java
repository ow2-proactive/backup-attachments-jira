package org.objectweb.proactive.examples.multiprotocol;

import java.io.IOException;
import java.net.URI;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.remoteobject.RemoteObjectSet.NotYetExposedException;


public class MPClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		String uriString = "rmi://Lorenzo:1099/myActiveServerObject";
				
		URI uri = URI.create(uriString);
			
		try {
			// look up for the (remote) active object on server and get its stub
			ServerObject ro = (ServerObject) PAActiveObject.lookupActive(ServerObject.class, uriString);
 			
			// force the communication protocol
			PAActiveObject.forceProtocol(ro, "http");

			// let (remote) active object print some stuff
			ro.requestPrint("\nHere I am, server! This is " + args[0]);
			
			// log remote active object url
			System.out.println("\nI am " + args[0] + " The remote object is at: " + uri);
		
		} catch (ProActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (NotYetExposedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}

