package com.sn.metadataServer.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sn.metadataServer.model.PostDetails;
import com.sn.metadataServer.model.User;

@Repository
public interface UserRepository extends  MongoRepository<User, String> {

	User save(User userData);

}

