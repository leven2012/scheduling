/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@ow2.org
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
 *  Contributor(s): ActiveEon Team - http://www.activeeon.com
 *
 * ################################################################
 * $$ACTIVEEON_CONTRIBUTOR$$
 */
package functionaltests.nodesource;

import java.net.InetAddress;

import org.ow2.proactive.resourcemanager.common.event.RMEventType;
import org.ow2.proactive.resourcemanager.nodesource.infrastructure.GCMCustomisedInfrastructure;
import org.ow2.proactive.resourcemanager.nodesource.policy.TimeSlotPolicy;

import functionaltests.RMTHelper;


/**
 *
 * Test checks the correct behavior of node source consisted of GCM infrastructure manager
 * and time slot acquisition policy.
 *
 * This test may failed by timeout if the machine is too slow, so gcm deployment takes more than 15 secs
 *
 * NOTE: The way how customized GCM infrastructure is used here is not quite correct. It uses local host
 * to deploy several nodes so it wont be able to remove them correctly one by one. But for the scenario
 * in this test it's not required.
 *
 */
public class TestGCMCustomizedInfrastructureTimeSlotPolicy extends TestGCMInfrastructureTimeSlotPolicy {

    protected byte[] hostsListData;

    @Override
    protected void init() throws Exception {
        super.init();
        hostsListData = InetAddress.getLocalHost().getHostName().getBytes();
    }

    @Override
    protected void createEmptyNodeSource(String sourceName) throws Exception {
        RMTHelper.getAdminInterface().createNodesource(sourceName,
                GCMCustomisedInfrastructure.class.getName(), null, TimeSlotPolicy.class.getName(),
                getPolicyParams());

        RMTHelper.waitForNodeSourceEvent(RMEventType.NODESOURCE_CREATED, sourceName);
    }

    @Override
    protected void createDefaultNodeSource(String sourceName) throws Exception {
        // creating node source
        RMTHelper.getAdminInterface().createNodesource(sourceName,
                GCMCustomisedInfrastructure.class.getName(),
                new Object[] { GCMDeploymentData, hostsListData }, TimeSlotPolicy.class.getName(),
                getPolicyParams());

        RMTHelper.waitForNodeSourceEvent(RMEventType.NODESOURCE_CREATED, sourceName);
    }
}