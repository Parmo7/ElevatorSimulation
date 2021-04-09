package uk.ac.aston.jpd.simulation.model;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.users.MaintenanceCrew;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

public class WaitingStatisticsTest {
	
	private Simulation sim;
	private Building building;
	private WaitingStatistics waitStats;
	 
	@Before 
	public void setUp() {
		sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		building = sim.getBuilding();
		waitStats = sim.getWaitingStatistics();
	}
	
	@Test
	public void userServed() {
		User m = new MaintenanceCrew(building);
		building.enter(m);
		sim.tick();  // elevator opens its doors
		assertTrue(isNaN(waitStats.getAverageWaitingTime()));
		sim.tick();  // users in queue get into the elevator
		assertTrue(waitStats.getAverageWaitingTime() == 1);
	}
	
	@Test
	public void noUserServed() {
		sim.tick(); // elevator opens its doors
		sim.tick(); // users in queue get into the elevator
		assertTrue(isNaN(waitStats.getAverageWaitingTime()));
	}
	
	private boolean isNaN(double v) {
	    return !Double.isFinite(v);
	}	
}
