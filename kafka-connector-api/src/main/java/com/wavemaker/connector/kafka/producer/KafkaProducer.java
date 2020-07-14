package com.wavemaker.connector.kafka.producer;

import com.wavemaker.connector.kafka.producer.callback.KafkaProducerCallback;
import com.wavemaker.connector.kafka.producer.callback.SendResult;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 7/7/20
 */
public interface KafkaProducer<K,V> {

    public void send(V v);

    public void send(V v, KafkaProducerCallback<SendResult<K, V>> callback);

    public void send(K k, V v);

    public void send(K k, V v, KafkaProducerCallback<SendResult<K, V>> callback);

    public void send(Integer partition, K k, V v);

    public void send(Integer partition, K k, V v, KafkaProducerCallback<SendResult<K, V>> callback);

}
