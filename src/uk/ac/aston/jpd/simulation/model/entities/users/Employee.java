package uk.ac.aston.jpd.simulation.model.entities.users;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

/**
 * The {@code Employee} class represents a generic employee user within a
 * building. An employee changes floor according to a probability p.
 * 
 * @author Jims, Parminder
 *
 */

public abstract class Employee extends User {

	private final double probabilityChangeFloor;

	/**
	 * Creates an employee within 
	 * 
	 * @param building the building the user is in
	 * @param id       the user's unique identity 
	 * @param reqSpace the required space in the elevator by the user
	 */
	public Employee(Building building, String id, int reqSpace) {
		super(building, id, reqSpace);
		this.probabilityChangeFloor = building.getSimulation().getProbabilityP();
	}

	/**
	 * Determines the behaviour of an employee.
	 * <p>
	 * Upon reaching a floor, an employee changes floor according to a probability.
	 */
	@Override
	public void tick() {
		if (getCurrentFloor().getOnFloor().contains(this)) {
			double value = getRandom().nextDouble();
			if (value < probabilityChangeFloor) {
				List<Floor> currentlyAccessibleFloors = new ArrayList<Floor>(getAllAccessibleFloors());
				currentlyAccessibleFloors.remove(getCurrentFloor());
				Floor randomFloor = generateRandomFloor(currentlyAccessibleFloors);
				setRequest(randomFloor);
			}
		}
	}
}
