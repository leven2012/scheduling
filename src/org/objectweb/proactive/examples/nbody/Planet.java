/*
 * Created on Jan 17, 2005
 */
package org.objectweb.proactive.examples.nbody;
/**
 * @author irosenbe
 */
public class Planet extends Info{
    
    int identification;
    double vx,vy;
    double diameter;
    
    private int debug_count = 0;
    private final double dt = 0.002;
    
    public Planet(){}
    
    /**
     * Defines a random Planet in the given bounds, with a unique id.
     * @param limits the rectangular bounds within which the Planet must be found.
     * @param id the unique identifier for the Planet.
     */
    public Planet(Rectangle limits, int id) {
        identification = id;
        x = limits.x + Math.random() * limits.width  ;
        y = limits.y + Math.random() * limits.height  ;
        mass = 1000 + Math.random()*100000 ; 
        
        //vx = 2000*(Math.random () -0.5 );  TODO Z6 decide whether initial speed should be zero, or not
        //vy = 2000*(Math.random () -0.5 );
        vx = 0 ; vy = 0;
        
        diameter = mass/2000+3; ;  
        radius = 1; // smaller than any possible diameter, from equations above
    }
    
    /**
     * Move the planet, and define a new speed vector to assign to this planet, depending on the total 
     * force which is applied to it.  
     *      * @param force The total Force which pushes this planet on the local iteration. 
     */
    public void moveWithForce(Force force) {
        // Using f(t+dt) ~= f(t) + dt * f'(t)
        
        //        if (debug_count !=0) {
        //            throw new NullPointerException("MOVING with " + vx);
        //        }
        //        debug_count++;
        //System.out.print ("speed is "  + Math.sqrt(vx * vx + vy *vy)  + " ");
        x += vx * dt;
        y += vy * dt;
        
        // sum F  = mass * acc;
        // a = sum F / mass:
        
        vx += dt * force.x / mass; 
        vy += dt * force.y / mass;
        
    }
}

