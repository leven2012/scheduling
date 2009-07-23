/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2008 INRIA/University of Nice-Sophia Antipolis
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
 * $PROACTIVE_INITIAL_DEV$
 */
package org.ow2.proactive.scheduler.common.exception;

/**
 * Exception generated when trying to connect the scheduler without being authenticate.<br>
 * 
 * Try to authenticate before using the Scheduler.
 *
 * @author The ProActive Team
 * @since ProActive Scheduling 1.0
 */
public class AuthenticationException extends SchedulerException {

    /**
     * Create a new instance of AuthenticationException
     * 
     * @param msg the message to attach
     */
    public AuthenticationException(String msg) {
        super(msg);
    }

    /**
     * Create a new instance of AuthenticationException
     * 
     */
    public AuthenticationException() {
    }

    /**
     * Create a new instance of AuthenticationException
     * 
     * @param msg the message to attach
     * @param cause the cause of the exception.
     */
    public AuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create a new instance of AuthenticationException
     * 
     * @param cause the cause of the exception.
     */
    public AuthenticationException(Throwable cause) {
        super(cause);
    }

}