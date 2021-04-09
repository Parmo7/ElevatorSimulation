package uk.ac.aston.jpd.simulation.visuals;

import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.WaitingStatistics;
import uk.ac.aston.jpd.simulation.model.entities.Elevator;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

/**
 * The class {@code TextView} represents a non-interactive text-based view of a
 * given simulation.
 * 
 * @author Parminder
 */
public class TextView {
	/**
	 * For the provided simulation, displays the status of the elevator and the
	 * floors at the current tick, along with the average waiting time and the
	 * number of complaints.
	 * 
	 * @param simulation the {@link Simulation} to display on the console.
	 */
	public void visualize(Simulation simulation) {
		
		Elevator elevator = simulation.getBuilding().getElevator();
		Floor[] floors = simulation.getBuilding().getFloors();
		WaitingStatistics statistics = simulation.getWaitingStatistics();
		
		System.out.println("**************** TICK " + simulation.getTick() + " *******************");	
		System.out.printf("Average waiting time: %.2f ticks", statistics.getAverageWaitingTime());
		System.out.println(" | Complaints: " + simulation.getComplaints() + "\n");
		
		System.out.println(elevator.getStatus());
		System.out.println();
				
		for (int i = floors.length - 1; i >=0; i--) {
			System.out.println(floors[i].getStatus());
		}
		
		System.out.println();
	}

}
