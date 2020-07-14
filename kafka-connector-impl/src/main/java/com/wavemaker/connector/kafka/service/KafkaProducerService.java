package com.wavemaker.connector.kafka.service;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 6/7/20
 */
@Service
public class KafkaProducerService {

    @Value("${bootstrap.servers}")
    private String bootstrapServers;

    @Value("${kafka.producer.acks}")
    private String producerAcks;

    @Value("${kafka.producer.retries}")
    private Integer producerRetries;

    @Value("${kafka.producer.batch.size}")
    private Integer producerBatchSize;

    @Value("${kafka.producer.linger.ms}")
    private Integer producerLingerMs;

    @Value("${kafka.producer.buffer.memory}")
    private Integer producerBufferMemory;


    public <K, V> KafkaTemplate<K, V> getkafkaTemplate(Class<K> keyClass, Class<V> valueClass) {
        return new KafkaTemplate<K, V>(getproducerFactory(keyClass, valueClass), true);
    }

    private <K, V> ProducerFactory<K, V> getproducerFactory(Class<K> keyClass, Class<V> valueClass) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        configProps.put("ack", "all");
        configProps.put("timeout.ms", 3000);
        configProps.put("acks", producerAcks);
        configProps.put("retries", producerRetries);
        configProps.put("batch.size", producerBatchSize);
        configProps.put("linger.ms", producerLingerMs);
        configProps.put("buffer.memory", producerBufferMemory);
        try {
            return new DefaultKafkaProducerFactory(configProps, getSerializer(keyClass), getSerializer(valueClass));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> Serializer getSerializer(Class<T> type) {
        if (type == Integer.class) {
            return new IntegerSerializer();
        } else if (type == Short.class) {
            return new ShortSerializer();
        } else if (type == Long.class) {
            return new LongSerializer();
        } else if (type == Double.class) {
            return new DoubleSerializer();
        } else if (type == Float.class) {
            return new FloatSerializer();
        } else if (type == String.class) {
            return new StringSerializer();
        } else if (type == Bytes.class) {
            return new BytesSerializer();
        } else if (type == ByteBuffer.class) {
            return new ByteBufferSerializer();
        } else if (type == byte[].class) {
            return new ByteArraySerializer();
        } else {
            return new JsonSerializer<T>();
        }
    }
}
