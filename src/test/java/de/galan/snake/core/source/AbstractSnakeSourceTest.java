package de.galan.snake.core.source;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.galan.commons.test.AbstractTestParent;
import de.galan.snake.core.DefaultSnakeInstance;
import de.galan.snake.core.SourceListener;


/**
 * CUT AbstractSnakeSource
 *
 * @author daniel
 */
public class AbstractSnakeSourceTest extends AbstractTestParent {

	private AbstractSnakeSource source;
	private Map<String, String> map;
	private TestSourceListener listener;


	@Before
	public void setup() {
		source = new AbstractSnakeSource() {/* nada */};
		source.initialize(new DefaultSnakeInstance());
		map = new HashMap<>();
		listener = new TestSourceListener();
		source.addListener(listener);
	}


	@Test
	public void singleMerge() throws Exception {
		map.put("aaa", "111");
		map.put("bbb", "222");
		source.mergeProperties(map, false);
		assertThat(listener.listAdded).hasSize(2);
		assertThat(listener.listRemoved).hasSize(0);
		assertThat(listener.listUpdated).hasSize(0);
		assertThat(listener.refreshed).isEqualTo(0);
		assertThat(listener.listAdded).containsOnly(new EventBean("aaa", "111"), new EventBean("bbb", "222"));
	}


	@Test
	public void mergeOverride() throws Exception {
		map.put("aaa", "111");
		map.put("bbb", "222");
		source.mergeProperties(map, false);
		reset();
		map.put("bbb", "333");
		map.put("ccc", "444");
		source.mergeProperties(map, false);
		assertThat(listener.listAdded).hasSize(1);
		assertThat(listener.listRemoved).hasSize(1);
		assertThat(listener.listUpdated).hasSize(1);
		assertThat(listener.refreshed).isEqualTo(0);
		assertThat(listener.listAdded).containsOnly(new EventBean("ccc", "444"));
		assertThat(listener.listRemoved).containsOnly(new EventBean("aaa", "111"));
		assertThat(listener.listUpdated).containsOnly(new UpdatedBean("ccc", "333", "222"));
	}


	private void reset() {
		map.clear();
		listener.reset();
	}

}


/** Mock */
class TestSourceListener implements SourceListener {

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


/** Mock */
class EventBean {

	public String name;
	public String value;


	public EventBean(String name, String value) {
		this.name = name;
		this.value = value;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EventBean other = (EventBean)obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		}
		else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

}


/** Mock */
class UpdatedBean {

	public String name;
	public String valueOld;
	public String valueNew;


	public UpdatedBean(String name, String valueNew, String valueOld) {
		this.name = name;
		this.valueNew = valueNew;
		this.valueOld = valueOld;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((valueNew == null) ? 0 : valueNew.hashCode());
		result = prime * result + ((valueOld == null) ? 0 : valueOld.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UpdatedBean other = (UpdatedBean)obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		}
		else if (!name.equals(other.name)) {
			return false;
		}
		if (valueNew == null) {
			if (other.valueNew != null) {
				return false;
			}
		}
		else if (!valueNew.equals(other.valueNew)) {
			return false;
		}
		if (valueOld == null) {
			if (other.valueOld != null) {
				return false;
			}
		}
		else if (!valueOld.equals(other.valueOld)) {
			return false;
		}
		return true;
	}

}
