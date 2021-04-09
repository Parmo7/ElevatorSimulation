package uk.ac.aston.jpd.simulation.model.entities.users;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

public class FloorRequestTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullUser() throws Exception {
		var sim = new Simulation.Builder().build();
		var floors = sim.getBuilding().getFloors();
		
		User user = null;
		new FloorRequest(user, floors[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullFloor() throws Exception {
		var sim = new Simulation.Builder().build();
		var building = sim.getBuilding();
		
		User user = new Client(building);
		new FloorRequest(user, null);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void inaccessibleFloor() throws Exception {
		var sim = new Simulation.Builder().build();
		var building = sim.getBuilding();
		var floors = building.getFloors();
		
		User client = new Client(building);
		Floor topFloor = floors[floors.length - 1];
		new FloorRequest(client, topFloor);
	}
	
	@Test
	public void badMarkAsDone() {
		var sim = new Simulation.Builder().build();
		var building = sim.getBuilding();
		
		User maintenanceCrew = new MaintenanceCrew(building);
		building.enter(maintenanceCrew);
		assertFalse(maintenanceCrew.getRequest().markAsDone());
	}
}
