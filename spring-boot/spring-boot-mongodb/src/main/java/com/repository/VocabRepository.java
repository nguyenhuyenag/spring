package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.entity.Vocabulary;

@Repository
//@Transactional
public interface VocabRepository extends MongoRepository<Vocabulary, String> {

	Optional<Vocabulary> findByWord(String word);

	List<Vocabulary> findByCountBetween(int from, int to);

	// JSON query methods
	@Query("{ 'name' : ?0 }")
	Optional<Vocabulary> findByWordUsingJSON(String word);

	@Query("{ 'count' : { $gte: ?0, $lte: ?1 } }")
	List<Vocabulary> findBetweenByJSON(int from, int to);

	@Query("{$or : [{word: ?0}, {word : ?1}]}")
	// TODO
	List<Vocabulary> findWithORConditons(String startWith1, String startWith2);

	@Query("{$and : [{$or : [{noOfPages: {$gt: 275}}, {noOfPages : {$lt: 200}}]}, {$or : [{id: {$gt: 103}}, {id : {$lt: 102}}]}]}")
	// TODO
	List<Vocabulary> findWithANDConditions();

	// Regex
	@Query("{ 'word' : { $regex: ?0 } }")
	List<Vocabulary> findWithRegex(String regex);

}
