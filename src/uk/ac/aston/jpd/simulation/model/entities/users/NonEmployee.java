package uk.ac.aston.jpd.simulation.model.entities.users;

import java.util.List;
import java.util.Random;

import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

/**
 * The {@link NonEmployee} class represents a generic user that does not work
 * within the building, such as a {@link Client} or {@link MaintenanceCrew}. A
 * non-employee leaves the building after his job within the building has been
 * completed.
 * 
 * @author Jims, Parminder
 */
public abstract class NonEmployee extends User {

	private final Building building;
	private final Floor[] floors;

	private final int minStayTicks;
	private final int maxStayTicks;

	private boolean firstRequestProcessed = false;
	private boolean isReadyToLeave = false;
	private int leaveTick = -1;

	/**
	 * Creates a non-employee within the provided building, with the given id,
	 * required space, minimum and maximum stay time.
	 * 
	 * @param building the building the user is in
	 * @param id       the user's unique identity used for the
	 * 
	 * @param reqSpace the space required in the elevator by the user
	 * @param minStay  minimum ticks for which the user will stay on a floor
	 * @param maxStay  maximum ticks for which the user will stay on a floor
	 */
	public NonEmployee(Building building, String id, int reqSpace, int minStay, int maxStay) {
		super(building, id, reqSpace);

		this.building = building;
		this.floors = building.getFloors();

		this.minStayTicks = minStay;
		this.maxStayTicks = maxStay;
	}

	/**
	 * Determines the behaviour of a non-employee.
	 * <p>
	 * Upon reaching the desired floor, the user generates a leave tick. When the
	 * leave tick is reached, the user prepares for leaving.
	 */
	@Override
	public void tick() {
		int currentTick = building.getSimulation().getTick();
		if (firstRequestProcessed && currentTick == leaveTick) {
			isReadyToLeave = true;
			if (!getCurrentFloor().equals(floors[0])) {
				List<Floor> newAccessibleFloors = getAllAccessibleFloors();
				newAccessibleFloors.add(floors[0]);
				setAccessibleFloors(newAccessibleFloors);
				setRequest(floors[0]);
			}
		}
		if (isReadyToLeave && getCurrentFloor().equals(floors[0])) {
			building.quit(this);
		}
	}

	/**
	 * As soon as the non-employee has reached its desired floor, it triggers the
	 * {@link #generateLeaveTick()} method to randomly pick a leave tick.
	 */
	@Override
	public void targetReached() {
		super.targetReached();
		if (!firstRequestProcessed) {
			firstRequestProcessed = true;
			generateLeaveTick();
		}
	}

	/**
	 * Generates a random number within the {@link #minStayTicks} and the
	 * {@link #maxStayTicks}, and sets the leave tick (i.e. the tick at which the
	 * user will prepare for leaving the building) accordingly. 
	 */
	private void generateLeaveTick() {
		int currentTick = building.getSimulation().getTick();
		Random rdm = building.getSimulation().getRandom();

		int stayTicks = minStayTicks + rdm.nextInt(maxStayTicks - minStayTicks);
		leaveTick = currentTick + stayTicks;
	}
	
	public int getLeaveTick() {
		return leaveTick;
	}
}