package uk.ac.aston.jpd.simulation.model.entities.users;

import uk.ac.aston.jpd.simulation.model.entities.Building;

/**
 * The {@code Mugtome} class represents a Mugtome Developer within a building.
 * 
 * @author Parminder
 */
public class Mugtome extends Developer {

	private static final String IDENTIFIER = "MU";
	private static int count = 0;

	/**
	 * Creates a Mugtome developer within the given building.
	 * 
	 * @param building
	 */
	public Mugtome(Building building) {
		super(building, IDENTIFIER, ++count);
	}

	/**
	 * Checks if the specified user is a rival to Mugtome developers
	 * @param u the {@code User} to check for rivalry
	 * @return true if the given user is a rival
	 */
	@Override
	protected boolean isRival(User u) {
		return (u instanceof Goggle);	
	}
}
