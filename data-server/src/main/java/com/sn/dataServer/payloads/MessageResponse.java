package com.sn.dataServer.payloads;

public class MessageResponse {
    private String message;

    public MessageResponse(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
