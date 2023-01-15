package cs1501_p4;

import java.util.ArrayList;

public class Dijkstra {
	private Edge[] edgeTo; //Shortest edge from tree vertex to non-tree vertex
	private double[] distTo;      // distTo[v] = weight of shortest such edge
	private int s;
	private IndexMinPQ<Double> pq;
 
	/**
	 * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
	 * @param G the edge-weighted graph
	 */
	public Dijkstra(Graph G, int s) {
		this.s = s;
	    edgeTo = new Edge[G.V()];
	    distTo = new double[G.V()];
	    pq = new IndexMinPQ<Double>(G.V());
	    for (int v = 0; v < G.V(); v++)
		   distTo[v] = Double.POSITIVE_INFINITY;
 
	    dijkstra(G, s);
	}
 
	// run Prim's algorithm in graph G, starting from vertex s
	private void dijkstra(Graph G, int s) {
	    distTo[s] = 0.0;
	    pq.insert(s, distTo[s]);
	    while (!pq.isEmpty()) {
		   int v = pq.delMin();
		   scan(G, v);
	    }
	}
 
	// scan vertex v
	private void scan(Graph G, int v) {
	    for (Edge e : G.adj(v)) {
		   int w = e.other(v);
		   if (e.latency() + distTo[v] < distTo[w]) {
			  distTo[w] = e.latency() + distTo[v];
			  edgeTo[w] = e;
			  if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
			  else                pq.insert(w, distTo[w]);
		   }
	    }
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public ArrayList<Integer> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        ArrayList<Integer> path = new ArrayList<Integer>();
		int curr = v;
        for (Edge e = edgeTo[curr]; e != null; e = edgeTo[e.other(e.other(curr))]) {
			path.add(0, curr);
			curr = e.other(curr);
			if (curr == s){
				path.add(0, curr);
			}
        }
        return path;
    }
	

    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }
 
	public String toString() {
	    String s = "";
	    for(int i = 0; i < edgeTo.length; i++) {
		   Edge e = edgeTo[i];
		   if(e == null)
		   continue;
		   int v1 = e.either();
		   int v2 = e.other(v1);
		   int via = v1;
		   if(v1 == i) {
			  via = v2;
		   }
		   double dist = distTo[i];
		   s += "Best way to " + i + " is via " + via + " with a distance of " + dist + ".\n";
	    }
	    return s;
    }
}
 
