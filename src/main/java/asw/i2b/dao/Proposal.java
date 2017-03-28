package asw.i2b.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class Proposal {
    private String category;
    private int upVotes;
    private int downVotes;
    private List<Comment> comments;
    private int minimalSupport;

    public Proposal(String category, int minimalSupport){
        this.category = category;
        this.upVotes = 0;
        this.downVotes = 0;
        this.comments = new ArrayList<>();
        this.minimalSupport = minimalSupport;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
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

    public int getDownVotes() {
        return downVotes;
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
