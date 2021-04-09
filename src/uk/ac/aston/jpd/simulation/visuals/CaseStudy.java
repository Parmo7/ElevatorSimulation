package uk.ac.aston.jpd.simulation.visuals;

import uk.ac.aston.jpd.simulation.model.Simulation;

/**
 * The class {@code CaseStudy} runs 8-hour simulations for different
 * combinations of values for: 
 * <br> - seed: the {@code int} value to be provided to the constructor of {@link java.util.Random}.
 * <br> - p: the probability according to which users change floor 
 * <br> - q: the probability according to which new non-employees enter the building
 * 
 * @author Parminder
 */

public class CaseStudy {
	
	private static final int[] DEFAULT_SEEDS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	private static final double[] DEFAULT_PROBS_P = {0.01, 0.02, 0.03, 0.04, 0.05};
	private static final double[] DEFAULT_PROBS_Q = {0.02, 0.04, 0.06, 0.08, 0.1};
	
	/**
	 * default number of ticks for which each simulation is run
	 */
	private static final int DEFAULT_RUN_TICKS = 2880; //8 hours
	
	/**
	 * Creates combinations of values of seed, p and q and passes them to
	 * {@code #printSimulationData(int, double, double)} to run simulations and
	 * to provide analysis data for each of them.
	 */
	public void visualize() {
		System.out.println("SEED \t P \t Q \t COMPLAINTS \t AVERAGE WAITING TIME \t CLIENTS \t M.CREWS");
		System.out.println();
		for (int seed : DEFAULT_SEEDS) {
			for (double p : DEFAULT_PROBS_P) {
				for (double q : DEFAULT_PROBS_Q) {
					printSimulationData(seed, p, q);					
				}
			}
		}
	}
	
	/**
	 * Runs an elevator simulation according to the parameters provided (seed, p, q)
	 * for the default number of ticks.
	 * <p>
	 * Then displays analysis data about the simulation on the console. <br>
	 * Specifically, it prints the combination of seed, p and q employed, the number
	 * of complaints, the average wait time, the total number of clients and
	 * maintenance crews generated.
	 * 
	 * @param seed the {@code int} value to be provided to the constructor of
	 *             {@link java.util.Random}.
	 * @param p    the probability according to which users change floor.
	 * @param q    the probability according to which new non-employees enter the
	 *             building.
	 */
	private void printSimulationData(int seed, double p, double q) {
		Simulation sim = new Simulation.Builder()
				.seed(seed).probabilityP(p).probabilityQ(q)
				.build();
		for (int i = 0; i < DEFAULT_RUN_TICKS; i++) {
			sim.tick();
		}
		
		double averageWaiting = sim.getWaitingStatistics().getAverageWaitingTime();
		int complaints = sim.getComplaints();
		System.out.printf("%d \t %.3f \t %.3f \t %d \t\t %.2f", seed, p, q, complaints, averageWaiting);
		int clients = sim.getArrivalSimulator().getTotalClients();
		int maintenanceCrews = sim.getArrivalSimulator().getTotalMaintenanceCrews();
		System.out.printf("\t\t\t %d \t\t %d", clients, maintenanceCrews);
		System.out.println();
	}
}
