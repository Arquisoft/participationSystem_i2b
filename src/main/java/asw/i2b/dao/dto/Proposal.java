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
@Document(collection = "proposals")
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
    private List<String> votedUsernames;
    private List<String> invalidWords;
    private List<Comment> comments;

    public Proposal() {

    }

    public Proposal(String author, String category, String title, String body, int minimalSupport) {
        this.author = author;
        this.created = new Date();
        this.category = category;
        this.title = title;
        this.body = body;
        this.minimalSupport = minimalSupport;
        this.votes = 0;
        this.votedUsernames = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.invalidWords = new ArrayList<>();
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

    public List<String> getVotedUsernames() {
        return votedUsernames;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void vote(String voteUsername) {
        votedUsernames.add(voteUsername);
        this.votes++;
    }

    public boolean isSupported() {
        if (votes >= minimalSupport)
            return true;
        return false;
    }

    public boolean isValid() {
        for (String s : ProposalRestrictions.getInstance().getNotAllowedWords())
            if (body.contains(s))
                return false;
        return true;
    }

    public void comment(Comment comment) {
        comment.setNum(getLastCommentId() + 1);
        this.comments.add(comment);
    }

    public long getLastCommentId() {
        long res = 0;
        for (Comment c : comments)
            res = (res < c.getNum()) ? c.getNum() : res;
        return res;
    }

    public Comment getComment(Long num) {
        for (Comment c : comments)
            if (c.getNum() == num) return c;
        return null;
    }

    public void deleteComment(long num) {
        for(int i = 0;i < this.comments.size();i++)
            if(this.comments.get(i).getNum() == num)
                this.comments.remove(i);
    }

    public List<String> getInvalidWords() {
        return invalidWords;
    }

    public void setInvalidWords(List<String> invalidWords) {
        this.invalidWords = invalidWords;
    }
}
