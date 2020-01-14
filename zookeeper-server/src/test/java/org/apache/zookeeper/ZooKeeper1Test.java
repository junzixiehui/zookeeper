/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zookeeper;

import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.cli.*;
import org.apache.zookeeper.client.ConnectStringParser;
import org.apache.zookeeper.client.HostProvider;
import org.apache.zookeeper.client.StaticHostProvider;
import org.apache.zookeeper.client.ZKClientConfig;
import org.apache.zookeeper.common.StringUtils;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 *
 * Testing ZooKeeper public methods
 *
 */
public class ZooKeeper1Test {


    @Test
    public void testDeleteRecursive() throws IOException, InterruptedException, CliException, KeeperException {
        final ZooKeeper zk = createClient();
        // making sure setdata works on /
        zk.setData("/", "some".getBytes(), -1);
        zk.create("/a", "some".getBytes(), Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);

        List<String> children = zk.getChildren("/a", false);

        Assert.assertEquals("2 children - b & c should be present ", children
                .size(), 2);
        Assert.assertTrue(children.contains("b"));
        Assert.assertTrue(children.contains("c"));

        ZooKeeperMain zkMain = new ZooKeeperMain(zk);
        // 'rmr' is deprecated, so the test here is just for backwards
        // compatibility.
        String cmdstring0 = "rmr /a/b/v";
        String cmdstring1 = "deleteall /a";
        zkMain.cl.parseCommand(cmdstring0);
        Assert.assertFalse(zkMain.processZKCmd(zkMain.cl));
        Assert.assertEquals(null, zk.exists("/a/b/v", null));
        zkMain.cl.parseCommand(cmdstring1);
        Assert.assertFalse(zkMain.processZKCmd(zkMain.cl));
        Assert.assertNull(zk.exists("/a", null));
    }

	private ZooKeeper createClient() {


    	try {
			ZooKeeper zooKeeper = new ZooKeeper("localhost:2182",10000,null);
			return zooKeeper;
		} catch (Exception e){

		}
    	return null;
	}
}
