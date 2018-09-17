package net.intelie.challenges;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import net.intelie.challenges.dao.EventStore;
import net.intelie.challenges.dao.EventStoreImpl;
import net.intelie.challenges.entity.Event;
import net.intelie.challenges.util.EventIterator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventStoreTest {

	Event event;
	Event event2;

	@Before
	public void setUp() throws Exception {

		event = new Event("some_type", 1531667011000L);
		event2 = new Event("another_type", 1526396611000L);
	}

	@Test
	public void insertAndQueryTest() {
		EventStore store = new EventStoreImpl();
		store.insert(event);
		store.insert(event2);
		store.insert(event2);
		store.insert(event);
		store.insert(event);

		EventIterator iterator = store.query("some_type", 1505487811000L, 1537023811000L);
		//It has to be three some_type events on the returned iterator
		iterator.moveNext();
		assertThat(iterator.current(), equalTo(event));
		iterator.moveNext();
		assertThat(iterator.current(), equalTo(event));
		iterator.moveNext();
		assertThat(iterator.current(), equalTo(event));

	}

	@Test
	public void removeAllAnotherTest() {
		EventStore store = new EventStoreImpl();
		store.removeAll("another_type");

		EventIterator iterator = store.query("some_type", 1505487811000L, 1537023811000L);
		assertTrue(iterator.moveNext());
	}

	@Test
	public void removeAllSomeTest() {
		EventStore store = new EventStoreImpl();
		store.removeAll("some_type");

		EventIterator iterator = store.query("some_type", 1505487811000L, 1537023811000L);
		assertFalse(iterator.moveNext());
	}

}
