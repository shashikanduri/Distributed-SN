package com.sn.metadataServer.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sn.metadataServer.model.PostDetails;
import com.sn.metadataServer.model.User;
import com.sn.metadataServer.payload.PostData;



@Repository
public interface PostRepository extends  MongoRepository<PostDetails, String> {

	PostDetails save(PostDetails postData);



}
