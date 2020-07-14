package com.wavemaker.connector.kafka.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;


/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 5/7/20
 */
@Service
public class ZookeeperClient {

    @Value("${zookeeper.hosts}")
    private String zookeeperHosts;

    public ZkUtils getZKClient() {
        ZkClient zkClient = null;
        try {
            int sessionTimeOutInMs = 15 * 1000; // 15 secs
            int connectionTimeOutInMs = 10 * 1000; // 10 secs
            zkClient = new ZkClient(zookeeperHosts, sessionTimeOutInMs, connectionTimeOutInMs, ZKStringSerializer$.MODULE$);
            return new ZkUtils(zkClient, new ZkConnection(zookeeperHosts), false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build zookeeper client", e);
        }
    }
}
