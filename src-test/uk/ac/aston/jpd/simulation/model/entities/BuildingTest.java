package uk.ac.aston.jpd.simulation.model.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.entities.users.Client;
import uk.ac.aston.jpd.simulation.model.entities.users.Goggle;
import uk.ac.aston.jpd.simulation.model.entities.users.MaintenanceCrew;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

public class BuildingTest {
	
	private Simulation sim;
	private Building building;
	private Floor[] floors;
	 
	@Before 
	public void setUp() {
		sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		building = sim.getBuilding();
		floors = building.getFloors();
	}
	
	@Test
	public void goodIncomingUser() {
		User goodUser = new Goggle(building);
		boolean landOnGroundFloor = building.enter(goodUser);
		assertTrue(landOnGroundFloor);
	}
	
	@Test
	public void badIncomingUser() {
		User goodUser = null;
		assertFalse(building.enter(goodUser));
	}
	
	@Test
	public void badLandingUser() {
		User user = new Goggle(building);
		boolean landOnTopFloor = floors[floors.length-1].land(user);
		assertFalse(landOnTopFloor);
	}
		
	@Test
	public void goodOutgoingUser() {
		User goodUser = new Client(building);
		sim.enter(goodUser);
		assertTrue(building.quit(goodUser));
	}
	
	@Test
	public void badOutgoingUser() {
		User badUser = new MaintenanceCrew(building);
		sim.enter(badUser);
		sim.tick(); // elevator opens its doors
		sim.tick(); // user in queue gets into the elevator
		boolean isInElevator = building.getElevator().getUsers().contains(badUser);
		if (isInElevator) {
			assertFalse(building.quit(badUser));
		}
	}	
}
