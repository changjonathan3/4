//Edited Book Code by Jonathan Chang
public class Edge implements Comparable<Edge>{

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

    public Edge(String material, int bandwidth, int length, int w, int v){
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

    public static void assignValues(Edge e, Edge f){
        e.v = f.v;
        e.w = f.w;
        e.setMaterial(f.getMaterial());
        e.setBandwidth(f.getBandwidth());
        e.setEnd(f.getEnd());
        e.setLength(f.getLength());
    }

    /**
     * Returns either endpoint of this edge.
     *
     * @return either endpoint of this edge
     */
    public int either() {
        return v;
    }

    @Override
    public int compareTo(Edge otherEdge){
        if(TimeToTravel > otherEdge.getTimeToTravel()){
            return 1;
        } else if(TimeToTravel == otherEdge.getTimeToTravel()){
            return 0;
        } else{
            return -1;
        }
    }
}
