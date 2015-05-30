package de.galan.snake.core;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import de.galan.commons.test.AbstractTestParent;
import de.galan.snake.test.TestSnakeSource;


/**
 * CUT SnakeModel
 *
 * @author daniel
 */
public class SnakeModelTest extends AbstractTestParent {

	private DefaultSnakeInstance instance;
	private TestSnakeSource source;


	@Before
	public void setup() {
		instance = new DefaultSnakeInstance();
		source = new TestSnakeSource();
		source.initialize(instance);
		source.set("empty", "");
	}


	@Test
	public void empty() throws Exception {
		SnakeModel model = new SnakeModel(source, instance);
		assertThat(model.get("aaa")).isNull();
		assertThat(model.getInt("aaa")).isNull();
		assertThat(model.getLong("aaa")).isNull();
		assertThat(model.getDouble("aaa")).isNull();
		assertThat(model.getBool("aaa")).isFalse();
		assertThat(model.getTime("aaa")).isNull();
	}


	@Test
	public void testString() throws Exception {
		source.set("string", "abcäöü€");
		SnakeModel model = new SnakeModel(source, instance);
		assertThat(model.get("string")).isEqualTo("abcäöü€");
		assertThat(model.get("string", "fallback")).isEqualTo("abcäöü€");
		assertThat(model.get("empty")).isEqualTo("");
		assertThat(model.get("doesnotexist", "fallback")).isEqualTo("fallback");
	}


	@Test
	public void testInt() throws Exception {
		source.set("int", "1234");
		SnakeModel model = new SnakeModel(source, instance);
		assertThat(model.getInt("int")).isEqualTo(1234);
		assertThat(model.getInt("int", 5678)).isEqualTo(1234);
		assertThat(model.getInt("empty")).isNull();
		assertThat(model.getInt("doesnotexist", 5678)).isEqualTo(5678);
	}


	@Test
	public void testLong() throws Exception {
		source.set("long", "1234");
		SnakeModel model = new SnakeModel(source, instance);
		assertThat(model.getLong("long")).isEqualTo(1234L);
		assertThat(model.getLong("long", 5678L)).isEqualTo(1234L);
		assertThat(model.getLong("empty")).isNull();
		assertThat(model.getLong("doesnotexist", 5678L)).isEqualTo(5678);
	}


	@Test
	public void testDouble() throws Exception {
		source.set("long", "1234.5678");
		SnakeModel model = new SnakeModel(source, instance);
		assertThat(model.getDouble("long")).isEqualTo(1234.5678d);
		assertThat(model.getDouble("long", 5678.9123d)).isEqualTo(1234.5678d);
		assertThat(model.getDouble("empty")).isNull();
		assertThat(model.getDouble("doesnotexist", 5678.9123d)).isEqualTo(5678.9123d);
	}


	@Test
	public void testBool() throws Exception {
		source.set("bool", "true");
		source.set("crap", "asdf");
		SnakeModel model = new SnakeModel(source, instance);
		assertThat(model.getBool("bool")).isTrue();
		assertThat(model.getBool("bool", false)).isTrue();
		assertThat(model.getBool("empty")).isFalse();
		assertThat(model.getBool("crap")).isFalse();
		assertThat(model.getBool("doesnotexist", true)).isTrue();
		assertThat(model.getBool("doesnotexist", false)).isFalse();
		assertThat(model.getBool("doesnotexist")).isFalse();
	}


	@Test
	public void testTime() throws Exception {
		source.set("time", "2h4m 2s");
		SnakeModel model = new SnakeModel(source, instance);
		assertThat(model.getTime("time")).isEqualTo(7442000L);
		assertThat(model.getTime("time", "5m")).isEqualTo(7442000L);
		assertThat(model.getTime("empty")).isNull();
		assertThat(model.getTime("doesnotexist", "5m")).isEqualTo(300000L);
	}

}
