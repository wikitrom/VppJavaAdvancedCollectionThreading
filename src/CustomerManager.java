import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CustomerManager {

	// ArrayList is not thread-safe
	// private List<Customer> customers = new ArrayList<Customer>();

	// Solution 1: Protect problem areas using synchronized {...}

	// Solution 2: Use CopyOnWriteArrayList which is thread-safe
	// private List<Customer> customers = new CopyOnWriteArrayList<Customer>();

	// Solution 3: Use Collections.synchronizedList, thread-safe but we need to
	// protect loops/iterators
	private List<Customer> customers = Collections.synchronizedList(new ArrayList<Customer>());

	private int nextId = 0;

	public void addCustomer(Customer customer) {

		// original not thread-safe
		//
		// customer.setId(nextId); <- not thread safe
		// nextId++; <- not thread safe
		// customers.add(customer); <- not thread safe

		// Solution 1: use synchronized {...}
		//
		// synchronized (customer) {
		// customer.setId(nextId);
		// nextId++;
		// customers.add(customer);
		// }

		// Solution 2 and 3 - nextId must be protected regardlessly
		synchronized (this) {
			customer.setId(nextId);
			nextId++;
		}
		// Solution 2 and 3 -> OK here
		customers.add(customer);
	}

	public void howManyCustomers() {
		int size = 0;

		// thread-safe, since we changed to use CopyOnWriteArrayList in
		// constructor. Thus no need to using synchronized here.
		// original - not thread-safe
		// size = customers.size();

		// Solution 1: use synchronized {...}
		// synchronized (customers) {
		// size = customers.size();
		// }

		// Solution 2 and 3 -> OK here
		size = customers.size();
		System.out.println("" + new Date() + " : " + size + " customers created");
	}

	public void displayCustomers() {

		// original - not thread/safe
		// The customers collection might change while looping
		// throwing a ConcurrentModificationException
		//
		// for (Customer c : customers) {
		// System.out.println(c.toString());
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }

		// Solution 1: Possible solution is to synchronize the looping
		// but his could have big negative impact on performance and 
		// throw ConcurrentModificationException
		//
		// synchronized (this) {
		// for (Customer c : customers) {
		// System.out.println(c.toString());
		// try {
		// Thread.sleep(500);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }

		// Solution 2: CopyOnWriteArrayList
		// A better solution is to loop over a copy of the original collection
		// using thread-safe CopyOnWriteArrayList in constructor.
		// Thus we are back to the original implementation
		//
		// for (Customer c : customers) {
		// System.out.println(c.toString());
		// try {
		// Thread.sleep(200);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }

		// Solution 3: Using Collections.synchronizedList in constructor imply we still
		// need to protect the iterator.
		synchronized (this) {
			for (Customer c : customers) {
				System.out.println(c.toString());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
