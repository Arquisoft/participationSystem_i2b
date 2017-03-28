package asw.i2b.service;

import asw.i2b.dao.ProposalsRepository;
import asw.i2b.dao.dto.Proposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!p.getVotedUsernames().contains(name)) {
            p.setVotes(p.getVotes() + 1);
            p.getVotedUsernames().add(name);
        }
    }
}
