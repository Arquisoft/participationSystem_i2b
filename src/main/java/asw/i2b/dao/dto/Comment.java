package asw.i2b.dao.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Pineirin on 28/03/2017.
 */
@Document(collection ="comments")
public class Comment {

    private ObjectId proposalId;
    private String author;
    private Date created;
    private String body;
    private int votes;

    public Comment(){

    }

    public Comment(String author, String body) {
        this.author = author;
        this.body = body;
        this.created = new Date();
        votes = 0;
    }

    public String getBody() {
        return body;
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
}
