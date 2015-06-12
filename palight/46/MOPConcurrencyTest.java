/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2013 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 *  * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.mop;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import org.junit.Assert;
import org.objectweb.proactive.core.Constants;


/**
 * MopConcurrencyTest
 *
 * @author The ProActive Team
 **/
public class MOPConcurrencyTest {

    public static final int NUMBER_OF_CLASSES = 500;

    public static final int NUMBER_OF_THREADS = 20;

    public static final String METHOD_NAME = "hello";

    @org.junit.Test
    public void testMopConcurrency() throws Throwable {
        String classNameBase = "test.MopConcurrencyTest";
        final ArrayList<String> classNames = new ArrayList<String>();
        for (int i = 1; i <= NUMBER_OF_CLASSES; i++) {
            classNames.add(classNameBase + i);
        }
        loadClassesIntoMop(classNames);

        ExecutorService service = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        final CountDownLatch latch = new CountDownLatch(NUMBER_OF_THREADS);

        ArrayList<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            final int index = i;
            tasks.add(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    // we synchronize all workers to maximize concurrency issues
                    latch.countDown();
                    latch.await();

                    for (String className : classNames) {

                        if (index % 2 == 0) {
                            StubObject found = (StubObject) MOP.newInstance(className, new Class[0], null, Constants.DEFAULT_FUTURE_PROXY_CLASS_NAME, null);

                        } else {
                            JavassistByteCodeStubBuilder.create(className, null);
                            Thread.sleep(20);
                        }

                    }
                    return true;
                }
            });
        }
        List<Future<Boolean>> results = service.invokeAll(tasks, 120, TimeUnit.SECONDS);

        for (Future<Boolean> result : results) {
            if (result.isCancelled()) {
                printAllStackTraces();
            }
            Assert.assertFalse(result.isCancelled());
            Assert.assertTrue(result.get());
        }

    }

    private static void printAllStackTraces() {

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        System.err.println(Arrays.toString(bean.dumpAllThreads(true, true))) ;


//        Map liveThreads = Thread.getAllStackTraces();
//        for (Iterator i = liveThreads.keySet().iterator(); i.hasNext(); ) {
//            Thread key = (Thread) i.next();
//            System.err.println("Thread " + key.getName());
//            StackTraceElement[] trace = (StackTraceElement[]) liveThreads.get(key);
//            for (int j = 0; j < trace.length; j++) {
//                System.err.println("\tat " + trace[j]);
//            }
//        }
    }


    /**
     * This method generate empty classes and add them to the MOP
     * @param classNames
     * @throws Exception
     */
    private static void loadClassesIntoMop(ArrayList<String> classNames) throws Exception {
        ArrayList<String> answer = new ArrayList();

        ClassPool pool = ClassPool.getDefault();
        CtClass serializableClass = pool.get("java.io.Serializable");
        CtClass stringClass = pool.get("java.lang.String");
        Set<Map.Entry<String, Class>> classesToLoad = new HashSet<Map.Entry<String, Class>>();

        //create new classes
        for (int i = 0; i < classNames.size(); i++) {
            String className = classNames.get(i);
            CtClass cc = pool.makeClass(className);
            cc.addInterface(serializableClass);

            //create no arg constructor
            CtConstructor cons = CtNewConstructor.defaultConstructor(cc);
            cc.addConstructor(cons);
            Random rn = new Random();
            int randomClassIndex = rn.nextInt(i + 1);

            CtMethod exec1 = CtNewMethod.make(javassist.Modifier.PUBLIC, pool.get(classNames.get(randomClassIndex)), METHOD_NAME, new CtClass[0],
                    new CtClass[0], "return new " + classNames.get(randomClassIndex) + "();", cc);
            cc.addMethod(exec1);

            classesToLoad.add(new AbstractMap.SimpleEntry(className, pool.toClass(cc)));
            answer.add(className);
            cc.defrost();
        }

        Field loadedClassField = MOP.class.getDeclaredField("loadedClass");
        loadedClassField.setAccessible(true);
        Map<String, Class<?>> loadedClass = (Map<String, Class<?>>) loadedClassField.get(null);
        for (Map.Entry<String, Class> entry : classesToLoad) {
            loadedClass.put(entry.getKey(), entry.getValue());
            //MOPClassLoader.classDataCache.put(entry.getKey(),entry.getValue().)
        }
    }

}