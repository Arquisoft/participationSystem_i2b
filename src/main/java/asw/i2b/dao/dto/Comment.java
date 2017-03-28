package asw.i2b.dao.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class Comment {

    @Id
    private ObjectId _id;

    private String text;
    private int number;

    public Comment(String text, int number) {
        this.text = text;
        this.number = number;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }
}
