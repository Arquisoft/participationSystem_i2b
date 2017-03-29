package asw.i2b.dao;

import asw.i2b.dao.dto.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author nokutu
 * @since 28/03/2017.
 */
@Repository
public interface CommentsRepository extends MongoRepository<Comment, ObjectId> {

    List<Comment> findByProposalId(ObjectId proposalId);
}
