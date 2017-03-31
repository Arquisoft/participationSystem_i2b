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

    @Id
    private ObjectId _id;

    private String text;
    private ObjectId proposalId;
    private String author;
    private Date created;

    public Comment(){

    }

    public Comment(String text, ObjectId proposalId) {
        this.text = text;
        this.proposalId = proposalId;
        author = "";
        created = new Date();
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
}
