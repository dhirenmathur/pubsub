package com.example.multistream;

import com.example.multistream.dto.Topic;
import com.example.multistream.dto.TopicSubscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class MultistreamApplication {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(MultistreamApplication.class, args);
    Queue queue = new Queue();
    ISubscriber sub = new Sub();
    ISubscriber sub2 = new Sub();
    queue.createTopic("Topic1", sub);
    TopicSubscriber subTopic = new TopicSubscriber(sub2, new AtomicInteger(0));

    queue.createTopic("Topic2", sub2);

    ArrayList<String> messageStream = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      messageStream.add("TestMessage" + i);
    }

    for (Topic topic : queue.getTopics()) {
      switch (topic.getName()) {
        case "Topic1":
          {
            queue.subscribe(topic, subTopic);

            new Thread(
                    () ->
                        messageStream.stream()
                            .forEach(message -> queue.push(topic, message + topic.getName())))
                .start();
            break;
          }
        case "Topic2":
          {
            new Thread(
                    () ->
                        messageStream.stream()
                            .forEach(message -> queue.push(topic, message + topic.getName())))
                .start();
            break;
          }
        default:
      }
      Thread.sleep(5000L);
      if (topic.getName().equalsIgnoreCase("Topic2")) {
        System.out.println(
            "\n\n\n\n\n\n\n\n\n\n\\********************************\nStarting to seek to offset 500\n\n\n\n\n\n\n\n\n\n");
        queue.resetOffset(topic, subTopic, 500);
      }
    }
  }
}
