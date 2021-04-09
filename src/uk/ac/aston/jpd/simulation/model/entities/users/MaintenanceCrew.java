package uk.ac.aston.jpd.simulation.model.entities.users;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

/**
 * The class {@code MaintenanceCrew} represent a maintenance crew within a
 * building. It only has access to the top floor and it leaves the building upon
 * completing its job.
 * 
 * @author Jims, Parminder
 */
public class MaintenanceCrew extends NonEmployee {
	
	private static final String IDENTIFIER = "MC";
	private static final int REQUIRED_SPACE = 4;
	private static final int MIN_STAY_TICKS = 120, MAX_STAY_TICKS = 240;

	private static int count = 0;
	
	/**
	 * Creates a maintenance crew in the provided building and sets its accessible floors so they only include the top floor.
	 * 
	 * @param building the building the maintenance crew is in
	 */
	public MaintenanceCrew(Building building) {
		super(building, IDENTIFIER + String.format("%02d", ++count), REQUIRED_SPACE, MIN_STAY_TICKS, MAX_STAY_TICKS);
		
		Floor[] floors = building.getFloors();
		Floor lastFloor = floors[floors.length - 1];
		List<Floor> accessibleFloors = new ArrayList<Floor>();
		accessibleFloors.add(lastFloor);
		setAccessibleFloors(accessibleFloors);
	}
}
