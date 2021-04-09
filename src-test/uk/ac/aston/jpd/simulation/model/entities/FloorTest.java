package uk.ac.aston.jpd.simulation.model.entities;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.entities.users.Client;
import uk.ac.aston.jpd.simulation.model.entities.users.Goggle;
import uk.ac.aston.jpd.simulation.model.entities.users.MaintenanceCrew;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

public class FloorTest {
			
	@Test
	public void goodQueue() {
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User c = new Client(building);
		floors[0].land(c);
		floors[0].joinQueue(c);
		User g = new Goggle(building);
		floors[0].land(g);
		floors[0].joinQueue(g);
		
		User firstInQueue = floors[0].getInQueue().get(0);
		assertTrue(firstInQueue instanceof Client);
	}
	
	@Test
	public void prioritizeClient() {
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User g = new Goggle(building);
		floors[0].land(g);
		floors[0].joinQueue(g);
		User c = new Client(building);
		floors[0].land(c);
		floors[0].joinQueue(c);
		User firstInQueue = floors[0].getInQueue().get(0);
		assertTrue(firstInQueue instanceof Client);
	}
	
	@Test
	public void twoClientsInQueue() {
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User c1 = new Client(building);
		floors[0].land(c1);
		floors[0].joinQueue(c1);
		User c2 = new Client(building);
		floors[0].land(c2);
		floors[0].joinQueue(c2);
		
		User first = floors[0].getInQueue().get(0);
		User second = floors[0].getInQueue().get(1);
		if (first.getTicketNumber() > second.getTicketNumber()) {
			fail("Clients not served in FIFO order");
		}
	}
	
	@Test
	public void duplicateQueueJoin(){
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User user1 = new Client(building);
		assertTrue(floors[0].land(user1));
		User user2 = user1;		
		assertFalse(floors[0].land(user2));		
	}
	
	@Test
	public void badQueueJoin() {
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User user = new MaintenanceCrew(building);
		assertFalse(floors[0].joinQueue(user));
	}
	
	@Test
	public void goodQueueJoin() {
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User user = new MaintenanceCrew(building);
		floors[0].land(user);
		assertTrue(floors[0].joinQueue(user));
	}
		
	@Test
	public void goodLand() {
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User user = new Goggle(building);
		assertTrue(floors[0].land(user));
	}
	
	
	@Test
	public void badLand() {
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User user = new Goggle(building);
		Floor lastFloor = floors[floors.length-1];
		assertFalse(lastFloor.land(user));
	}
	
	@Test
	public void goodLeaveFromFloor(){
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User user = new Client(building);
		floors[0].land(user);
		assertTrue(floors[0].leave(user));
	}
	
	@Test
	public void goodLeaveFromQueue(){
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User user = new Client(building);
		floors[0].land(user);
		floors[0].joinQueue(user);
		assertTrue(floors[0].leave(user));
	}
	
	@Test
	public void badLeave(){
		var sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User user = new Client(building);
		assertFalse(floors[0].leave(user));
	}
}
