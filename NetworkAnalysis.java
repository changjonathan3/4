/**
@author Jonathan Chang
 Main menu for computer network analysis program
*/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;

public class NetworkAnalysis{
	static Scanner s;
	private static EdgeWeightedGraph edgeWeightedGraph;
	private static EdgeWeightedDigraph edgeWeightedDigraph;
	private static int V; //vertices
	private static Graph copperGraph;
	private static boolean CopperOnly = true;
	private static ArrayList<Edge> copperList;
	private static FlowNetwork flowNetwork;
	private static Graph survival;
	private static ArrayList<Edge> surviveList;

	public static void main(String[] args){

		if(args.length == 1){
			try{
				File f = new File(args[0]);
				s= new Scanner(f);
				V = Integer.parseInt(s.nextLine());
				edgeWeightedGraph = new EdgeWeightedGraph(V);                             //initialize adjacency list
				edgeWeightedDigraph = new EdgeWeightedDigraph(V);
				flowNetwork = new FlowNetwork(V);
				copperGraph = new Graph(V);
				copperList = new ArrayList<>();
				survival = new Graph(V);
				surviveList = new ArrayList<>();

				while(s.hasNextLine()){
					String in = s.nextLine();
					String [] split = in.split("\\s+");

					int start = Integer.parseInt(split[0]);
					int end = Integer.parseInt(split[1]);
					String material = split[2];
					int bandwidth = Integer.parseInt(split[3]); //Maximum flow of edge
					int length = Integer.parseInt(split[4]); //Length of the edge

					Edge e = new Edge(material, bandwidth, length, end, start);
					edgeWeightedGraph.addEdge(e);

					survival.addEdge(start, end);
					surviveList.add(e);

					DirectedEdge d1 = new DirectedEdge(material, bandwidth, length, end, start);
					edgeWeightedDigraph.addEdge(d1);
					DirectedEdge d2 = new DirectedEdge(material, bandwidth, length, start, end);
					edgeWeightedDigraph.addEdge(d2);

					FlowEdge fe = new FlowEdge(start, end, bandwidth);
					FlowEdge fe2 = new FlowEdge(end, start, bandwidth);
					flowNetwork.addEdge(fe);
					flowNetwork.addEdge(fe2);

					if(material.equals("optical")) {
						CopperOnly = false;
						//If at any point while constructing this graph there is an optical fiber, then it is not copper-only connected
					}
					if(material.equals("copper")){
						copperList.add(e);
						copperGraph.addEdge(start, end);
					}
				}

			}catch(FileNotFoundException e){
				System.err.println("File Not Found. Exiting");
				System.exit(0);
			}

			s = new Scanner(System.in);
			//Main Menu
			while(true){
				System.out.println("\n Welcome to the computer network analyzer\n" +
									"\t1. Lowest latency path\n" +
									"\t2. Copper only connectivity\n" +
									"\t3. Maximum data flow\n" +
									"\t4. Minimum average latency spanning tree\n" +
									"\t5. Surviving 2 vertex failures\n" +
									"\t6. Exit program\n");
				System.out.print("Select an option: ");
				int user = Integer.parseInt(s.nextLine());
				if(user>=1 && user<=6){
					if(user==1){
						lowLatency();
					}
					if(user==2){
						copperOnly();
					}
					if(user==3){
						maxFlow();
					}
					if(user==4){
						minMST();
					}
					if(user==5){
						surviveFail();
					}
					if(user==6){
						System.out.println("Exiting");
						System.exit(0);
					}
				}
				else{
					System.out.println("Not a valid choice. Try again\n");
				}
			}
		}
		else{
			System.out.println("No file specified. Exiting\n");
		}
	}

	//1) Uses DijkstraSP to find shortest path
	private static void lowLatency(){
		int start = -1;
		int end = -1;
		while(start==-1 || end==-1){
			System.out.println("Enter start vertex number: ");
			int starting = Integer.parseInt(s.nextLine());
			if(starting<0||starting>=V) {
				System.err.println("Invalid starting vertex");
				continue;
			}
			start = edgeWeightedDigraph.getVerticesIndex(starting);  //returns -1 if not found

			System.out.println("Enter end vertex number: ");
			int ending = Integer.parseInt(s.nextLine());
			if(ending<0||ending>=V) {
				System.err.println("Invalid ending vertex");
				continue;
			}
			end = edgeWeightedDigraph.getVerticesIndex(ending);
		}
		DijkstraSP d = new DijkstraSP(edgeWeightedDigraph, start);

		if(d.hasPathTo(end)){
			int minBandwidth = 0;
			DirectedEdge[] edgeTo = d.getEdgeTo();
			ArrayList<String> path = new ArrayList<>();
			for (DirectedEdge e = edgeTo[end]; e != null; e = edgeTo[e.from()]) {
				path.add(0, "(" + e.getStart() + " , " + e.getEnd() + ")");
				minBandwidth+=e.getBandwidth();
			}
			System.out.println("Lowest Latency Path: " + path.toString() +
								"\n Bandwidth: " + minBandwidth + "\n");
		}
		else{
			System.out.println("No path for the vertices specified\n");
		}
	}

