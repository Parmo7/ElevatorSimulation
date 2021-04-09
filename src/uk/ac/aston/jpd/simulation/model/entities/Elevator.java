package uk.ac.aston.jpd.simulation.model.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.aston.jpd.simulation.model.entities.users.FloorRequest;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

/**
 * The class {@code Elevator} represents a lift within a building. <br>
 * It allows {@link User} objects to enter and it takes them to their desired
 * floor. In order to do so, it checks the {@link FloorRequest} of each user.
 * 
 * @author Parminder
 */
public class Elevator {

	private static final String UP = "UP";
	private static final String DOWN = "DOWN";

	private final Floor[] floors;
	private final List<User> users = new ArrayList<User>();
	private final List<Floor> targetFloors = new ArrayList<Floor>();

	private int availableSpace;
	private String direction = UP;
	private boolean isOpen = false;
	private Floor currentFloor;

	/**
	 * Constructor for the {@code Elevator} entity. Takes a Building in order to
	 * retrieve its own capacity and the floors it can move between.
	 * 
	 * @param building the {@link Building} the elevator belongs to.
	 * @throws InvalidElevatorException if the provided elevator capacity is less
	 *                                  than 1.
	 */
	public Elevator(Building building) {
		floors = building.getFloors();
		currentFloor = floors[0];
		availableSpace = building.getSimulation().getElevatorCapacity();
		if (availableSpace < 1) {
			throw new InvalidElevatorException("Incorrect capacity", availableSpace);
		}
	}

	/**
	 * Delegates the ticking to {@code #tickIfOpen()} or {@code #tickIfClosed()},
	 * depending on whether the doors are open or closed.
	 */
	public void tick() {
		if (isOpen) {
			tickIfOpen();
		} else {
			tickIfClosed();
		}
	}

	/**
	 * Determines behaviour of the elevator when doors are open: it looks for
	 * requests on the {@code currentFloor}: if any, it allows users to
	 * {@code #board()} and {@code #alight()}. If none, it closes the doors.
	 */
	private void tickIfOpen() {
		if (hasRequests(currentFloor)) {
			alight();
			board();
		} else {
			isOpen = false;
			targetFloors.remove(currentFloor);
			currentFloor.resetWaitingForNext();
		}
	}

	/**
	 * Determines behaviour of the elevator when doors are closed: it looks for
	 * requests in the current direction. 
	 * <br> If none, it checks the opposite direction.
	 * <br> If there is no request in both directions, it checks the {@code currentFloor}.
	 * <br> Finally, if there is no request at all, it starts  moving towards the 
	 * ground floor.
	 */
	private void tickIfClosed() {
		String opposite = getOppositeDirection();
		if (hasRequests(direction)) { // check current direction
			move(direction);
			if (hasRequests(currentFloor)) {
				isOpen = true;
			}
		} else if (hasRequests(opposite)) { // check opposite direction
			direction = opposite;
			move(opposite);
			if (hasRequests(currentFloor)) {
				isOpen = true;
			}
		} else if (hasRequests(currentFloor)) { // check current floor
			isOpen = true;
		} else if (!currentFloor.equals(floors[0])) { // check if the current floor is the ground floor
			move(DOWN);
		}
	}

	/**
	 * Depending on the provided direction, moves the elevator up or down by one
	 * floor and notifies users within it that the {@code currentFloor} has changed.
	 * 
	 * @param dir direction to move to, either {@code UP} or {@code DOWN}
	 */
	private void move(String dir) {
		int lev = currentFloor.getLevel();
		if (dir.equals(UP)) {
			currentFloor = floors[lev + 1];
		} else if (dir.equals(DOWN)) {
			currentFloor = floors[lev - 1];
		}

		/* notify users that the floor has changed */
		for (User u : users) {
			u.updateFloor(currentFloor);
		}
	}

