package com.example.multistream;

import com.example.multistream.dto.Message;
import com.example.multistream.dto.Topic;
import com.example.multistream.dto.TopicSubscriber;
import com.example.multistream.worker.SubscriberWorker;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@NoArgsConstructor
public class Queue {
  private static List<Topic> topics = new ArrayList<>();
  private Map<String, List<SubscriberWorker>> subscriberMap = new HashMap<>();

  public synchronized void push(Topic topic, String msg) {
    topic.getMessageList().add(new Message(msg));
    topic.getLatestOffset().incrementAndGet();
    for (SubscriberWorker worker : subscriberMap.get(topic.getId())) {
      worker.wake();
    }
  }

  public List<Topic> getTopics() {
    return topics;
  }

  public void createTopic(String topicName, ISubscriber subscriber) {
    TopicSubscriber sub = new TopicSubscriber(subscriber, new AtomicInteger(0));
    Topic topic = new Topic(topicName, new ArrayList<>(Arrays.asList(sub)));
    topics.add(topic);
    subscribe(topic, sub);
  }

  public void subscribe(Topic topic, TopicSubscriber subscriber) {
    SubscriberWorker worker = new SubscriberWorker(topic, subscriber);

    if (!subscriberMap.containsKey(topic.getId())) {
      topic.getSubscribers().add(subscriber);
      subscriberMap.put(topic.getId(), new ArrayList<>(Collections.singletonList(worker)));
    } else {
      topic.getSubscribers().add(subscriber);
      subscriberMap.get(topic.getId()).add(worker);
    }
    for (SubscriberWorker workerz : subscriberMap.get(topic.getId())) {
      new Thread(workerz).start();
    }
  }

  public void resetOffset(Topic topic, TopicSubscriber subscriber, int newOffset) {
    for (SubscriberWorker workerz : subscriberMap.get(topic.getId())) {
      if (workerz
          .getTopicSubscriber()
          .getSubscriber()
          .getId()
          .equals(subscriber.getSubscriber().getId())) {
        subscriber.setOffset(new AtomicInteger(newOffset));
        workerz.getTopicSubscriber().setOffset(new AtomicInteger(newOffset));
        System.out.println("waking");
        workerz.wake();
      }
    }
  }
}
