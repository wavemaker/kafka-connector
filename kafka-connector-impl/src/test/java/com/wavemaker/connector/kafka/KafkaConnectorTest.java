package com.wavemaker.connector.kafka;

import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wavemaker.connector.kafka.consumer.KafkaConsumer;
import com.wavemaker.connector.kafka.consumer.callback.Acknowledgment;
import com.wavemaker.connector.kafka.consumer.callback.ConsumerRecord;
import com.wavemaker.connector.kafka.consumer.callback.KafkaConsumerCallback;
import com.wavemaker.connector.kafka.consumer.KafkaConsumerConnector;
import com.wavemaker.connector.kafka.producer.KafkaProducer;
import com.wavemaker.connector.kafka.producer.KafkaProducerConnector;
import com.wavemaker.connector.kafka.topic.KafkaTopicConnector;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KafkaConnectorTestConfiguration.class)
public class KafkaConnectorTest {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConnectorTest.class);

    private final String topicName = "asyncTopic";

    @Autowired
    private KafkaTopicConnector kafkaTopicConnector;

    @Autowired
    private KafkaProducerConnector kafkaProducerConnector;

    @Autowired
    private KafkaConsumerConnector kafkaConsumerConnector;

    @Test
    public void createATopic() {
        String topicName = "test" + new Random().nextInt(100);
        System.out.println("Topic topicName : " + topicName);
        kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());
        deleteTopic(topicName);
    }

    @Test
    public void topicExist() {
        String topicName = "test" + new Random().nextInt(100);
        System.out.println("Topic name : " + topicName);
        kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());
        Assert.assertTrue("Topic must be exist", kafkaTopicConnector.isExist(topicName));
        deleteTopic(topicName);
    }

    @Test
    public void ListTopics() {
        boolean topicExist = false;
        String topicName = "test" + new Random().nextInt(100);
        kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());
        List<String> topics = kafkaTopicConnector.listTopics();
        for (String topic : topics) {
            if (topic.equals(topicName)) {
                topicExist = true;
                break;
            }
        }
        Assert.assertTrue("Topic must be exist in the list topics", topicExist);
        deleteTopic(topicName);
    }

    private void deleteTopic(String topic) {
        System.out.println("Deleting topic " + topic);
        kafkaTopicConnector.deleteTopic(topic);
    }

    @Test
    public void sendMessage() {
        String topicName = "test" + new Random().nextInt(100);
        System.out.println("Topic is :" + topicName);
        kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());
        System.out.println("Topic created : " + topicName);
        KafkaProducer<Integer, String> kafkaProducer = kafkaProducerConnector.getKafkaProducer(topicName, Integer.class, String.class
        );
        for (int i = 10; i < 20; i++) {
            try {
                kafkaProducer.send(i, "Message no " + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        deleteTopic(topicName);
    }


    @Test
    public void sendAndReceiveStringMessage() {
        String topicName = "test" + new Random().nextInt(100);
        System.out.println("Topic is :" + topicName);
        kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());
        System.out.println("Topic created : " + topicName);
        KafkaProducer<String, String> kafkaProducer = kafkaProducerConnector.getKafkaProducer(topicName, String.class, String.class);
        for (int i = 10; i < 20; i++) {
            kafkaProducer.send(i + "", "Hello, testing count " + i);
        }
        KafkaConsumer<String, String> kafkaConsumer = kafkaConsumerConnector.getKafkaConsumer("group1", 2, String.class, String.class);

        kafkaConsumer.start(topicName, new KafkaConsumerCallback<String, String>() {
            @Override
            public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
                logger.info("Consumer receiving messages : " + record.key() + ":" + record.value() + " in partition [ " + record.partition() + " ]" + " with offset no [ " + record.offset() + " ]");
            }

        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deleteTopic(topicName);
    }

    @Test
    public void sendAndReceiveUserMessage() {
        String topicName = "test" + new Random().nextInt(100);
        System.out.println("Topic is :" + topicName);
        kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());

        KafkaProducer<Integer, User> kafkaProducer = kafkaProducerConnector.getKafkaProducer(topicName, Integer.class, User.class);

        for (int i = 10; i < 20; i++) {
            kafkaProducer.send(i, new User(i, "user" + i));
        }

        KafkaConsumer<Integer, User> kafkaConsumer = kafkaConsumerConnector.getKafkaConsumer("group1", 2, Integer.class, User.class);

        kafkaConsumer.start(topicName, new KafkaConsumerCallback<Integer, User>() {

            @Override
            public void onMessage(ConsumerRecord<Integer, User> data, Acknowledgment acknowledgment) {
                logger.info("Consumer receiving meesage : " + data.key() + ":" + data.value() + " in partition [ " + data.partition() + " ]" + " with offset no [ " + data.offset() + " ]");
            }

        });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deleteTopic(topicName);
    }

    @Test
    public void sendAndReceiveIntegerMessage() {
        String topicName = "test" + new Random().nextInt(100);
        System.out.println("Topic is :" + topicName);
        kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());

        KafkaProducer<String, Integer> kafkaProducer = kafkaProducerConnector.getKafkaProducer(topicName, String.class, Integer.class);

        for (int i = 10; i < 20; i++) {
            kafkaProducer.send(i + ". we are receiving messages", i);
        }

        KafkaConsumer<String, Integer> kafkaConsumer = kafkaConsumerConnector.getKafkaConsumer("group1", 2, String.class, Integer.class);

        kafkaConsumer.start(topicName,
                new KafkaConsumerCallback<String, Integer>() {
                    @Override
                    public void onMessage(ConsumerRecord<String, Integer> data, Acknowledgment acknowledgment) {
                        logger.info("Consumer receiving meesage : " + data.key() + " : " + data.value() + " in partition [ " + data.partition() + " ]" + " with offset no [ " + data.offset() + " ]");
                        acknowledgment.acknowledge();
                    }

                });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deleteTopic(topicName);
    }


    @Test
    public void sendAndReceiveOnlyValueMessage() {
        String topicName = "test" + new Random().nextInt(100);
        System.out.println("Topic is :" + topicName);
        kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());

        KafkaProducer<String, Integer> kafkaProducer = kafkaProducerConnector.getKafkaProducer(topicName, String.class, Integer.class);

        for (int i = 10; i < 20; i++) {
            kafkaProducer.send(i);
        }

        KafkaConsumer<String, Integer> kafkaConsumer = kafkaConsumerConnector.getKafkaConsumer("group1", 2, String.class, Integer.class);

        kafkaConsumer.start(topicName,
                new KafkaConsumerCallback<String, Integer>() {
                    @Override
                    public void onMessage(ConsumerRecord<String, Integer> data, Acknowledgment acknowledgment) {
                        logger.info("Consumer receiving message : "+data.value() + " in partition [ " + data.partition() + " ]" + " with offset no [ " + data.offset() + " ]");
                        acknowledgment.acknowledge();
                    }

                });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deleteTopic(topicName);
    }

    @Test
    public void sendAsyncMessage() {
        System.out.println("Topic is :" + topicName);
        if (!kafkaTopicConnector.isExist(topicName)) {
            kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());
        }
        KafkaProducer<Integer, String> kafkaProducer = kafkaProducerConnector.getKafkaProducer(topicName, Integer.class, String.class);

        for (int i = 0; i < 10; i++) {
            kafkaProducer.send(i, "Hello, testing count " + i);
        }
    }

    @Test
    public void receiveAsyncMessage() {

        KafkaConsumer<Integer, String> kafkaConsumer = kafkaConsumerConnector.getKafkaConsumer("group1", 2, Integer.class, String.class);
        kafkaConsumer.start(topicName,
                new KafkaConsumerCallback<Integer, String>() {
                    @Override
                    public void onMessage(ConsumerRecord<Integer, String> data, Acknowledgment acknowledgment) {
                        logger.info("Consumer receiving message : "+ data.key() + " : " + data.value() + " in partition [ " + data.partition() + " ]" + " with offset no [ " + data.offset() + " ]");
                        acknowledgment.acknowledge();
                    }

                });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}