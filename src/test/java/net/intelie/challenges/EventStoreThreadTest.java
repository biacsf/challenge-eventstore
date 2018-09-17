package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import net.intelie.challenges.dao.EventStore;
import net.intelie.challenges.dao.EventStoreImpl;
import net.intelie.challenges.entity.Event;
import net.intelie.challenges.util.EventIterator;

public class EventStoreThreadTest {
	
	static final String SOME_TYPE = "some_type";
	static final String ANOTHER_TYPE = "another_type";


	@Test
	public void storeThreadsTest() {

		Event event = new Event(SOME_TYPE, 1531667011000L);
		Event event2 = new Event(ANOTHER_TYPE, 1526396611000L);

		EventStore store = new EventStoreImpl();

		ExecutorService executor = Executors.newFixedThreadPool(100);

		List<Callable<String>> tasks = new ArrayList<>();

		Callable<String> insertEvent1Task = () -> {
			System.out.println("Executing insertEvent1Task");
			store.insert(event);
			return "Inserted event1 in memory: "+event;
		};
		Callable<String> insertEvent2Task = () -> {
			System.out.println("Executing insertEvent2Task");
			store.insert(event2);
			return "Inserted event2 in memory: "+event2;
		};
		Callable<String> removeSomeTypeTask = () -> {
			System.out.println("Executing removeSomeTypeTask");
			store.removeAll(SOME_TYPE);
			return "Removed all events of type: "+SOME_TYPE;
		};
		Callable<String> removeAnotherTypeTask = () -> {
			System.out.println("Executing removeAnotherTypeTask");
			store.removeAll(ANOTHER_TYPE);
			return "Removed all events of type: "+ANOTHER_TYPE;
		};
		
		Callable<String> queryEventsTask = () -> {
			System.out.println("Executing queryEventsTask");
			EventIterator iterator = store.query(SOME_TYPE,1505487811000L, 1537023811000L);
			
			while(iterator.moveNext()) {
				System.out.println("Evento em memoria: "+iterator.current());
			}
			
			return "Query events of type: "+SOME_TYPE+" and between timestamps: 1505487811000L and 1537023811000L";
		};
		
		tasks.add(insertEvent1Task);
		tasks.add(removeSomeTypeTask);
		tasks.add(queryEventsTask);
		tasks.add(insertEvent1Task);
		tasks.add(insertEvent2Task);
		tasks.add(insertEvent1Task);
		tasks.add(removeAnotherTypeTask);
		tasks.add(removeAnotherTypeTask);
		tasks.add(removeSomeTypeTask);
		tasks.add(queryEventsTask);
		tasks.add(insertEvent1Task);
		tasks.add(insertEvent2Task);
		tasks.add(insertEvent2Task);
		tasks.add(insertEvent2Task);
		tasks.add(removeSomeTypeTask);


		try {
			executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
