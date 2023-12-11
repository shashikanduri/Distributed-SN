package com.sn.socialEmulator.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sn.socialEmulator.model.PostDetails;
import com.sn.socialEmulator.model.User;

@Repository
public interface UserRepository extends  MongoRepository<User, String> {

	User save(User userData);

}

