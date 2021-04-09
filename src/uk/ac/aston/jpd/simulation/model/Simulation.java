package uk.ac.aston.jpd.simulation.model;

import java.util.Random;

import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

/**
 * The class {@code Simulation} represents an elevator simulation within a
 * building. <br>
 * Before creating the simulation, it allows to set specific parameters, such as
 * the capacity of the elevator, the number of floors in the building, the
 * numbers and types of users in the building.
 * 
 * @author Parminder
 */
public class Simulation {
	
	private static final int DEFAULT_SEED = 0;
	private static final double DEFAULT_PROB_P = 0.001, DEFAULT_PROB_Q = 0.002, DEFAULT_PROB_MC = 0.005;
	private static final int DEFAULT_FLOORS = 7;
	private static final int DEFAULT_ELEVATOR_CAPACITY = 4;
	private static final int DEFAULT_NON_DEVS = 10, DEFAULT_GOGGLES = 5, DEFAULT_MUGTOMES = 5;	
	
	private ArrivalSimulator arrivalSimulator;
	private Building building;
	private WaitingStatistics statistics;
	
	private Random random;
	private double probP, probQ, probMC; 
	private int numFloors;
	private int elevatorCapacity;
	private int nonDevs, goggles, mugtomes;
	
	private int tick = 0;
	private int arrivals = 0;
	private int exits = 0;
	private int complaints = 0;
		
	/**
	 * The class {@code Simulation.Builder} allows to build a {@link Simulation}
	 * object.
	 * <p>
	 * In order to do so, it allows to set some parameters: the seed, the
	 * probability of users changing the floor, the probability of new clients and
	 * maintenance crews, the number of floors for the building, the elevator
	 * capacity and the number of employees within the building - i.e.
	 * non-developers, Goggle developers and Mugtome developers.
	 * </p>
	 * 
	 * If these parameters are not provided, default values are employed.
	 * 
	 * @author Parminder
	 */
	public static class Builder {
		private int seed = DEFAULT_SEED;
		private double probP = DEFAULT_PROB_P, probQ = DEFAULT_PROB_Q, probMC = DEFAULT_PROB_MC;
		private int floors = DEFAULT_FLOORS;
		private int elevatorCapacity = DEFAULT_ELEVATOR_CAPACITY;
		private int nonDevs = DEFAULT_NON_DEVS, goggles = DEFAULT_GOGGLES, mugtomes = DEFAULT_MUGTOMES;
	
		public Builder seed(int seed) {
			this.seed = seed;
			return this;
		}
		
		public Builder probabilityP(double probP) {
			this.probP = probP;
			return this;
		}
		
		public Builder probabilityQ(double probQ) {
			this.probQ = probQ;
			return this;
		}
		
		public Builder probabilityMaintenance(double probMC) {
			this.probMC = probMC;
			return this;
		}
		
		public Builder numFloors(int floors) {
			this.floors = floors;
			return this;
		}
		
		public Builder elevatorCapacity(int capacity) {
			this.elevatorCapacity = capacity;
			return this;
		}
		
		public Builder nonDevelopers(int numNonDevs) {
			this.nonDevs = numNonDevs;
			return this;
		}
		
		public Builder goggles(int numGoggles) {
			this.goggles = numGoggles;
			return this;
		}
		
		public Builder mugtomes(int numMugtomes) {
			this.mugtomes = numMugtomes;
			return this;
		}
		
		public Simulation build() {
			return new Simulation(seed, probP, probQ, probMC, floors, elevatorCapacity, nonDevs, goggles, mugtomes);
		}
	}

	
	/**
	 * Creates a {@code Simulation} according to the given parameters.
	 * 
	 * @param seed the {@code int} value to provide to the {@code Random} constructor.
	 * @param probP {@code double} value representing the probability of users changing floor.
	 * @param probQ {@code double} value representing the probability of new clients.
	 * @param probMC {@code double} value representing the probability of new maintenance crews
	 * @param numFloors the number of floors of the {@link Building}
	 * @param elevatorCapacity the total capacity of the {@link Elevator}
	 * @param nonDevs the number of non-developers initially in the building
	 * @param goggles the number of Goggle developers initially in the building
	 * @param mugtomes the number of Mugtome developers initially in the building
	 */
	private Simulation(int seed, double probP, double probQ, double probMC, int numFloors, int elevatorCapacity, int nonDevs, int goggles, int mugtomes) {
		this.random = new Random(seed);
		this.probP = probP;
		this.probQ = probQ;
		this.probMC = probMC;
		this.numFloors = numFloors;
		this.elevatorCapacity = elevatorCapacity;
		this.nonDevs = nonDevs;
		this.goggles = goggles;
		this.mugtomes = mugtomes;
		
		this.building = new Building(this);		
		this.statistics = new WaitingStatistics(this);		
		this.arrivalSimulator = new ArrivalSimulator(this);			
	}	

	/**
	 * Delegates the ticking to the {@link ArrivalSimulator} and to the {@link Buiding}.
	 * <br> Then, increases the number of ticks.
	 */
	public void tick() {
		arrivalSimulator.tick();
		building.tick();
		++tick;
	}

	/**
	 * Asks the {@link Building} to let the given user enter.
	 * 
	 * @param incoming the {@code User} attempting to enter the building
	 */
	public void enter(User incoming) {
		if (building.enter(incoming)) {
			arrivals++;	
		}
	}

	/**
	 * Increases the {@code int} value that keeps track of how many users have left
	 * the building so far.
	 * 
	 * @param incoming the {@code User} attempting to enter the building
	 */
	public void quit() {
		exits++;
	}

	/**
	 * Notifies the {@link WaitingStatistics} that the given user has joined the
	 * queue for the elevator, at a certain floor.
	 * 
	 * @param u the {@code User} that has just joined the queue.
	 */
	public void queueJoined(User u) {
		if (u != null) { 
			statistics.queueJoined(u);
		}
	}
	
	/**
	 * Notifies the {@link WaitingStatistics} that the elevator has started serving the
	 * specified user.
	 * 
	 * @param u the {@code User} that is now being served by the elevator.
	 */
	public void servingStarted(User u) {
		if (u != null) {
			statistics.servingStarted(u);
		}
	}
	
	/**
	 * Increases the total number of complaints by users.
	 */
	public void fileComplaint()	{
		complaints++;
	}
	
	public int getTick() {
		return tick;
	}

	public ArrivalSimulator getArrivalSimulator() {
		return arrivalSimulator;
	}

	public Building getBuilding() {
		return building;
	}

	public WaitingStatistics getWaitingStatistics() {
		return statistics;
	}

	public Random getRandom() {
		return random;
	}
	
	public double getProbabilityP() {
		return probP; //probability of changing floor
	}
	
	public double getProbabilityQ() {
		return probQ; //probability of new clients
	}
	
	public double getProbabilityMC() {
		return probMC;
	}
	
	public int getTotalFloors() {
		return numFloors;
	}
	
	public int getElevatorCapacity() {
		return elevatorCapacity;
	}
	
	public int getNonDevs() {
		return nonDevs;
	}
	
	public int getGoggles() {
		return goggles;
	}
	
	public int getMugtomes() {
		return mugtomes;
	}
	
	public int getCurrentUsers() {
		return arrivals - exits;
	}
	
	public int getComplaints() {
		return complaints;
	}
}