	/**
	 * Checks if the floors in the specified direction have any requests.
	 * 
	 * @param dir direction to check, either {@code UP} or {@code DOWN}.
	 * @return true if there are requests in the specified direction, false
	 *         otherwise.
	 */
	private boolean hasRequests(String dir) {
		if (dir.equals(UP)) {
			for (int i = currentFloor.getLevel() + 1; i < floors.length; i++) {
				if (hasRequests(floors[i])) {
					return true;
				}
			}
		} else if (dir.equals(DOWN)) {
			for (int i = currentFloor.getLevel() - 1; i >= 0; i--) {
				if (hasRequests(floors[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the specified {@link Floor} has any acceptable requests.
	 * 
	 * @param floor the {@link Floor} to check
	 * @return true if any of the users within the elevator has to get off at the
	 *         floor, or if any of the users in queue at the floor can be boarded.
	 *         Returns false otherwise.
	 */
	private boolean hasRequests(Floor floor) {
		boolean isInTargetFloors = targetFloors.contains(floor);

		if (isInTargetFloors) {
			return true;
		} else {
			List<User> waitingQueue = new ArrayList<>(floor.getInQueue());
			for (User waitingUser : waitingQueue) {
				if (waitingUser.getRequiredSpace() <= availableSpace && !waitingUser.isWaitingForNext()) {
					return true;
				}
			}
		}
		return false;
	}

	
	/**
	 * Checks all users within the elevator and allows them to get off if their
	 * requested floor matches the {@code currentFloor }. 
	 */
	private void alight() {
		List<User> inElevator = new ArrayList<>(users);
		for (User passenger : inElevator) {
			if (passenger.getRequest().getTargetFloor().equals(currentFloor)) {
				passenger.targetReached();
				users.remove(passenger);
				availableSpace += passenger.getRequiredSpace();

				targetFloors.remove(passenger.getRequest().getTargetFloor());
			}
		}
	}

	/**
	 * Checks all the users in queue on the {@code currentFloor}, if any, and allows
	 * them to enter the elevator if certain conditions are met.
	 */
	private void board() {
		List<User> waitingQueue = new ArrayList<>(currentFloor.getInQueue());

		for (int index = 0; index < waitingQueue.size() && availableSpace > 0; index++) {
			User passenger = waitingQueue.get(index);
			if (canBeBoarded(passenger)) {
				users.add(passenger);
				availableSpace -= passenger.getRequiredSpace();

				Floor target = passenger.getRequest().getTargetFloor();
				if (!targetFloors.contains(target)) {
					targetFloors.add(target);
				}
				passenger.requestAccepted();
			}
		}
	}

	/**
	 * Checks whether a user can be allowed to enter the elevator, depending on the
	 * available space and on the current state of the user.
	 * 
	 * @param passenger the {@link User} that wants to take the elevator
	 * @return true if the user can enter the elevator, false otherwise.
	 */
	private boolean canBeBoarded(User passenger) {
		FloorRequest request = passenger.getRequest();
		if (request == null) {
			return false;
		} else {
			Floor target = passenger.getRequest().getTargetFloor();
			boolean isInElevator = users.contains(passenger);
			boolean hasValidRequest = Arrays.asList(floors).contains(target) && !request.isDone();
			boolean fitsInElevator = passenger.getRequiredSpace() <= availableSpace;
			boolean isReadyToBoard = passenger.isReadyToBoard();
			return !isInElevator && hasValidRequest && fitsInElevator && isReadyToBoard;
		}
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Floor> getTargetFloors() {
		return targetFloors;
	}

	public int getAvailableSpace() {
		return availableSpace;
	}

	public Floor getCurrentFloor() {
		return currentFloor;
	}

	public String getDirection() {
		return direction;
	}

	public String getOppositeDirection() {
		return direction.equals(UP) ? DOWN : UP;
	}
	
	/**
	 * Provides a {@code String} with the current state of the elevator:
	 * specifically available space, current Floor, direction, door status and users
	 * within it.
	 * 
	 * @return a {@code String} reporting the current state of the elevator.
	 */
	public String getStatus() {
		String status = "ELEVATOR > currently at: " + currentFloor + ", going: " + direction;
		status += "\n         > available space: " + availableSpace + ", door: ";
		status += isOpen ? "OPEN" : "CLOSED";
		status += "\n         > users in elevator= ";
		for (User u : users) {
			status += u;
		}
		return status;
	}
}
