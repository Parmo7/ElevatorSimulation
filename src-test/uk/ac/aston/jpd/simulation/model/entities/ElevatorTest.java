package uk.ac.aston.jpd.simulation.model.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.entities.users.Client;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

public class ElevatorTest {
	
	@Test
	public void serveInQueue() {
		Simulation sim = new Simulation.Builder()
				.elevatorCapacity(4)
				.goggles(3).mugtomes(0).nonDevelopers(0)
				.probabilityQ(0).probabilityMaintenance(0)
				.build();
		Elevator elevator = sim.getBuilding().getElevator();
		Floor[] floors = sim.getBuilding().getFloors();
		
		sim.tick(); // users join the queue, elevator opens its doors
		List<User> inQueue = new ArrayList<>(floors[0].getInQueue());		
		sim.tick(); // users get into the elevator
		assertEquals(0, floors[0].getInQueue().size());
		assertEquals(inQueue, elevator.getUsers());
	}
	
	@Test 
	public void decreaseAvailableSpace() {
		Simulation sim = new Simulation.Builder()
				.elevatorCapacity(4)
				.goggles(1).mugtomes(0).nonDevelopers(0)
				.probabilityQ(0).probabilityMaintenance(0)
				.build();
		Elevator elevator = sim.getBuilding().getElevator();
		
		sim.tick(); // users join queue, elevator opens its doors	
		sim.tick(); // users get into the elevator		
		assertEquals(3, elevator.getAvailableSpace());
	}
	
	@Test 
	public void noMoreAvailableSpace() {
		Simulation sim = new Simulation.Builder()
				.elevatorCapacity(4)
				.goggles(0).mugtomes(10).nonDevelopers(0)
				.probabilityQ(0).probabilityMaintenance(0)
				.build();
		Floor[] floors = sim.getBuilding().getFloors();
		Elevator elevator = sim.getBuilding().getElevator();
		
		sim.tick(); // users join queue, elevator opens its doors	
		sim.tick(); // users get into the elevator	
		assertTrue(elevator.getAvailableSpace() == 0);
		assertEquals(6, floors[0].getInQueue().size());
	}
	
	@Test 
	public void rivalNotBoarded() {
		Simulation sim = new Simulation.Builder()
				.elevatorCapacity(4)
				.goggles(1).mugtomes(1).nonDevelopers(0)
				.probabilityQ(0).probabilityMaintenance(0)
				.build();
		Elevator elevator = sim.getBuilding().getElevator();
		
		sim.tick(); // users join queue, elevator opens its doors
		sim.tick(); // users get into the elevator	
		List<User> inElevator = elevator.getUsers();
		assertEquals(1, inElevator.size());
	}
	
	@Test 
	public void badUserInQueue() {
		Simulation sim = new Simulation.Builder()
				.elevatorCapacity(1)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.probabilityQ(0).probabilityMaintenance(0)
				.build();
		Building building = sim.getBuilding();
		Floor[] floors = building.getFloors();
		Elevator elevator = building.getElevator();
		
		User badUser = new Client(building); // user does not have a request
		floors[0].land(badUser);
		floors[0].joinQueue(badUser);	
		sim.tick(); // user joins queue, elevator opens its doors
		sim.tick(); // user gets into the elevator	
		List<User> inElevator = elevator.getUsers();
		assertEquals(0, inElevator.size());
		assertTrue(floors[0].getInQueue().contains(badUser));
	}
}
