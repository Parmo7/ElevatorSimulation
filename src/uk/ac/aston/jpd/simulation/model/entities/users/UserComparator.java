package uk.ac.aston.jpd.simulation.model.entities.users;

import java.util.Comparator;

/**
 * The {@code UserComparator} class allows to compare different types of users
 * according to their priority.
 * 
 * @author Parminder
 */
public class UserComparator implements Comparator<User> {
	
	/**
	 * Compares two {@link User}s: specifically, it prioritises clients, or if they
	 * are not clients or both clients, it prioritises the user that first joined
	 * the queue.
	 * 
	 * @param u1 the first user to be compared
	 * @param u2 the second user to be compared
	 *
	 */
	@Override
	public int compare(User u1, User u2) {
		if ((u1 instanceof Client) && !(u2 instanceof Client)) {         //u1 client, u2 user   > -1
			return -1;			
		} else if (!(u1 instanceof Client) && (u2 instanceof Client)) {  //u1   user, u2 client > 1
			return 1;
		} else {
			return Integer.compare(u1.getTicketNumber(), u2.getTicketNumber());
		}
	}
}

