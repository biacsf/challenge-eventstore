package net.intelie.challenges.dao;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import net.intelie.challenges.entity.Event;
import net.intelie.challenges.util.DateUtils;
import net.intelie.challenges.util.EventIterator;
import net.intelie.challenges.util.EventIteratorImpl;

/**
 * EventStore Implementation
 * 
 * @author Bianca Munaro
 *
 */
public class EventStoreImpl implements EventStore {

	// With volatile keyword all the write will happen before any read
	private static volatile Queue<Event> events;

	private static Queue<Event> getInstance() {

		if (events == null) { // if there is no instance available... create new one
			// It prevents two threads from calling getInstance and finding that events is
			// null
			synchronized (EventStoreImpl.class) {
				if (events == null)
					//ConcurrentLinkedQueue is non blocking, wait-free algorithm implementation. 
					//It doesn't block operations as it is done in the implementations of BlockingQueue interface
					//Appropriate choice when many threads will share access to a common collection
					events = new ConcurrentLinkedQueue<Event>();
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

		return new EventIteratorImpl(getInstance().parallelStream()
				.filter((Event event) -> (event.type() == type
						&& DateUtils.isDateBeetween(event.timestamp(), startTime, endTime)))
				.collect(Collectors.toList()));
	}

}
