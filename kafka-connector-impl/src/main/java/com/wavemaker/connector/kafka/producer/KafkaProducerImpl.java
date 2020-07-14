package com.wavemaker.connector.kafka.producer;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.wavemaker.connector.kafka.producer.callback.KafkaProducerCallback;
import com.wavemaker.connector.kafka.producer.model.Header;
import com.wavemaker.connector.kafka.producer.callback.SendResult;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 8/7/20
 */
public class KafkaProducerImpl<K, V> implements KafkaProducer<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerImpl.class);


    private KafkaTemplate<K, V> kafkaTemplate;

    private ListenableFutureCallback<org.springframework.kafka.support.SendResult<K, V>> defaultCallback;

    public KafkaProducerImpl(String topic, KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.defaultCallback = buildDefaultCallback();
        kafkaTemplate.setDefaultTopic(topic);
    }

    @Override
    public void send(V v) {
        ListenableFuture<org.springframework.kafka.support.SendResult<K, V>> listenableFuture = kafkaTemplate.sendDefault(v);
        listenableFuture.addCallback(defaultCallback);
    }

    @Override
    public void send(@Nullable V v, KafkaProducerCallback<SendResult<K, V>> callback) {
        ListenableFuture<org.springframework.kafka.support.SendResult<K, V>> listenableFuture = kafkaTemplate.sendDefault(v);
        listenableFuture.addCallback(toKafkaCallback(callback));
    }

    @Override
    public void send(K k, @Nullable V v) {
        ListenableFuture<org.springframework.kafka.support.SendResult<K, V>> listenableFuture = kafkaTemplate.sendDefault(k, v);
        listenableFuture.addCallback(defaultCallback);
    }

    @Override
    public void send(K k, @Nullable V v, KafkaProducerCallback<SendResult<K, V>> callback) {
        ListenableFuture<org.springframework.kafka.support.SendResult<K, V>> listenableFuture = kafkaTemplate.sendDefault(k, v);
        listenableFuture.addCallback(toKafkaCallback(callback));
    }

    @Override
    public void send(Integer partition, K k, @Nullable V v) {
        ListenableFuture<org.springframework.kafka.support.SendResult<K, V>> listenableFuture = kafkaTemplate.sendDefault(partition, k, v);
        listenableFuture.addCallback(defaultCallback);
    }

    @Override
    public void send(Integer partition, K k, @Nullable V v, KafkaProducerCallback<SendResult<K, V>> callback) {
        kafkaTemplate.sendDefault(partition, k, v);
    }

    private ListenableFutureCallback<org.springframework.kafka.support.SendResult<K, V>> buildDefaultCallback() {
        return new ListenableFutureCallback<org.springframework.kafka.support.SendResult<K, V>>() {

            @Override
            public void onSuccess(org.springframework.kafka.support.SendResult<K, V> result) {
                K key = result.getProducerRecord().key();
                V value = result.getProducerRecord().value();
                logger.info("Sent message=[" + key + ":" + value +
                        "] in partition=[" + result.getRecordMetadata().partition() + " with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("Unable to send message in kafka due to" + ex.getMessage());
                new RuntimeException("Unable to send message in kafka", ex);
            }
        };
    }

    private ListenableFutureCallback<org.springframework.kafka.support.SendResult<K, V>> toKafkaCallback(KafkaProducerCallback<SendResult<K, V>> callback) {
        return new ListenableFutureCallback<org.springframework.kafka.support.SendResult<K, V>>() {
            @Override
            public void onSuccess(org.springframework.kafka.support.SendResult<K, V> kvSendResult) {
                callback.onSuccess(new SendResult<K, V>() {
                    @Override
                    public Integer getPartition() {
                        return kvSendResult.getProducerRecord().partition();
                    }

                    @Override
                    public String getTopic() {
                        return kvSendResult.getProducerRecord().topic();
                    }

                    @Override
                    public K Key() {
                        return kvSendResult.getProducerRecord().key();
                    }

                    @Override
                    public V Value() {
                        return kvSendResult.getProducerRecord().value();
                    }

                    @Override
                    public long getTimestamp() {
                        return kvSendResult.getProducerRecord().timestamp();
                    }

                    @Override
                    public List<Header> getHeaders() {
                        List<Header> kafkaHeaders = new ArrayList<>();
                        Headers headers = kvSendResult.getProducerRecord().headers();
                        final org.apache.kafka.common.header.Header[] headersArray = headers.toArray();
                        for (int i = 0; i < headersArray.length; i++) {
                            kafkaHeaders.add(new Header(headersArray[i].key(), headersArray[i].value()));
                        }
                        return kafkaHeaders;
                    }

                    @Override
                    public long offset() {
                        return kvSendResult.getRecordMetadata().offset();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
        };
    }

}
