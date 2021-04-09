package uk.ac.aston.jpd.simulation.model.entities;

/**
 * The exception {@code InvalidBuildingException} is thrown to indicate that a
 * {@link Building} has been provided with an invalid number of floors.
 * 
 * @author Parminder
 */
public class InvalidBuildingException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;
	private int numFloors;
	
	/**
	 * Creates an exception with the provided error message and invalid numver of floors.
	 * 
	 * @param message the error message to be displayed for the exception.
	 * @param numFloors the incorrect number of floors.
	 */
	public InvalidBuildingException(String message, int numFloors) {
		super(message);
		this.numFloors = numFloors;
	}
	
	public int getNumFloors() {
		return numFloors;
	}
}
