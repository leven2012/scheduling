/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2007 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@objectweb.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 */
package org.ow2.proactive.scheduler.common.job;

import java.io.IOException;
import java.io.Serializable;

import org.ow2.proactive.scheduler.util.classloading.JarUtils;


/**
 * This class contains all the elements needed for executing the executable
 * contained in a job, as the classpath for Java executables.
 * @author The ProActive team
 */
public class JobEnvironment implements Serializable {

    // job classpath
    // used for resolving classes only on user side !
    private String[] jobClasspath;
    // jar file containing the job classpath
    // TODO cdelbe : stream the classpath file instead of sending it in one shot ?
    private byte[] jobClasspathContent;

    /**
     * return the byte[] representation of the jar file containing the job classpath.
     * @return the byte[] representation of the jar file containing the job classpath.
     */
    public byte[] getJobClasspathContent() {
        return jobClasspathContent;
    }

    /**
     * return the job classpath.
     * @return the job classpath.
     */
    public String[] getJobClasspath() {
        return jobClasspath;
    }

    /**
     * @param jobClasspath the jobClasspath to set
     * @throws IOException if the classpath cannot be built
     */
    public void setJobClasspath(String[] jobClasspath) throws IOException {
        this.jobClasspath = jobClasspath;
        // TODO cdelbe : define version and cp of the jar classpath ?
        this.jobClasspathContent = JarUtils.jarDirectories(jobClasspath, "1.0", null, null);
    }
}
