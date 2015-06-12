package fr.inria.oasis.proactive.components.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.core.config.CentralPAPropertyRepository;

public class Main {

	private static Factory factory;

	static {
		CentralPAPropertyRepository.GCM_PROVIDER
				.setValue("org.objectweb.proactive.core.component.Fractive");
		try {
			factory = FactoryFactory.getFactory();
		} catch (ADLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		new Main().run();
	}

	public void run() {
		ExecutorService threadPool = Executors.newFixedThreadPool(8);

		List<Future<Component>> futures = new ArrayList<Future<Component>>();

		for (int i = 0; i < 16; i++) {
			futures.add(threadPool.submit(new Callable<Component>() {
				public Component call() throws Exception {
					Component c = null;
					try {
						c = createComponent();
					} catch (ADLException e) {
						e.printStackTrace();
					} catch (IllegalLifeCycleException e) {
						e.printStackTrace();
					} catch (NoSuchInterfaceException e) {
						e.printStackTrace();
					}

					return c;
				}
			}));
		}

		threadPool.shutdown();

		for (int i = 0; i < futures.size(); i++) {
			try {
				System.out.println(i + ". " + futures.get(i).get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private Component createComponent() throws ADLException,
			IllegalLifeCycleException, NoSuchInterfaceException {
		Component slave = (Component) factory.newComponent(
				"fr.inria.oasis.proactive.components.test.Slave",
				new HashMap<String, Object>());

		GCM.getGCMLifeCycleController(slave).startFc();

		return slave;
	}

}
