package uk.ac.aston.jpd.simulation.model;

/**
 * The exception {@code InvalidArrivalsException} is thrown to indicate that an
 * {@link ArrivalSimulator} has been provided with invalid parameter(s) for
 * generating new incoming users.
 * 
 * @author Parminder
 */
public class InvalidArrivalsException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;
	private double invalidParameter;
	
	/**
	 * 
	 * @param message the error message to be displayed for the exception.
	 * @param invalidParameter the wrong parameter provided to the arrival simulator.
	 */
	public InvalidArrivalsException(String message, double invalidParameter) {
		super(message);
		this.invalidParameter = invalidParameter;
	}
	
	public double getInvalidParameter() {
		return invalidParameter;
	}
}