	//2) See if graph is connected only by copper.
	// Depth first search to see if the graph is connected only by copper
	private static void copperOnly(){
		if(CopperOnly){
			System.out.println("This graph is only connected with copper links\n");
		}
		else{ //check for only copper connectivity
			System.out.println("This graph is NOT only connected with copper links\n");
			if(copperGraph.V() == 0){
				System.out.println("NO copper links, so not a connected copper graph\n");
			}
			else{
				int source = copperList.get(0).getStart();
				DepthFirstPaths d = new DepthFirstPaths(copperGraph, source);
				for (int v = 0; v < copperList.size(); v++) {
					int b = copperList.get(v).getEnd();
					if (!d.hasPathTo(b)) {
						System.out.println(source + " to " + b + " is not connected\n" +
								"This graph is NOT connected when considering only copper links");
						return;
					}
					int a = copperList.get(v).getStart();
					if (!d.hasPathTo(a)) {
						System.out.println(source + " to " + a + " is not connected\n" +
								"This graph is NOT connected when considering only copper links");
						return;
					}
				}
				System.out.println("This graph is connected when considering only copper links");
			}
		}
	}

	//3) Find max flow between s, t with Ford Fulkerson Edmonds Karp
	private static void maxFlow(){
		int start = -1;
		int end = -1;
		while(start==-1 || end==-1){
			System.out.println("Enter start vertex number: ");
			int starting = Integer.parseInt(s.nextLine());
			if(starting<0||starting>=V) {
				System.err.println("Invalid starting vertex");
				continue;
			}
			start = edgeWeightedDigraph.getVerticesIndex(starting);  //returns -1 if not found

			System.out.println("Enter end vertex number: ");
			int ending = Integer.parseInt(s.nextLine());
			if(ending<0||ending>=V) {
				System.err.println("Invalid ending vertex");
				continue;
			}
			end = edgeWeightedDigraph.getVerticesIndex(ending);
		}
		// compute maximum flow

		try {
			FordFulkerson maxflow = new FordFulkerson(flowNetwork, start, end);
			System.out.println("Max flow	= " + maxflow.value());
		}catch(Exception e){

		}

	}

	//4) Uses KruskalMST to find min latency tree
	private static void minMST(){
		KruskalMST k = new KruskalMST(edgeWeightedGraph);
		for(Edge e : k.edges()){
			System.out.println("( " + e.getStart() + " , " + e.getEnd() + " )");
		}
	}

	//5) Determine survival of failure of 2 vertices
	private static void surviveFail(){
		for(int i=0;i<V-1;i++){
			for(int j=i+1;j<V;j++){ //takes all possible combinations
				Graph temp = new Graph(V);
				for (int v = 0; v < surviveList.size(); v++) {
					int start = surviveList.get(v).getStart();
					int end = surviveList.get(v).getEnd();
					if(i == start || i == end || j == start || j == end){
						continue;
					}
					temp.addEdge(start, end); //build the graph
				}
				int source; //pick a different vertex
				if(i!=0){
					source = i-1;
				}
				else{
					if(j!=V-1){
						source = j+1;
					}
					else{
						while(true){
							Random r = new Random();
							source = r.nextInt(V);
							if(source!=i && source!=j) break;
						}
					}
				}
				DepthFirstPaths d = new DepthFirstPaths(temp, source);
				for (int v = 0; v<V; v++) {
					if(v==i || v==j){
						continue;
					}
					if (!d.hasPathTo(v)) {
						System.out.println(i + " and " + j + " if failed will disconnect the graph\n"
								+ "This graph is NOT connected if two vertices fail");
						return;
					}
				}
			}
		}
		System.out.println("This graph is connected even if 2 vertices fail\n");
	}
}
