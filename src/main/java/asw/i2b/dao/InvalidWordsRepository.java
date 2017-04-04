package asw.i2b.dao;

import asw.i2b.dao.dto.InvalidWord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by nicolas on 4/04/17 for participationSystem0.
 */
public interface InvalidWordsRepository extends MongoRepository<InvalidWord, ObjectId> {
}
