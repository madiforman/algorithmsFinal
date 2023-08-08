package graph;
//import graph.Graph;

import util.*;

public class GraphAlgorithms {
	static int INF = 9999; //denotes infinity
	/**
	 * Converts a Graph<Integer> G into an adjacency matrix
	 * @param G graph
	 * @return adjacency matrix of graph
	 */
	public static int[][] getAdjacencyMatrix(Graph<Integer> G){
		int[][] matrix = new int[G.numVertices()][G.numVertices()];
		int V = G.numVertices();
		for(Integer i = 0; i < V; i++) {
			for(Integer j = 0; j < V; j++) {
				if(G.edgeExists(i, j)) {
					matrix[i][j] = 1;
				} else {
					matrix[i][j] = INF;
				}
				
			}
		}
		return matrix;
	}
/**
 * Runs Floyd Warshall algorithm on graph G
 * @param G graph
 * @return matrix of shortest paths from all nodes
 */
    public static int[][] floydWarshall(Graph<Integer> G){
    	int[][] dist = getAdjacencyMatrix(G);
    	for(int k = 0; k < G.numVertices(); k++) {
        	for(int i = 0; i < G.numVertices(); i++) {
        		for(int j = 0; j < G.numVertices(); j++) {
        				if(dist[i][k] == 0 || dist[k][j]==0) {
        					continue;
        				}
            			int alt = dist[i][k] + dist[k][j];
            			if(alt < dist[i][j]) {
            				dist[i][j] = alt;
            			}
        			}
        		}
        	}
    		
    	}
    	return dist;
    }
/**
 * Runs Dijkstra's Algorithm on the graph. 
 * @param G graph
 * @param source node
 * @return list of nodes on shortest
 */
    public static Integer[] dijkstrasAlgorithm(Graph<Integer> G, int source) {
		Integer[] dist = new Integer[G.numVertices()];
		Integer[] prev = new Integer [G.numVertices()];
		PriorityQueue Q = new PriorityQueue(); 
		
		dist[source] = 0;
		for(int v : G.getVertices()) {
			if(v != source) {
				dist[v] = INF;
				prev[v] = null;
			}
			Q.push(dist[v], v);
		}
		while(!Q.isEmpty()) {
			int u = Q.pop();
			for(Integer v : G.getNeighbors(u)) {
				int alt = dist[u] + 1;
				if(alt < dist[v]) {
					dist[v] = alt;
					prev[v] = u;
					if(Q.isPresent(v)) {
						Q.changePriority(alt, v);
					}
					else {
						Q.push(alt, v);
					}
				}
			}
			
			
		}
		
	    	return prev;
    }


}
