package com.wavemaker.connector.kafka.consumer;

import java.util.List;

import com.wavemaker.connector.kafka.consumer.callback.ConsumerRecord;
import com.wavemaker.connector.kafka.producer.model.Header;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 10/7/20
 *
 *
 */
public class ConsumerRecordImpl<K, V> implements ConsumerRecord<K, V> {

    private final String topic;
    private final int partition;
    private final long offset;
    private final long timestamp;
    private final List<Header> headers;
    private final K key;
    private final V value;

    /**
     * Creates a record to be received from a specified topic and partition
     *
     * @param topic     The topic this record is received from
     * @param partition The partition of the topic this record is received from
     * @param offset    The offset of this record in the corresponding Kafka partition
     * @param timestamp The timestamp of the record.
     * @param key       The key of the record, if one exists (null is allowed)
     * @param value     The record contents
     * @param headers   The headers of the record
     */
    public ConsumerRecordImpl(String topic, int partition, long offset, long timestamp, List<Header> headers, K key, V value) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.timestamp = timestamp;
        this.headers = headers;
        this.key = key;
        this.value = value;
    }

    /**
     * The topic this record is received from (never null)
     */
    public String topic() {
        return this.topic;
    }

    /**
     * The partition from which this record is received
     */
    public int partition() {
        return this.partition;
    }

    /**
     * The headers (never null)
     */
    public List<Header> headers() {
        return headers;
    }

    /**
     * The key (or null if no key is specified)
     */
    public K key() {
        return key;
    }

    /**
     * The value
     */
    public V value() {
        return value;
    }

    /**
     * The position of this record in the corresponding Kafka partition.
     */
    public long offset() {
        return offset;
    }

    /**
     * The timestamp of this record
     */
    public long timestamp() {
        return timestamp;
    }


}
