package net.intelie.challenges;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * EventStore Implementation
 * 
 * Here we could use java 8 ConcurrentLinkedQueue
 * 
 * @author Bianca Munaro
 *
 */
public class EventStoreImpl implements EventStore {

	// With volatile keyword all the write will happen before any read
	private static volatile Queue<Event> events;

	private EventStoreImpl() {

		if (events != null) {
			throw new RuntimeException(
					"Use getInstance() method to get the single instance of the events inserted on memory.");
		}
	}

	public static Queue<Event> getInstance() {

		if (events == null) { // if there is no instance available... create new one
			// It prevents two threads from calling getInstance and finding that events is
			// null
			synchronized (events) {
				if (events == null)
					events = new ConcurrentLinkedQueue<>();
			}

		}

		return events;
	}

	@Override
	public void insert(Event event) {
		getInstance().add(event);

	}

	@Override
	public void removeAll(String type) {
		getInstance().removeIf((Event event) -> event.type() == type);
	}

	@Override
	public EventIterator query(String type, long startTime, long endTime) {

		return new EventIteratorImpl(events.parallelStream()
				.filter((Event event) -> (event.type() == type
						&& DateUtils.isDateBeetween(event.timestamp(), startTime, endTime)))
				.collect(Collectors.toList()));
	}

}
