package uk.ac.aston.jpd.simulation.model;

import uk.ac.aston.jpd.simulation.model.entities.users.User;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * The class {@code WaitingStatistics} collects and analyses data about the time
 * users have to wait in queue before being served. <br>
 * On demand, it also provides the average waiting time for the simulation.
 * 
 * @author Parminder, Jims, Joseph
 */
public class WaitingStatistics {
	
	/**
	 * The class {@code UserWait} provides the wait data for a user: it stores the
	 * tick at which a {@link User} joined the queue and the tick at which his
	 * serving started.
	 * 
	 * @author Parminder
	 */
	private static class UserWait {
		private final User waitingUser;
		private final int queueJoinedTick;
		private Integer servingStartedTick;

		/**
		 * Creates a new UserWait with the given user and tick.
		 * 
		 * @param user the {@code User} to which the wait data belongs
		 * @param queueJoinedTick the tick at which the user joined the queue
		 */
		public UserWait(User user, int queueJoinedTick){
			this.waitingUser = user;
			this.queueJoinedTick = queueJoinedTick;					
		}

		/**
		 * Sets the tick at which a user's request got accepted
		 * 
		 * @param servingStartedTick the tick at which the user's serving started
		 */
		public void setServingStartedTick(Integer servingStartedTick) {
			if (servingStartedTick > 0) { 
				this.servingStartedTick = servingStartedTick;
			}
		}

		public int getQueueJoinedTick() {
			return queueJoinedTick;
		}

		public Integer getServingStartedTick() {
			return servingStartedTick;
		}

		@SuppressWarnings("unused")
		public User getUser() {
			return waitingUser;
		}
	}

	private final HashMap<User, ArrayList<UserWait>> waitData = new HashMap<>();
	private final Simulation simulation;

	/**
	 * Creates new waiting statistics for the given simulation. 
	 * 
	 * @param simulation the {@code Simulation} for which statistics are to be provided
	 */
	public WaitingStatistics(Simulation simulation) {
		this.simulation = simulation;
	}

	/**
	 * Creates and stores a new {@link UserWait} for a given user, who just joined
	 * the queue.
	 * 
	 * @param u the {@code User} which has joined the queue
	 */
	public void queueJoined(User u) {
		if (u != null) {
			ArrayList<UserWait> waitsPerUser = waitData.get(u);
			if (waitsPerUser == null) {
				waitsPerUser = new ArrayList<UserWait>();
			}
			waitsPerUser.add(new UserWait(u, simulation.getTick()));
			waitData.put(u, waitsPerUser);		
		}
	}

	/**
	 * Updates the {@link UserWait} for a given user, by setting the serving tick of the user to the current tick.
	 * 
	 * @param u the {@code User} which is now being served.
	 */
	public void servingStarted(User u) {  
		ArrayList<UserWait> waitsPerUser = waitData.get(u);
		if (waitsPerUser != null) {
			UserWait lastUserWait = waitsPerUser.get(waitsPerUser.size() - 1);
			lastUserWait.setServingStartedTick(simulation.getTick());
		}		
	}

	/**
	 * Computes and returns the average wait time in queue for the simulation.
	 * 
	 * @return {@code double} value in ticks, representing the average waiting time in queue. Returns {@code Double.NaN} if no wait data is available.
	 */
	public double getAverageWaitingTime() {
		int samples = 0;
		double total = 0;

		for (ArrayList<UserWait> waitsPerUser : waitData.values()) {
			for (UserWait uw : waitsPerUser) {
				if (uw.getServingStartedTick() != null) {
					samples++;
					total += uw.getServingStartedTick() - uw.getQueueJoinedTick();
				}
			}
		}

		if (samples > 0) {
			return total / samples;
		} else {
			return Double.NaN;
		}
	}
}