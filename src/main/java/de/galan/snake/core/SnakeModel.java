package de.galan.snake.core;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.galan.commons.logging.Say;
import de.galan.commons.time.Durations;
import de.galan.snake.util.PropertiesPrinter;


/**
 * Provides mapped access to the properties provided by the SnakeSource and SnakeInstance.
 *
 * @author galan
 */
public class SnakeModel implements Overlayable {

	private SnakeSource source;
	private SnakeInstance instance;


	public SnakeModel(SnakeSource source, SnakeInstance instance) {
		this.source = source;
		this.instance = instance;
	}


	protected SnakeSource getSource() {
		return source;
	}


	protected SnakeInstance getSnakeInstance() {
		return instance;
	}


	public String get(String name) {
		return getProperties().get(name);
	}


	public String get(String name, String fallback) {
		return defaultIfBlank(get(name), fallback);
	}


	public boolean isSet(String name) {
		return isNotBlank(get(name));
	}


	public Integer getInt(String name) {
		Integer result = null;
		try {
			String value = get(name);
			result = isBlank(value) ? null : Integer.valueOf(value);
		}
		catch (NumberFormatException nfex) {
			Say.error("Property could not be converted to Integer: {}", name);
		}
		return result;
	}


	public Integer getInt(String name, Integer fallback) {
		Integer value = getInt(name);
		return (value != null) ? value : fallback;
	}


	public Long getLong(String name) {
		Long result = null;
		try {
			String value = get(name);
			result = isBlank(value) ? null : Long.valueOf(value);
		}
		catch (NumberFormatException nfex) {
			Say.error("Property could not be converted to Long: {}", name);
		}
		return result;
	}


	public Long getLong(String name, Long fallback) {
		Long value = getLong(name);
		return (value != null) ? value : fallback;
	}


	public Double getDouble(String name) {
		Double result = null;
		try {
			String value = get(name);
			result = isBlank(value) ? null : Double.valueOf(value);
		}
		catch (NumberFormatException nfex) {
			Say.error("Property could not be converted to Double: {}", name);
		}
		return result;
	}


	public Double getDouble(String name, Double fallback) {
		Double value = getDouble(name);
		return (value != null) ? value : fallback;
	}


	public boolean getBool(String name) {
		return getBool(name, false);
	}


	public boolean getBool(String name, boolean fallback) {
		boolean result = false;
		String value = get(name);
		if (StringUtils.equals(value, "true")) {
			result = true;
		}
		else if (StringUtils.equals(value, "false")) {
			result = false;
		}
		else {
			result = fallback;
		}
		return result;
	}


	public Long getTime(String name) {
		return getTime(name, null);
	}


	public Long getTime(String name, String fallback) {
		String value = get(name);
		Long result = Durations.dehumanize(value);
		if (result == null) {
			result = Durations.dehumanize(fallback);
		}
		return result;
	}


	public Map<String, String> getProperties() {
		return getSource().getProperties();
	}


	public Set<String> getProperties(String prefix) {
		return getSource().getProperties().keySet().stream().filter(key -> startsWith(key, prefix)).collect(toSet());
	}


	public String getInstance() {
		return getSnakeInstance().getInstance();
	}


	public String getDirectoryBase() {
		return getSnakeInstance().getDirectoryBase();
	}


	public String getDirectoryInstance() {
		return getSnakeInstance().getDirectoryInstance();
	}


	public String getDirectoryStorage() {
		return getSnakeInstance().getDirectoryStorage();
	}


	public String getDirectoryStorage(String subdirectory) {
		return getSnakeInstance().getDirectoryStorage(subdirectory);
	}


	public String getDirectoryLog() {
		return getSnakeInstance().getDirectoryLog();
	}


	public String getDirectoryConfiguration() {
		return getSnakeInstance().getDirectoryConfiguration();
	}


	public String getDirectoryTemp() {
		return getSnakeInstance().getDirectoryTemp();
	}


	public String getDirectoryScript() {
		return getSnakeInstance().getDirectoryScript();
	}


	/** Logs an overview of the properties */
	public void printProperties() {
		PropertiesPrinter.print(getSource().getProperties(), getSnakeInstance().getInstance());
	}


	public void addListener(SourceListener listener) {
		getSource().addListener(listener);
	}


	public void removeListener(SourceListener listener) {
		getSource().removeListener(listener);
	}

}
