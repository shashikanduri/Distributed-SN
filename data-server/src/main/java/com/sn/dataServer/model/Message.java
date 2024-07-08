package com.sn.dataServer.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
public class Message {
    @Id @NotBlank
    private final String id;
    private final String message;
    private final String sender;

    public Message(String id, String message, String sender){
        this.id = id;
        this.message = message;
        this.sender = sender;

    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

}
