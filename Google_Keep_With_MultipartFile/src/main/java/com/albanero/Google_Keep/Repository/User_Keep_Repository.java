package com.albanero.Google_Keep.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.albanero.Google_Keep.Model.User_Keep;

@Repository
public interface User_Keep_Repository extends MongoRepository<User_Keep, String> {

	Optional<User_Keep> findByUserName(String userName);

}
