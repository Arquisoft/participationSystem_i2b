package asw.i2b.dao.dto;

import asw.i2b.model.ProposalRestrictions;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
@Document(collection ="proposals")
public class Proposal {

    @Id
    private ObjectId _id;

    private String author;
    private Date created;
    private String category;
    private String title;
    private String body;
    private int minimalSupport;
    private int votes;
    private List<String> votesUsernames;
    private List<Comment> comments;

    public Proposal() {

    }

    public Proposal(String author, String category, String title, String body, int minimalSupport){
        this.author = author;
        this.created = new Date();
        this.category = category;
        this.title = title;
        this.body = body;
        this.minimalSupport = minimalSupport;
        this.votes = 0;
        this.votesUsernames = new ArrayList<>();
        this.comments = new ArrayList<>();

    }

    public ObjectId getId() {
        return _id;
    }

    public String getAuthor() {
        return author;
    }

    public Date getCreated() {
        return created;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getMinimalSupport() {
        return minimalSupport;
    }

    public int getVotes() {
        return votes;
    }

    public List<String> getVotesUsernames() {
        return votesUsernames;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void vote(String voteUsername){
        votesUsernames.add(voteUsername);
        this.votes++;
    }

    public boolean isSupported(){
        if (votes > minimalSupport)
            return true;
        return false;
    }

    public boolean isValid(){
        for(String s : ProposalRestrictions.getInstance().getNotAllowedWords())
            if(body.contains(s))
                return false;
        return true;
    }
}
