package com.example.multistream.dto;

import com.example.multistream.ISubscriber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public class TopicSubscriber {
    private ISubscriber subscriber;
    @Setter
    private AtomicInteger offset;
}
