import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CustomerManager {

	private List<Customer> customers = new ArrayList<Customer>();
	private int nextId = 0;

	public void addCustomer(Customer customer) {

		// not thread-safe, make atomic operation
		// customer.setId(nextId);
		// nextId++;

		synchronized (this) {
			customer.setId(nextId);
			nextId++;
			customers.add(customer);
		}
	}

	public void howManyCustomers() {
		int size = 0;
		// might get interference from addCustomer
		// size = customers.size();
		synchronized (this) {
			size = customers.size();
		}
		System.out.println("" + new Date() + " : " + size + " customers created");
	}

	public void displayCustomers() {

		// this customers collection might change while looping
		// throwing a ConcurrentModificationException
		// for (Customer c : customers) {
		// System.out.println(c.toString());
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		// possible solution is to synchronize the looping
		// but his could have big negative impact on performance
		
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

		// better solution is to loop over a copy of the original collection
		// using thread-safe CopyOnWriteArrayList
		List<Customer> tmpCollection = new CopyOnWriteArrayList<>(customers);
		for (Customer c : tmpCollection) {
			System.out.println(c.toString());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
