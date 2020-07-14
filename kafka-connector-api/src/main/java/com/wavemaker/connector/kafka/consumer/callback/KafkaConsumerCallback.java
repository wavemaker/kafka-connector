package com.wavemaker.connector.kafka.consumer.callback;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 10/7/20
 */

public interface KafkaConsumerCallback<K, V> extends GenericMessageListener<ConsumerRecord<K, V>> {

}
