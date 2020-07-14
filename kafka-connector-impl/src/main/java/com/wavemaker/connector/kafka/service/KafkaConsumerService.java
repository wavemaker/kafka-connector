package com.wavemaker.connector.kafka.service;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:sunil.pulugula@wavemaker.com">Sunil Kumar</a>
 * @since 6/7/20
 */
@Service
public class KafkaConsumerService {

    @Value("${bootstrap.servers}")
    private String bootstrapServers;

    public <K, V> ConcurrentKafkaListenerContainerFactory<K, V> getKafkaListenerContainerFactory(String groupId, Class<K> keyClass, Class<V> valueClass) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(groupId, keyClass, valueClass));
        return factory;
    }

    public <K, V> ConsumerFactory<K, V> consumerFactory(String groupId, Class<K> keyClass, Class<V> valueClass) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");

        return new DefaultKafkaConsumerFactory<K, V>(configProps, getDeserializer(keyClass), getDeserializer(valueClass));
    }

    private <T> Deserializer getDeserializer(Class<T> type) {
        if (type == Integer.class) {
            return new IntegerDeserializer();
        } else if (type == Short.class) {
            return new ShortDeserializer();
        } else if (type == Long.class) {
            return new LongDeserializer();
        } else if (type == Double.class) {
            return new DoubleDeserializer();
        } else if (type == String.class) {
            return new StringDeserializer();
        } else if (type == Bytes.class) {
            return new BytesDeserializer();
        } else if (type == ByteBuffer.class) {
            return new ByteBufferDeserializer();
        } else if (type == byte[].class) {
            return new ByteArrayDeserializer();
        } else {
            JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<T>();
            jsonDeserializer.addTrustedPackages(getPackage(type));
            return jsonDeserializer;
        }

    }

    private String getPackage(Class<?> aClass) {
        return aClass.getPackage().getName();
    }


}
