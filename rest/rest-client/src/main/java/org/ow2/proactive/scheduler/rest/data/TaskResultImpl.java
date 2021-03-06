/*
 *  
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
package org.ow2.proactive.scheduler.rest.data;

import java.io.Serializable;
import java.util.Map;

import javax.swing.JPanel;

import org.ow2.proactive.scheduler.common.task.TaskId;
import org.ow2.proactive.scheduler.common.task.TaskLogs;
import org.ow2.proactive.scheduler.common.task.TaskResult;
import org.ow2.proactive.scheduler.common.task.flow.FlowAction;
import org.ow2.proactive_grid_cloud_portal.scheduler.dto.TaskResultData;

import static org.ow2.proactive_grid_cloud_portal.utils.ObjectUtility.object;


public class TaskResultImpl implements TaskResult {
    private static final long serialVersionUID = 1L;

    private TaskId id;
    private byte[] serializedValue;
    private Serializable value;
    private boolean hadException;
    private TaskLogs taskLogs;

    public TaskResultImpl(TaskId id, TaskResultData d) {
        this.id = id;
        this.serializedValue = d.getSerializedValue();
    }

    @Override
    public FlowAction getAction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Throwable getException() {
        throw new UnsupportedOperationException();
    }

    public void setOutput(TaskLogs taskLogs) {
        this.taskLogs = taskLogs;
    }

    @Override
    public TaskLogs getOutput() {
        return taskLogs;
    }

    @Override
    public Map<String, byte[]> getPropagatedVariables() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getSerializedValue() {
        return serializedValue;
    }

    @Override
    public TaskId getTaskId() {
        return id;
    }

    public void setHadException(boolean hadException) {
        this.hadException = hadException;
    }

    @Override
    public boolean hadException() {
        return hadException;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }

    @Override
    public Serializable value() throws Throwable {
        return (null == value) ? ((null == serializedValue) ? null : (Serializable) object(serializedValue))
                : value;

    }

}
