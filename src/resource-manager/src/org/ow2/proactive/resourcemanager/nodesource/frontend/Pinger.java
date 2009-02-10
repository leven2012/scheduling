package org.ow2.proactive.resourcemanager.nodesource.frontend;

import org.apache.log4j.Logger;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.core.body.exceptions.BodyTerminatedException;
import org.objectweb.proactive.core.node.Node;
import org.objectweb.proactive.core.util.log.ProActiveLogger;
import org.ow2.proactive.authentication.RestrictedService;
import org.ow2.proactive.resourcemanager.utils.RMLoggers;


/**
 * Class which implements the Pinger thread.
 * <BR>This class communicate with its NodeSource upper class by the NodeSource AO stub,
 * not directly, in order to avoid concurrent access.
 * This object ask periodically list of nodes managed by its NodeSource object,
 * verify if nodes are still alive, and warn the {@link NodeSource} object if a node is down by calling
 *  {@link NodeSource#detectedPingedDownNode(String)} method.
 * @see org.ow2.proactive.resourcemanager.nodesource.frontend.NodeSource
 *
 */
public class Pinger extends RestrictedService {

    /** stub of the NodeSource Active Object*/
    private NodeSource nodeSource;

    /** state of the thread, true Pinger "ping", false
     * pinger is stopped */
    private boolean active;

    public Pinger() {
    }

    /**
     * Pinger constructor.
     * Launch the nodes monitoring thread
     * @param source stub of the NodeSource Active Object
     */
    public Pinger(NodeSource source) {
        nodeSource = source;
        registerTrustedService(nodeSource);
        this.active = true;
    }

    /**
     * shutdown the Pinger thread .
     */
    public synchronized void shutdown() {
        this.active = false;
    }

    /**
     * Gives the state the Thread's state.
     * @return boolean indicating thread's state :
     * true, Pinger continues Pinging or
     * false Pinger thread stops.
     */
    public synchronized boolean isActive() {
        return this.active;
    }

    /**
     * Activity thread of the Pinger.
     * <BR>Each ping frequency time
     * the Pinger get the NodeList of the NodeSource,
     * and verify if nodes are always reachable
     * if one of them is unreachable, the node will be said "down",
     * and must be removed from the NodeSource.
     * {@link NodeSource#detectedPingedDownNode(String)} is called when a down node is detected.
     */
    public void ping() {
        while (this.isActive()) {
            try {
                try {
                    Thread.sleep(nodeSource.getPingFrequency().intValue());
                } catch (InterruptedException ex) {
                    active = false;
                }
                if (!this.isActive()) {
                    break;
                }
                for (Node node : nodeSource.getNodes()) {
                    // check active between each ping
                    if (!this.isActive()) {
                        break;
                    }
                    String nodeURL = node.getNodeInformation().getURL();
                    try {
                        node.getNumberOfActiveObjects();
                    } catch (Exception e) {
                        this.nodeSource.detectedPingedDownNode(nodeURL);
                    } //catch
                } //for
            } catch (BodyTerminatedException e) {
                // node source is terminated 
                // terminate...
                break;
            }
        } //while !interrupted
    }

    public Logger getLogger() {
        return ProActiveLogger.getLogger(RMLoggers.CONNECTION);
    }

}
