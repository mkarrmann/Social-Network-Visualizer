/*
 * File: SocialNetwork.java
 * Project: A-Team Social Media Project
 * 
 * This file contains the class implementation of SocialNetworkADT
 * to be used in Main.java for handling data organization and import/export
 * functionality.
 * 
 * Contributors:
 * Maxwell Kleinsasser
 * Apeksha Maithal
 * Isaac Zaman
 * Matthew Karrmann
 * 
 */

package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @author mkleinsasser, mkarrmann
 *
 */
public class SocialNetwork implements SocialNetworkADT {

	private Graph graph;
	Person activeUser = null;

	/**
	 * Constructs the SocialNetwork object, initializing the 
	 */
	public SocialNetwork() {
		graph = new Graph();
	}

	/**
	 * Adds an edge between the two users in the graph
	 */
	@Override
	public boolean addFriends(String friend1, String friend2) {

		if (friend1 == null || friend2 == null) {
			return false;
		}
		int origSize = graph.size();

		Person p1 = new Person(friend1);
		Person p2 = new Person(friend2);

		try {
			graph.addEdge(p1, p2);
			if (graph.size() > origSize) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Removes an edge between two users in the graph.
	 */
	@Override
	public boolean removeFriends(String friend1, String friend2) {

		if (friend1 == null || friend2 == null) {
			return false;
		}

		int origSize = graph.size();

		Person p1 = new Person(friend1);
		Person p2 = new Person(friend2);

		try {
			graph.removeEdge(p1, p2);
			if (graph.size() < origSize) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Adds a user to the graph
	 */
	@Override
	public boolean addUser(String user) {
		if (user == null) {
			return false;
		}
		if (graph.contains(user)) {
			return false;
		} else {
			Person node = new Person(user);
			graph.addNode(node);
			return true;
		}
	}

	/**
	 * Removes a user from the graph
	 */
	@Override
	public boolean removeUser(String user) {
		if (user == null) {
			return false;
		}
		if (graph.contains(user)) {
			graph.removeNode(new Person(user));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a set of the friends of the specified user
	 */
	@Override
	public Set<Person> getFriends(String user) {

		if (user == null) {
			return null;
		}
		Person node = null;
		try {
			node = graph.getNode(user);
			return graph.getNeighbors(node);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * returns a Set containing the mutual friends of the method's arguments
	 */
	@Override
	public Set<Person> getMutualFriends(String user1, String user2) {

		if (user1 == null || user2 == null) {
			return null;
		}
		if (graph.contains(user1) && graph.contains(user2)) {
			try {
				Set<Person> set1 = getFriends(user1);
				Set<Person> set2 = getFriends(user2);
				set1.retainAll(set2);
				return set1;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Helper method for getShortestPath(). After performing BFS to find second
	 * user, returns List of appropriate visited users
	 * 
	 * @param visited    HashMap mapping each visited user to its prior user
	 * @param currPerson p2
	 * @return List of shortest path
	 */
	private List<Person> shortestPath(HashMap<Person, Person> visited, Person currPerson) {
		ArrayList<Person> path = new ArrayList<Person>(); // ArrayList to become path
		// Iterates back through visited users until null is reached
		while (currPerson != null) {
			path.add(0, currPerson); // Adds to beginning of list
			currPerson = visited.get(currPerson); // Updates currPerson
		}
		return path;
	}

	/**
	 * Get shortest path (in users) from one user to another
	 * 
	 * @param p1, first user
	 * @param p2, second user
	 * @return List of users describing shortest path from p1 to p2
	 */
	@Override
	public List<Person> getShortestPath(String p1, String p2) throws Exception {
		// Maps each visited user to its prior user
		HashMap<Person, Person> visited = new HashMap<Person, Person>();
		// Queue to be used for BFS
		Queue<Person> queue = new LinkedList<Person>();
		visited.put(new Person(p1), null); // Adds first user to visited, with null prior user
		queue.add(new Person(p1)); // Adds first user queue
		Person currPerson; // User currently being considered
		Set<Person> friendsList; // Friends list of currPerson
		Person friend; // current friend of currPerson being considered
		Iterator<Person> it;
		// While queue is not empty
		while (queue.peek() != null) {
			currPerson = queue.poll();
			friendsList = graph.getNeighbors(currPerson); // get friends of currPerson
			it = friendsList.iterator();
			// Iterate until end of friends list
			while (it.hasNext()) {
				friend = it.next();
				// If friend has not been visited, mark them visited. If friend is p2, call
				// helper to
				// construct list and return it
				if (!visited.containsKey(friend)) {
					visited.put(friend, currPerson);
					if (friend.equals(new Person(p2))) {
						return shortestPath(visited, friend);
					} else {
						// Else add friend to queue and continue
						queue.add(friend);
					}
				}
			}
		}
		// Only reached if no path found. Throw exception.
		throw new Exception("No path between people found.");
	}

	/**
	 * Helper method for getConnectComponents(). Used to find single connected
	 * component.
	 * 
	 * @param subgraph graph which connected component is to be added to
	 * @param node     current node method if being called on
	 * @param visited  set of nodes which have been visited
	 * @return connected component (the part which had not been visited before being
	 *         called)
	 */
	private Graph connectedComponentsHelper(Graph subgraph, Person node, HashSet<Person> visited) {
		// Friends of node
		ArrayList<Person> friends = new ArrayList<Person>(graph.getNeighbors(node));
		subgraph.addNode(node); // add node to subgraph
		visited.add(node); // mark node visited
		// Iterate through each of node's friends:
		for (Person friend : friends) {
			// add edge between friends indicating friendship (adding friend to graph if
			// they are not
			// already present)
			subgraph.addEdge(node, friend);
			// If friend has not been visited, call helper method on them
			if (!visited.contains(friend)) {
				connectedComponentsHelper(subgraph, friend, visited);
			}
		}
		return subgraph;
	}

	/**
	 * Gets a set of subgraphs containing the connected components of the whole
	 * graph
	 * 
	 * @return set of connected subgraphs
	 */
	@Override
	public Set<Graph> getConnectedComponents() {
		// Nodes which have not been visited
		HashSet<Person> remaining = (HashSet<Person>) graph.getAllNodes();
		Iterator<Person> it = remaining.iterator(); // Iterator used to select remaining user
		HashSet<Graph> connectedComponents = new HashSet<Graph>(); // Set to be returned
		Graph subgraph; // subgraph to pass to helper
		Graph component; // to refer to single connected component
		HashSet<Person> visited; // to pass to helper
		Person currentNode; // currentNode selected by iterator
		// While there remain nodes
		while (it.hasNext()) {
			currentNode = it.next(); // selects new node
			subgraph = new Graph(); // sets to empty graph
			visited = new HashSet<Person>(); // sets to empty set
			// Call helper method and save
			component = connectedComponentsHelper(subgraph, currentNode, visited);
			connectedComponents.add(component); // add component to set
			remaining.removeAll(component.getAllNodes()); // remove added nodes from remaining
			it = remaining.iterator(); // Redefine iterator on new remaining set
		}
		return connectedComponents;
	}

	@Override
	public String loadFromFile(File f) throws ParseException {
		StringBuilder contentBuilder = new StringBuilder();

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				contentBuilder.append(sCurrentLine).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String i = contentBuilder.toString();
		Scanner iScanner = new Scanner(i);

		while (iScanner.hasNextLine()) {
			runAction(iScanner.nextLine());
		}
		iScanner.close();
		return i;
	}

	public List<Person> runAction(String action) throws ParseException {
		Scanner scnr = new Scanner(action);
		action = action.trim();
		
		if (action == null) {
			scnr.close();
			throw new ParseException("Invalid Input", -1);
		}
		
		if (action.equals("")) {
			scnr.close();
			return null;
		}

		String actionId = "";
		String user1 = null;
		String user2 = null;
		
		for (int i = 0; scnr.hasNext(); i++) {
			if (i == 0) {
				actionId = scnr.next();
				if (!(actionId.equals("a") || actionId.equals("r") || actionId.equals("s"))) {
					scnr.close();
					throw new ParseException("", -1);
				}
			}
			if (i == 1) {
				user1 = scnr.next();
			}
			if (i == 2) {
				if (actionId.equals("s")) {
					scnr.close();
					throw new ParseException("s action allows 1 user argument", -1);
				}
				user2 = scnr.next();
			}
		}
		
		if (user1 == null) {
			scnr.close();
			throw new ParseException("user1 == null", -1);
		}

		if (actionId.equals("a")) {
			if (user2 == null) {
				this.addUser(user1);
				System.out.println("user added");
			} else {
				this.addFriends(user1, user2);
				System.out.println("friendship added");
			}
		}

		if (actionId.equals("r")) {
			if (user2 == null) {
				this.removeUser(user1);
				System.out.println("user removed");
			} else {
				this.removeFriends(user1, user2);
				System.out.println("friendship removed");
			}
		}

		if (actionId.equals("s")) {
			this.activeUser = graph.getNode(user1);
			System.out.println("active user set");
		}
		
		System.out.println("Action handled");
		scnr.close();
		return this.graph.getVertices();
	}

	@Override
	public void saveToFile(String s, String fileName) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    writer.write(s);
	    writer.close();
	}

	/**
	 * Getter for graph.
	 * 
	 * @return graph of network.
	 */
	public Graph getGraph() {
		return this.graph;
	}
	
	public void clear() {
		this.graph = new Graph();
		this.activeUser = null;
	}
}