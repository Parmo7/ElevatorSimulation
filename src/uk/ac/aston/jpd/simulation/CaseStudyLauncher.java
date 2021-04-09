package uk.ac.aston.jpd.simulation;

import uk.ac.aston.jpd.simulation.visuals.CaseStudy;

/**
 * The {@code CaseStudyLauncher} is a non-interactive launcher for analysis
 * purposes: it runs different simulations with different combinations of seed,
 * p and q and provides data accordingly.
 * 
 * @author Parminder
 */
public class CaseStudyLauncher {
	
	/**
	 * Entry point for the case study launcher.
	 */
	public static void main(String[] args) {
		CaseStudy study = new CaseStudy();
		study.visualize();
	}
}
