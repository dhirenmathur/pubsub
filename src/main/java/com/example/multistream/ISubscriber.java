package com.example.multistream;

import com.example.multistream.dto.Message;

import java.util.concurrent.atomic.AtomicInteger;

public interface ISubscriber {
  static AtomicInteger counter = new AtomicInteger(1);
  String getId();

  void consume(Message message);
}
