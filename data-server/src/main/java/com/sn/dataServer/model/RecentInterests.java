package com.sn.dataServer.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
public class RecentInterests {
    @Id @NotBlank
    private final String id;
    private final String title;
    private final String description;


    public RecentInterests(String id, String title, String description){
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return title;
    }

    public String getSender() {
        return description;
    }

}
