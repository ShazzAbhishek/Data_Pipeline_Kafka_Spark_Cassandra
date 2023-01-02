class UserKafkaConsumerTest {

  @Test
  void testLogKafkaMessages() throws JsonProcessingException {
      // Write a message (John Wick user) to Kafka using a test producer
      String uuid = "11111";
      String message = objectMapper.writeValueAsString(new User(uuid, "John", "Wick"));
      producer.send(new ProducerRecord<>(TOPIC_NAME, 0, uuid, message));
      producer.flush();

      // Read the message and assert its properties
      verify(userKafkaConsumer, timeout(5000).times(1))
              .logKafkaMessages(userArgumentCaptor.capture(), topicArgumentCaptor.capture(),
                      partitionArgumentCaptor.capture(), offsetArgumentCaptor.capture());

      User user = userArgumentCaptor.getValue();
      assertNotNull(user);
      assertEquals("11111", user.getUuid());
      assertEquals("John", user.getFirstName());
      assertEquals("Wick", user.getLastName());
      assertEquals(TOPIC_NAME, topicArgumentCaptor.getValue());
      assertEquals(0, partitionArgumentCaptor.getValue());
      assertEquals(0, offsetArgumentCaptor.getValue());
  }
}