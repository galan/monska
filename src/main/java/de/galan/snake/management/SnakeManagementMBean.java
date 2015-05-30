package de.galan.snake.management;

/**
 * Simple ManagementBean for Snake, might be improved when required
 *
 * @author daniel
 */
public interface SnakeManagementMBean {

	public String getSystemInstance();


	public String getSystemBase();


	public void printProperties();


	public String[] getProperties();

}
