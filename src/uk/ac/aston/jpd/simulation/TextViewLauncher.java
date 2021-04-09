package uk.ac.aston.jpd.simulation;

import uk.ac.aston.jpd.simulation.model.InvalidArrivalsException;
import uk.ac.aston.jpd.simulation.model.Simulation;
import uk.ac.aston.jpd.simulation.model.entities.InvalidBuildingException;
import uk.ac.aston.jpd.simulation.model.entities.InvalidElevatorException;
import uk.ac.aston.jpd.simulation.visuals.TextView;

/**
 * The {@code TextViewLauncher} class is a non-interactive text-based view. At
 * each tick, it will display the status of the elevator and the floors on the
 * console.
 * 
 * @author Parminder
 */
public class TextViewLauncher {
	
	private static final int DEFAULT_RUN_TICKS = 50;
	
	/**
	 * Entry point for the text view launcher: it displays the status of elevator
	 * and floors on the console for each tick.
	 */
	public static void main(String[] args) {
		try {
			Simulation simulation = new Simulation.Builder().build();	
			TextView textView = new TextView();
			for (int i = 0; i < DEFAULT_RUN_TICKS; i++) {
				simulation.tick();
				textView.visualize(simulation);
			}
		} catch (InvalidBuildingException buildingEx) {
			System.err.println("Cannot create Building with " + buildingEx.getNumFloors() + " floors");
			buildingEx.printStackTrace();
		} catch (InvalidElevatorException elevatorEx) {
			System.err.println("Cannot create Elevator with " + elevatorEx.getElevatorCapacity() + " capacity");
			elevatorEx.printStackTrace();
		} catch (InvalidArrivalsException arrivalsEx) {
			System.err.println("Invalid parameter for generating new users: " + arrivalsEx.getInvalidParameter());
			arrivalsEx.printStackTrace();
		}
	}
}
