package com.wavemaker.connector.kafka.topic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.wavemaker.connector.kafka.zkclient.ZookeeperClient;
import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZkUtils;
import scala.collection.JavaConversions;

@Service
@Primary
public class KafkaTopicConnectorImpl implements KafkaTopicConnector {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTopicConnectorImpl.class);

    @Autowired
    private ZookeeperClient zkClient;


    @Override
    public boolean isExist(String topicName) {
        List<String> topics = listTopics();
        for (String topic : topics) {
            if (topic.equals(topicName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createTopic(String topicName, int noOfPartitions, int noOfReplicas, Properties topicConfiguration) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = this.zkClient.getZKClient();
            AdminUtils.createTopic(zkUtils, topicName, noOfPartitions, noOfReplicas, topicConfiguration, new RackAwareMode.Safe$());
        } finally {
            closeZKClient(zkUtils);
        }
    }

    @Override
    public List<String> listTopics() {
        ZkUtils zkUtils = null;
        try {
            zkUtils = this.zkClient.getZKClient();
            Collection<String> topics = JavaConversions.asJavaCollection(zkUtils.getAllTopics());
            return new ArrayList<>(topics);
        } finally {
            closeZKClient(zkUtils);
        }
    }

    @Override
    public void deleteTopic(String topicName) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = this.zkClient.getZKClient();
            AdminUtils.deleteTopic(zkUtils, topicName);
        } finally {
            closeZKClient(zkUtils);
        }
    }

    @Override
    public void updateTopicConfiguration(String topicName, Properties properties) {
        ZkUtils zkUtils = null;
        try {
            zkUtils = this.zkClient.getZKClient();
            AdminUtils.changeTopicConfig(zkUtils,topicName,properties);
        } finally {
            closeZKClient(zkUtils);
        }
    }

    private void closeZKClient(ZkUtils zkUtils) {
        if (zkUtils != null) {
            zkUtils.close();
        }
    }
}