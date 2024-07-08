package com.sn.metadataServer.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.sn.metadataServer.model.PostDetails;
import com.sn.metadataServer.payload.PostData;
import com.sn.metadataServer.repositories.PostRepository;
import com.sn.metadataServer.repositories.UserRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository prepository;
	@Autowired
	private UserRepository urepository;
	
	
	public ResponseEntity<List<PostDetails>> getAllData() {
//	    try {
	        List<PostDetails> data = prepository.findAll();
			System.out.println(data);
	        return new ResponseEntity<>(data, HttpStatus.OK);
//	    } catch (Exception e) {
//	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
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
