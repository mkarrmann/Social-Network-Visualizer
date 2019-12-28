/*
 * File: SocialNetworkADT.java
 * Project: A-Team Social Media Project
 * 
 * This file provides the interface specifications for
 * the SocialMedia abstract data type.
 * 
 * Contributors:
 * Maxwell Kleinsasser
 * Apeksha Maithal
 * Isaac Zaman
 * Matthew Karrmann
 * 
 */

package application;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * @author mkleinsasser@wisc.edu
 * 
 * Interface for the Social Network abstract data type
 * to be implemented in SocialNetwork.java
 *
 */
public interface SocialNetworkADT {
	
	/**
	 * add an edge (friendship) in the SM graph between two vertices (users)
	 * @param f1
	 * @param f2
	 * @return
	 */
	public boolean addFriends(String f1, String f2);
	
	/**
	 * remove an edge (friendship) in the SN graph between two vertices (users)
	 * @param f1
	 * @param f2
	 * @return
	 */
	public boolean removeFriends(String f1, String f2);
	
	/**
	 * add a vertex (user) to the graph
	 * @param user
	 * @return
	 */
	public boolean addUser(String user);
	
	/**
	 * remove a vertex (user) from the graph
	 * @param user
	 * @return
	 */
	public boolean removeUser(String user);
	
	/**
	 * get all the friends of a particular user
	 * (get all adjacent vertices of selected vertex)
	 * @param user
	 * @return
	 */
	public Set<Person> getFriends(String user);
	
	/**
	 * get friends in common between two users
	 * @param p1
	 * @param p2
	 * @return
	 */
	public Set<Person> getMutualFriends(String p1, String p2);
	
	/**
	 * Get shortest path (in users) from one user to another
	 * @param p1
	 * @param p2
	 * @return
	 */
	public List<Person> getShortestPath(String p1, String p2) throws Exception;
	
	/**
	 * returns a set of subgraphs containing the connected compnents
	 * of the whole graph
	 * @return
	 */
	public Set<Graph> getConnectedComponents();
	
	/**
	 * load set of instructions from a file
	 * @param f
	 * @return 
	 */
	public String loadFromFile(File f) throws ParseException;
	
	/**
	 * write out previous session instructions to a file
	 * @param f
	 * @throws IOException 
	 */
	public void saveToFile(String s, String fileName) throws IOException;
	
}
