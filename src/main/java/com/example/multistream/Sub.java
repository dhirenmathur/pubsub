package com.example.multistream;

import com.example.multistream.dto.Message;

public class Sub implements ISubscriber {
  String id;

  public Sub() {
    id = "Subscriber" + counter.getAndIncrement();
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void consume(Message message) {
    System.out.println("Thread: " + Thread.currentThread() + " Consumed message: " + message.getMessageStr() + " in " + id);
  }
}
