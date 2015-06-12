package de.galan.snake.core.source;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.galan.commons.test.AbstractTestParent;
import de.galan.snake.core.DefaultSnakeInstance;


/**
 * CUT AbstractSnakeSource
 *
 * @author galan
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
		source.mergeProperties(map, true);
		assertThat(listener.listAdded).hasSize(2);
		assertThat(listener.listRemoved).hasSize(0);
		assertThat(listener.listUpdated).hasSize(0);
		assertThat(listener.refreshed).isEqualTo(1);
		assertThat(listener.listAdded).containsOnly(new EventBean("aaa", "111"), new EventBean("bbb", "222"));
	}


	@Test
	public void mergeOverride() throws Exception {
		map.put("aaa", "111");
		map.put("bbb", "222");
		source.mergeProperties(map, true);
		reset();
		map.put("bbb", "333");
		map.put("ccc", "444");
		source.mergeProperties(map, true);
		assertThat(listener.listAdded).hasSize(1);
		assertThat(listener.listRemoved).hasSize(1);
		assertThat(listener.listUpdated).hasSize(1);
		assertThat(listener.refreshed).isEqualTo(1);
		assertThat(listener.listAdded).containsOnly(new EventBean("ccc", "444"));
		assertThat(listener.listRemoved).containsOnly(new EventBean("aaa", "111"));
		assertThat(listener.listUpdated).containsOnly(new UpdatedBean("bbb", "333", "222"));
	}


	private void reset() {
		map.clear();
		listener.reset();
	}

}
