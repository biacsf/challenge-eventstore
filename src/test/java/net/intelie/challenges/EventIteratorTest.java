package net.intelie.challenges;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.intelie.challenges.entity.Event;
import net.intelie.challenges.util.EventIterator;
import net.intelie.challenges.util.EventIteratorImpl;

public class EventIteratorTest {

	List<Event> events;
	Event event;
	Event event2;

	@Before
	public void setUp() {
		event = new Event("some_type", 123L);
		event2 = new Event("another_type", 456L);

		events = new ArrayList<>();

		events.add(event);
		events.add(event2);

	}

	@Test(expected = IllegalStateException.class)
	public void currentWithoutMoveNextTest() {
		new EventIteratorImpl().current();
	}

	@Test(expected = IllegalStateException.class)
	public void removeWithoutMoveNextTest() {
		new EventIteratorImpl().remove();
	}

	@Test
	public void currentAfterMoveNextTest() {
		EventIterator iterator = new EventIteratorImpl(events);
		iterator.moveNext();
		assertEquals(event, iterator.current());
	}

	@Test
	public void removeAfterMoveNextTest() {
		EventIterator iterator = new EventIteratorImpl(events);
		iterator.moveNext();
		iterator.remove();
		assertEquals(event2, iterator.current());
	}
}
