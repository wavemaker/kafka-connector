package com.wavemaker.connector.kafka.consumer;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.wavemaker.runtime.connector.annotation.WMConnector;


@WMConnector(name = "kafka",
        description = "This connector is used to publish and receive messages through kafka distributed messaging framework")
public interface KafkaConsumerConnector {

    /**
     * create a kafka consumer for a specific, kafka consumer is used to receieve messages from any topic for this group.
     *
     * @param groupId           consumer group Id
     * @param noOfConsumers     no of consumers in consumer group, this makes to receive messages concurrently from different partitions
     * @param keyClass   keyClass is to determine kafka deserializer, deserializer is used to deserialize key in the consumer message
     * @param valueClass valueClass is to determine kafka deserializer,deserializer is used to deserialize value in the consumer message
     * @param <K>               key type of the message
     * @param <V>               value type of the message
     */
    public <K, V> KafkaConsumer<K, V>  getKafkaConsumer(@NonNull String groupId, @Nullable Integer noOfConsumers, @NonNull Class<K> keyClass, @NonNull Class<V> valueClass);
}