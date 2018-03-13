import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CustomerManager {

	// ArrayList is not thread-safe, better use CopyOnWriteArrayList which is
	// thread-safe
	// private List<Customer> customers = new ArrayList<Customer>();
	private List<Customer> customers = new CopyOnWriteArrayList<Customer>();
	private int nextId = 0;

	public void addCustomer(Customer customer) {

		// not thread-safe, but working since we changed to use CopyOnWriteArrayList in
		// constructor. Thus only need to use synchronized for nextId

		synchronized (customer) {
			customer.setId(nextId);
			nextId++;
		}
		customers.add(customer);
	}

	public void howManyCustomers() {
		int size = 0;

		// thread-safe, since we changed to use CopyOnWriteArrayList in
		// constructor. Thus no need to using synchronized here.
		size = customers.size();
		System.out.println("" + new Date() + " : " + size + " customers created");
	}

	public void displayCustomers() {

		// This is not thread/safe
		// The customers collection might change while looping
		// throwing a ConcurrentModificationException
		//
		// for (Customer c : customers) {
		// System.out.println(c.toString());
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		// Possible solution is to synchronize the looping
		// but his could have big negative impact on performance
		//
		// synchronized (this) {
		// for (Customer c : customers) {
		// System.out.println(c.toString());
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }

		// Solution:
		// A better solution is to loop over a copy of the original collection
		// using thread-safe CopyOnWriteArrayList in constructor.
		// Thus we are back to the original implementation

		for (Customer c : customers) {
			System.out.println(c.toString());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
