package cs1501_p4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class NetAnalysis implements NetAnalysis_Inter{

    public int v;
    public Graph network;

    public NetAnalysis(String fname) throws FileNotFoundException{
        File file = new File(fname);
        Scanner sc = new Scanner(file);
        String line = sc.nextLine();
        v = Integer.parseInt(line);
        network = new Graph(v);
        while(sc.hasNext()){
            line = sc.nextLine();
            String[] elements = line.split(" ");
            if (elements[2].equals("optical")){
                Edge connection = new Edge(Integer.parseInt(elements[0]),Integer.parseInt(elements[1]), false,  Double.parseDouble(elements[4]), Double.parseDouble(elements[3]));
                network.addEdge(connection);
            } else if (elements[2].equals("copper")){
                Edge connection = new Edge(Integer.parseInt(elements[0]),Integer.parseInt(elements[1]), true, Double.parseDouble(elements[4]), Double.parseDouble(elements[3]));
                network.addEdge(connection);
            }
        }
        sc.close();
    }

    @Override
    public ArrayList<Integer> lowestLatencyPath(int u, int w) {
        Dijkstra dijkstra = new Dijkstra(network, u);
        if (!dijkstra.hasPathTo(w)) return null;
        else return dijkstra.pathTo(w);
    }

    @Override
    public int bandwidthAlongPath(ArrayList<Integer> p) throws IllegalArgumentException {
        double minBW = Double.MAX_VALUE;
        //might have to change if ArrayList indexes starts with 1
        int i = 0;
            while(i < (p.size() -1)){
                int curr = p.get(i);
                int next = p.get(i+1);
                network.validateVertex(curr);
                network.validateVertex(next);
                boolean connected = false;
                for(Edge e : network.adj(curr)){
                    if(e.other(curr) == next){
                        if (e.bandwith() < minBW) minBW = e.bandwith();
                        connected = true;
                    }
                }
                if (!connected) throw new IllegalArgumentException("Path not valid");
                i++;
            }
        return (int) minBW;
    }

    @Override
    public boolean copperOnlyConnected() {
        if (v == 0) return false;
        Queue<Integer> q = new Queue<Integer>();
        boolean[] marked = new boolean[network.V()];
        for (int i = 0; i<v; i++) marked[i] = false;
        marked[0] = true;
        q.enqueue(0);
        while (!q.isEmpty()) {
            int vv = q.dequeue();
            for (Edge w : network.adj(vv)) {
                int vertex = w.either();
                if (!marked[vertex] && w.type()) {
                    marked[vertex] = true;
                    q.enqueue(vertex);
                }
                if (!marked[w.other(vertex)] && w.type()) {
                    marked[w.other(vertex)] = true;
                    q.enqueue(w.other(vertex));
                }
            }
        }
        for (int i = 0; i < marked.length; i++){
            if(!marked[i]) return false;
        }
        return true;
    }
   
    @Override
    public boolean connectedTwoVertFail() {
        for (int i = 0; i < v; i++){
            for (int j = i+1; j < v; j++){
                Queue<Integer> q = new Queue<Integer>();
                boolean[] marked = new boolean[network.V()];
                for (int k = 0; k<v; k++) marked[k] = false;
                marked[i] = true;
                marked[j] = true;
                if ((j+1)%v != i){
                    q.enqueue((j+1)%v);
                    marked[(j+1)%v] = true;
                }else{
                    q.enqueue(i+1);
                    marked[i+1] = true;
                }
                while (!q.isEmpty()) {
                    int vv = q.dequeue();
                    for (Edge w : network.adj(vv)) {
                        int vertex = w.either();
                        if (!marked[vertex]) {
                            marked[vertex] = true;
                            q.enqueue(vertex);
                        }
                        if (!marked[w.other(vertex)]) {
                            marked[w.other(vertex)] = true;
                            q.enqueue(w.other(vertex));
                        }
                    }
                }
                for (int k = 0; k < marked.length; k++){
                    if(!marked[k]) return false;
                }
            }
        }
        return true;
    }

    @Override
    public ArrayList<STE> lowestAvgLatST() {
        if (v == 0) return null;
        Queue<Integer> q = new Queue<Integer>();
        boolean[] marked = new boolean[network.V()];
        for (int i = 0; i<v; i++) marked[i] = false;
        marked[0] = true;
        q.enqueue(0);
        while (!q.isEmpty()) {
            int vv = q.dequeue();
            for (Edge w : network.adj(vv)) {
                int vertex = w.either();
                if (!marked[vertex]) {
                    marked[vertex] = true;
                    q.enqueue(vertex);
                }
                if (!marked[w.other(vertex)]) {
                    marked[w.other(vertex)] = true;
                    q.enqueue(w.other(vertex));
                }
            }
        }
        for (int i = 0; i < marked.length; i++){
            if(!marked[i]) return null;
        }
        KruskalMST MST = new KruskalMST(network);
        return MST.edges();
        
    }
    
}
