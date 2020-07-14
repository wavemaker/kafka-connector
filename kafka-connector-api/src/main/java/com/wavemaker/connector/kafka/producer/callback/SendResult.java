package com.wavemaker.connector.kafka.producer.callback;

import java.util.List;

import com.wavemaker.connector.kafka.producer.model.Header;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 9/7/20
 */
public interface SendResult<K,V> {

    public Integer getPartition();

    public String getTopic();

    public K Key();

    public V Value();

    public long getTimestamp();

    public List<Header> getHeaders();

    public long offset();

}
