package asw.i2b.service;

import asw.i2b.dao.CommentsRepository;
import asw.i2b.dao.dto.Comment;
import asw.i2b.dao.dto.Proposal;
import asw.i2b.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public void createCommnet(Comment comment) {
        commentsRepository.insert(comment);
    }

    public List<Comment> getCommentsForProposal(Proposal p) {
        return commentsRepository.findByProposalId(p.getId());
    }

    public void save(Comment comment){
        commentsRepository.save(comment);
    }

    public void vote(Comment c) {
        String author = ((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        if (!c.getVotedUsernames().contains(author)) {
            c.vote(author);
            save(c);
        }
    }
}
