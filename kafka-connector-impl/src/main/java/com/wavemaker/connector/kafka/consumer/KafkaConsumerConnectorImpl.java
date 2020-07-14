package com.wavemaker.connector.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Service;

import com.wavemaker.connector.kafka.service.KafkaConsumerService;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 6/7/20
 */
@Primary
@Service
public class KafkaConsumerConnectorImpl implements KafkaConsumerConnector {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConnectorImpl.class);

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @Override
    public <K, V> KafkaConsumer<K, V> getKafkaConsumer(String groupId, Integer noOfConsumers, Class<K> keyClass, Class<V> valueClass) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = kafkaConsumerService.getKafkaListenerContainerFactory(groupId, keyClass, valueClass);
        int concurrency = noOfConsumers == null ? 1 : noOfConsumers;
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(concurrency * 1000);
        return new KafkaConsumerImpl<K, V>(factory);
    }
}
