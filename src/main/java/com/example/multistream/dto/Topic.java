package com.example.multistream.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public class Topic {

  private String name;
  private String id;
  private AtomicInteger latestOffset;
  private List<Message> messageList = new ArrayList<>();
  private List<TopicSubscriber> subscribers;

  public Topic(String name, List<TopicSubscriber> subscribers) {
    this.name = name;
    this.id = UUID.randomUUID().toString();
    this.latestOffset = new AtomicInteger(0);
    this.subscribers = subscribers;
  }

  public void setMessage(Message message) {
    this.messageList.add(message);
  }

  public void setSubscribers(TopicSubscriber subscriber) {
    if( ObjectUtils.isEmpty(this.subscribers)){
      this.subscribers = new ArrayList<>();
    }
    this.subscribers.add(subscriber);
  }
}
