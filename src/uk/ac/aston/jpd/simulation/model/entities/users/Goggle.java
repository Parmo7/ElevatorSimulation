package uk.ac.aston.jpd.simulation.model.entities.users;

import uk.ac.aston.jpd.simulation.model.entities.Building;

/**
 * The {@code Goggle} class represents a Goggle developer within a building.
 * 
 * @author Parminder
 */
public class Goggle extends Developer {
	
	private static final String IDENTIFIER = "GG";
	private static int count = 0;	
	
	/**
	 * Creates a Goggle developer within the given building.
	 * 
	 * @param building
	 */
	public Goggle(Building building) {
		super(building, IDENTIFIER, ++count);
	}

	/**
	 * Checks if the specified user is a rival to Goggle developers
	 * @param u the {@code User} to check for rivalry
	 * @return true if the given user is a rival
	 */
	@Override
	protected boolean isRival(User u) {
		return (u instanceof Mugtome);
	}
}
