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
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.ow2.proactive.resourcemanager.common.event;

import java.io.Serializable;

import org.objectweb.proactive.annotation.PublicAPI;


/**
 * Upper class for RM's event objects
 *
 * @author The ProActive Team
 * @since ProActive Scheduling 0.9
 */
@PublicAPI
public class RMEvent implements Serializable {

    /** Resource manager URL */
    private String RMUrl = null;
    protected RMEventType type;

    /**
     * ProActive empty constructor
     */
    public RMEvent() {
    }

    /**
     * Creates the node event object.
     */
    public RMEvent(RMEventType type) {
        this.type = type;
    }

    /**
     * Returns the RM's URL of the event.
     * @return node source type of the event.
     */
    public String getRMUrl() {
        return this.RMUrl;
    }

    /**
     * Set the RM's URL of the event.
     * @param RMURL URL of the RM to set
     */
    public void setRMUrl(String RMURL) {
        this.RMUrl = RMURL;
    }

    public RMEventType getEventType() {
        return type;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.type + "[" + this.RMUrl + "]";
    }
}
