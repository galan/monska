package de.galan.snake.util;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import com.google.common.base.StandardSystemProperty;

import de.galan.commons.util.MessageBox;


/**
 * Prints the given properties to the log
 *
 * @author daniel
 */
public class PropertiesPrinter {

	public static void print(Map<String, String> properties, String instance) {
		String user = StandardSystemProperty.USER_NAME.value();
		String title = "Snake properties (" + user + ":" + instance + ")";
		List<String> entries = properties.keySet().stream().sorted().map((key) -> key + " = " + properties.get(key)).collect(toList());
		MessageBox.printBox(title, entries);
	}

}
