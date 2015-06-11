package de.galan.snake.client;

import java.util.Map;
import java.util.Set;

import de.galan.snake.core.SnakeModel;
import de.galan.snake.core.SourceListener;


/**
 * Static facade to access snake configuration properties within clients.
 *
 * @author galan
 */
public class Snake {

	private static SnakeModel model;


	public static void setModel(SnakeModel model) {
		Snake.model = model;
	}


	public static SnakeModel getModel() {
		return model;
	}


	public static String get(String name) {
		return getModel().get(name);
	}


	public static String get(String name, String fallback) {
		return getModel().get(name, fallback);
	}


	public static Map<String, String> getProperties() {
		return getModel().getProperties();
	}


	public static Set<String> getProperties(String prefix) {
		return getModel().getProperties(prefix);
	}


	public static boolean isSet(String name) {
		return getModel().isSet(name);
	}


	public static Integer getInt(String name) {
		return getModel().getInt(name);
	}


	public static Integer getInt(String name, Integer fallback) {
		return getModel().getInt(name, fallback);

	}


	public static Long getLong(String name) {
		return getModel().getLong(name);
	}


	public static Long getLong(String name, Long fallback) {
		return getModel().getLong(name, fallback);
	}


	public static Double getDouble(String name) {
		return getModel().getDouble(name);
	}


	public static Double getDouble(String name, Double fallback) {
		return getModel().getDouble(name, fallback);
	}


	public static boolean getBool(String name) {
		return getModel().getBool(name);
	}


	public static boolean getBool(String name, boolean fallback) {
		return getModel().getBool(name, fallback);
	}


	public static Long getTime(String name) {
		return getModel().getTime(name);
	}


	public static Long getTime(String name, String fallback) {
		return getModel().getTime(name, fallback);
	}


	public static String getInstance() {
		return getModel().getInstance();
	}


	public static String getDirectoryBase() {
		return getModel().getDirectoryBase();
	}


	public static String getDirectoryInstance() {
		return getModel().getDirectoryInstance();
	}


	public static String getDirectoryStorage() {
		return getModel().getDirectoryStorage();
	}


	public static String getDirectoryStorage(String subdirectory) {
		return getModel().getDirectoryStorage(subdirectory);
	}


	public static String getDirectoryLog() {
		return getModel().getDirectoryLog();
	}


	public static String getDirectoryConfiguration() {
		return getModel().getDirectoryConfiguration();
	}


	public static String getDirectoryTemp() {
		return getModel().getDirectoryTemp();
	}


	public static String getDirectoryScript() {
		return getModel().getDirectoryScript();
	}


	/** Logs an overview of the properties */
	public static void printProperties() {
		getModel().printProperties();
	}


	public static void addListener(SourceListener listener) {
		getModel().addListener(listener);
	}


	public static void removeListener(SourceListener listener) {
		getModel().removeListener(listener);
	}


	public static void overlay(String name, String value) {
		getModel().set(name, value);
	}


	public static void overlayInt(String name, Integer value) {
		getModel().setInt(name, value);
	}


	public static void overlayLong(String name, Long value) {
		getModel().setLong(name, value);
	}


	public static void overlayDouble(String name, Double value) {
		getModel().setDouble(name, value);
	}


	public static void overlayBool(String name, Boolean value) {
		getModel().setBool(name, value);
	}


	public static void overlayTime(String name, String value) {
		getModel().setTime(name, value);
	}

}
