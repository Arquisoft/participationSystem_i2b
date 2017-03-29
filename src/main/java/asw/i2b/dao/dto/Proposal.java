package asw.i2b.dao.dto;

import asw.i2b.model.ProposalRestrictions;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class Proposal {

    @Id
    private ObjectId id;

    private String category;
    private String proposalText;
    private int votes;
    private List<String> votedUsernames;
    private List<Comment> comments;
    private int minimalSupport;
    private String author;
    private Date created;

    public Proposal(String category, String proposalText, int minimalSupport, String author, Date created){
        this.category = category;
        this.votes = 0;
        this.proposalText = proposalText;
        this.votedUsernames = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.minimalSupport = minimalSupport;
        this.author = author;
        this.created = created;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setMinimalSupport(int minimalSupport) {
        this.minimalSupport = minimalSupport;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setProposalText(String proposalText) {
        this.proposalText = proposalText;
    }

    public void setVotedUsernames(List<String> votedUsernames) {
        this.votedUsernames = votedUsernames;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCategory() {
        return category;
    }

    public int getVotes() {
        return votes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getMinimalSupport() {
        return minimalSupport;
    }

    public List<String> getVotedUsernames() {
        return votedUsernames;
    }

    public ObjectId getId() {
        return id;
    }

    public String getProposalText() {
        return proposalText;
    }

    public String getAuthor() {
        return author;
    }

    public Date getCreated() {
        return created;
    }

    public void vote(String voteUsername){
        getVotedUsernames().add(voteUsername);
        setVotes(getVotes()+1);
    }

    public boolean isSupported(){
        if (getVotes() > getMinimalSupport())
            return true;
        return false;
    }

    public boolean isValid(){
        for(String s : ProposalRestrictions.getInstance().getNotAllowedWords())
            if(getProposalText().contains(s))
                return false;
        return true;
    }
}
