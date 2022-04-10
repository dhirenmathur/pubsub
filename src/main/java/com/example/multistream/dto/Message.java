package com.example.multistream.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Message {

    @NonNull
    private String messageStr;


}
