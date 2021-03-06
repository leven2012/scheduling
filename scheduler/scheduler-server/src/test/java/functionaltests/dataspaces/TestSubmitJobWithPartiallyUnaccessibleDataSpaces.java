/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
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
 *  * $$PROACTIVE_INITIAL_DEV$$
 */
package functionaltests.dataspaces;

import functionaltests.utils.SchedulerFunctionalTestWithCustomConfigAndRestart;
import functionaltests.utils.SchedulerTHelper;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.objectweb.proactive.extensions.vfsprovider.FileSystemServerDeployer;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;


/**
 * TestSubmitJobWithUnaccessibleDataSpaces
 *
 * This test configures the scheduler with a list of dataspace with multi-urls some are accessible and others are not
 * it then submits a job that transfer a file from one space to another and makes sure that the file is transferred
 *
 * it is mainly to test that the TaskLauncher is able to skip unaccessible dataspace urls and keep the accessible one
 *
 * @author The ProActive Team
 */
public class TestSubmitJobWithPartiallyUnaccessibleDataSpaces extends SchedulerFunctionalTestWithCustomConfigAndRestart {

    static URL jobDescriptor = TestSubmitJobWithPartiallyUnaccessibleDataSpaces.class
            .getResource("/functionaltests/dataspaces/Job_DataSpacePartiallyUnacc.xml");

    static URL configFile = TestSubmitJobWithPartiallyUnaccessibleDataSpaces.class
            .getResource("/functionaltests/dataspaces/schedulerPropertiesPartiallyUnaccessibleSpaces.ini");
    static File spaceRoot;
    static File spaceRootUser;

    @ClassRule
    static public TemporaryFolder tmpFolder = new TemporaryFolder();

    static FileSystemServerDeployer deployer;

    @BeforeClass
    public static void before() throws Throwable {
        spaceRoot = tmpFolder.newFolder("space");
        spaceRootUser = new File(spaceRoot, "demo");
        spaceRootUser.mkdirs();

        deployer = new FileSystemServerDeployer(spaceRoot.getAbsolutePath(), false);
        SchedulerTHelper.log("Dataspace started in : " + spaceRoot.getAbsolutePath());
        File inputFile = new File(spaceRootUser, "myfilein1");
        inputFile.createNewFile();
        File propertiesfile = new File(configFile.toURI());
        String propContent = FileUtils.readFileToString(propertiesfile, Charset.defaultCharset().toString());
        String newContent = propContent.replace("$$TOREPLACE$$", deployer.getVFSRootURL());
        FileUtils.writeStringToFile(propertiesfile, newContent);

        schedulerHelper = new SchedulerTHelper(true, propertiesfile.getAbsolutePath());
    }

    @Test
    public void testSubmitJobWithPartiallyUnaccessibleDataSpaces() throws Throwable {

        schedulerHelper.testJobSubmission(new File(jobDescriptor.toURI())
                .getAbsolutePath());

        File lastoutputFile = new File(spaceRootUser, "myfileout4");
        assertTrue(lastoutputFile + " exists", lastoutputFile.exists());
    }

    @After
    public void after() throws Throwable {
        if (deployer != null) {
            deployer.terminate();
        }
    }

}
