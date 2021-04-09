package uk.ac.aston.jpd.simulation.model.entities.users;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.Elevator;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

/**
 * The {@code Developer} class represents a generic developer within a building.
 * If a developer is rival to another developer, he will not take the same
 * elevator.
 * 
 * @author Jims, Parminder
 */
public abstract class Developer extends Employee {
	
	private static final int REQUIRED_SPACE = 1;
	
	private final Elevator elevator;

	/**
	 * Creates a {@code Developer} within the given building. 
	 * 
	 * @param building   the building the user is in 
	 * @param identifier the user's unique identifier
	 * @param count      the number to be appended to the identifier
	 */
	public Developer(Building building, String identifier, int count) {
		super(building, identifier + String.format("%02d", count), REQUIRED_SPACE);
		this.elevator = building.getElevator();
		
		List<Floor> accessibleFloors = new ArrayList<Floor>();
		int middle = (building.getFloors().length)/2;
		for (Floor f : building.getFloors()) {
			if (f.getLevel() >= middle) {
				accessibleFloors.add(f);
			}
		}
		setAccessibleFloors(accessibleFloors);
	}
	
	/**
	 * Checks whether the users already into the elevator are rivals to the current
	 * user.
	 * 
	 * @return true if the developer is ready to enter the elevator, false
	 *         otherwise.
	 */
	@Override
	public boolean isReadyToBoard() {
		List<User> inElevator = new ArrayList<User>(elevator.getUsers());
		for (User user: inElevator) {
			if (isRival(user)) {
				setWaitingForNext(true);
				joinQueue();  //rejoin the queue
				return false;
			}
		}
		return true;
	}
	
	protected abstract boolean isRival(User u);
	
	@Override
	public void requestAccepted() {
		setWaitingForNext(false);
		super.requestAccepted();
	}
}