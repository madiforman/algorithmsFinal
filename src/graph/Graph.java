package graph;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

/**
 * This class implements a generic graph mapping keys to sets
 * 
 * @author Madison Sanchez-Forman
 * @version November 30, 2022
 */
public class Graph<V> implements GraphIfc<V> {
	Map<V, Set<V>> G;

	/**
	 * Constructor for graph class, instantiates a new Graph
	 */
	public Graph() {
		this.G = new HashMap<>();
	}

	/**
	 * Returns the number of vertices in the graph
	 * 
	 * @return The number of vertices in the graph
	 */
	public int numVertices() {
		return G.keySet().size(); // keySet() will return the set of all vertices in the graph
	}

	/**
	 * Returns the number of edges in the graph
	 * 
	 * @return The number of edges in the graph
	 */
	public int numEdges() {
		int sum = 0;
		for (V current : G.keySet()) {
			sum += G.get(current).size(); // accumulate sum of len(adjacency lists) for each vertex
		}
		return sum;
	}

	/**
	 * Removes all vertices from the graph
	 */
	public void clear() {
		G.clear(); // removes all vertices
	}

	/**
	 * Adds a vertex to the graph. This method has no effect if the vertex already
	 * exists in the graph.
	 * 
	 * @param v The vertex to be added
	 */
	public void addVertex(V v) {
		G.putIfAbsent(v, new HashSet<V>()); // if the vertex doesnt exist

	}

	/**
	 * Adds an edge between vertices u and v in the graph.
	 *
	 * @param u A vertex in the graph
	 * @param v A vertex in the graph
	 * @throws IllegalArgumentException if either vertex does not occur in the
	 *                                  graph.
	 */
	public void addEdge(V u, V v) {
		if (!G.containsKey(u)) {
			throw new IllegalArgumentException("Source node not contained in Graph, cannot addEdge()");
		} else if (!G.containsKey(v)) {
			throw new IllegalArgumentException("Destination node not contained in Graph, cannot addEdge()");
		}
		G.get(u).add(v); // u -> v
//		if(isUndirected == 1) {
//			System.out.println("here");
//			G.get(v).add(u); // v -> u for undirected
//		}
	}

	/**
	 * Returns the set of all vertices in the graph.
	 * 
	 * @return A set containing all vertices in the graph
	 */
	public Collection<V> getVertices() {
		return G.keySet(); // return all vertices
	}

	/**
	 * Returns the neighbors of v in the graph. A neighbor is a vertex that is
	 * connected to
	 * v by an edge. If the graph is directed, this returns the vertices u for which
	 * an
	 * edge (v, u) exists.
	 * 
	 * @param v An existing node in the graph
	 * @return All neighbors of v in the graph.
	 * @throws IllegalArgumentException if the vertex does not occur in the graph
	 */
	public Collection<V> getNeighbors(V v) {
		if (!G.containsKey(v)) {
			throw new IllegalArgumentException("vertex is not contained in Graph, cannot getNeighbors()");
		}
		return G.get(v); // return all adjacent to V
	}

	/**
	 * Determines whether the given vertex is already contained in the graph. The
	 * comparison
	 * is based on the <code>equals()</code> method in the class V.
	 * 
	 * @param v The vertex to be tested.
	 * @return True if v exists in the graph, false otherwise.
	 */
	public boolean containsVertex(V v) {
		if (G.containsKey(v)) {
			return true;
		}
		return false;
	}

	/**
	 * Determines whether an edge exists between two vertices. In a directed graph,
	 * this returns true only if the edge starts at v and ends at u.
	 * 
	 * @param v A node in the graph
	 * @param u A node in the graph
	 * @return True if an edge exists between the two vertices
	 * @throws IllegalArgumentException if either vertex does not occur in the graph
	 */
	public boolean edgeExists(V v, V u) {
		if (!G.containsKey(u)) {
			throw new IllegalArgumentException("Destination node not contained in Graph, edgeExists() failed");
		} else if (!G.containsKey(v)) {
			throw new IllegalArgumentException("Source node not contained in Graph, edgeExists() failed");
		} else if (G.get(v).contains(u)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the degree of the vertex. In a directed graph, this returns the
	 * outdegree of the
	 * vertex.
	 * 
	 * @param v A vertex in the graph
	 * @return The degree of the vertex
	 * @throws IllegalArgumentException if the vertex does not occur in the graph
	 */
	public int degree(V v) {
		return G.get(v).size();
	}

	/**
	 * Returns a string representation of the graph. The string representation shows
	 * all
	 * vertices and edges in the graph.
	 * 
	 * @return A string representation of the graph
	 */
	public String toString() {
		String str = "";
		for (V vertex : G.keySet()) {
			str += (vertex + "-> ");
			for (V edge : G.get(vertex)) {
				str += (edge + " ");
			}
			str += "\n";
		}
		return str;
	}
	public int[][] adjacencyMatrix(){
		int[][] matrix = new int[G.size()][G.size()];
		int i = 0, j = 0;
		for(V v : G.keySet()) {
			for(V u : G.get(v)) {
				if(edgeExists(v,u) && i < G.size() && j < G.size()) {
					matrix[i][j] = 1;
					i++;
					j++;
				}
			}
		}
		//printMatrix(matrix);
		return matrix; 
	}
	//public int minDistance(int dist[], )
	public void printMatrix(int mat[][]) {
		 for (int i = 0; i < mat.length; i++) {
			      for (int j = 0; j < mat[i].length; j++) {
			         System.out.print(mat[i][j] + " ");
			      }
			      System.out.println();
		 }
	            // Loop through all elements of current row
	}
	

}