package asw.i2b.dao;

import asw.i2b.dao.dto.Proposal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nokutu
 * @since 28/03/2017.
 */
@Repository
public interface ProposalsRepository extends MongoRepository<Proposal, ObjectId> {
}
