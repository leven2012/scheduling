/*
 * Created on Sep 10, 2003
 */
package nonregressiontest.activeobject.equality;

import nonregressiontest.descriptor.defaultnodes.TestNodes;

import nonregressiontest.group.A;

import org.objectweb.proactive.ProActive;
import org.objectweb.proactive.core.group.ProActiveGroup;
import org.objectweb.proactive.core.util.UrlBuilder;

import testsuite.test.Assertions;
import testsuite.test.FunctionalTest;


/**
 * This test has to be run AFTER the tests of :
 * - active object creation
 * - lookupactive
 * - groups
 * 
 * @author Matthieu Morel
 */
public class Test extends FunctionalTest {
    private A group1 = null;
    private A group2 = null;
    private A group3 = null;
    private A a1 = null;
    private A a2 = null;
    private nonregressiontest.activeobject.lookupactive.A registeredA1 = null;
    private nonregressiontest.activeobject.lookupactive.A registeredA2 = null;
    

    public Test() {
        super("comparisons with active objects using equals", "compares active objects, groups and standard objects using with the equals method");
    }

    public void action() throws Exception {
        a1 = (A) ProActive.newActive(A.class.getName(), new Object[] { "a1" },
                TestNodes.getSameVMNode());
        a2 = (A) ProActive.newActive(A.class.getName(), new Object[] { "a2" },
                TestNodes.getLocalVMNode());

        group1 = (A) ProActiveGroup.newGroup(A.class.getName());
        group2 = (A) ProActiveGroup.newGroup(A.class.getName());
        group3 = (A) ProActiveGroup.newGroup(A.class.getName());
        
        registeredA1 = (nonregressiontest.activeobject.lookupactive.A)ProActive.newActive(nonregressiontest.activeobject.lookupactive.A.class.getName(), new Object[] { "toto" });
        Thread.sleep(5000);
        registeredA2 = (nonregressiontest.activeobject.lookupactive.A) ProActive.lookupActive(nonregressiontest.activeobject.lookupactive.A.class.getName(), UrlBuilder.buildUrlFromProperties("localhost","A"));
        
        ProActiveGroup.getGroup(group1).add(a1);
        ProActiveGroup.getGroup(group1).add(a2);

        ProActiveGroup.getGroup(group2).add(a1);
        ProActiveGroup.getGroup(group2).add(a2);

        ProActiveGroup.getGroup(group3).add(a1);
        
    }

    public void initTest() throws Exception {
        // nothing to do
    }

    public void endTest() throws Exception {
        // nothing to do
    }

    public boolean postConditions() throws Exception {
        Assertions.assertEquals(group1, group2);
        Assertions.assertNonEquals(group1, group3);
        Assertions.assertNonEquals(a1, "x");
        Assertions.assertNonEquals(a1, null);
        Assertions.assertNonEquals(group1, "x");
        Assertions.assertNonEquals(group1, null);
        Assertions.assertNonEquals(a1, group3);
        Assertions.assertNonEquals(group3, a1);
        Assertions.assertEquals(registeredA1, registeredA2);
        
        return true;
    }
}
