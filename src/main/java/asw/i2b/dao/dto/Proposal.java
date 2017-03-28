package asw.i2b.dao.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class Proposal {

    @Id
    private ObjectId _id;

    private String category;
    private int upVotes;
    private List<Comment> comments;
    private int minimalSupport;

    public Proposal(String category, int minimalSupport){
        this.category = category;
        this.upVotes = 0;
        this.comments = new ArrayList<>();
        this.minimalSupport = minimalSupport;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
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

    public int getUpVotes() {
        return upVotes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getMinimalSupport() {
        return minimalSupport;
    }

    public boolean isSupported(){
        if (getUpVotes() > getMinimalSupport())
            return true;
        return false;
    }
}
