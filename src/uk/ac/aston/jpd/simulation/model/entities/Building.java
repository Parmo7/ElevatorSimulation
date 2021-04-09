package uk.ac.aston.jpd.simulation.model.entities;

import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

/**
 * the class {@code Floor} represents the building for the Simulator. it
 * contains {@link Floor} and {@link Elevator}.
 * 
 * @author Joseph, Parminder
 *
 */
public class Building {

	private final Simulation simulation;
	private final Floor[] floors;
	private final Elevator elevator;

	/**
	 * 
	 * {@code Building} creates the {@code Elevator} and {@code Floor} according to
	 * the number of floors from using {@link Simulation#getTotalFloors()} method
	 * 
	 * @param simulation the {@link Simulation} which the building belongs to.
	 * @throws InvalidBuildingException when number of floors provided is less than
	 */
	public Building(Simulation simulation) {
		this.simulation = simulation;
		int numFloors = simulation.getTotalFloors();
		if (numFloors < 1) {
			throw new InvalidBuildingException("Incorrect number of floors", numFloors);
		}
		floors = new Floor[numFloors];
		for (int level = 0; level < floors.length; level++) {
			floors[level] = new Floor(this, level);
		}

		elevator = new Elevator(this);
	}

	/**
	 * {@code Building.tick()} propogates all the floors and elevator
	 */
	public void tick() {
		for (Floor f : floors) {
			f.tick();
		}
		elevator.tick();
	}

	/**
	 * Allows a {@Link User} to enter the building's ground floor.
	 * 
	 * @param incoming the {@code User} that would like to enter the building
	 * @return true if the user is within the ground floor's on floor list, false
	 *         otherwise
	 */
	public boolean enter(User incoming) {
		if (floors[0].land(incoming)) { 
			incoming.setFirstRequest();
			return true;
		}
		return false;
	}

	/**
	 * Allows a {@Link User} to leave the building. This will return true if the
	 * user has successfully been removed from the building
	 * 
	 * @param outgoing the {@code User} that would like to quit the building
	 * @return true if the user successfully leaves the building, false otherwise.
	 */
	public boolean quit(User outgoing) {
		if (floors[0].leave(outgoing)) {
			simulation.quit();
			return true;
		}
		return false;
	}

	/**
	 * Notifies the {@link Simulation} that the User has joined the queue.
	 * 
	 * @param u the {@code User} that is now in the queue of a floor
	 */
	public void queueJoined(User u) {
		if (u.isInQueue()) { 
			simulation.queueJoined(u);
		}
	}

	/**
	 * Notifies the {@link Simulation} that the elevator has started serving the
	 * specified user.
	 * 
	 * @param u the {@code User} that is now being served by the elevator.
	 */
	public void servingStarted(User u) {
		if (elevator.getUsers().contains(u)) { 
			simulation.servingStarted(u);
		}
	}

	public void fileComplaint() {
		simulation.fileComplaint();
	}

	public Simulation getSimulation() {
		return simulation;
	}

	public Elevator getElevator() {
		return elevator;
	}

	public Floor[] getFloors() {
		return floors;
	}
}
