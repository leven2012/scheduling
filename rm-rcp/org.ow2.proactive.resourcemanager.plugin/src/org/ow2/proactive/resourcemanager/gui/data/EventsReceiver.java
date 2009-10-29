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
package org.ow2.proactive.resourcemanager.gui.data;

import org.eclipse.swt.widgets.Display;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.InitActive;
import org.objectweb.proactive.api.PAActiveObject;
import org.ow2.proactive.resourcemanager.common.event.RMEvent;
import org.ow2.proactive.resourcemanager.common.event.RMInitialState;
import org.ow2.proactive.resourcemanager.common.event.RMNodeEvent;
import org.ow2.proactive.resourcemanager.common.event.RMNodeSourceEvent;
import org.ow2.proactive.resourcemanager.exception.RMException;
import org.ow2.proactive.resourcemanager.frontend.RMEventListener;
import org.ow2.proactive.resourcemanager.frontend.RMMonitoring;
import org.ow2.proactive.resourcemanager.frontend.RMUser;
import org.ow2.proactive.resourcemanager.gui.data.model.RMModel;
import org.ow2.proactive.resourcemanager.gui.views.ResourceExplorerView;
import org.ow2.proactive.resourcemanager.gui.views.ResourcesCompactView;
import org.ow2.proactive.resourcemanager.gui.views.ResourcesTabView;
import org.ow2.proactive.resourcemanager.gui.views.StatisticsView;


public class EventsReceiver implements InitActive, RMEventListener {

    private static final long RM_SERVER_PING_FREQUENCY = 5000;
    private RMModel model = null;
    private RMMonitoring monitor = null;
    private Thread pinger;

    public EventsReceiver() {
    }

    public EventsReceiver(RMMonitoring monitorStub) {
        this.monitor = monitorStub;
    }

    public void initActivity(Body body) {
        model = RMStore.getInstance().getModel();
        RMInitialState initialState = monitor.addRMEventListener((RMEventListener) PAActiveObject
                .getStubOnThis());
        for (RMNodeSourceEvent nodeSourceEvent : initialState.getNodeSource()) {
            model.addNodeSource(nodeSourceEvent);
        }

        for (RMNodeEvent nodeEvent : initialState.getNodesEvents()) {
            model.addNode(nodeEvent);
        }

        model.setUpdateViews(true);
        // Init opened views AFTER model's construction
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                //init Tab view if tab panel is displayed
                if (ResourcesTabView.getTabViewer() != null) {
                    ResourcesTabView.init();
                }
                //init Tree view if tree panel is displayed
                if (ResourceExplorerView.getTreeViewer() != null) {
                    ResourceExplorerView.init();
                    ResourceExplorerView.getTreeViewer().expandAll();
                }
                //init Tree view if tree panel is displayed
                if (ResourcesCompactView.getCompactViewer() != null) {
                    ResourcesCompactView.getCompactViewer().loadMatrix();
                }
                //init stats view if stats panel is displayed
                if (StatisticsView.getStatsViewer() != null) {
                    StatisticsView.init();
                }
            }
        });
        startPinger();
    }

    private void startPinger() {
        pinger = new Thread() {
            @Override
            public void run() {
                while (!pinger.isInterrupted()) {
                    try {
                        try {
                            //try to ping RM server
                            RMUser userAO = RMStore.getInstance().getRMUser();
                            if (!PAActiveObject.pingActiveObject(userAO)) {
                                throw new RMException("RM seems to be down");
                            }
                        } catch (RMException e) {
                            //if exception, considered RM as down
                            rmShutDownEvent(true);
                            break;
                        }
                        Thread.sleep(RM_SERVER_PING_FREQUENCY);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
        pinger.start();
    }

    // ----------------------------------------------------------------------
    // methods implemented from RMEvent listener
    // ----------------------------------------------------------------------
    /**
     * @see org.ow2.proactive.resourcemanager.frontend.RMEventListener#nodeEvent(org.ow2.proactive.resourcemanager.common.event.RMNodeEvent)
     */
    public void nodeEvent(RMNodeEvent event) {
        switch (event.getEventType()) {
            case NODE_ADDED:
                model.addNode(event);
                break;
            case NODE_REMOVED:
                model.removeNode(event);
                break;
            case NODE_STATE_CHANGED:
                switch (event.getNodeState()) {
                    case BUSY:
                    case DOWN:
                    case TO_BE_RELEASED:
                    case FREE:
                        model.changeNodeState(event);
                        break;
                }
        }
    }

    /**
     * @see org.ow2.proactive.resourcemanager.frontend.RMEventListener#nodeSourceEvent(org.ow2.proactive.resourcemanager.common.event.RMNodeSourceEvent)
     */
    public void nodeSourceEvent(RMNodeSourceEvent event) {
        switch (event.getEventType()) {
            case NODESOURCE_CREATED:
                model.addNodeSource(event);
                break;
            case NODESOURCE_REMOVED:
                model.removeNodeSource(event);
                break;
        }
    }

    /**
     * @see org.ow2.proactive.resourcemanager.frontend.RMEventListener#rmEvent(org.ow2.proactive.resourcemanager.common.event.RMEvent)
     */
    public void rmEvent(RMEvent event) {
        switch (event.getEventType()) {
            case STARTED:
                break;
            case SHUTTING_DOWN:
                Display.getDefault().syncExec(new Runnable() {
                    public void run() {
                        RMStatusBarItem.getInstance().setText("RM shutting down");
                    }
                });
                break;
            case SHUTDOWN:
                rmShutDownEvent(false);
                break;
        }
    }

    public synchronized void rmShutDownEvent(boolean failed) {
        if (RMStore.getInstance().getModel() != null) {
            pinger.interrupt();
            RMStore.getInstance().shutDownActions(failed);
        }
    }
}