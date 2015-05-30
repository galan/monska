package de.galan.snake.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Iterables;

import de.galan.snake.client.Snake;


/**
 * ManagementBean for Snake for introspection using JMX.
 *
 * @author daniel
 */
public class SnakeManagement implements SnakeManagementMBean {

	@Override
	public String getSystemBase() {
		return Snake.getDirectoryBase();
	}


	@Override
	public String getSystemInstance() {
		return Snake.getInstance();
	}


	@Override
	public void printProperties() {
		Snake.printProperties();
	}


	@Override
	public String[] getProperties() {
		List<String> properties = new ArrayList<>();
		for (Entry<String, String> entry: Snake.getProperties().entrySet()) {
			properties.add(entry.getKey() + "=" + entry.getValue());
		}
		Collections.sort(properties);
		return Iterables.toArray(properties, String.class);
	}

}
