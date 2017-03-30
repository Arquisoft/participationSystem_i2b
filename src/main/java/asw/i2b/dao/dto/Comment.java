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


    public Comment(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }

}
