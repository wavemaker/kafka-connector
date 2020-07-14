package com.wavemaker.connector.kafka.topic;

import java.util.List;
import java.util.Properties;

import com.wavemaker.runtime.connector.annotation.WMConnector;


@WMConnector(name = "kafka",
        description = "This connector is used to publish and receive messages through kafka distributed messaging framework")
public interface KafkaTopicConnector {

    /**
     * Verifies topic existing in the kafka cluster
     */
    public boolean isExist(String topicName);

    /**
     * Creates a topic in the kafka cluster with no of partition that topic should slips
     *
     * @param topicName          name of the topic to be created in kafka-zookeeper.
     * @param noOfPartitions     No of partitions that topic message should split in persists
     * @param noOfReplicas       No of replicas that topics messages should be replicated, recommended is (NoOfPartitions - 1)
     * @param topicConfiguration If any specific properties you want to set to topic
     */
    public void createTopic(String topicName, int noOfPartitions, int noOfReplicas, Properties topicConfiguration);

    /**
     * Lists the topics in the kafka cluster
     */
    public List<String> listTopics();

    /**
     * Delete a topic in the kafka cluster
     */
    public void deleteTopic(String topicName);

    /**
     * Updates the configuration of kafka topic in kafka cluster
     */
    public void updateTopicConfiguration(String topicName, Properties properties);

}