package uk.ac.aston.jpd.simulation.model.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import uk.ac.aston.jpd.simulation.model.entities.users.User;
import uk.ac.aston.jpd.simulation.model.entities.users.UserComparator;

/**
 * The class {@code Floor} represents a floor within a building. 
 * <br> It contains {@link User}s: the ones that are staying 
 * on the floor and the ones in queue for the elevator.
 * 
 * @author Parminder
 */
public class Floor {

	private final Building building;
	private final int level;
	private final List<User> usersOnFloor = new ArrayList<User>();
	private final List<User> usersInQueue = new LinkedList<User>();
	
	/**
	 * keeps track of the number of users that have joined the queue so far
	 */
	private int queueCount = 0;
	
	/**
	 * Creates a {@code Floor} with the given level in a {@link Building}.
	 * 
	 * @param building the {@link Building} which the floor belongs to
	 * @param level    the {@code int} value representing the floor level.
	 */
	public Floor(Building building, int level) {
		this.building = building;
		this.level = level;
	}

	/**
	 * Propagates the ticking to all users on the floor, including those that are
	 * waiting in the queue.
	 */
	public void tick() {
		for (User u : new ArrayList<User>(usersOnFloor)) {
			u.tick();
		}
		for (User u : new ArrayList<User>(usersInQueue)) {
			u.tick();
		}
	}

	/**
	 * Allows a {@link User} to enter the floor, if not already present in it.
	 * Returns true if the user successfully lands on the floor.
	 * 
	 * @param u the {@code User} that would like to access the floor.
	 * @return true if the user successfully enters the floor, false otherwise.
	 */
	public boolean land(User u) {
		if (u != null) {
			if (!usersOnFloor.contains(u) && u.getCurrentFloor().equals(this)) {
				usersOnFloor.add(u);
				u.updateFloor(this);
				return true;
			}
		}
		return false;
	}

	/**
	 * Allows a {@link User} to leave the floor. Returns true if the user is
	 * successfully removed from the floor.
	 * 
	 * @param u the {@code User} that would like to leave the floor.
	 * @return true if the user successfully leaves the floor, false otherwise.
	 */
	public boolean leave(User u) { 
		return (usersOnFloor.remove(u) || usersInQueue.remove(u));
	}

	/**
	 * Allows a {@link User} on the floor to join the queue for the elevator.
	 * Returns true if the user successfully joins the queue.
	 * 
	 * @param u the {@code User} that would like to join the queue.
	 * @return true if the user successfully joins the queue on the floor, false
	 *         otherwise.
	 */
	public boolean joinQueue(User u) {
		if (u != null) {
			if (usersOnFloor.remove(u) || usersInQueue.remove(u)) {
				/* Assign the user a number depending on the time he joins the queue */
				u.assignTicketNumber(queueCount++);
				usersInQueue.add(u);
				Collections.sort(usersInQueue, new UserComparator());
				building.queueJoined(u);
				return true;
			}	
		}
		return false;
	}
	
	/**
	 * Notifies the {@link Building} that the elevator has started serving the
	 * specified user.
	 * 
	 * @param u the {@code User} that is now being served by the elevator.
	 */
	public void servingStarted(User u) {
		if (u != null) {
			building.servingStarted(u); 
		}
	}
	
	/**
	 * Updates the status of all users waiting in the queue by setting their
	 * {@code waitingForNext} field to false;
	 */
	public void resetWaitingForNext() { 
		for (User user: new LinkedList<>(usersInQueue)) {
			user.setWaitingForNext(false);
		}
	}
	
	public List<User> getOnFloor() {
		return usersOnFloor;
	}

	public List<User> getInQueue() {
		return usersInQueue;
	}

	public int getLevel() {
		return level;
	}
	
	public int getQueueCount() {
		return queueCount;
	}

	/**
	 * Provides a {@code String} with the current state of the floor, i.e. which
	 * users are on the floor and which are waiting in the queue.
	 * 
	 * @return a {@code String} reporting the current state of the floor.
	 */
	public String getStatus() {
		String status = toString() + ": > on floor= ";
		for (User u : usersOnFloor) {
			status += u;
		}
		status += "\n         > in queue= ";
		for (User u : usersInQueue) {
			status += u;
		}
		return status;
	}	
	
	public String toString() {
		if (level == 0) {
			return "Floor G";
		} else {
			return "Floor " + level;
		}
	}
}
