package com.wavemaker.connector.kafka.consumer;

import org.springframework.lang.Nullable;

import com.wavemaker.connector.kafka.consumer.callback.KafkaConsumerCallback;
import com.wavemaker.connector.kafka.consumer.container.KafkaConsumerContainer;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 8/7/20
 */
public interface KafkaConsumer<K, V> {

    public KafkaConsumerContainer<K, V> start(String topic, @Nullable KafkaConsumerCallback<K, V> callback);

    public KafkaConsumerContainer<K, V> start(String topic, int partition, @Nullable KafkaConsumerCallback<K, V> callback);

}
