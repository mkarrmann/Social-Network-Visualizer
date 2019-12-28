/*
 * File: Graph.java
 * Project: A-Team Social Media Project
 * 
 * This file provides the class implementation of GraphADT.java
 * 
 * Contributors:
 * Maxwell Kleinsasser
 * Apeksha Maithal
 * Isaac Zaman
 * Matthew Karrmann
 * 
 */

package application;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

/**
 * Implementation of GraphADT, an unweighted, undirected graph, implementing
 * GraphADT.
 * 
 * @author Matthew Karrmann
 *
 */
public class Graph implements GraphADT {

	// Adjacency list which stores nodes of edges (persons and friendships_
	private HashMap<Person, List<Person>> adjacencyList;
	private ArrayList<Person> vertices;
	public ArrayList<Person> getVertices() {
		return vertices;
	}

	private int size; // size of graph (number of edges)
	private int order; // order of graph (number of nodes)

	/*
	 * Default no-argument constructor
	 */
	public Graph() {
		// Initialize instance variables to reflect empty graph
		adjacencyList = new HashMap<Person, List<Person>>();
		vertices = new ArrayList<Person>();
		this.size = 0;
		this.order = 0;
	}

	/**
	 * Adds node to graph
	 * 
	 * @param node to add
	 */
	public void addNode(Person node) {
		// Prevents adding null Person
		if (node == null || node.getName() == null) {
			throw new IllegalArgumentException("Person is null. Please provide valid input.");
		} else if (!this.adjacencyList.containsKey(node)) {
			this.adjacencyList.put(node, new ArrayList<Person>()); // add to graph
			vertices.add(node);
			++this.order; // Increment order of graph
		}
	}

	/**
	 * Adds edge between people by adding each person to the other's list.
	 * 
	 * @param node1, first node
	 * @param node2, second node
	 */
	public void addEdge(Person node1, Person node2) {
		// If either node is null, throw an exception
		if (node1 == null || node1.getName() == null || node2 == null || node2.getName() == null) {
			throw new IllegalArgumentException("One or both persons are null. Please provide valid input.");
		} else {
			// If first node is not present, add it and increment order
			if (!this.adjacencyList.containsKey(node1)) {
				this.adjacencyList.put(node1, new ArrayList<Person>());
				vertices.add(node1);
				++this.order;
			}
			// If second node is not present, add it and increment order
			if (!this.adjacencyList.containsKey(node2)) {
				this.adjacencyList.put(node2, new ArrayList<Person>());
				vertices.add(node2);
				++this.order;
			}
			// Checks that edge does not already exist and that nodes are not equal
			if (!this.adjacencyList.get(node1).contains(node2) && !node1.equals(node2)) {
				// Add each node to the other's list
				this.adjacencyList.get(node1).add(node2);
				this.adjacencyList.get(node2).add(node1);
				++this.size; // Increment size of graph
			}
		}
	}

	/**
	 * Removes edge between two people by removing each person from the other's
	 * list.
	 * 
	 * @param node1, first node
	 * @param node2, second node
	 */
	public void removeEdge(Person node1, Person node2) {
		// Checks that both people are present
		if (this.adjacencyList.containsKey(node1) && this.adjacencyList.containsKey(node2)) {
			// If edge is present, remove it
			if (this.adjacencyList.get(node1).contains(node2)) {
				this.adjacencyList.get(node1).remove(node2);
				this.adjacencyList.get(node2).remove(node1);
				--this.size; // Decrement size of graph
			}
		} else {
			// Else throw exception
			throw new IllegalArgumentException(
					"One or both persons are not in the network. Please provide valid input.");
		}
	}

	/**
	 * Remove node from list, along with all edges connecting to node.
	 * 
	 * @param node in question
	 */
	public void removeNode(Person node) {
		// If person is present in graph
		if (this.adjacencyList.containsKey(node)) {
			// Get list of all adjacent nodes
			ArrayList<Person> friendList = (ArrayList<Person>) this.adjacencyList.get(node);
			// Removes person from list of each from
			for (Person friend : friendList) {
				--this.size; // Decrement size for each edge removed
				this.adjacencyList.get(friend).remove(node);
			}
			// Remove person from graph itself
			this.adjacencyList.remove(node);
			this.vertices.remove(node);
			--this.order; // Decrement order of graph
		} else {
			// If person is not present, throw exception.
			throw new IllegalArgumentException("Person is not in the network. Please provide valid input.");
		}
	}

	/**
	 * Gets all neighbors of given person in graph
	 * 
	 * @param node in question
	 * @return set of people adjacent to node
	 */
	public Set<Person> getNeighbors(Person node) {
		if (this.adjacencyList.containsKey(node)) {
			return new HashSet<Person>(this.adjacencyList.get(node));
		} else {
			throw new IllegalArgumentException("Person is not in the network. Please provide valid input.");
		}
	}

	/**
	 * Gets person with given name
	 * 
	 * @param name, name of person
	 */
	public Person getNode(String name) {
		// If name is present (note that hashcode and equals of Person class are
		// overriden to allow for
		// this lookup), then return new Person with given name (identical to instance
		// of Person being
		// looked for)
		if (this.adjacencyList.containsKey(new Person(name))) {
			return new Person(name);
		} else {
			// If person is not present, throw exception
			throw new IllegalArgumentException("Person is not in the network. Please provide valid input.");
		}
	}

	/**
	 * Gets all nodes in the graph.
	 * 
	 * @return Set of Persons in graph
	 */
	public Set<Person> getAllNodes() {
		// Returns keyset of adjacency list as a HashSet
		return new HashSet<Person>(this.adjacencyList.keySet());
	}

	/**
	 * Getter for order of graph.
	 * 
	 * @return order of graph
	 */
	public int order() {
		return this.order;
	}

	/**
	 * Getter for size of graph.
	 * 
	 * @return size of graph.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Determines if person with given name is present in graph.
	 * 
	 * @param name of person being searched for
	 * @return true if person is present, false otherwise
	 */
	public boolean contains(String name) {
		if (this.adjacencyList.containsKey(new Person(name))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Getter for adjacency list
	 * 
	 * @return current adjacency list
	 */
	public HashMap<Person, List<Person>> getAdjacencyList() {
		return this.adjacencyList;
	}
}