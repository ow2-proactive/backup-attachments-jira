package org.objectweb.proactive.examples.multiprotocol;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.api.PARemoteObject;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.body.AbstractBody;
import org.objectweb.proactive.core.body.UniversalBody;
import org.objectweb.proactive.core.remoteobject.RemoteObjectExposer;
import org.objectweb.proactive.core.remoteobject.RemoteObjectHelper;
import org.objectweb.proactive.core.remoteobject.exception.UnknownProtocolException;

import java.net.URI;


public class ServerObject{
	
	public ServerObject(){
		
	}
	
	public void requestPrint(String stringToPrint) {
		
		System.out.println("This is what client, by remote calling, sent me :" + stringToPrint + "\n");
	}

	
	public void unexport(URI uri) throws ProActiveException {
	
		AbstractBody myBody = (AbstractBody) PAActiveObject.getBodyOnThis();
		
		RemoteObjectExposer<UniversalBody> roe = ((AbstractBody) myBody).getRemoteObjectExposer();		
		
		System.out.println("trying to remove " + uri.toString());
		
		roe.unexport(uri);	
	}
	
	public void addProtocol(String protocol, String nameObj) throws UnknownProtocolException, ProActiveException{
		
		AbstractBody myBody = (AbstractBody) PAActiveObject.getBodyOnThis();
		
		RemoteObjectExposer<UniversalBody> roe = ((AbstractBody) myBody).getRemoteObjectExposer();
		
		PARemoteObject.bind(roe,RemoteObjectHelper.generateUrl(protocol, nameObj));
	}

}
