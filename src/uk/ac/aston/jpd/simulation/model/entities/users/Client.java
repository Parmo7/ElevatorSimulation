package uk.ac.aston.jpd.simulation.model.entities.users;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.Floor;

public class Client extends NonEmployee {
	
	private static final String IDENTIFIER = "CL";
	private static final int REQUIRED_SPACE = 1;
	private static final int MIN_STAY_TICKS = 60, MAX_STAY_TICKS = 180;
	private static final int MAX_WAIT_TICKS = 60;

	private static int count = 0;
	
	private Building building;
	private int queueJoinedTick = -1;

	public Client(Building building) {
		super(building, IDENTIFIER + String.format("%02d", ++count), REQUIRED_SPACE, MIN_STAY_TICKS, MAX_STAY_TICKS);
		this.building = building;
		
		List<Floor> accessibleFloors = new ArrayList<Floor>();
		int middle = (building.getFloors().length)/2;
		for (Floor f : building.getFloors()) {
			if (f.getLevel() <= middle) {
				accessibleFloors.add(f);
			}
		}
		setAccessibleFloors(accessibleFloors);
	}

	@Override
	public void tick() {
		super.tick();
		if ( isInGroundFloorQueue() && isAnnoyed()) {
			building.fileComplaint();
			building.quit(this);
		}
	}
	
	@Override
	public void joinQueue() {
		super.joinQueue();
		queueJoinedTick = building.getSimulation().getTick();
	}
	
	private boolean isAnnoyed() {
		int waitSoFarTicks = building.getSimulation().getTick() - queueJoinedTick; 
		return waitSoFarTicks > MAX_WAIT_TICKS;
	}

	private boolean isInGroundFloorQueue() {
		return building.getFloors()[0].getInQueue().contains(this);
	}
}
