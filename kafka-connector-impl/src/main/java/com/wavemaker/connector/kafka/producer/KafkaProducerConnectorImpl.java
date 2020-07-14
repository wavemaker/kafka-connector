package com.wavemaker.connector.kafka.producer;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.wavemaker.connector.kafka.service.KafkaProducerService;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 6/7/20
 */
@Primary
@Service
public class KafkaProducerConnectorImpl implements KafkaProducerConnector {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerConnectorImpl.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Override
    public <K, V> KafkaProducer<K, V> getKafkaProducer(String topicName, Class<K> keyClass, Class<V> valueClass) {
        KafkaTemplate<K, V> kafkaTemplate = kafkaProducerService.getkafkaTemplate(keyClass,valueClass);
        logger.info("Kafka template has been created for topic " + topicName);
        return new KafkaProducerImpl<K, V>(topicName, kafkaTemplate);
    }
}
