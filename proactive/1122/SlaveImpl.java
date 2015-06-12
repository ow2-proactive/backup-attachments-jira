package fr.inria.oasis.proactive.components.test;

import java.util.List;

public class SlaveImpl implements Slave {

	public void compute(List<String> arg) {
		System.out.println("I am working a lot");
	}

}