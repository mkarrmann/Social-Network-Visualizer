/*
 * File: Graph.java
 * Project: A-Team Social Media Project
 * 
 * This file defines the Person object, which is a data type
 * organized by the Graph.java class.
 * 
 * Contributors:
 * Maxwell Kleinsasser
 * Apeksha Maithal
 * Isaac Zaman
 * Matthew Karrmann
 * 
 */

package application;

/**
 * Provides functionality of person.
 * 
 * @author Matthew Karrmann
 *
 */
public class Person {
  private String name; // instance variable, name of person

  /**
   * Default no arg constructor
   */
  Person() {
    this.name = null;
  }

  /**
   * Constructor with name
   * 
   * @param name of person
   */
  Person(String name) {
    this.name = name;
  }

  /**
   * Getter for name
   * 
   * @return name for person
   */
  public String getName() {
    return this.name;
  }

  /**
   * Overrides hashcode to be same as hashcode of String. Allows for easy lookup in hashtable.
   */
  @Override
  public int hashCode() {
    if (this.name == null) {
      return 0;
    } else {
      return this.name.hashCode();
    }
  }

  /**
   * Overrides equals so that Persons with same name are equal. Prevents duplicate entries in
   * hashtable.
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof Person) {
      return this.name.equals(((Person) o).name);
    } else {
      return false;
    }
  }
}