package net.intelie.challenges.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.intelie.challenges.entity.Event;

/**
 * Linked List Implementation
 * 
 * @author Bianca Munaro
 *
 */

public class EventIteratorImpl implements EventIterator {

	//Thread-safe integer
	private AtomicInteger marker = new AtomicInteger(-1);

	private final List<Event> events;

	public EventIteratorImpl(List<Event> events) {
		super();
		this.events = events;
	}
	
	public EventIteratorImpl() {
		super();
		this.events = new ArrayList<>();
	}

	@Override
	public void close() throws Exception {
		events.clear();
	}

	@Override
	public boolean moveNext() {
		if (hasNext()) {
			marker.incrementAndGet();
			return true;
		}
		return false;

	}

	@Override
	public Event current() {
		validadeCurrentOrRemove();
		return events.get(marker.get());

	}

	@Override
	public void remove() {
		validadeCurrentOrRemove();
		events.remove(marker.get());
	}

	private boolean hasNext() {
		return events.size() != 0 && events.size() > marker.get();
	}
	
	private void validadeCurrentOrRemove() {
		if(marker.get() == -1) {
			throw new IllegalStateException("movenext() was never called");
		}
		if(!hasNext()) {
			throw new IllegalStateException("The array has reached its end");
		}
	}

}
