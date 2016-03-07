package de.galan.snake.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import de.galan.commons.logging.Say;
import de.galan.commons.util.JvmUtil;


/**
 * Initializes and starts the application
 */
public class Launcher {

	private List<AbstractModule> guiceModules;


	public Launcher() {
		BootstrapSnake.initWithDefaults();
		guiceModules = new ArrayList<>();
	}


	public Launcher modules(AbstractModule... modules) {
		guiceModules.addAll(Arrays.asList(modules));
		return this;
	}


	public void start() {
		Injector injector = Guice.createInjector(guiceModules);
		Application application = injector.getInstance(Application.class);
		try {
			application.initialize();
			JvmUtil.addShutdownHook(application::shutdown);
		}
		catch (Throwable ex) {
			Say.error("Unable to initialize application", ex);
			JvmUtil.terminate().returnCode(1).now();
		}

		try {
			Say.info("Starting application: {}", application.getName());
			new Thread(application::start, application.getName() + " application thread").start();
		}
		catch (Throwable ex) {
			Say.error("Unable to start application", ex);
			JvmUtil.terminate().returnCode(2).now();
		}
	}

}
