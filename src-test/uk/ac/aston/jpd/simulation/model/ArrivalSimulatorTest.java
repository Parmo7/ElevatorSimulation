package uk.ac.aston.jpd.simulation.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.ac.aston.jpd.simulation.model.entities.Floor;
import uk.ac.aston.jpd.simulation.model.entities.users.Client;
import uk.ac.aston.jpd.simulation.model.entities.users.MaintenanceCrew;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

public class ArrivalSimulatorTest {
	
	@Test
	public void noIncomings() throws Exception {
		Simulation sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		ArrivalSimulator arrivalSim = sim.getArrivalSimulator();
		assertEquals(0, arrivalSim.getTotalClients());
		assertEquals(0, arrivalSim.getTotalDevelopers());
		assertEquals(0, arrivalSim.getTotalMaintenanceCrews());
		assertEquals(0, arrivalSim.getTotalNonDevelopers());
	}
	
	@Test
	public void incomingClient() throws Exception {
		Simulation sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(1).probabilityMaintenance(0)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		Floor[] floors = sim.getBuilding().getFloors();
	
		sim.tick();
		User user = null;
		if (!floors[0].getInQueue().isEmpty()) {
			user = floors[0].getInQueue().get(0);
		} else if (!floors[0].getOnFloor().isEmpty()) {
			user = floors[0].getOnFloor().get(0);
		}
		assertTrue(user instanceof Client);
	}
	
	@Test
	public void incomingMaintenanceCrew() throws Exception {
		Simulation sim = new Simulation.Builder()
				.probabilityP(0).probabilityQ(0).probabilityMaintenance(1)
				.goggles(0).mugtomes(0).nonDevelopers(0)
				.build();
		Floor[] floors = sim.getBuilding().getFloors();
	
		sim.tick();
		User user = null;
		if (!floors[0].getInQueue().isEmpty()) {
			user = floors[0].getInQueue().get(0);
		} else if (!floors[0].getOnFloor().isEmpty()) {
			user = floors[0].getOnFloor().get(0);
		}
		assertTrue(user instanceof MaintenanceCrew);
	}
	
	@Test(expected = InvalidArrivalsException.class)
	public void badValue() throws Exception {
		new Simulation.Builder().probabilityQ(-1).build();
	}
	
	@Test(expected = InvalidArrivalsException.class)
	public void badValues() throws Exception {
		new Simulation.Builder().nonDevelopers(-1).goggles(-1).mugtomes(-1).build();
	}
	
	@Test(expected = InvalidArrivalsException.class)
	public void goodAndBadValue() throws Exception {
		new Simulation.Builder().probabilityQ(0.001).probabilityMaintenance(-1).build();
	}	
		
	@Test
	public void goodValues() throws Exception {
		Simulation sim = new Simulation.Builder()
				.probabilityQ(0.001).probabilityP(0.001).probabilityMaintenance(0.001)
				.nonDevelopers(1).goggles(1).mugtomes(1)
				.build();
		sim.tick();
	}
}

