package de.galan.snake.core.source.file;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.StandardSystemProperty;

import de.galan.commons.io.file.FileListener;
import de.galan.commons.io.file.FilesystemObserver;
import de.galan.commons.logging.Say;
import de.galan.commons.util.JvmUtils;
import de.galan.snake.core.SnakeInstance;
import de.galan.snake.core.source.AbstractSnakeSource;


/**
 * daniel should have written a comment here.
 *
 * @author daniel
 */
public class FileSnakeSource extends AbstractSnakeSource {

	private File fileInstance;
	private File fileUser;


	@Override
	public void initialize(SnakeInstance instance) {
		super.initialize(instance);
		fileInstance = determineInstanceFile(getInstance());
		fileUser = determineUserFile(getInstance());
		boolean observe = "true".equals(System.getProperty("snake.source.file.observe", "true"));
		if (observe) {
			try {
				//FilePropertySupplier fileSupplier = new FilePropertySupplier(fileInstance, fileUser);
				//RefreshPropertiesListener listener = new RefreshPropertiesListener(access, fileSupplier);
				RefreshPropertiesListenerNew listener = new RefreshPropertiesListenerNew();
				FilesystemObserver observer = new FilesystemObserver();
				observer.registerFileListener(listener, fileInstance);
				observer.registerFileListener(listener, fileUser);
			}
			catch (Exception ex) {
				JvmUtils.terminate().message("Snake failed observing properties-file.").returnCode(2).now();
			}
		}
		refresh(true);
	}


	protected void refresh(boolean notify) {
		Map<String, String> result = new HashMap<>();
		Properties properties = new Properties();
		properties.putAll(loadProperties(fileInstance));
		properties.putAll(loadProperties(fileUser));
		for (Object key: properties.keySet()) { // TODO filter defaults?
			// Whitespaces may lead to unrecognized properties
			result.put((String)key, trimToEmpty(properties.getProperty((String)key)));
		}
		mergeProperties(result, notify);
	}


	protected Properties loadProperties(File propertyFile) {
		Properties result = new Properties();
		if (propertyFile != null && propertyFile.exists()) {
			try (FileReader reader = new FileReader(propertyFile)) {
				result.load(reader);
			}
			catch (Exception ex) {
				Say.error("Snake properties file {} could not be read", ex, propertyFile.getName());
			}
		}
		return result;
	}


	protected File determineInstanceFile(SnakeInstance instance) {
		String defaultFile = instance.getDirectoryConfiguration() + "instance.properties";
		File result = new File(System.getProperty("snake.source.file.instance", defaultFile));
		if (!result.exists() || result.isDirectory()) {
			JvmUtils.terminate().message("No snake instance properties-file exists.\n(searched in " + result + ")").returnCode(2).now();
		}
		return result;
	}


	protected File determineUserFile(SnakeInstance instance) {
		String defaultFile = instance.getDirectoryConfiguration() + StandardSystemProperty.USER_NAME.value() + ".properties";
		return new File(System.getProperty("snake.source.file.user", defaultFile));
	}

	/** Notification on changes for a file */
	private class RefreshPropertiesListenerNew implements FileListener {

		@Override
		public void notifyFileCreated(File file) {
			if (isTrackingAll()) {
				refresh(true);
			}
		}


		@Override
		public void notifyFileChanged(File file) {
			refresh(true);
		}


		@Override
		public void notifyFileDeleted(File file) {
			if (isTrackingAll()) {
				refresh(true);
			}
		}


		// http://stackoverflow.com/questions/607435/why-does-vim-save-files-with-a-extension
		protected boolean isTrackingAll() {
			String modificationsOnly = System.getProperty("snake.source.file.trackAnyChange", "true");
			return "true".equals(modificationsOnly);
		}

	}

}
