package uk.ac.aston.jpd.simulation.model.entities;
/**
 * The exception {@code InvalidElevatorException} is thrown to indicate that an
 * {@link Elevator} has been provided with an invalid total capacity.
 * 
 * @author Parminder
 */
public class InvalidElevatorException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;
	private int elevatorCapacity;
	
	/**
	 * Creates an exception with the provided error message and the incorrect elevator capacity.
	 * 
	 * @param message the error message to be displayed for the exception.
	 * @param elevatorCapacity the incorrect capacity for the elevator.
	 */
	public InvalidElevatorException(String message, int elevatorCapacity) {
		super(message);
		this.elevatorCapacity = elevatorCapacity;
	}
	
	public int getElevatorCapacity() {
		return elevatorCapacity;
	}
}
