package asw.i2b.dao.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
@Service("proposal")
public class Proposal {

    @Id
    private ObjectId _id;

    private String category;
    private int votes;
    private List<String> votedUsernames;
    private List<Comment> comments;
    private int minimalSupport;
    private String author;
    private Date created;

    public Proposal(String category, int minimalSupport, String author, Date created){
        this.category = category;
        this.votes = 0;
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

    public boolean isSupported(){
        if (getVotes() > getMinimalSupport())
            return true;
        return false;
    }

    public List<String> getVotedUsernames() {
        return votedUsernames;
    }

    public ObjectId getId() {
        return _id;
    }
}
