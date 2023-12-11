package com.sn.socialEmulator.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import com.sn.socialEmulator.model.PostDetails;
import com.sn.socialEmulator.payload.PostData;
import com.sn.socialEmulator.repositories.PostRepository;
import com.sn.socialEmulator.repositories.UserRepository;

import jakarta.validation.Valid;

@Service
public class PostService {
	@Autowired
	private PostRepository prepository;
	@Autowired
	private UserRepository urepository;
	
	
	public ResponseEntity<List<PostDetails>> getAllData() {
	    try {
	        List<PostDetails> data = prepository.findAll();
	        return new ResponseEntity<>(data, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	public ResponseEntity<?> savePostData(PostData request) {
		PostDetails postData = new PostDetails();
	    postData.setEmail(request.getEmail());
	    postData.setDigitalSignature(request.getDigitalSignature());
	    postData.setCaption(request.getCaption());
	    postData.setName(request.getName());
	    postData.setTimeStamp(new Date());

	    try {
	        postData = prepository.save(postData);
	        return new ResponseEntity<>(postData, HttpStatus.OK);
	    } catch (Exception e) {
	    	System.out.println(e);
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


}
