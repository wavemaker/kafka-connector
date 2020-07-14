package com.wavemaker.connector.kafka.consumer;

import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import com.wavemaker.connector.kafka.consumer.container.KafkaConsumerContainer;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 10/7/20
 */
public class KafkaConsumerContainerImpl<K,V> implements KafkaConsumerContainer<K,V> {

    private final ConcurrentMessageListenerContainer<K,V> concurrentMessageListenerContainer;

    public KafkaConsumerContainerImpl(ConcurrentMessageListenerContainer<K, V> concurrentMessageListenerContainer) {
        this.concurrentMessageListenerContainer = concurrentMessageListenerContainer;
    }

    @Override
    public void stop() {
        concurrentMessageListenerContainer.stop();
    }
}
