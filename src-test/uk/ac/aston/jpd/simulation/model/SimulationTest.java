package uk.ac.aston.jpd.simulation.model;

import org.junit.Test;
import uk.ac.aston.jpd.simulation.model.entities.InvalidBuildingException;
import uk.ac.aston.jpd.simulation.model.entities.InvalidElevatorException;

public class SimulationTest {
	
	@Test(expected = InvalidBuildingException.class)
	public void badNumFloors() throws Exception {
		new Simulation.Builder().numFloors(-1).elevatorCapacity(4).build();
	}
	
	@Test(expected = InvalidBuildingException.class)
	public void badNumFloorsAndElevCapacity() throws Exception {
		new Simulation.Builder().numFloors(-1).elevatorCapacity(-1).build();
	}
	
	@Test(expected = InvalidElevatorException.class)
	public void badElevatorCapacity() throws Exception {
		new Simulation.Builder().numFloors(4).elevatorCapacity(-1).build();
	}	
	
	@Test
	public void goodValues() throws Exception {
		new Simulation.Builder().numFloors(4).elevatorCapacity(4).build();
	}
}
