package asw.i2b.dao;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * @author nokutu
 * @since 28/03/2017.
 */
@Component
public interface ProposalsRepository extends MongoRepository<Proposal, ObjectId> {
}
