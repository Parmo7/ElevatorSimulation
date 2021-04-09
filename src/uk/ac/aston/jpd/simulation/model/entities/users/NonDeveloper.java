package uk.ac.aston.jpd.simulation.model.entities.users;

import uk.ac.aston.jpd.simulation.model.entities.Building;

/**
 * The class {@code NonDeveloper} represents a non-developer within a building.
 * It has access to all floors within it. 
 * @author Jims, Parminder
 */
public class NonDeveloper extends Employee {
	
	private static final String IDENTIFIER = "ND";
	private static final int REQUIRED_SPACE = 1;
	private static int count = 0;
	
	/**
	 * Creates a non-developers within the given {@link Building} object.
	 * 
	 * @param building the building the user is in
	 */
	public NonDeveloper(Building building) {
		super(building, IDENTIFIER + String.format("%02d", ++count), REQUIRED_SPACE);
	}
}