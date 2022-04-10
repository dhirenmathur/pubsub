package com.example.multistream.worker;

import com.example.multistream.dto.Message;
import com.example.multistream.dto.Topic;
import com.example.multistream.dto.TopicSubscriber;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;

public class SubscriberWorker implements Runnable {
  private final Topic topic;
  @Getter
  private final TopicSubscriber topicSubscriber;

  public SubscriberWorker(@NonNull Topic topic, @NonNull TopicSubscriber topicSubscriber) {
    this.topic = topic;
    this.topicSubscriber = topicSubscriber;
  }

  @SneakyThrows
  @Override
  public void run() {
    synchronized (topicSubscriber) {
      while (topicSubscriber.getOffset().get() <= topic.getMessageList().size()) {
        int curr = topicSubscriber.getOffset().get();
        if (curr == topic.getMessageList().size()) {
          topicSubscriber.wait();
          continue;
        } else {
          topicSubscriber.getOffset().getAndIncrement();
        }
        Message msg = topic.getMessageList().get(curr);
        topicSubscriber.getSubscriber().consume(msg);
      }
    }
  }

  public void wake() {
    synchronized (topicSubscriber) {
      topicSubscriber.notifyAll();
    }
  }
}
