package com.wavemaker.connector.kafka.consumer.callback;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 10/7/20
 */
public interface Acknowledgment {

    /**
     * Invoked when the record or batch for which the acknowledgment has been created has
     * been processed. Calling this method implies that all the previous messages in the
     * partition have been processed already.
     */
    void acknowledge();
}
