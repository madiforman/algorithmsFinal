package analyzer;
import graph.*;
import java.util.Scanner;
import java.util.Map;
import data.Movie;
import data.Reviewer;
import util.DataLoader;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/* ================================================================================
 Name        : MovieLensAnalyzer
 Authors     : Madison Sanchez-Forman
 Version     : 12.16.22
 Description : Uses a collection of movie review data to build a graph and allows the
 user to traverse the graph. 
 ================================================================================ */
public class MovieLensAnalyzer {
	private static Map<Integer, Movie> movies;
	private static Map<Integer, Reviewer> reviewers;
	private static Graph<Integer> G = new Graph<Integer>(); //Graph for movies
	private static int INF = 9999;
	
/**
 * Helper function, adds all movies to the graph G
 */
public static void addVertices() {
	for(Movie movie : movies.values()) {
		G.addVertex(movie.getMovieId()); //add each movie as vertex 
	}
}
/**
 * If the user chose option one
 */
public static void graphOption1() {
	addVertices();
	for(Movie movie0 : movies.values()) {
		for(Movie movie1 : movies.values()) { //for each pair of movies
			int movieID0 = movie0.getMovieId(), movieID1 = movie1.getMovieId();
			int count = 0;
			if(movieID0 != movieID1) { //check we are not looking at the same movie
				for(Reviewer R : reviewers.values()) {
					int id = R.getReviewerId();
					if(movie0.rated(id) && movie1.rated(id) && R.getMovieRating(movieID0) == R.getMovieRating(movieID1)){
						//if both movies were rated by user and given the same rating 
							count++;
							if(count == 12) {
								G.addEdge(movieID0, movieID1);
							}
					}
						
				}
				
			}
				
		}
	}
}
/**
 * If the user chooses option 2
 */
public static void graphOption2() {
	addVertices();
	for(Movie movie0 : movies.values()) { //for each pair of movies
		for(Movie movie1 : movies.values()) {
			int movieID0 = movie0.getMovieId(), movieID1 = movie1.getMovieId();
				int count = 0;
				if(movieID0 != movieID1) {
					for(Reviewer R : reviewers.values()) {
						int id = R.getReviewerId();
						if(movie0.rated(id) && movie1.rated(id)){ //only checking if both users rated each movie
							count++;
							if(count == 12) {
								G.addEdge(movieID0, movieID1);
							}
						}
							
					}
					
					
				}
		}
	}
}

/**
 * Prints graph statistics 
 */
public static void graphStatistics() {
	float numerator = 2 * G.numEdges();
	float denom = G.numVertices() * (G.numVertices() - 1);
	int maxDegree = 0;
	int node = 0;
	for(Integer movieID : G.getVertices()) {
		if(G.degree(movieID) > maxDegree) { //we have found a new max degree
			maxDegree = G.degree(movieID); 
			node = movieID + 1;
		}
	}
	int [][] shortestPaths = GraphAlgorithms.floydWarshall(G);  //running floydWarshall
	int diameter = 0;
	int src = 0, dst = 0;
	float sum = 0, numPaths = 0;
	for(int i = 0; i < G.numVertices(); i++) {
		for(int j = 0; j < G.numVertices(); j++) {
			if(shortestPaths[i][j] != INF) { //
				sum += shortestPaths[i][j];
				numPaths++;
				if(shortestPaths[i][j] > diameter) { //found a new longest path 
					diameter = shortestPaths[i][j];
					src = i + 1;
					dst = j + 1;
				}
				
			}
		}
	}
	float avgPathLen = sum/numPaths;
	System.out.println("|V| = " + G.numVertices());
	System.out.println("|E| = " + G.numEdges());
	System.out.println("Density: " + numerator / denom);
	System.out.println("Max degree = " + maxDegree + " (node " + node + ")");
	System.out.println("Diameter = " +  diameter + " (from node " + src + " to " + dst + ")");
	System.out.println("Avg. path length = " + avgPathLen + "\n");
	
}
/**
 * Prints information about a node
 * @param scan scanner being used
 */
public static void nodeInformation(Scanner scan){
	System.out.print("Enter movie id (1-1000): ");
	boolean flag = true; //used to end while loop for incorrect user input
	while(flag) {
		try {
			int choice = Integer.parseInt(scan.nextLine()) - 1;
			System.out.println(movies.get(choice));
			System.out.println("Neighbors: ");
			flag = false;
		
			for (Integer movieID : G.getNeighbors(choice)) {
				System.out.println("\t" + movies.get(movieID).getTitle());
			}
			System.out.println();
				
		} catch(NumberFormatException e) {
			System.out.println("Please enter a movie id (1-1000): ");
		}
	}
}
/**
 * print the path found from Dijkstra's algorithm
 * @param prev list of previous nodes
 * @param src source node
 * @param dest destination node
 */
public static void printPath(Integer[] prev, int src, int dest) {
	int end = dest;
	ArrayList<Integer> path = new ArrayList<Integer>();
	while(prev[dest] != src && prev[dest] != null ) { //if the current node is on the path
		dest = prev[dest];
		path.add(dest);
	}
	Collections.reverse(path); //reverse the path
	System.out.print("\n" + movies.get(src).getTitle() + "-> ");
	for(int i = 0; i < path.size(); i++) {
		System.out.println(movies.get(path.get(i)).getTitle() + "-> ");
	}//
	System.out.println(movies.get(end).getTitle() + "\n");
}
/**
 * User chose option 3, and now we run Dijkstra's algorithm
 * @param scan scanner being used
 */
public static void printDijkstra(Scanner scan) {
	boolean flag = true; //used to terminate while loop
	int src = 0, dest = 0;
	do {
		System.out.print("Enter starting node (1-1000): ");
		try {
			src = Integer.parseInt(scan.nextLine()) - 1;
			System.out.print("Enter destination node (1-1000): ");
			dest = Integer.parseInt(scan.nextLine()) - 1;
			
			Integer[] prev = GraphAlgorithms.dijkstrasAlgorithm(G, src);
			printPath(prev, src, dest);
			flag = false;
			
		} catch(NumberFormatException e) {
			flag = true;
		}
		
	}while(flag);
	
}
/**
 * Searches through each movie title, and compares each word in the movie title to the search key entered. 
 * If the key is found in a movie title, it is added to a set of movies titles with that word, which
 * are then printed
 * @param scan scanner being used in main
 */
public static void searchByKeyword(Scanner scan) {
	System.out.print("Enter a keyword: ");
	String str = scan.nextLine();
	Set<String> moviesFound = new HashSet<String>();

		for(Movie movie : movies.values()) { 
			for(String word : movie.getTitle().split("\\s+")) { //split movie by white spaces
				if(word.contains(str)) { //if we have found a match
				moviesFound.add(movie.getTitle());
				}	
			}
		}
		if(moviesFound.size() == 0) { //if no movies were found
			System.out.println("No movies were found with the word [" + str + "]");
		} else {
			System.out.println("Movies found with the word [" + str + "]: ");
			for(String found : moviesFound) {
				System.out.println("\t" + found);
			}
		}

		System.out.println();
}
/**
 * Prints different ways of defining adjacency to user
 * @param scan scanner being used
 */
public static void graphOptions(Scanner scan) {
	boolean flag = true; //used to terminate determine if menu options should be printed again
	do {
		if(flag) {
		System.out.println("There are 2 choices for defining adjacency: ");
		System.out.println("[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies");
		System.out.println("[Option 2] u and v are adjacent if the same 12 users watched both movies (regardless of rating)" + "\n");
		System.out.print("Choose an option to build the graph (1-2): ");
		flag = false; //
		}
		int choice;
		try {
			choice = Integer.parseInt(scan.nextLine());
			System.out.print("Creating graph... ");
			switch(choice) {
			case 1: 
				graphOption1(); //build graph
				System.out.println("graph has been created \n");
				exploreGraph(scan); //next menu
				break;
			case 2:
				graphOption2(); //build graph
				System.out.println("graph has been created \n");
				exploreGraph(scan); //next menu
				break;
			default: //user entered number that wasn't 1 or 2
				System.out.println("\nPlease enter a valid option (1-2) \n");
				flag = true;					
			}
			
		} catch (NumberFormatException e) { //user entered something that couldnt be converted to an integer
			System.out.println("\nPlease enter a valid option (1-2) \n");
			flag = true;
		}
	}	while(true); //infinite loop because terminating condition is in next menu
}

/**
 * Prints menu options allowing user to explore the graph
 * @param scan scanner being used
 */
	public static void exploreGraph(Scanner scan) {
		int choice = 0;
		do {
		System.out.println("[Option 1] Print out statistics about the graph");
		System.out.println("[Option 2] Print node information");
		System.out.println("[Option 3] Display shortest path between two nodes");
		System.out.println("[Option 4] Search for movie by keyword");
		System.out.println("[Option 5] Quit");
		System.out.print("Choose an option (1-5): ");
			try {
				choice = Integer.parseInt(scan.nextLine());
				System.out.println();
				switch(choice) {
				case 1:
					graphStatistics();
					break;
					//FW
				case 2:
					nodeInformation(scan);
					//GraphAlgorithms.floydWarshall(G);
					break;

				case 3:
					printDijkstra(scan);
					break;
				case 4: 
					searchByKeyword(scan);
					break;
				case 5:
					scan.close();
					System.out.println("Exiting program... Goodbye :)");
					System.exit(0);
				default: System.out.print("Please enter a valid option (1-5): ");
					
				}
			} catch(NumberFormatException e) {
				System.out.print("Please enter a valid option (1-4): ");
			}
		} while(choice != 5);

	}
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}
		
		DataLoader p = new DataLoader();
		p.loadData(args[0], args[1]);
		reviewers = p.getReviewers();
		movies = p.getMovies();
		
		Scanner scan = new Scanner(System.in);
		System.out.println("========= Welcome to MovieLens Analyzer =========");
		System.out.println("The files being used are: ");
		System.out.println(args[0]);
		System.out.println(args[1] + "\n");
		
		graphOptions(scan);
		
	}
}
