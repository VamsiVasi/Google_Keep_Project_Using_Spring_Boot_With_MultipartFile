package com.albanero.Google_Keep.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import com.albanero.Google_Keep.Model.Notes_Keep;

@Repository
public interface Notes_Keep_Repository extends MongoRepository<Notes_Keep, String> {

	Optional<Notes_Keep> findByName(String name);

}
