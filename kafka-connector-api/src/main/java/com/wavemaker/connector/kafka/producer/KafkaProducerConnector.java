package com.wavemaker.connector.kafka.producer;

import org.springframework.lang.NonNull;

import com.wavemaker.runtime.connector.annotation.WMConnector;


@WMConnector(name = "kafka",
        description = "This connector is used to publish messages through kafka distributed messaging framework")
public interface KafkaProducerConnector {

    /**
     * This api will kafkaProducer for your topic
     *
     * @param topicName       name of the kafka topic
     * @param keyClass   keyClass is used to determine the kafka serializer, serializer is used to serialize the key in the message.
     * @param valueClass keyClass is used to determine the kafka serializer, serializer is used to serialize the value in the message.
     */
    public <K, V> KafkaProducer<K, V> getKafkaProducer(@NonNull String topicName, @NonNull Class<K> keyClass, @NonNull Class<V> valueClass);
}