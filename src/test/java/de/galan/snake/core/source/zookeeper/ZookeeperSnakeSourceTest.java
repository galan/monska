package de.galan.snake.core.source.zookeeper;

import static de.galan.commons.test.Tests.*;
import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;

import de.galan.commons.test.AbstractTestParent;
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


	@Before
	public void setup() {
		System.getProperties().remove(ZookeeperSnakeSource.PROPERTY_CONNECTION); // reset
		//System.setProperty(DefaultSnakeInstance.SNAKE_DIR_CONFIGURATION, dirTest);
		source = new ZookeeperSnakeSource();
		listener = new TestSourceListener();
		source.addListener(listener);
	}


	@After
	public void teardown() throws IOException {
		server.close();
	}


	@Test
	public void testName() throws Exception {
		server = new TestingServer(-1, new File(getTestDirectory(true), "zookeeper-server"), true);
		System.setProperty(ZookeeperSnakeSource.PROPERTY_CONNECTION, server.getConnectString());

		CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(), new RetryOneTime(1000));
		client.start();
		client.create().forPath("/snake", "a=0".getBytes(Charsets.UTF_8));
		client.create().forPath("/snake/undefined", "a=1".getBytes(Charsets.UTF_8));
		client.close();

		//TODO add elements to zk programatically
		source.initialize(new DefaultSnakeInstance());
		assertThat(source.getProperties().get("a")).isEqualTo("1");
	}

}
