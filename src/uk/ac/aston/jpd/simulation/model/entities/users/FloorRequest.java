package uk.ac.aston.jpd.simulation.model.entities.users;

import uk.ac.aston.jpd.simulation.model.entities.Floor;

/**
 * The {@code FloorRequest} class stores a user's desired floor.
 * 
 * @author Jims, Parminder
 */
public class FloorRequest {
	private final User user;
	private final Floor sourceFloor;
	private final Floor targetFloor;

	private boolean isDone = false;

	/**
	 * Creates a {@code FloorRequest} object for the given user.
	 * 
	 * 
	 * @param user   the user to which the request belongs to
	 * @param target the user's target floor.
	 * 
	 * @throws IllegalArgumentException      when either user or target is null.
	 * @throws UnsuppoeredOperationException when the target floor is not within the
	 *                                       user's accessible floors.
	 */
	public FloorRequest(User user, Floor target) {
		if (user == null || target == null) {
			throw new IllegalArgumentException("Invalid request");
		} else 	if (!user.getAllAccessibleFloors().contains(target)) {
			throw new UnsupportedOperationException("Inaccesible floor requested");
		}
		this.user = user;
		this.sourceFloor = user.getCurrentFloor();
		this.targetFloor = target;		
	}
	
	public Floor getSourceFloor() {
		return sourceFloor;
	}

	public Floor getTargetFloor() {
		return targetFloor;
	}
	
	public User getUser() {
		return user;
	}

	/**
	 * Checks if the current floor of the user is the target floor. If so, it marks
	 * the request as done.
	 * 
	 * @return true if the current floor is the target floor. False otherwise.
	 */
	public boolean markAsDone() {
		if (user.getCurrentFloor().equals(targetFloor)) {
			isDone = true;
			return true;
		}
		return false;
	}

	public boolean isDone() {
		return isDone;
	}
}