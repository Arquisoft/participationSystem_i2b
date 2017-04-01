package asw.i2b.dao.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
@Document(collection ="comments")
public class Comment {

    @Id
    private ObjectId _id;

    private ObjectId proposalId;
    private String author;
    private Date created;
    private String text;
    private int votes;
    private List<String> votedUsernames;

    public Comment(){

    }

    public Comment(ObjectId proposalId, String author, String text) {
        this.proposalId = proposalId;
        this.author = author;
        this.text = text;
        this.created = new Date();
        votes = 0;
        this.votedUsernames = new ArrayList<>();
    }

    public ObjectId getId() {
        return _id;
    }

    public String getText() {
        return text;
    }

    public ObjectId getProposalId() {
        return proposalId;
    }

    public String getAuthor() {
        return author;
    }

    public Date getCreated() {
        return created;
    }

    public int getVotes(){
        return votes;
    }

    public List<String> getVotedUsernames() {
        return votedUsernames;
    }

    public void vote(String voteUsername){
        votedUsernames.add(voteUsername);
        this.votes++;
    }

}
