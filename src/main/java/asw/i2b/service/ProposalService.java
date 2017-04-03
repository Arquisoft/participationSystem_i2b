package asw.i2b.service;

import asw.i2b.dao.ProposalsRepository;
import asw.i2b.dao.dto.Proposal;
import asw.i2b.model.UserModel;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author nokutu
 * @since 28/03/2017.
 */
@Service
public class ProposalService {

    @Autowired
    private ProposalsRepository proposalsRepository;

    public List<Proposal> getAllProposals() {
        return proposalsRepository.findAll();
    }

    public void vote(Proposal p) {
        String author = ((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        if (!p.getVotedUsernames().contains(author)) {
            p.vote(author);
            save(p);
        }
    }

    public void save(Proposal proposal){
        proposalsRepository.save(proposal);
    }

    public Proposal findProposalById(String id) {
        return proposalsRepository.findOne(new ObjectId(id));
    }

    public List<Proposal> getProposalsByPopularity() {
        List<Proposal> ret = getAllProposals();
        ret.sort((a, b) -> b.getVotes() - a.getVotes());
        return ret;
    }

    public void createProposal(Proposal proposal) {
        proposalsRepository.insert(proposal);
    }

    public void delete(Proposal proposal) {
        proposalsRepository.delete(proposal);
    }



}
