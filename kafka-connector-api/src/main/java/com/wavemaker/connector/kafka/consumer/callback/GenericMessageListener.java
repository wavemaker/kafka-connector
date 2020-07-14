package com.wavemaker.connector.kafka.consumer.callback;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 10/7/20
 */

@FunctionalInterface
public interface GenericMessageListener<T> {

    /**
     * Invoked with data from kafka.
     *
     * @param data the data to be processed.
     */
    void onMessage(T data, Acknowledgment acknowledgment);
}
