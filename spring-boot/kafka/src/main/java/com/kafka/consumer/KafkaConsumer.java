package com.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.kafka.config.KafkaConstant;
import com.service.MessageService;

@Component
public class KafkaConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);
	
	@Autowired
	private MessageService servive;

	/**
	 * Khi chỉ định Listener thì phải chỉ định đủ tất cả các partitions, nếu không
	 * sẽ gặp lỗi `Commit cannot be completed since the group has already rebalanced
	 * and assigned the partitions to another member` do những Listener đọc dữ liệu
	 * ngẫu nhiên có thể đọc trùng partitions với Listener có chỉ định partitions.
	 */
//	@KafkaListener(
//		autoStartup = "${kafka.auto.startup}", //
//		// topics = "${kafka.topic.consumer}",
//		topicPartitions = {@TopicPartition(topic = "${kafka.topic.consumer}", partitions = { "0" }) },
//		id = "id0",
//		groupId = "group-id-1",
//		containerFactory = KafkaConstant.KAFKA_LISTENER_CONTAINER_FACTORY
//	)
//	public void listenPartition0(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
//		LOG.info("Listener 0: Partition = {}, Message size = {}", partition, message.length());
//	}

	@KafkaListener( //
		autoStartup = "${kafka.auto.startup}", //
		topics = "${kafka.topic.consumer}", //
		id = "id1", //
		groupId = "group-id-11", // groupId của 2 Listener phải giống nhau, nếu khác sẽ cùng đọc dữ liệu
		containerFactory = KafkaConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
	public void listenPartition1(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		LOG.info("Listener 1: Partition: {}, Message: {}", partition, message);
		// servive.receivedByThread(message);
	}

//	@KafkaListener( //
//		autoStartup = "${kafka.auto.startup}", //
//		topics = "${kafka.topic.consumer}", //
//		id = "id2", //
//		groupId = "group-id-11", //
//		containerFactory = KafkaConstant.KAFKA_LISTENER_CONTAINER_FACTORY)
//	public void listenPartition2(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
//		LOG.info("Listener 2: Partition = {}, Message size = {}", partition, message.length());
//		servive.receivedByThread(message);
//	}

}