package uk.ac.aston.jpd.simulation.model;

import java.util.Random;

import uk.ac.aston.jpd.simulation.model.entities.Building;
import uk.ac.aston.jpd.simulation.model.entities.users.Client;
import uk.ac.aston.jpd.simulation.model.entities.users.Goggle;
import uk.ac.aston.jpd.simulation.model.entities.users.MaintenanceCrew;
import uk.ac.aston.jpd.simulation.model.entities.users.Mugtome;
import uk.ac.aston.jpd.simulation.model.entities.users.NonDeveloper;
import uk.ac.aston.jpd.simulation.model.entities.users.User;

/**
 * The class {@code ArrivalSimualtor} simulates new incoming users. <br>
 * Specifically,  it generates all employees at once at the beginning. <br>
 * It also generates non-employees according to the given probabilities.
 * 
 * @author Parminder
 */
public class ArrivalSimulator {
	
	private static final double MIN_PROB_Q = 0.00;
	private static final double MIN_PROB_MC = 0.00;
	private static final int MIN_NON_DEVS = 0, MIN_GOGGLES = 0, MIN_MUGTOMES = 0;
	
	private final Simulation simulation;
	private final Building building;
	private final Random random;	
	
	private final int nonDevelopers;
	private final int goggles;
	private final int mugtomes;	
	private final double probClient;
	private final double probMaintenanceCrew;
	
	private int clients = 0;
	private int maintenanceCrews = 0;
	
	/**
	 * Creates an {@code ArrivalSimulator} for the given simulation.
	 * <p> Retrieves all the required parameters for generating new users
	 * from the simulation.These are: 
	 * <br> - the probability of new clients
	 * <br> - the probability of new maintenance crews
	 * <br> - the total number of non-developers
	 * <br> - the total number of Goggle developers
	 * <br> - the total number of Mugtome developers.
	 * </p>
	 * 
	 * @param simulation the {@link Simulation} for which new arrivals will be
	 *                   simulated.
	 * @throws InvalidArrivalsException if any of the parameters provided for generating new users is incorrect.
	 */
	public ArrivalSimulator(Simulation simulation) {
		this.simulation = simulation;
		this.building = simulation.getBuilding();		
		this.random = simulation.getRandom();
		
		this.probClient = simulation.getProbabilityQ();
		this.probMaintenanceCrew = simulation.getProbabilityMC();
		this.nonDevelopers = simulation.getNonDevs();
		this.goggles = simulation.getGoggles();
		this.mugtomes = simulation.getMugtomes();
		
		if (probClient < MIN_PROB_Q) {
			throw new InvalidArrivalsException("Invalid probability for Clients", probClient);
		}
		if (probMaintenanceCrew < MIN_PROB_MC) {
			throw new InvalidArrivalsException("Invalid probability for Maintenance Crew", probMaintenanceCrew);
		}
		if (nonDevelopers < MIN_NON_DEVS) {
			throw new InvalidArrivalsException("Invalid number of non-Developers", nonDevelopers);
		} 
		if (goggles < MIN_GOGGLES) {
			throw new InvalidArrivalsException("Invalid number of Goggles", goggles);
		} 
		if (mugtomes < MIN_MUGTOMES) {
			throw new InvalidArrivalsException("Invalid number of Mugtomes", mugtomes);
		}	
	}

	/**
	 * Generates new users according to some criteria:
	 * <br> - in the first tick, generates all employees at once
	 * <br> - in all ticks, generates non-employees according to a probability
	 */
	public void tick() {
		if (simulation.getTick() == 0) {
			generateEmployees();
		}
		
		double value = random.nextDouble();
		if (value < probClient) {
			clients++;
			User client = new Client(building);
			simulation.enter(client);
		} else if (value < probClient + probMaintenanceCrew) {
			maintenanceCrews++;
			User maintenanceCrew = new MaintenanceCrew(building);
			simulation.enter(maintenanceCrew);
		}
	}

	/**
	 * Generates new employees, specifically non-developers, Goggles, Mugtomes.
	 */
	private void generateEmployees() {
		for (int i = 0; i < goggles; i++) {
			User goggle = new Goggle(building);
			simulation.enter(goggle);
		}

		for (int i = 0; i < mugtomes; i++) {
			User mugtome = new Mugtome(building);
			simulation.enter(mugtome);
		}		

		for (int i = 1; i <= nonDevelopers; i++) {
			User nonDeveloper = new NonDeveloper(building);
			simulation.enter(nonDeveloper);
		}
	}

	public int getTotalDevelopers() {
		return goggles + mugtomes;
	}

	public int getTotalNonDevelopers() {
		return nonDevelopers;
	}

	public int getTotalClients() {
		return clients;
	}

	public int getTotalMaintenanceCrews() {
		return maintenanceCrews;
	}
	
	public double getProbabilityClient() {
		return probClient;
	}

	public double getProbabilityMaintenanceCrew() {
		return probMaintenanceCrew;
	}	
}
