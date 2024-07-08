package com.sn.dataServer.repositories;

import com.sn.dataServer.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostsRepository extends MongoRepository<Post, String> {
}
