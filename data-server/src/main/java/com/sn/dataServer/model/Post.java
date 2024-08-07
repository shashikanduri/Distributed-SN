package com.sn.dataServer.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
public class Post {
    @Id @NotBlank
    private final String imgSignature;
    private final String imageData;
    private final String userId;
    private final String caption;
    private final String iv;

    public Post(String imgSignature, String imageData, String userId, String caption, String iv){
        this.imgSignature = imgSignature;
        this.imageData = imageData;
        this.userId = userId;
        this.caption = caption;
        this.iv = iv;
    }

    public String getImgSignature() {
        return imgSignature;
    }

    public String getImageData() {
        return imageData;
    }

    public String getUserId() {
        return userId;
    }

    public String getCaption() {
        return caption;
    }

    public String getIv() { return iv; }
}
