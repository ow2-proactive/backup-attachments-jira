import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.RunActive;
import org.objectweb.proactive.Service;
import org.objectweb.proactive.annotation.ImmediateService;
import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.body.request.Request;
import org.objectweb.proactive.core.node.NodeException;


public class ActiveObject {

    public ActiveObject() {

    }

    public void ping(ActiveObject ao, ActiveObject ao2) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ActiveObject.ping()");
        ao.pong((ActiveObject) PAActiveObject.getStubOnThis(), ao2);
    }

    public void pong(ActiveObject ao, ActiveObject ao2) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ActiveObject.pong()");
        ao.ping((ActiveObject) PAActiveObject.getStubOnThis(), ao2);
    }

    public ReturnObject echo() {
        System.out.println("ActiveObject.echo()");
        Thread.dumpStack();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PAActiveObject.getBodyOnThis().getID();
        System.out.println("ActiveObject.echo() ... done");
        return new ReturnObject();
    }

    public void terminate() {
        System.out.println("ActiveObject.terminate()");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PAActiveObject.terminateActiveObject(false);
        System.out.println("ActiveObject.terminate() ..... done");
    }


    public static void main(String[] args) {
        ActiveObject ao1 = null;
        ActiveObject ao2 = null;
        ActiveObject ao3 = null;
        try {
            System.out.println("ActiveObject.main()");
            ao1 = PAActiveObject.newActive(ActiveObject.class, null);
            ao2 = PAActiveObject.newActive(ActiveObject.class, null);
            ao3 = PAActiveObject.newActive(ActiveObject.class, null);
        } catch (ActiveObjectCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NodeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ao1.ping(ao2, ao3);
        try {
            Thread.sleep(1400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ao3.terminate();

    }

}
