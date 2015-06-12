package de.galan.snake.core.source;

import java.util.ArrayList;
import java.util.List;

import de.galan.snake.core.SourceListener;


/** Mock */
public class TestSourceListener implements SourceListener {

	public List<EventBean> listAdded;
	public List<EventBean> listRemoved;
	public List<UpdatedBean> listUpdated;
	public int refreshed;


	public TestSourceListener() {
		reset();
	}


	@Override
	public void addedProperty(String name, String value) {
		listAdded.add(new EventBean(name, value));
	}


	@Override
	public void removedProperty(String name, String oldValue) {
		listRemoved.add(new EventBean(name, oldValue));
	}


	@Override
	public void updatedProperty(String name, String newValue, String oldValue) {
		listUpdated.add(new UpdatedBean(name, newValue, oldValue));
	}


	@Override
	public void refreshedProperties() {
		refreshed++;
	}


	public void reset() {
		listAdded = new ArrayList<>();
		listRemoved = new ArrayList<>();
		listUpdated = new ArrayList<>();
		refreshed = 0;
	}

}
