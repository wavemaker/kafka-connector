# kafka-connector

This Connector exposes APIs to intracts with Apacahe Kafka Cluster which can be integrated into WaveMaker Application.

## Kafka Introduction
Apache Kafka is a distributed publish-subscribe messaging system.Kafka is fast, scalable, durable, fault-tolerant and distributed messaging system by design.
It was originally developed by Linked later it became of part of Apache project.

## Kafka Fundamentals

### Topic
Messages belongs to a particular category is known as topic.

### Partition
Topics are split into partitions, for each topic Kafka will keep at least one partition.

### Broker
A Kafka cluster consists of one or more servers (Kafka brokers) running Kafka. A Kafka server, a Kafka broker and a Kafka node all refer to the same concept in cluster.

### Kafka Controller
Within a Kafka cluster, a single broker serves as the active controller which is responsible for state management of partitions and replicas. So in your case, if you have a cluster with 100 brokers, one of them will act as the controller.

### Zoo Keeper
Zookeeper is a top-level software developed by Apache that acts as a centralized service and is used to maintain naming and configuration data.
Zookeeper keeps track of status of the Kafka cluster nodes and it also keeps track of Kafka topics, partitions etc.

### Consumer
Consumers read data from brokers. Consumers subscribes to one or more topics and consume published messages by pulling data from the brokers.

### Consumer Group
When multiple consumers are subscribed to a topic and belong to the same consumer group.
Each Consumer group has unique Id and each group can register for one or more topics.

## Build 
You can build this connector using following command
```
mvn clean install 
```

## Deploy 
You can import connector dist/kafka.zip artifact in WaveMaker studio application using file upload option.
On after deploying Kafka-Connector in the WaveMaker studio application, make sure to update connector properties in the profile properties.Such as 
zookeeper IP, Kafka brokers IPs...etc

## Using Connector in WaveMaker
This connector has three connector interfaces which are used for different operations in Kafka cluster.

### TopicConnector
This connector Interface apis are used to manages topics in the Kafka cluster.Such as creating, Listing, Deleting, Updating Topics in the cluster.
```
@Autowired
private KafkaTopicConnector kafkaTopicConnector;

String topicName = "demoTopic";
kafkaTopicConnector.createTopic(topicName, 2, 1, new Properties());
```
### ProducerConnector
This connector interface apis are used to send massages to specific topic in the kafka cluster
```
@Autowired
private KafkaProducerConnector kafkaProducerConnector;

KafkaProducer<Integer, String> kafkaProducer = kafkaProducerConnector.getKafkaProducer(topicName, Integer.class, String.class);
kafkaProducer.send(1,"Hello, Welcome to kafka connector");
```
### ConsumerConnector
This connector interface apis are used to consume message from a specific topic using consumer groups in the kafka cluster.
```
@Autowired
private KafkaConsumerConnector kafkaConsumerConnector;

KafkaConsumer<String, String> kafkaConsumer = kafkaConsumerConnector.getKafkaConsumer("group1", 2, Integer.class, String.class);
kafkaConsumer.start(topicName, new KafkaConsumerCallback<Integer, String>() {
   @Override
   public void onMessage(ConsumerRecord<Integer, String> record, Acknowledgment acknowledgment) {
      logger.info("Consumer receiving messages : " + record.key() + ":" + record.value() + " in partition [ " + record.partition() + " ]" + " with offset no [ " + record.offset() + " ]");
   }
});
```
Apart from above apis, there are other apis in each connector interface, please visit connector interfaces in the kafka-api module.


## Conclusion
Using Kafka-Connector in the WaveMaker application, it is provides an easy apis to interact with Kafka cluster.
























