package com.wavemaker.connector.kafka.consumer.container;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 10/7/20
 */
public interface KafkaConsumerContainer<K,V> {

    public void stop();
}
