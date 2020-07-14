package com.wavemaker.connector.kafka.consumer.callback;

import java.util.List;

import com.wavemaker.connector.kafka.producer.model.Header;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 10/7/20
 *
 *  A key/value pair to be received from Kafka. This also consists of a topic name and
 *  a partition number from which the record is being received, an offset that points
 *  to the record in a Kafka partition, and a timestamp as marked by the corresponding ProducerRecord.
 */
public interface ConsumerRecord<K,V> {

    /**
     * The topic this record is received from (never null)
     */
    public String topic();

    /**
     * The partition from which this record is received
     */
    public int partition();

    /**
     * The headers (never null)
     */
    public List<Header> headers();

    /**
     * The key (or null if no key is specified)
     */
    public K key();

    /**
     * The value
     */
    public V value();

    /**
     * The position of this record in the corresponding Kafka partition.
     */
    public long offset();

    /**
     * The timestamp of this record
     */
    public long timestamp();
}
