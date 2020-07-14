package com.wavemaker.connector.kafka.producer.callback;

import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 9/7/20
 */
public interface KafkaProducerCallback<T> extends SuccessCallback<T>, FailureCallback {

}
