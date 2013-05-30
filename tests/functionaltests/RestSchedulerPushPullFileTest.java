/*
 *  *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2011 INRIA/University of
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
package functionaltests;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.log4j.Level;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.proactive.core.util.log.ProActiveLogger;
import org.ow2.proactive.scheduler.common.Scheduler;
import org.ow2.proactive.scheduler.common.SchedulerConstants;
import org.ow2.proactive.scheduler.common.SchedulerState;
import org.ow2.proactive.scheduler.common.job.JobId;
import org.ow2.proactive.scheduler.common.job.JobState;
import org.ow2.proactive.scheduler.core.SchedulerFrontend;
import org.ow2.proactive_grid_cloud_portal.scheduler.SchedulerStateRest;


/**
 * RestSchedulerPushPullFileTest
 *
 * This test tries to push and pull a file to the Global and User space
 *
 * @author The ProActive Team
 */
public class RestSchedulerPushPullFileTest extends AbstractRestFuncTestCase {
    @BeforeClass
    public static void beforeClass() throws Exception {
        ProActiveLogger.getLogger(SchedulerStateRest.class).setLevel(Level.DEBUG);
        try {
            RestFuncTHelper.startRestfulSchedulerWebapp();
        } catch (Exception e) {
            e.printStackTrace();
            RestFuncTHelper.stopRestfulSchedulerWebapp();
            throw e;
        }
    }

    @AfterClass
    public static void afterClass() {
        RestFuncTHelper.stopRestfulSchedulerWebapp();
    }

    @Before
    public void setUp() throws Exception {
        Scheduler scheduler = RestFuncTHelper.getScheduler();
        SchedulerState state = scheduler.getState();
        List<JobState> jobStates = new ArrayList<JobState>();
        jobStates.addAll(state.getPendingJobs());
        jobStates.addAll(state.getRunningJobs());
        jobStates.addAll(state.getFinishedJobs());
        for (JobState jobState : jobStates) {
            JobId jobId = jobState.getId();
            scheduler.killJob(jobId);
            scheduler.removeJob(jobId);
        }
    }

    @Test
    public void testPushPull() throws Exception {
        Scheduler scheduler = RestFuncTHelper.getScheduler();

        String destPath = "test/push/pull";

        String[] spacesNames = { SchedulerConstants.GLOBALSPACE_NAME, SchedulerConstants.USERSPACE_NAME };

        String[] spacesPaths = { ((SchedulerFrontend) scheduler).getGlobalSpaceURIs().get(0),
                ((SchedulerFrontend) scheduler).getGlobalSpaceURIs().get(0)};

        for (int i = 0; i < spacesNames.length; i++) {
            String spaceName = spacesNames[i];
            String spacePath = spacesPaths[i];
            File testPushFile = RestFuncTHelper.getDefaultJobXmlfile();
            // you can test pushing pulling a big file :
            // testPushFile = new File("path_to_a_big_file");
            File destFile = new File(new File(spacePath, destPath), testPushFile.getName());
            if (destFile.exists()) {
                destFile.delete();
            }

            // PUSHING THE FILE
            String pushfileUrl = getResourceUrl("dataspace/" + spaceName + "/" +
                URLEncoder.encode(destPath, "UTF-8"));

            HttpPost reqPush = new HttpPost(pushfileUrl);
            setSessionHeader(reqPush);
            // we push a xml job as a simple test

            MultipartEntity multipartEntity = new MultipartEntity();
            multipartEntity.addPart("fileName", new StringBody(testPushFile.getName()));
            multipartEntity.addPart("fileContent", new InputStreamBody(FileUtils
                    .openInputStream(testPushFile), MediaType.APPLICATION_OCTET_STREAM, null));
            reqPush.setEntity(multipartEntity);
            HttpResponse response = executeUriRequest(reqPush);

            System.out.println(response.getStatusLine());
            assertHttpStatusOK(response);

            Assert.assertTrue(destFile + " exists", destFile.exists());

            Assert.assertTrue("Original file and result are equals for " + spaceName, FileUtils
                    .contentEquals(testPushFile, destFile));

            // LISTING THE TARGET DIRECTORY
            String pullListUrl = getResourceUrl("dataspace/" + spaceName + "/" +
                URLEncoder.encode(destPath, "UTF-8"));

            HttpGet reqPullList = new HttpGet(pullListUrl);
            setSessionHeader(reqPullList);

            HttpResponse response2 = executeUriRequest(reqPullList);

            System.out.println(response2.getStatusLine());
            assertHttpStatusOK(response2);

            InputStream is = response2.getEntity().getContent();
            List<String> lines = IOUtils.readLines(is);
            Assert.assertTrue("Nb Files == 1", lines.size() == 1);
            Assert.assertTrue("Pushed file correctly listed", lines.get(0).equals(testPushFile.getName()));

            // PULLING THE FILE
            String pullfileUrl = getResourceUrl("dataspace/" + spaceName + "/" +
                URLEncoder.encode(destPath + "/" + testPushFile.getName(), "UTF-8"));

            HttpGet reqPull = new HttpGet(pullfileUrl);
            setSessionHeader(reqPull);

            HttpResponse response3 = executeUriRequest(reqPull);

            System.out.println(response3.getStatusLine());
            assertHttpStatusOK(response3);

            InputStream is2 = response3.getEntity().getContent();

            File answerFile = File.createTempFile("answer", ".xml");
            FileUtils.copyInputStreamToFile(is2, answerFile);

            Assert.assertTrue("Original file and result are equals for " + spaceName, FileUtils
                    .contentEquals(answerFile, testPushFile));

            // DELETING THE HIERARCHY
            String deleteUrl = getResourceUrl("dataspace/" + spaceName + "/" +
                URLEncoder.encode("test", "UTF-8"));
            HttpDelete reqDelete = new HttpDelete(deleteUrl);
            setSessionHeader(reqDelete);

            HttpResponse response4 = executeUriRequest(reqDelete);

            System.out.println(response4.getStatusLine());
            assertHttpStatusOK(response4);

            Assert.assertTrue(destFile + " does not exist", !destFile.exists());

        }
    }
}
