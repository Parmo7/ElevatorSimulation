package uk.ac.aston.jpd.simulation.model.entities.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

/**
 * The {@code User} class represents a generic person in a building. A user may
 * have one or more {@link FloorRequest}
 * 
 * @author Parminder, Jims
 */
public abstract class User {
	
	private static final int DEFAULT_REQ_SPACE = 1;
	
	private final Floor[] floors;
	private final String id;
	private final int requiredSpace;
	private final Random random;

	private Floor currentFloor;
	private List<Floor> allAccessibleFloors;
	
	private FloorRequest request;
	private int tktNumber;	
	private boolean waitingForNext = false;

	/**
	 * Creates a {@code User} in the specified building, with the given unique ID
	 * and required space.
	 * 
	 * @param building the building the user is in
	 * @param id       the user's unique identity
	 * @param reqSpace space required in the elevator by the user
	 */
	public User(Building building, String id, int reqSpace) {
		this.floors = building.getFloors();	
		this.currentFloor = floors[0];
		this.allAccessibleFloors = new ArrayList<Floor>(Arrays.asList(floors));
		this.id = id;
		this.requiredSpace = (reqSpace > 0)? reqSpace : DEFAULT_REQ_SPACE; 

		random = building.getSimulation().getRandom();
	}

	public abstract void tick();
	
	/**
	 * Sets the floors that are accessible by a specific user.
	 * 
	 * @param accessible list of floors the user is allowed to be in
	 */
	protected void setAccessibleFloors(List<Floor> accessible) {
		if (Arrays.asList(floors).containsAll(accessible)) { 
			this.allAccessibleFloors = accessible;
		}
	}
	
	/**
	 * Randomly generates a {@link FloorRequest} by choosing among all the initially
	 * accessible floors.
	 */
	public void setFirstRequest() {
		Floor rdmFloor = generateRandomFloor(allAccessibleFloors);
		setRequest(rdmFloor);
	}
	
	/**
	 * Randomly picks a {@link Floor} from the accessible floors it has been
	 * provided with.
	 * 
	 * @param accessibleFloors the list of floors to pick from
	 * @return a {@link Floor} if the list of accessible floors is not empty.
	 *         Returns null otherwise.
	 */
	protected final Floor generateRandomFloor(List<Floor> accessibleFloors) {
		if (accessibleFloors.size() != 0) {
			int rdmIndex = random.nextInt(accessibleFloors.size());
			Floor rdmFloor = accessibleFloors.get(rdmIndex);
			return rdmFloor;
		} else {
			return null;
		}
	}

	/**
	 * Sets a new {@link FloorRequest} to the provided floor. <br>
	 * Makes the user join the queue if the request is to a different floor.
	 * 
	 * @param targetFloor the desired floor
	 */
	protected final void setRequest(Floor targetFloor) {
		try {
			request = new FloorRequest(this, targetFloor);
			if (!targetFloor.equals(currentFloor)) {
				joinQueue();
			} else {
				targetReached();
			}
		} catch (IllegalArgumentException ex) {
			System.err.println("Illegal user request"); 
		} catch (UnsupportedOperationException ex) {
			System.err.println("Floor " + targetFloor.getLevel() + " is not accessible by " + this.getID());
		}	
	}
	
	public void setWaitingForNext(boolean value) {
		waitingForNext = value;
	}

	protected void joinQueue() {
		currentFloor.joinQueue(this);
	}

	public void assignTicketNumber(int ticketNumber) {
		this.tktNumber = ticketNumber;
	}

	/**
	 * Notifies the current floor that this user's request has been accepted by the elevator.
	 */
	public void requestAccepted() {
		currentFloor.servingStarted(this);
		currentFloor.leave(this);
	}
	
	/**
	 * Updates the current floor of the user by replacing it with the provided one.
	 * 
	 * @param f the new {@link Floor}
	 */
	public void updateFloor(Floor f) {
		currentFloor = f;
	}

	/**
	 * If the desired floor has been reached, allows the user to enter.
	 */
	public void targetReached() {
		if (request.markAsDone()) {
			currentFloor.land(this);
		}
	}

	public boolean isInQueue() {
		return currentFloor.getInQueue().contains(this);
	}
	
	public boolean isReadyToBoard() {
		return true;
	}
	
	public boolean isWaitingForNext() {
		return waitingForNext;
	}
	
	public String getID() {
		return id;
	}

	public int getRequiredSpace() {
		return requiredSpace;
	}

	public Floor getCurrentFloor() {
		return currentFloor;
	}

	public FloorRequest getRequest() {
		return request;
	}

	public Random getRandom() {
		return random;
	}

	public List<Floor> getAllAccessibleFloors() {
		return allAccessibleFloors;
	}

	public int getTicketNumber() {
		return tktNumber;
	}
	
	public String toString() {
		if (request != null) {
			return id + "[" + request.getTargetFloor().getLevel() + "] ";
		} else {
			return id + "[*] ";
		}
	}
}
