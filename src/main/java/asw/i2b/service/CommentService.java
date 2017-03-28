package asw.i2b.service;

import asw.i2b.dao.CommentsRepository;
import asw.i2b.dao.dto.Comment;
import asw.i2b.dao.dto.Proposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nokutu
 * @since 28/03/2017.
 */
@Service
public class CommentService {

    @Autowired
    private CommentsRepository commentsRepository;

    public List<Comment> getCommentsForProposal(Proposal p) {
        return commentsRepository.findByProposalId(p.getId());
    }
}
