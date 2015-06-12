package org.objectweb.proactive.examples.multiprotocol;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.api.PARemoteObject;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.body.AbstractBody;
import org.objectweb.proactive.core.body.AbstractUniversalBody;
import org.objectweb.proactive.core.body.BodyImpl;
import org.objectweb.proactive.core.body.LocalBodyStrategy;
import org.objectweb.proactive.core.body.UniversalBody;
import org.objectweb.proactive.core.node.NodeException;
import org.objectweb.proactive.core.remoteobject.RemoteObject;
import org.objectweb.proactive.core.remoteobject.RemoteObjectExposer;
import org.objectweb.proactive.core.remoteobject.RemoteObjectFactory;
import org.objectweb.proactive.core.remoteobject.RemoteObjectHelper;
import org.objectweb.proactive.core.remoteobject.RemoteObjectImpl;
import org.objectweb.proactive.core.runtime.ProActiveRuntime;
import org.objectweb.proactive.core.runtime.ProActiveRuntimeImpl;
import org.objectweb.proactive.*;

import java.io.Serializable;
import java.net.URI;


public class ServerObject implements Serializable{
	
	private String nameObj;
	
	public ServerObject(){
		
	}
	
	public ServerObject(String name){
		this.nameObj = name;
	}
	
	public void requestPrint(String stringToPrint) {
		System.out.println("This is what client, by remote calling, sent me :" + stringToPrint + "\n");
	}

	
	public void unexport(URI uri) {
	
		
		AbstractBody myBody = (AbstractBody) PAActiveObject.getBodyOnThis();
		
		RemoteObjectExposer<UniversalBody> roe = ((AbstractBody) myBody).getRemoteObjectExposer();		
		
	
		try {
			
		System.out.println("trying to remove " + uri.toString());
			
			roe.unexport(uri);
		} catch (ProActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addProtocol(String protocol){
		
		AbstractBody myBody = (AbstractBody) PAActiveObject.getBodyOnThis();
		
		RemoteObjectExposer<UniversalBody> roe = ((AbstractBody) myBody).getRemoteObjectExposer();
		
		try {
			PARemoteObject.bind(roe,RemoteObjectHelper.generateUrl(protocol, this.nameObj));
		} catch (ProActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getName() {
		return nameObj;
	}

	public void setName(String name) {
		this.nameObj = name;
	}
	
	

}

