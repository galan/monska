package de.galan.snake.core.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;

import de.galan.commons.logging.Say;
import de.galan.snake.core.SnakeInstance;
import de.galan.snake.core.SnakeSource;
import de.galan.snake.core.SourceListener;
import de.galan.snake.util.PropertiesPrinter;


/**
 * Default behaviour and fields for a source. Implements ready-to-use merging, listeners, properties and SnakeInstance
 * field.
 *
 * @author galan
 */
public abstract class AbstractSnakeSource implements SnakeSource {

	private List<SourceListener> listeners = new ArrayList<>();
	/** Current properties */
	private Map<String, String> properties;
	private SnakeInstance instance;


	@Override
	public void initialize(SnakeInstance snakeInstance) {
		instance = snakeInstance;
		properties = createDefaultProperties();
	}


	protected Map<String, String> createDefaultProperties() {
		return Collections.emptyMap();
	}


	@Override
	public Map<String, String> getProperties() {
		return properties;
	}


	protected SnakeInstance getInstance() {
		return instance;
	}


	protected void mergeProperties(Map<String, String> propertiesNew, boolean notify) {
		// compare
		List<Consumer<SourceListener>> consumers = new ArrayList<>();
		for (String keyOld: properties.keySet()) {
			if (propertiesNew.containsKey(keyOld)) {
				if (!propertiesNew.get(keyOld).equals(properties.get(keyOld))) {
					// Values have to be extracted prior creating consumer, or they will reference the wrong map on invokation
					String valueNew = propertiesNew.get(keyOld);
					String valueOld = properties.get(keyOld);
					consumers.add(c -> c.updatedProperty(keyOld, valueNew, valueOld));
				}
			}
			else {
				String valueOld = properties.get(keyOld);
				consumers.add(c -> c.removedProperty(keyOld, valueOld));
			}
		}
		for (String keyNew: propertiesNew.keySet()) {
			if (!properties.containsKey(keyNew)) {
				String valueNew = propertiesNew.get(keyNew);
				consumers.add(c -> c.addedProperty(keyNew, valueNew));
			}
		}

		if (!consumers.isEmpty()) {
			consumers.add(c -> c.refreshedProperties()); // General notification that properties have been changed

			// assign
			properties = ImmutableMap.copyOf(propertiesNew);

			// notify listener afterwards
			consumers.forEach(this::notifyListener);

			// log overview of the new properties
			if (notify) {
				PropertiesPrinter.print(getProperties(), getInstance().getInstance());
			}
			Say.info("merged properties"); // TODO set to debug later
		}
	}


	protected List<SourceListener> getListeners() {
		return listeners;
	}


	@Override
	public void addListener(SourceListener listener) {
		getListeners().add(listener);
	}


	@Override
	public void removeListener(SourceListener listener) {
		getListeners().remove(listener);
	}


	protected void notifyListener(Consumer<SourceListener> call) {
		getListeners().forEach(call);
	}

}
