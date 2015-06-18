package de.galan.snake.core.source.zookeeper;

import static de.galan.commons.test.Tests.*;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Charsets;

import de.galan.commons.test.AbstractTestParent;
import de.galan.commons.time.Sleeper;
import de.galan.snake.core.DefaultSnakeInstance;
import de.galan.snake.core.source.TestSourceListener;


/**
 * CUT ZookeeperSnakeSource
 *
 * @author galan
 */
public class ZookeeperSnakeSourceTest extends AbstractTestParent {

	private TestingServer server;

	private ZookeeperSnakeSource source;
	private TestSourceListener listener;

	private CuratorFramework client;


	@Before
	public void setup() {
		System.clearProperty(ZookeeperSnakeSource.PROPERTY_PATH);
		System.getProperties().remove(ZookeeperSnakeSource.PROPERTY_CONNECTION); // reset
		//System.setProperty(DefaultSnakeInstance.SNAKE_DIR_CONFIGURATION, dirTest);
		source = new ZookeeperSnakeSource();
		listener = new TestSourceListener();
		source.addListener(listener);
	}


	@After
	public void tearDown() {
		if (client != null && (client.getState() == CuratorFrameworkState.STARTED)) {
			client.close();
		}
	}


	private void startZookeeper() throws Exception {
		server = new TestingServer(-1, new File(getTestDirectory(true), "zookeeper-server"), true);
		System.setProperty(ZookeeperSnakeSource.PROPERTY_CONNECTION, server.getConnectString());
		client = CuratorFrameworkFactory.newClient(server.getConnectString(), new RetryOneTime(1000));
		client.start();
	}


	@After
	public void teardown() throws IOException {
		server.close();
	}


	@Test
	public void undefinedPathInitialized() throws Exception {
		startZookeeper();
		client.create().forPath("/snake", "a=0".getBytes(Charsets.UTF_8));
		client.create().forPath("/snake/undefined", "a=1".getBytes(Charsets.UTF_8));
		source.initialize(new DefaultSnakeInstance());
		assertThat(source.getProperties().get("a")).isEqualTo("1");
	}


	@Test
	public void undefinedPathCreatedImplicit() throws Exception {
		startZookeeper();
		assertThat(client.checkExists().forPath("/snake/undefined")).isNull();
		source.initialize(new DefaultSnakeInstance());
		assertThat(client.checkExists().forPath("/snake/undefined")).isNotNull();
	}


	@Test
	public void definedPathCreatedImplicit() throws Exception {
		System.setProperty(ZookeeperSnakeSource.PROPERTY_PATH, "/something/completly/different");
		startZookeeper();
		assertThat(client.checkExists().forPath("/snake/something/completly/different")).isNull();
		source.initialize(new DefaultSnakeInstance());
		assertThat(client.checkExists().forPath("/snake/something/completly/different")).isNotNull();
	}


	@Test
	public void overwriteAfterwardsBehaviour() throws Exception {
		System.setProperty(ZookeeperSnakeSource.PROPERTY_PATH, "/my/path");
		startZookeeper();
		source.initialize(new DefaultSnakeInstance());
		assertThat(source.getProperties().get("a")).isNull();

		client.setData().forPath("/snake", "a=0".getBytes(Charsets.UTF_8));
		Sleeper.sleep("200ms");
		assertThat(source.getProperties().get("a")).isEqualTo("0");
		client.setData().forPath("/snake/my", "a=1".getBytes(Charsets.UTF_8));
		Sleeper.sleep("200ms");
		assertThat(source.getProperties().get("a")).isEqualTo("1");
		client.setData().forPath("/snake/my/path", "a=2".getBytes(Charsets.UTF_8));
		Sleeper.sleep("200ms");
		assertThat(source.getProperties().get("a")).isEqualTo("2");
	}


	@Test
	@Ignore
	public void invalidData() throws Exception {
		startZookeeper();
		client.create().forPath("/snake", "what makes an invalid property?".getBytes(Charsets.UTF_8));
		source.initialize(new DefaultSnakeInstance());
	}

}
