package com.sn.socialEmulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.sn.socialEmulator.model.PostDetails;
import com.sn.socialEmulator.model.User;
import com.sn.socialEmulator.payload.PostData;
import com.sn.socialEmulator.repositories.UserRepository;

public class UserService {
	@Autowired
	private UserRepository urepository;
	PasswordEncoder passwordEncoder = new StandardPasswordEncoder();

	
	public ResponseEntity<?> saveUserData(User request) {
		User userDetails = new User();
		userDetails.setEmail(request.getEmail());
		userDetails.setPassword(passwordEncoder.encode(request.getPassword()));

	    try {
	    	userDetails = urepository.save(userDetails);
	        return new ResponseEntity<>(userDetails, HttpStatus.OK);
	    } catch (Exception e) {
	    	System.out.println(e);
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
