
/******************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *  Dependencies: StdOut.java
 *
 *  Immutable weighted directed edge.
 *
 ******************************************************************************/

//package edu.princeton.cs.algs4;
/**
 *  The {@code DirectedEdge} class represents a weighted edge in an 
 *  {@link EdgeWeightedDigraph}. Each edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the directed edge and
 *  the weight.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class DirectedEdge {
    private final int COPPER_SPEED = 230000000;
    //The speed at which a single data packet can be sent across copper wire in meters per second
    private final int FIBER_SPEED = 200000000;
    //The speed at which a single data packet can be sent across fiber optic wire in meters per second
    private String Material; //Optical or Copper edge
    private int Bandwidth; //Bandwidth of edge in megabits per second
    private int Length; //Length of edge in meters
    private int v; //The source of this edge
    private int w; //ID of the destination vertex
    private double TimeToTravel; //Time to travel on this edge based on its length and material in nanoseconds

    public DirectedEdge(){}

    public DirectedEdge(String material, int bandwidth, int length, int w, int v){
        if (v < 0) throw new IndexOutOfBoundsException("Vertex start must be a nonnegative integer");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex end must be a nonnegative integer");
        if (bandwidth < 0) throw new IndexOutOfBoundsException("Bandwidth must be a nonnegative integer");
        if (length < 0) throw new IndexOutOfBoundsException("Length must be a nonnegative integer");
        if (material.equals("")) throw new IllegalArgumentException("Material name not specified");
        Material = material;
        Bandwidth = bandwidth;
        Length = length;
        this.v=v;
        this.w=w;

        if(Material.equals("copper")){
            TimeToTravel = ((double) 1/COPPER_SPEED) * Length * Math.pow(10, 9);
            //For every meter, travelling along copper wire takes 1/230,000,000 seconds; convert it to nanoseconds
        }
        if(Material.equals("optical")){
            TimeToTravel = ((double) 1/FIBER_SPEED) * Length * Math.pow(10, 9);
            //For every meter, travelling along optical wire takes 1/200,000,000 seconds; convert it to nanoseconds
        }
    }

    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return w;
    }

    public void setMaterial(String newMaterial){
        Material = newMaterial;
    }

    public void setBandwidth(int newBandwidth){
        Bandwidth = newBandwidth;
    }

    public void setLength(int newLength){
        Length = newLength;
    }

    public void setEnd(int newDestination){
        w = newDestination;
    }

    public String getMaterial(){
        return Material;
    }

    public int getBandwidth(){
        return Bandwidth;
    }

    public int getLength(){
        return Length;
    }

    public int getEnd(){
        return w;
    }

    public double getTimeToTravel(){
        return TimeToTravel;
    }

    public int getStart(){
        return v;
    }

    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    public static void assignValues(DirectedEdge e, DirectedEdge f){
        e.v = f.v;
        e.w = f.w;
        e.setMaterial(f.getMaterial());
        e.setBandwidth(f.getBandwidth());
        e.setEnd(f.getEnd());
        e.setLength(f.getLength());
    }

}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
