package com.wavemaker.connector.kafka.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.AcknowledgingConsumerAwareMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.TopicPartitionInitialOffset;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.lang.Nullable;

import com.wavemaker.connector.kafka.consumer.callback.KafkaConsumerCallback;
import com.wavemaker.connector.kafka.consumer.container.KafkaConsumerContainer;
import com.wavemaker.connector.kafka.producer.model.Header;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 8/7/20
 */
public class KafkaConsumerImpl<K, V> implements KafkaConsumer<K, V> {

    private ConcurrentKafkaListenerContainerFactory<K, V> factory;

    public KafkaConsumerImpl(ConcurrentKafkaListenerContainerFactory<K, V> factory) {
        this.factory = factory;
        factory.setMessageConverter(new StringJsonMessageConverter());
    }

    public KafkaConsumerContainer<K, V> start(String topic, @Nullable KafkaConsumerCallback<K, V> callback) {
        ConcurrentMessageListenerContainer<K, V> topicContainer = factory.createContainer(topic);
        setCallbackToContainer(topicContainer, callback);
        topicContainer.start();
        return toKafkaConsumerConcurrentContainer(topicContainer);
    }

    public KafkaConsumerContainer<K, V> start(String topic, int partition, @Nullable KafkaConsumerCallback<K, V> callback) {
        ArrayList<TopicPartitionInitialOffset> topicPartitions = new ArrayList<>();
        topicPartitions.add(new TopicPartitionInitialOffset(topic,partition));
        ConcurrentMessageListenerContainer<K, V> topicContainer = factory.createContainer(topicPartitions);
        setCallbackToContainer(topicContainer, callback);
        topicContainer.start();
        return toKafkaConsumerConcurrentContainer(topicContainer);
    }

    private void setCallbackToContainer(ConcurrentMessageListenerContainer<K, V> topicContainer, KafkaConsumerCallback<K, V> callback) {
        if (callback != null) {
            topicContainer.setupMessageListener(toMessageListenerCallback(callback));
        }
    }

    private KafkaConsumerContainer<K, V> toKafkaConsumerConcurrentContainer(final ConcurrentMessageListenerContainer<K, V> topicContainer) {
        return new KafkaConsumerContainer<K, V>() {
            @Override
            public void stop() {
                topicContainer.stop();
            }
        };
    }

    private MessageListener<K, V> toMessageListenerCallback(KafkaConsumerCallback<K, V> callback) {
        return new AcknowledgingConsumerAwareMessageListener<K, V>() {
            @Override
            public void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
                callback.onMessage(new ConsumerRecordImpl<K, V>(data.topic(), data.partition(), data.offset(), data.timestamp(), toHeaders(data.headers()), data.key(), data.value()), new com.wavemaker.connector.kafka.consumer.callback.Acknowledgment() {
                    @Override
                    public void acknowledge() {
                        if(acknowledgment != null) {
                            acknowledgment.acknowledge();
                        }
                    }
                });
            }

            private List<Header> toHeaders(Headers headers) {
                List<Header> kafkaHeaders = new ArrayList<>();
                final org.apache.kafka.common.header.Header[] headersArray = headers.toArray();
                for (int i = 0; i < headersArray.length; i++) {
                    kafkaHeaders.add(new Header(headersArray[i].key(), headersArray[i].value()));
                }
                return kafkaHeaders;
            }
        };
    }
}
