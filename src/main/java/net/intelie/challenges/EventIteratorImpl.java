package net.intelie.challenges;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Linked List Implementation
 * 
 * @author Bianca Munaro
 *
 */

public class EventIteratorImpl implements EventIterator {

	private AtomicInteger marker = new AtomicInteger(-1);

	private final List<Event> events;

	public EventIteratorImpl(List<Event> events) {
		super();
		this.events = events;
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
		return events.size() > marker.get();
	}
	
	private void validadeCurrentOrRemove() {
		if(marker.get() == -1) {
			throw new IllegalStateException("movenext was never called");
		}
		if(!hasNext()) {
			throw new IllegalStateException("The array has reached its end");
		}
	}

}
