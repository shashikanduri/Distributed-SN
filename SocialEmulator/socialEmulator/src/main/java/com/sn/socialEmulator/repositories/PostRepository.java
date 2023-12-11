package com.sn.socialEmulator.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sn.socialEmulator.model.PostDetails;
import com.sn.socialEmulator.model.User;
import com.sn.socialEmulator.payload.PostData;



@Repository
public interface PostRepository extends  MongoRepository<PostDetails, String> {

	PostDetails save(PostDetails postData);



}
