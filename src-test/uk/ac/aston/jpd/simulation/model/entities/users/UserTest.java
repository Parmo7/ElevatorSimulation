package uk.ac.aston.jpd.simulation.model.entities.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

import java.util.ArrayList;
import java.util.List;

public class UserTest {
	
	private Simulation sim;
	private Building building;
	private Floor[] floors;
	 
	@Before 
	public void setUp() {
		sim = new Simulation.Builder()
				.elevatorCapacity(4).numFloors(5)
				.probabilityP(1).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		building = sim.getBuilding();
		floors = building.getFloors();
	}
	
	@Test
	public void emptyAccessibleFloors() {
		User u = new Client(building);
		
		List<Floor> empty = new ArrayList<Floor>();
		assertEquals(null, u.generateRandomFloor(empty));
	}

	@Test
	public void currentFloorRequest() {
		User user = new NonDeveloper(building);
		floors[0].land(user);
		user.setRequest(user.getCurrentFloor());
		assertFalse(user.isInQueue());
	}
	
	@Test
	public void differentFloorRequest() {
		User user = new NonDeveloper(building);
		floors[0].land(user);
		user.setRequest(floors[floors.length - 1]);
		assertTrue(user.isInQueue());
	}
	
	@Test
	public void changeFloorEmployee() {
		Employee nonDev = new NonDeveloper(building);
		floors[0].land(nonDev);
		Floor firstFloor = floors[1];
		nonDev.setRequest(firstFloor);
		
		do {
			sim.tick();
		} while (!nonDev.getCurrentFloor().equals(firstFloor));
		
		sim.tick(); // user enters the first floor
		assertTrue(firstFloor.getOnFloor().contains(nonDev));
		assertFalse(nonDev.isInQueue());
		
		nonDev.tick(); // user joins the queue
		assertTrue(nonDev.isInQueue());
	}
		
	@Test
	public void nonEmployeeLeaving() {
		NonEmployee ne = new MaintenanceCrew(building);
		building.enter(ne);
		do {
			sim.tick();
		} while (ne.getLeaveTick() == -1 || sim.getTick() <= ne.getLeaveTick() );		
		boolean hasGroundFloorRequest = ne.getRequest().getTargetFloor().equals(floors[0]);
		assertTrue(hasGroundFloorRequest);
		assertTrue(ne.isInQueue());
		do {
			sim.tick();
		} while (ne.getCurrentFloor() != floors[0]);
		sim.tick(); // user enters the ground floor
		assertTrue(floors[0].getOnFloor().contains(ne));
		sim.tick(); // user leaves the building
		assertFalse(floors[0].getOnFloor().contains(ne));
	}
	
	@Test
	public void annoyedClient() {
		Client c = new Client(building);
		floors[0].land(c);
		floors[0].joinQueue(c); //make client join queue without a request
		do {
			sim.tick();
		} while (!floors[0].getInQueue().isEmpty());		
		assertEquals(1, sim.getComplaints());
	}
}
